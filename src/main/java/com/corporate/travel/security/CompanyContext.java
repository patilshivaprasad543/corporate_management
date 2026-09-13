package com.corporate.travel.security;

/**
 * Thread-local holder for the current authenticated user's company (organization) scope.
 * Populated by JWT filter in Phase 4; used for tenant-isolated queries.
 */
public final class CompanyContext {

    private static final ThreadLocal<Long> CURRENT_ORG_ID = new ThreadLocal<>();

    private CompanyContext() {}

    public static void setOrganizationId(Long organizationId) {
        CURRENT_ORG_ID.set(organizationId);
    }

    public static Long getOrganizationId() {
        return CURRENT_ORG_ID.get();
    }

    public static void clear() {
        CURRENT_ORG_ID.remove();
    }
}
