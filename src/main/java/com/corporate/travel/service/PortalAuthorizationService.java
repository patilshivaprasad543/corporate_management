package com.corporate.travel.service;

import com.corporate.travel.entity.Role;
import com.corporate.travel.entity.User;
import com.corporate.travel.entity.enums.PortalType;
import com.corporate.travel.entity.enums.RoleType;
import com.corporate.travel.entity.enums.UserStatus;
import com.corporate.travel.exception.PortalAccessDeniedException;
import com.corporate.travel.exception.UnauthorizedException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PortalAuthorizationService {

    public void validatePortalAccess(User user, PortalType portal) {
        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new UnauthorizedException("Account is disabled");
        }
        if (user.getStatus() == UserStatus.SUSPENDED || user.getStatus() == UserStatus.DEACTIVATED) {
            throw new UnauthorizedException("Account is suspended");
        }
        if (!Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new UnauthorizedException("Email not verified. Please complete OTP verification.");
        }

        Set<RoleType> userRoles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        if (!portal.userHasPortalAccess(userRoles)) {
            throw new PortalAccessDeniedException(
                    "Your account does not have access to the " + portal.name() + " portal");
        }
    }

    public PortalType parsePortal(String portal) {
        if (portal == null || portal.isBlank()) {
            throw new PortalAccessDeniedException("Portal is required");
        }
        try {
            return PortalType.valueOf(portal.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new PortalAccessDeniedException("Unknown portal: " + portal);
        }
    }
}
