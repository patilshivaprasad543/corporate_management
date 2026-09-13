package com.corporate.travel.security;

import com.corporate.travel.exception.UnauthorizedException;

/**
 * Centralizes the boundary between authenticated APIs and seeded demo data.
 * Seed data may be used for local login, but must never become an implicit
 * identity for a request that did not authenticate.
 */
public final class AuthenticatedUser {
    private AuthenticatedUser() {
    }

    public static Long requireId(UserPrincipal principal) {
        if (principal == null) {
            throw new UnauthorizedException("Authentication is required for this action");
        }
        return principal.getId();
    }
}
