package com.corporate.travel.controller;

import com.corporate.travel.common.ApiResponse;
import com.corporate.travel.dto.AnalyticsDto;
import com.corporate.travel.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
@Tag(name = "Analytics & Reports", description = "Executive travel dashboard, budget burn, category spend, and carbon footprint")
public class AnalyticsController {
    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }


    private final AnalyticsService analyticsService;

    @GetMapping(value = {"/executive-summary", "/dashboard"})
    @PreAuthorize("hasAuthority('PERM_ANALYTICS_VIEW')")
    @Operation(summary = "Get enterprise executive travel analytics summary")
    public ResponseEntity<ApiResponse<AnalyticsDto.ExecutiveDashboardSummary>> getExecutiveSummary() {
        return ResponseEntity.ok(ApiResponse.ok(analyticsService.getExecutiveSummary(), "Executive analytics summary retrieved"));
    }

}
