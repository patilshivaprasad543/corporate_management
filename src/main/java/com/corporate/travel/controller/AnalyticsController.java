package com.corporate.travel.controller;

import com.corporate.travel.common.ApiResponse;
import com.corporate.travel.dto.AnalyticsDto;
import com.corporate.travel.service.AnalyticsService;
import com.corporate.travel.service.WorkflowDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
@Tag(name = "Analytics & Reports", description = "Executive travel dashboard, budget burn, category spend, and carbon footprint")
public class AnalyticsController {
    public AnalyticsController(AnalyticsService analyticsService, WorkflowDashboardService workflowDashboardService) {
        this.analyticsService = analyticsService;
        this.workflowDashboardService = workflowDashboardService;
    }


    private final AnalyticsService analyticsService;
    private final WorkflowDashboardService workflowDashboardService;

    @GetMapping(value = {"/executive-summary", "/dashboard"})
    @Operation(summary = "Get enterprise executive travel analytics summary")
    public ResponseEntity<ApiResponse<AnalyticsDto.ExecutiveDashboardSummary>> getExecutiveSummary() {
        return ResponseEntity.ok(ApiResponse.ok(analyticsService.getExecutiveSummary(), "Executive analytics summary retrieved"));
    }

    @GetMapping("/hr-budget")
    @Operation(summary = "Get HR budget utilization dashboard")
    public ResponseEntity<ApiResponse<com.corporate.travel.dto.WorkflowDto.HrBudgetSummary>> getHrBudgetSummary() {
        return ResponseEntity.ok(ApiResponse.ok(workflowDashboardService.getHrBudgetSummary(), "HR budget summary retrieved"));
    }

}
