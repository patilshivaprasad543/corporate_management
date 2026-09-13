package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")

public class User extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(length = 50)
    private String phone;

    @Column(name = "is_active")

    private Boolean active = true;

    @Column(name = "is_email_verified")

    private Boolean emailVerified = true;

    @Column(name = "two_factor_enabled")

    private Boolean twoFactorEnabled = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )

    private Set<Role> roles = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    public String getFullName() {
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }


    public User() {}

    public User(String username, String email, String password, String firstName, String lastName, String phone, Boolean active, Boolean emailVerified, Boolean twoFactorEnabled, Set<Role> roles, Organization organization) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.active = active;
        this.emailVerified = emailVerified;
        this.twoFactorEnabled = twoFactorEnabled;
        this.roles = roles;
        this.organization = organization;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public Boolean isActive() { return active; }

    public Boolean getActive() { return active; }

    public void setActive(Boolean active) { this.active = active; }

    public Boolean isEmailVerified() { return emailVerified; }

    public Boolean getEmailVerified() { return emailVerified; }

    public void setEmailVerified(Boolean emailVerified) { this.emailVerified = emailVerified; }

    public Boolean isTwoFactorEnabled() { return twoFactorEnabled; }

    public Boolean getTwoFactorEnabled() { return twoFactorEnabled; }

    public void setTwoFactorEnabled(Boolean twoFactorEnabled) { this.twoFactorEnabled = twoFactorEnabled; }

    public Set<Role> getRoles() { return roles; }

    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public Organization getOrganization() { return organization; }

    public void setOrganization(Organization organization) { this.organization = organization; }

    public static UserBuilder builder() { return new UserBuilder(); }

    public static class UserBuilder {
        private Long id;
        private String username;
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private String phone;
        private Boolean active = true;
        private Boolean emailVerified = true;
        private Boolean twoFactorEnabled = false;
        private Set<Role> roles = new HashSet<>();
        private Organization organization;

        public UserBuilder id(Long id) { this.id = id; return this; }
        public UserBuilder username(String username) { this.username = username; return this; }
        public UserBuilder email(String email) { this.email = email; return this; }
        public UserBuilder password(String password) { this.password = password; return this; }
        public UserBuilder firstName(String firstName) { this.firstName = firstName; return this; }
        public UserBuilder lastName(String lastName) { this.lastName = lastName; return this; }
        public UserBuilder phone(String phone) { this.phone = phone; return this; }
        public UserBuilder active(Boolean active) { this.active = active; return this; }
        public UserBuilder emailVerified(Boolean emailVerified) { this.emailVerified = emailVerified; return this; }
        public UserBuilder twoFactorEnabled(Boolean twoFactorEnabled) { this.twoFactorEnabled = twoFactorEnabled; return this; }
        public UserBuilder roles(Set<Role> roles) { this.roles = roles; return this; }
        public UserBuilder organization(Organization organization) { this.organization = organization; return this; }

        public User build() {
            User obj = new User();
            obj.setId(this.id);
            obj.setUsername(this.username);
            obj.setEmail(this.email);
            obj.setPassword(this.password);
            obj.setFirstName(this.firstName);
            obj.setLastName(this.lastName);
            obj.setPhone(this.phone);
            obj.setActive(this.active);
            obj.setEmailVerified(this.emailVerified);
            obj.setTwoFactorEnabled(this.twoFactorEnabled);
            obj.setRoles(this.roles);
            obj.setOrganization(this.organization);
            return obj;
        }
    }
}
