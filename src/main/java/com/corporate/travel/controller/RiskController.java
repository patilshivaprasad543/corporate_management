package com.corporate.travel.controller;

import com.corporate.travel.common.ApiResponse;
import com.corporate.travel.entity.RiskAlert;
import com.corporate.travel.service.RiskManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/risk")
@Tag(name = "Risk & Emergency Center", description = "Destination safety scoring, disruption warnings, and emergency traveler tracking")
public class RiskController {
    public RiskController(RiskManagementService riskService) {
        this.riskService = riskService;
    }


    private final RiskManagementService riskService;

    @GetMapping("/alerts")
    @Operation(summary = "Get active travel risk advisories and weather disruptions")
    public ResponseEntity<ApiResponse<List<RiskAlert>>> getActiveAlerts() {
        return ResponseEntity.ok(ApiResponse.ok(riskService.getActiveAlerts(), "Risk alerts retrieved"));
    }

}
