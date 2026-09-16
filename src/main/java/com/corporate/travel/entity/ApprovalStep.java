package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import com.corporate.travel.entity.enums.ApprovalStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "approval_steps")
public class ApprovalStep extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_request_id", nullable = false)
    private TravelRequest travelRequest;

    @Column(name = "step_order", nullable = false)
    private Integer stepOrder;

    @Column(name = "approver_role", length = 50, nullable = false)
    private String approverRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private User approver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus status = ApprovalStatus.PENDING;

    @Column(length = 1000)
    private String comments;

    @Column(name = "action_timestamp")
    private LocalDateTime actionTimestamp;

    public ApprovalStep() {}

    public ApprovalStep(TravelRequest travelRequest, Integer stepOrder, String approverRole, User approver,
                        ApprovalStatus status, String comments, LocalDateTime actionTimestamp) {
        this.travelRequest = travelRequest;
        this.stepOrder = stepOrder;
        this.approverRole = approverRole;
        this.approver = approver;
        this.status = status;
        this.comments = comments;
        this.actionTimestamp = actionTimestamp;
    }

    public TravelRequest getTravelRequest() { return travelRequest; }
    public void setTravelRequest(TravelRequest travelRequest) { this.travelRequest = travelRequest; }
    public Integer getStepOrder() { return stepOrder; }
    public void setStepOrder(Integer stepOrder) { this.stepOrder = stepOrder; }
    public String getApproverRole() { return approverRole; }
    public void setApproverRole(String approverRole) { this.approverRole = approverRole; }
    public User getApprover() { return approver; }
    public void setApprover(User approver) { this.approver = approver; }
    public ApprovalStatus getStatus() { return status; }
    public void setStatus(ApprovalStatus status) { this.status = status; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public LocalDateTime getActionTimestamp() { return actionTimestamp; }
    public void setActionTimestamp(LocalDateTime actionTimestamp) { this.actionTimestamp = actionTimestamp; }

    public static ApprovalStepBuilder builder() { return new ApprovalStepBuilder(); }
    public static class ApprovalStepBuilder {
        private Long id;
        private TravelRequest travelRequest;
        private Integer stepOrder;
        private String approverRole;
        private User approver;
        private ApprovalStatus status = ApprovalStatus.PENDING;
        private String comments;
        private LocalDateTime actionTimestamp;
        public ApprovalStepBuilder id(Long id) { this.id = id; return this; }
        public ApprovalStepBuilder travelRequest(TravelRequest v) { this.travelRequest = v; return this; }
        public ApprovalStepBuilder stepOrder(Integer v) { this.stepOrder = v; return this; }
        public ApprovalStepBuilder approverRole(String v) { this.approverRole = v; return this; }
        public ApprovalStepBuilder approver(User v) { this.approver = v; return this; }
        public ApprovalStepBuilder status(ApprovalStatus v) { this.status = v; return this; }
        public ApprovalStepBuilder comments(String v) { this.comments = v; return this; }
        public ApprovalStepBuilder actionTimestamp(LocalDateTime v) { this.actionTimestamp = v; return this; }
        public ApprovalStep build() {
            ApprovalStep obj = new ApprovalStep();
            obj.setId(id); obj.setTravelRequest(travelRequest); obj.setStepOrder(stepOrder);
            obj.setApproverRole(approverRole); obj.setApprover(approver); obj.setStatus(status);
            obj.setComments(comments); obj.setActionTimestamp(actionTimestamp);
            return obj;
        }
    }
}
