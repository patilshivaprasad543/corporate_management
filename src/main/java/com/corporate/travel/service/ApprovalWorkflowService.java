package com.corporate.travel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corporate.travel.dto.TravelRequestDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApprovalWorkflowService {
    private static final Logger log = LoggerFactory.getLogger(ApprovalWorkflowService.class);

    public ApprovalWorkflowService(TravelRequestRepository requestRepository, ApprovalStepRepository approvalStepRepository, UserRepository userRepository, NotificationService notificationService, AuditService auditService) {
        this.requestRepository = requestRepository;
        this.approvalStepRepository = approvalStepRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.auditService = auditService;
    }


    private final TravelRequestRepository requestRepository;
    private final ApprovalStepRepository approvalStepRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final AuditService auditService;

    @Transactional
    public TravelRequest processApproval(Long requestId, Long approverUserId, ApprovalStatus status, String comments) {
        TravelRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("TravelRequest", "id", requestId));

        User approver = userRepository.findById(approverUserId).orElse(null);

        List<ApprovalStep> steps = approvalStepRepository.findByTravelRequestIdOrderByStepOrderAsc(requestId);

        // Find current pending step
        ApprovalStep currentStep = steps.stream()
                .filter(s -> s.getStatus() == ApprovalStatus.PENDING)
                .findFirst()
                .orElse(null);

        if (currentStep != null) {
            currentStep.setStatus(status);
            currentStep.setApprover(approver);
            currentStep.setComments(comments);
            currentStep.setActionTimestamp(LocalDateTime.now());
            approvalStepRepository.save(currentStep);
        }

        if (status == ApprovalStatus.REJECTED) {
            request.setStatus(RequestStatus.REJECTED);
            notificationService.sendNotification(request.getEmployee().getId(), "Travel Request Rejected",
                    "Your travel request " + request.getRequestNumber() + " was rejected. Reason: " + comments,
                    NotificationType.REQUEST_REJECTED, "/requests");
        } else if (status == ApprovalStatus.CHANGES_REQUESTED) {
            request.setStatus(RequestStatus.MODIFICATION_REQUESTED);
        } else if (status == ApprovalStatus.APPROVED) {
            // Check if all steps approved
            boolean allApproved = steps.stream().allMatch(s -> s.getStatus() == ApprovalStatus.APPROVED);
            if (allApproved) {
                request.setStatus(RequestStatus.APPROVED);
                notificationService.sendNotification(request.getEmployee().getId(), "Travel Request Approved",
                        "Great news! Your travel request " + request.getRequestNumber() + " has been fully approved. You may now book your itinerary.",
                        NotificationType.REQUEST_APPROVED, "/book");
            } else {
                request.setStatus(RequestStatus.MANAGER_REVIEW);
            }
        }

        TravelRequest saved = requestRepository.save(request);
        auditService.logAction(approver != null ? approver.getEmail() : "SYSTEM", "APPROVE_TRAVEL_REQUEST",
                "TravelRequest", saved.getId(), "Status set to " + status.name() + " for request " + request.getRequestNumber(), null);

        return saved;
    }
}
