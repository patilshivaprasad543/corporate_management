package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import com.corporate.travel.entity.enums.RoleType;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")

public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 50)
    private RoleType name;

    @Column(length = 255)
    private String description;


    public Role() {}

    public Role(RoleType name, String description) {
        this.name = name;
        this.description = description;
    }

    public RoleType getName() { return name; }

    public void setName(RoleType name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public static RoleBuilder builder() { return new RoleBuilder(); }

    public static class RoleBuilder {
        private Long id;
        private RoleType name;
        private String description;

        public RoleBuilder id(Long id) { this.id = id; return this; }
        public RoleBuilder name(RoleType name) { this.name = name; return this; }
        public RoleBuilder description(String description) { this.description = description; return this; }

        public Role build() {
            Role obj = new Role();
            obj.setId(this.id);
            obj.setName(this.name);
            obj.setDescription(this.description);
            return obj;
        }
    }
}
