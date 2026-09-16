package com.corporate.travel.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ApprovalActionDto {
    public enum Action { APPROVE, REJECT, REQUEST_CHANGES }

    @NotNull
    private Action action;

    @Size(max = 1000)
    private String comments;

    public Action getAction() { return action; }
    public void setAction(Action action) { this.action = action; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
}
