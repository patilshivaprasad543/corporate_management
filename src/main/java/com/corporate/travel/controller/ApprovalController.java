package com.corporate.travel.controller;

import com.corporate.travel.common.ApiResponse;
import com.corporate.travel.dto.TravelRequestDto;
import com.corporate.travel.entity.TravelRequest;
import com.corporate.travel.entity.enums.ApprovalStatus;
import com.corporate.travel.security.UserPrincipal;
import com.corporate.travel.security.AuthenticatedUser;
import com.corporate.travel.service.ApprovalWorkflowService;
import com.corporate.travel.service.TravelRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/approvals")
@Tag(name = "Approval Workflow", description = "Multi-tier approval routing for Managers, Finance, and Company Admins")
public class ApprovalController {
    public ApprovalController(ApprovalWorkflowService approvalWorkflowService, TravelRequestService travelRequestService) {
        this.approvalWorkflowService = approvalWorkflowService;
        this.travelRequestService = travelRequestService;
    }


    private final ApprovalWorkflowService approvalWorkflowService;
    private final TravelRequestService travelRequestService;

    @PostMapping("/{requestId}/approve")
    @PreAuthorize("hasAuthority('PERM_TRAVEL_REQUEST_APPROVE')")
    @Operation(summary = "Approve a pending travel request")
    public ResponseEntity<ApiResponse<TravelRequestDto.Response>> approve(
            @PathVariable("requestId") Long requestId,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(name = "comments", required = false, defaultValue = "Approved via enterprise portal") String comments) {
        Long approverId = AuthenticatedUser.requireId(principal);
        TravelRequest req = approvalWorkflowService.processApproval(requestId, approverId, ApprovalStatus.APPROVED, comments);
        return ResponseEntity.ok(ApiResponse.ok(travelRequestService.mapToResponse(req), "Travel request approved"));
    }

    @PostMapping("/{requestId}/reject")
    @PreAuthorize("hasAuthority('PERM_TRAVEL_REQUEST_REJECT')")
    @Operation(summary = "Reject a pending travel request with reason")
    public ResponseEntity<ApiResponse<TravelRequestDto.Response>> reject(
            @PathVariable("requestId") Long requestId,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(name = "comments", required = false, defaultValue = "Budget exceeded / Trip deferred") String comments) {
        Long approverId = AuthenticatedUser.requireId(principal);
        TravelRequest req = approvalWorkflowService.processApproval(requestId, approverId, ApprovalStatus.REJECTED, comments);
        return ResponseEntity.ok(ApiResponse.ok(travelRequestService.mapToResponse(req), "Travel request rejected"));
    }

    @PostMapping("/{requestId}/request-changes")
    @PreAuthorize("hasAuthority('PERM_TRAVEL_REQUEST_APPROVE')")
    @Operation(summary = "Request modifications on travel request")
    public ResponseEntity<ApiResponse<TravelRequestDto.Response>> requestChanges(
            @PathVariable("requestId") Long requestId,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(name = "comments", required = false, defaultValue = "Please adjust dates or choose economy tier") String comments) {
        Long approverId = AuthenticatedUser.requireId(principal);
        TravelRequest req = approvalWorkflowService.processApproval(requestId, approverId, ApprovalStatus.CHANGES_REQUESTED, comments);
        return ResponseEntity.ok(ApiResponse.ok(travelRequestService.mapToResponse(req), "Modification requested"));
    }

}
