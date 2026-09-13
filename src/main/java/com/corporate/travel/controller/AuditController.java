package com.corporate.travel.controller;

import com.corporate.travel.common.ApiResponse;
import com.corporate.travel.entity.AuditLog;
import com.corporate.travel.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@Tag(name = "Audit Logs", description = "Tamper-evident audit trail of all enterprise booking, approval, and financial actions")
public class AuditController {
    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }


    private final AuditService auditService;

    @GetMapping
    @PreAuthorize("hasAuthority('PERM_AUDIT_VIEW')")
    @Operation(summary = "Get recent enterprise audit logs")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getAuditLogs() {
        return ResponseEntity.ok(ApiResponse.ok(auditService.getRecentLogs(), "Audit logs retrieved"));
    }

}
