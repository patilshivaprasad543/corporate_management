package com.corporate.travel.security;

import com.corporate.travel.entity.enums.RoleType;
import com.corporate.travel.exception.ForbiddenException;
import org.springframework.stereotype.Service;

/**
 * Enforces company (organization) isolation for tenant-scoped data access.
 * Super admins may access all companies; all other roles are scoped to their organization.
 */
@Service
public class TenantAccessService {

    private final SecurityAuthorizationService securityAuthorizationService;

    public TenantAccessService(SecurityAuthorizationService securityAuthorizationService) {
        this.securityAuthorizationService = securityAuthorizationService;
    }

    public boolean isSuperAdmin() {
        return securityAuthorizationService.hasRole(RoleType.ROLE_SUPER_ADMIN);
    }

    /**
     * @return organization id for tenant-scoped queries, or null when super admin (all tenants)
     */
    public Long resolveOrganizationScope() {
        if (isSuperAdmin()) {
            return null;
        }
        UserPrincipal principal = securityAuthorizationService.requirePrincipal();
        if (principal.getOrganizationId() == null) {
            throw new ForbiddenException("Organization context required");
        }
        return principal.getOrganizationId();
    }

    public void assertCanAccessOrganization(Long organizationId) {
        if (organizationId == null) {
            throw new ForbiddenException("Organization context required");
        }
        if (isSuperAdmin()) {
            return;
        }
        securityAuthorizationService.requireSameOrganization(organizationId);
    }

    public void assertCanAccessResource(Long resourceOrganizationId) {
        assertCanAccessOrganization(resourceOrganizationId);
    }
}
