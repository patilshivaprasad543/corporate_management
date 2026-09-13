package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "audit_logs")

public class AuditLog extends BaseEntity {

    @Column(name = "user_email", length = 150)
    private String userEmail;

    @Column(name = "action_name", nullable = false, length = 100)
    private String actionName;

    @Column(name = "entity_type", length = 100)
    private String entityType;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "status", length = 50)

    private String status = "SUCCESS";


    public AuditLog() {}

    public AuditLog(String userEmail, String actionName, String entityType, Long entityId, String description, String ipAddress, String status) {
        this.userEmail = userEmail;
        this.actionName = actionName;
        this.entityType = entityType;
        this.entityId = entityId;
        this.description = description;
        this.ipAddress = ipAddress;
        this.status = status;
    }

    public String getUserEmail() { return userEmail; }

    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getActionName() { return actionName; }

    public void setActionName(String actionName) { this.actionName = actionName; }

    public String getEntityType() { return entityType; }

    public void setEntityType(String entityType) { this.entityType = entityType; }

    public Long getEntityId() { return entityId; }

    public void setEntityId(Long entityId) { this.entityId = entityId; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getIpAddress() { return ipAddress; }

    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public static AuditLogBuilder builder() { return new AuditLogBuilder(); }

    public static class AuditLogBuilder {
        private Long id;
        private String userEmail;
        private String actionName;
        private String entityType;
        private Long entityId;
        private String description;
        private String ipAddress;
        private String status = "SUCCESS";

        public AuditLogBuilder id(Long id) { this.id = id; return this; }
        public AuditLogBuilder userEmail(String userEmail) { this.userEmail = userEmail; return this; }
        public AuditLogBuilder actionName(String actionName) { this.actionName = actionName; return this; }
        public AuditLogBuilder entityType(String entityType) { this.entityType = entityType; return this; }
        public AuditLogBuilder entityId(Long entityId) { this.entityId = entityId; return this; }
        public AuditLogBuilder description(String description) { this.description = description; return this; }
        public AuditLogBuilder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public AuditLogBuilder status(String status) { this.status = status; return this; }

        public AuditLog build() {
            AuditLog obj = new AuditLog();
            obj.setId(this.id);
            obj.setUserEmail(this.userEmail);
            obj.setActionName(this.actionName);
            obj.setEntityType(this.entityType);
            obj.setEntityId(this.entityId);
            obj.setDescription(this.description);
            obj.setIpAddress(this.ipAddress);
            obj.setStatus(this.status);
            return obj;
        }
    }
}
