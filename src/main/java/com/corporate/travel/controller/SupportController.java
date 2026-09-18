package com.corporate.travel.controller;

import com.corporate.travel.common.ApiResponse;
import com.corporate.travel.dto.WorkflowDto;
import com.corporate.travel.service.WorkflowDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/support")
@Tag(name = "Support Desk", description = "Travel agent desk metrics and upcoming itineraries")
public class SupportController {

    private final WorkflowDashboardService workflowDashboardService;

    public SupportController(WorkflowDashboardService workflowDashboardService) {
        this.workflowDashboardService = workflowDashboardService;
    }

    @GetMapping("/desk")
    @Operation(summary = "Get support desk dashboard with active bookings and upcoming itineraries")
    public ResponseEntity<ApiResponse<WorkflowDto.SupportDeskSummary>> getSupportDesk() {
        return ResponseEntity.ok(ApiResponse.ok(workflowDashboardService.getSupportDeskSummary(), "Support desk summary retrieved"));
    }
}
