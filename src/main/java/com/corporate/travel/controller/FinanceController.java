package com.corporate.travel.controller;

import com.corporate.travel.common.ApiResponse;
import com.corporate.travel.dto.ExpenseDto;
import com.corporate.travel.dto.WorkflowDto;
import com.corporate.travel.security.AuthenticatedUser;
import com.corporate.travel.security.UserPrincipal;
import com.corporate.travel.service.ExpenseService;
import com.corporate.travel.service.WorkflowDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance")
@Tag(name = "Finance Workflow", description = "Pending fund releases and reimbursement disbursements")
public class FinanceController {

    private final WorkflowDashboardService workflowDashboardService;
    private final ExpenseService expenseService;

    public FinanceController(WorkflowDashboardService workflowDashboardService, ExpenseService expenseService) {
        this.workflowDashboardService = workflowDashboardService;
        this.expenseService = expenseService;
    }

    @GetMapping("/pending-releases")
    @Operation(summary = "List pending fund releases awaiting finance disbursement")
    public ResponseEntity<ApiResponse<List<WorkflowDto.PendingFundRelease>>> getPendingReleases() {
        return ResponseEntity.ok(ApiResponse.ok(workflowDashboardService.getPendingFundReleases(), "Pending fund releases retrieved"));
    }

    @PostMapping("/pending-releases/{reportId}/release")
    @Operation(summary = "Release approved funds to employee wallet")
    public ResponseEntity<ApiResponse<ExpenseDto.ReportResponse>> releaseFund(
            @PathVariable("reportId") Long reportId,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long financeId = AuthenticatedUser.requireId(principal);
        return ResponseEntity.ok(ApiResponse.ok(expenseService.releaseFund(reportId, financeId), "Fund released successfully"));
    }
}
