package com.corporate.travel.entity.enums;

import java.util.Set;

/**
 * Login portal identifiers. Each portal maps to exactly one allowed RoleType.
 */
public enum PortalType {
    EMPLOYEE(RoleType.ROLE_EMPLOYEE),
    MANAGER(RoleType.ROLE_APPROVER),
    TRAVEL_ADMIN(RoleType.ROLE_TRAVEL_MANAGER),
    FINANCE(RoleType.ROLE_FINANCE),
    TRAVEL_AGENT(RoleType.ROLE_SUPPORT),
    VENDOR(RoleType.ROLE_VENDOR),
    SUPER_ADMIN(RoleType.ROLE_SUPER_ADMIN);

    private final RoleType allowedRole;

    PortalType(RoleType allowedRole) {
        this.allowedRole = allowedRole;
    }

    public RoleType getAllowedRole() {
        return allowedRole;
    }

    public boolean isRoleAllowed(RoleType role) {
        if (role == allowedRole) {
            return true;
        }
        // Super admin may access super-admin portal only; company admin is separate
        return false;
    }

    public boolean userHasPortalAccess(Set<RoleType> userRoles) {
        return userRoles.contains(allowedRole);
    }
}
