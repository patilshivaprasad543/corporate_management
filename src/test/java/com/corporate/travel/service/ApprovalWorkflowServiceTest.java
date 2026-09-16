package com.corporate.travel.service;

import com.corporate.travel.entity.ApprovalStep;
import com.corporate.travel.entity.TravelRequest;
import com.corporate.travel.entity.User;
import com.corporate.travel.entity.enums.ApprovalStatus;
import com.corporate.travel.entity.enums.RequestStatus;
import com.corporate.travel.repository.ApprovalStepRepository;
import com.corporate.travel.repository.TravelRequestRepository;
import com.corporate.travel.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApprovalWorkflowServiceTest {
    @Mock TravelRequestRepository requestRepository;
    @Mock ApprovalStepRepository approvalStepRepository;
    @Mock UserRepository userRepository;
    @Mock NotificationService notificationService;
    @Mock AuditService auditService;

    private ApprovalWorkflowService service;
    private TravelRequest request;
    private User assignedApprover;
    private User otherApprover;
    private ApprovalStep step;

    @BeforeEach
    void setUp() {
        service = new ApprovalWorkflowService(requestRepository, approvalStepRepository, userRepository,
                notificationService, auditService);
        request = new TravelRequest();
        request.setId(100L);
        request.setRequestNumber("TR-TEST001");
        request.setStatus(RequestStatus.MANAGER_REVIEW);

        assignedApprover = new User();
        assignedApprover.setId(10L);
        assignedApprover.setEmail("manager@example.com");

        otherApprover = new User();
        otherApprover.setId(11L);
        otherApprover.setEmail("other@example.com");

        step = new ApprovalStep();
        step.setId(200L);
        step.setTravelRequest(request);
        step.setStepOrder(1);
        step.setApproverRole("ROLE_APPROVER");
        step.setStatus(ApprovalStatus.PENDING);
        step.setApprover(assignedApprover);
    }

    @Test
    void rejectsActionFromWrongDesignatedApprover() {
        when(requestRepository.findById(100L)).thenReturn(Optional.of(request));
        when(userRepository.findById(11L)).thenReturn(Optional.of(otherApprover));
        when(approvalStepRepository.findByTravelRequestIdOrderByStepOrderAsc(100L)).thenReturn(List.of(step));

        assertThrows(AccessDeniedException.class,
                () -> service.processApproval(100L, 11L, ApprovalStatus.APPROVED, "approved"));
        verify(approvalStepRepository, never()).save(any());
    }

    @Test
    void rejectsActionAfterRequestIsFinal() {
        request.setStatus(RequestStatus.APPROVED);
        when(requestRepository.findById(100L)).thenReturn(Optional.of(request));
        when(userRepository.findById(10L)).thenReturn(Optional.of(assignedApprover));

        assertThrows(IllegalStateException.class,
                () -> service.processApproval(100L, 10L, ApprovalStatus.APPROVED, "again"));
        verify(approvalStepRepository, never()).save(any());
    }

    @Test
    void allowsAssignedApproverToApprove() {
        when(requestRepository.findById(100L)).thenReturn(Optional.of(request));
        when(userRepository.findById(10L)).thenReturn(Optional.of(assignedApprover));
        when(approvalStepRepository.findByTravelRequestIdOrderByStepOrderAsc(100L)).thenReturn(List.of(step));
        when(approvalStepRepository.save(any(ApprovalStep.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(requestRepository.save(any(TravelRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TravelRequest result = service.processApproval(100L, 10L, ApprovalStatus.APPROVED, "approved");

        assertEquals(RequestStatus.APPROVED, result.getStatus());
        assertEquals(ApprovalStatus.APPROVED, step.getStatus());
        assertEquals(assignedApprover, step.getApprover());
        verify(approvalStepRepository).save(step);
        verify(requestRepository).save(request);
    }
}
