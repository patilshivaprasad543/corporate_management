package com.corporate.travel.service;

import com.corporate.travel.entity.Permission;
import com.corporate.travel.entity.Role;
import com.corporate.travel.entity.enums.PermissionType;
import com.corporate.travel.entity.enums.RoleType;
import com.corporate.travel.repository.PermissionRepository;
import com.corporate.travel.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.*;

@Service
@Order(100)
public class PermissionSeedService implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(PermissionSeedService.class);

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    public PermissionSeedService(PermissionRepository permissionRepository, RoleRepository roleRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Map<PermissionType, Permission> permissionMap = new HashMap<>();
        for (PermissionType pt : PermissionType.values()) {
            permissionMap.put(pt, permissionRepository.findByName(pt)
                    .orElseGet(() -> permissionRepository.save(new Permission(pt, pt.name().replace('_', ' ')))));
        }

        Map<RoleType, Set<PermissionType>> rolePermissions = buildRolePermissionMap();
        for (Map.Entry<RoleType, Set<PermissionType>> entry : rolePermissions.entrySet()) {
            roleRepository.findByName(entry.getKey()).ifPresent(role -> {
                Set<Permission> perms = new HashSet<>();
                for (PermissionType pt : entry.getValue()) {
                    perms.add(permissionMap.get(pt));
                }
                role.setPermissions(perms);
                roleRepository.save(role);
            });
        }
        log.info("Permissions seeded for {} roles", rolePermissions.size());
    }

    private Map<RoleType, Set<PermissionType>> buildRolePermissionMap() {
        Map<RoleType, Set<PermissionType>> map = new HashMap<>();

        map.put(RoleType.ROLE_EMPLOYEE, Set.of(
                PermissionType.TRAVEL_REQUEST_CREATE, PermissionType.TRAVEL_REQUEST_VIEW,
                PermissionType.TRAVEL_REQUEST_UPDATE, PermissionType.TRAVEL_REQUEST_SUBMIT,
                PermissionType.BOOKING_VIEW, PermissionType.EXPENSE_CREATE, PermissionType.EXPENSE_VIEW,
                PermissionType.EMPLOYEE_VIEW, PermissionType.POLICY_VIEW
        ));

        map.put(RoleType.ROLE_APPROVER, Set.of(
                PermissionType.TRAVEL_REQUEST_VIEW, PermissionType.TRAVEL_REQUEST_APPROVE,
                PermissionType.TRAVEL_REQUEST_REJECT, PermissionType.EMPLOYEE_VIEW,
                PermissionType.ANALYTICS_VIEW, PermissionType.REPORT_VIEW
        ));

        map.put(RoleType.ROLE_TRAVEL_MANAGER, Set.of(
                PermissionType.TRAVEL_REQUEST_VIEW, PermissionType.TRAVEL_REQUEST_UPDATE,
                PermissionType.BOOKING_VIEW, PermissionType.BOOKING_CREATE, PermissionType.BOOKING_UPDATE,
                PermissionType.BOOKING_CANCEL, PermissionType.POLICY_VIEW, PermissionType.POLICY_MANAGE,
                PermissionType.VENDOR_VIEW, PermissionType.ANALYTICS_VIEW
        ));

        map.put(RoleType.ROLE_FINANCE, Set.of(
                PermissionType.EXPENSE_VIEW, PermissionType.EXPENSE_APPROVE,
                PermissionType.REIMBURSEMENT_VIEW, PermissionType.REIMBURSEMENT_PROCESS,
                PermissionType.PAYMENT_VIEW, PermissionType.PAYMENT_PROCESS,
                PermissionType.ANALYTICS_VIEW, PermissionType.REPORT_VIEW, PermissionType.AUDIT_VIEW
        ));

        map.put(RoleType.ROLE_SUPPORT, Set.of(
                PermissionType.TRAVEL_REQUEST_VIEW, PermissionType.BOOKING_VIEW,
                PermissionType.BOOKING_CREATE, PermissionType.BOOKING_UPDATE,
                PermissionType.VENDOR_VIEW, PermissionType.EMPLOYEE_VIEW
        ));

        map.put(RoleType.ROLE_VENDOR, Set.of(
                PermissionType.VENDOR_VIEW, PermissionType.BOOKING_VIEW
        ));

        map.put(RoleType.ROLE_SUPER_ADMIN, new HashSet<>(Arrays.asList(PermissionType.values())));

        map.put(RoleType.ROLE_COMPANY_ADMIN, Set.of(
                PermissionType.COMPANY_VIEW, PermissionType.COMPANY_MANAGE,
                PermissionType.USER_VIEW, PermissionType.USER_CREATE, PermissionType.USER_UPDATE,
                PermissionType.EMPLOYEE_MANAGE, PermissionType.POLICY_MANAGE, PermissionType.ANALYTICS_VIEW
        ));

        map.put(RoleType.ROLE_HR, Set.of(
                PermissionType.EMPLOYEE_VIEW, PermissionType.EMPLOYEE_MANAGE,
                PermissionType.USER_VIEW, PermissionType.REPORT_VIEW
        ));

        return map;
    }
}
