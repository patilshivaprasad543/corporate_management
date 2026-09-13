package com.corporate.travel.exception;

import java.util.List;

public class PolicyViolationException extends RuntimeException {
    private List<String> violations;

    public PolicyViolationException(String message) {
        super(message);
    }

    public PolicyViolationException(String message, List<String> violations) {
        super(message);
        this.violations = violations;
    }

    public List<String> getViolations() {
        return violations;
    }

    public void setViolations(List<String> violations) {
        this.violations = violations;
    }
}
