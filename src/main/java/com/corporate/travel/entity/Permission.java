package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import com.corporate.travel.entity.enums.PermissionType;
import jakarta.persistence.*;

@Entity
@Table(name = "permissions")
public class Permission extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 60)
    private PermissionType name;

    @Column(length = 255)
    private String description;

    public Permission() {}

    public Permission(PermissionType name, String description) {
        this.name = name;
        this.description = description;
    }

    public PermissionType getName() { return name; }
    public void setName(PermissionType name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
