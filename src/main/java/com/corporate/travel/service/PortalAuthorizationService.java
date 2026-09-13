package com.corporate.travel.service;

import com.corporate.travel.entity.Organization;
import com.corporate.travel.entity.Role;
import com.corporate.travel.entity.User;
import com.corporate.travel.entity.enums.PortalType;
import com.corporate.travel.entity.enums.RoleType;
import com.corporate.travel.entity.enums.UserStatus;
import com.corporate.travel.exception.BadRequestException;
import com.corporate.travel.exception.PortalAccessDeniedException;
import com.corporate.travel.exception.UnauthorizedException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PortalAuthorizationService {

    public void validatePortalAccess(User user, PortalType portal, Long requestedOrganizationId) {
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

        validateCompanyScope(user, portal, requestedOrganizationId);
    }

    public void validateCompanyScope(User user, PortalType portal, Long requestedOrganizationId) {
        if (portal == PortalType.SUPER_ADMIN) {
            return;
        }

        if (requestedOrganizationId == null) {
            throw new BadRequestException("Company selection is required for this portal");
        }

        Organization userOrg = user.getOrganization();
        if (userOrg == null) {
            throw new PortalAccessDeniedException("Your account is not assigned to a company");
        }
        if (!Boolean.TRUE.equals(userOrg.getActive())) {
            throw new UnauthorizedException("Your company account is inactive");
        }
        if (!userOrg.getId().equals(requestedOrganizationId)) {
            throw new PortalAccessDeniedException("You do not belong to the selected company");
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
