package com.corporate.travel.repository;

import com.corporate.travel.entity.Permission;
import com.corporate.travel.entity.enums.PermissionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(PermissionType name);
}
