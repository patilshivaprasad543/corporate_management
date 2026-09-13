package com.corporate.travel.security;

import com.corporate.travel.entity.User;
import com.corporate.travel.entity.enums.PermissionType;
import com.corporate.travel.entity.enums.RoleType;
import com.corporate.travel.exception.ForbiddenException;
import com.corporate.travel.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityAuthorizationService {

    private final UserRepository userRepository;

    public SecurityAuthorizationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserPrincipal currentPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal principal)) {
            return null;
        }
        return principal;
    }

    public UserPrincipal requirePrincipal() {
        UserPrincipal principal = currentPrincipal();
        if (principal == null) {
            throw new ForbiddenException("Authentication required");
        }
        return principal;
    }

    public boolean hasPermission(PermissionType permission) {
        UserPrincipal principal = currentPrincipal();
        if (principal == null) {
            return false;
        }
        String authority = SecurityConstants.permissionAuthority(permission.name());
        return principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals(authority));
    }

    public void requirePermission(PermissionType permission) {
        if (!hasPermission(permission)) {
            throw new ForbiddenException("Missing permission: " + permission.name());
        }
    }

    public boolean hasRole(RoleType role) {
        UserPrincipal principal = currentPrincipal();
        if (principal == null) {
            return false;
        }
        return principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals(role.name()));
    }

    public void requireSameOrganization(Long resourceOrganizationId) {
        UserPrincipal principal = requirePrincipal();
        if (hasRole(RoleType.ROLE_SUPER_ADMIN)) {
            return;
        }
        if (principal.getOrganizationId() == null || resourceOrganizationId == null) {
            throw new ForbiddenException("Organization context required");
        }
        if (!principal.getOrganizationId().equals(resourceOrganizationId)) {
            throw new ForbiddenException("Access denied: resource belongs to another company");
        }
    }

    public User requireUserInSameOrganization(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ForbiddenException("User not found"));
        Long orgId = user.getOrganization() != null ? user.getOrganization().getId() : null;
        requireSameOrganization(orgId);
        return user;
    }
}
