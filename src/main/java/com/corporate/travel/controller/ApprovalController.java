package com.corporate.travel.controller;

import com.corporate.travel.common.ApiResponse;
import com.corporate.travel.dto.TravelRequestDto;
import com.corporate.travel.entity.TravelRequest;
import com.corporate.travel.entity.enums.ApprovalStatus;
import com.corporate.travel.security.AuthenticatedUser;
import com.corporate.travel.security.UserPrincipal;
import com.corporate.travel.service.ApprovalWorkflowService;
import com.corporate.travel.service.TravelRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/approvals")
@Tag(name = "Approval Workflow", description = "Ordered multi-tier approval workflow")
public class ApprovalController {
    private final ApprovalWorkflowService approvalWorkflowService;
    private final TravelRequestService travelRequestService;

    public ApprovalController(ApprovalWorkflowService approvalWorkflowService, TravelRequestService travelRequestService) {
        this.approvalWorkflowService = approvalWorkflowService;
        this.travelRequestService = travelRequestService;
    }

    @PostMapping("/{requestId}/approve")
    @Operation(summary = "Approve the current approval step")
    public ResponseEntity<ApiResponse<TravelRequestDto.Response>> approve(
            @PathVariable Long requestId,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) String comments) {
        TravelRequest req = approvalWorkflowService.processApproval(requestId,
                AuthenticatedUser.requireId(principal), ApprovalStatus.APPROVED,
                comments == null ? "Approved via enterprise portal" : comments);
        return ResponseEntity.ok(ApiResponse.ok(travelRequestService.mapToResponse(req), "Approval completed"));
    }

    @PostMapping("/{requestId}/reject")
    @Operation(summary = "Reject the current approval step")
    public ResponseEntity<ApiResponse<TravelRequestDto.Response>> reject(
            @PathVariable Long requestId,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam String comments) {
        TravelRequest req = approvalWorkflowService.processApproval(requestId,
                AuthenticatedUser.requireId(principal), ApprovalStatus.REJECTED, comments);
        return ResponseEntity.ok(ApiResponse.ok(travelRequestService.mapToResponse(req), "Approval rejected"));
    }

    @PostMapping("/{requestId}/request-changes")
    @Operation(summary = "Request changes on the current approval step")
    public ResponseEntity<ApiResponse<TravelRequestDto.Response>> requestChanges(
            @PathVariable Long requestId,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam String comments) {
        TravelRequest req = approvalWorkflowService.processApproval(requestId,
                AuthenticatedUser.requireId(principal), ApprovalStatus.CHANGES_REQUESTED, comments);
        return ResponseEntity.ok(ApiResponse.ok(travelRequestService.mapToResponse(req), "Changes requested"));
    }
}
