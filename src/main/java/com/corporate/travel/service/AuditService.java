package com.corporate.travel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corporate.travel.entity.AuditLog;
import com.corporate.travel.repository.AuditLogRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {
    private static final Logger log = LoggerFactory.getLogger(AuditService.class);

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }


    private final AuditLogRepository auditLogRepository;

    @Async
    public void logAction(String userEmail, String actionName, String entityType, Long entityId, String description, String ipAddress) {
        try {
            AuditLog logEntry = AuditLog.builder()
                    .userEmail(userEmail != null ? userEmail : "SYSTEM")
                    .actionName(actionName)
                    .entityType(entityType)
                    .entityId(entityId)
                    .description(description)
                    .ipAddress(ipAddress != null ? ipAddress : "127.0.0.1")
                    .status("SUCCESS")
                    .build();
            auditLogRepository.save(logEntry);
        } catch (Exception e) {
            log.error("Failed to record audit log", e);
        }
    }

    public List<AuditLog> getRecentLogs() {
        return auditLogRepository.findTop100ByOrderByCreatedAtDesc();
    }
}
