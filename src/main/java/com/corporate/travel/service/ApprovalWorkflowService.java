package com.corporate.travel.service;

import com.corporate.travel.entity.ApprovalStep;
import com.corporate.travel.entity.TravelRequest;
import com.corporate.travel.entity.User;
import com.corporate.travel.entity.enums.ApprovalStatus;
import com.corporate.travel.entity.enums.NotificationType;
import com.corporate.travel.entity.enums.RequestStatus;
import com.corporate.travel.exception.ResourceNotFoundException;
import com.corporate.travel.repository.ApprovalStepRepository;
import com.corporate.travel.repository.TravelRequestRepository;
import com.corporate.travel.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApprovalWorkflowService {
    private final TravelRequestRepository requestRepository;
    private final ApprovalStepRepository approvalStepRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final AuditService auditService;

    public ApprovalWorkflowService(TravelRequestRepository requestRepository,
                                   ApprovalStepRepository approvalStepRepository,
                                   UserRepository userRepository,
                                   NotificationService notificationService,
                                   AuditService auditService) {
        this.requestRepository = requestRepository;
        this.approvalStepRepository = approvalStepRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.auditService = auditService;
    }

    @Transactional
    public TravelRequest processApproval(Long requestId, Long approverUserId, ApprovalStatus status, String comments) {
        TravelRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("TravelRequest", "id", requestId));
        User approver = userRepository.findById(approverUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", approverUserId));

        List<ApprovalStep> steps = approvalStepRepository.findByTravelRequestIdOrderByStepOrderAsc(requestId);
        ApprovalStep currentStep = steps.stream()
                .filter(s -> s.getStatus() == ApprovalStatus.PENDING)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("There is no pending approval step for this travel request"));

        boolean authorized = approver.getRoles() != null && approver.getRoles().stream()
                .anyMatch(role -> currentStep.getApproverRole().equals(role.getName().name())
                        || "ROLE_SUPER_ADMIN".equals(role.getName().name()));
        if (!authorized) {
            throw new AccessDeniedException("You are not authorized for the current approval step");
        }

        if (status == null || status == ApprovalStatus.PENDING) {
            throw new IllegalArgumentException("A final approval action is required");
        }
        if ((status == ApprovalStatus.REJECTED || status == ApprovalStatus.CHANGES_REQUESTED)
                && (comments == null || comments.isBlank())) {
            throw new IllegalArgumentException("Comments are required when rejecting or requesting changes");
        }

        currentStep.setStatus(status);
        currentStep.setApprover(approver);
        currentStep.setComments(comments);
        currentStep.setActionTimestamp(LocalDateTime.now());
        approvalStepRepository.save(currentStep);

        if (status == ApprovalStatus.REJECTED) {
            request.setStatus(RequestStatus.REJECTED);
            notifyEmployee(request, "Travel Request Rejected",
                    "Your travel request " + request.getRequestNumber() + " was rejected. Reason: " + comments,
                    NotificationType.REQUEST_REJECTED, "/requests");
        } else if (status == ApprovalStatus.CHANGES_REQUESTED) {
            request.setStatus(RequestStatus.MODIFICATION_REQUESTED);
        } else {
            boolean allApproved = steps.stream().allMatch(s -> s.getStatus() == ApprovalStatus.APPROVED);
            if (allApproved) {
                request.setStatus(RequestStatus.APPROVED);
                notifyEmployee(request, "Travel Request Approved",
                        "Your travel request " + request.getRequestNumber() + " has been fully approved and can proceed to booking.",
                        NotificationType.REQUEST_APPROVED, "/book");
            } else {
                ApprovalStep next = steps.stream()
                        .filter(s -> s.getStatus() == ApprovalStatus.PENDING)
                        .findFirst().orElse(null);
                request.setStatus(next != null && "ROLE_FINANCE".equals(next.getApproverRole())
                        ? RequestStatus.FINANCE_REVIEW : RequestStatus.MANAGER_REVIEW);
            }
        }

        TravelRequest saved = requestRepository.save(request);
        auditService.logAction(approver.getEmail(), "TRAVEL_APPROVAL_" + status.name(),
                "TravelRequest", saved.getId(),
                "Approval step " + currentStep.getStepOrder() + " completed for " + request.getRequestNumber(), null);
        return saved;
    }

    private void notifyEmployee(TravelRequest request, String title, String message,
                                NotificationType type, String path) {
        if (request.getEmployee() != null) {
            notificationService.sendNotification(request.getEmployee().getId(), title, message, type, path);
        }
    }
}
