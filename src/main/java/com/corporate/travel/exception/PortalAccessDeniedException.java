package com.corporate.travel.exception;

public class PortalAccessDeniedException extends RuntimeException {
    public PortalAccessDeniedException(String message) {
        super(message);
    }
}
