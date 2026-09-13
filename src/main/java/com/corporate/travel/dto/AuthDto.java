package com.corporate.travel.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.Set;

public class AuthDto {

    public static class LoginRequest {
        @Email
        private String email;

        @JsonAlias("usernameOrEmail")
        private String usernameOrEmail;

        @NotBlank
        private String password;

        @NotBlank
        private String portal;

        private Long organizationId;

        public LoginRequest() {}

        public String resolveIdentifier() {
            if (email != null && !email.isBlank()) return email.trim();
            if (usernameOrEmail != null && !usernameOrEmail.isBlank()) return usernameOrEmail.trim();
            throw new IllegalArgumentException("Email is required");
        }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getUsernameOrEmail() { return usernameOrEmail; }
        public void setUsernameOrEmail(String usernameOrEmail) { this.usernameOrEmail = usernameOrEmail; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getPortal() { return portal; }
        public void setPortal(String portal) { this.portal = portal; }
        public Long getOrganizationId() { return organizationId; }
        public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }

        public static LoginRequestBuilder builder() { return new LoginRequestBuilder(); }

        public static class LoginRequestBuilder {
            private String email;
            private String usernameOrEmail;
            private String password;
            private String portal;
            private Long organizationId;

            public LoginRequestBuilder email(String email) { this.email = email; return this; }
            public LoginRequestBuilder usernameOrEmail(String v) { this.usernameOrEmail = v; return this; }
            public LoginRequestBuilder password(String password) { this.password = password; return this; }
            public LoginRequestBuilder portal(String portal) { this.portal = portal; return this; }
            public LoginRequestBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }

            public LoginRequest build() {
                LoginRequest obj = new LoginRequest();
                obj.setEmail(this.email);
                obj.setUsernameOrEmail(this.usernameOrEmail);
                obj.setPassword(this.password);
                obj.setPortal(this.portal);
                obj.setOrganizationId(this.organizationId);
                return obj;
            }
        }
    }

    public static class RegisterRequest {
        @NotBlank private String username;
        @NotBlank @Email private String email;
        @NotBlank private String password;
        @NotBlank private String confirmPassword;
        @NotBlank private String firstName;
        @NotBlank private String lastName;
        @NotBlank private String employeeId;
        private String phone;
        private Long organizationId;
        private Long departmentId;

        public RegisterRequest() {}

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getConfirmPassword() { return confirmPassword; }
        public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public Long getOrganizationId() { return organizationId; }
        public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
        public String getEmployeeId() { return employeeId; }
        public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
        public Long getDepartmentId() { return departmentId; }
        public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    }

    public static class OtpVerifyRequest {
        @NotBlank @Email private String email;
        @NotBlank private String otp;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getOtp() { return otp; }
        public void setOtp(String otp) { this.otp = otp; }
    }

    public static class RefreshRequest {
        @NotBlank private String refreshToken;

        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    }

    public static class LogoutRequest {
        private String refreshToken;

        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    }

    public static class AuthResponse {
        private String accessToken;
        private String refreshToken;
        private String tokenType = "Bearer";
        private Long userId;
        private String username;
        private String email;
        private String fullName;
        private Long organizationId;
        private String organizationName;
        private Set<String> roles;
        private String portal;
        private Boolean emailVerified;
        private String message;

        public AuthResponse() {}

        public String getAccessToken() { return accessToken; }
        public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
        public String getTokenType() { return tokenType; }
        public void setTokenType(String tokenType) { this.tokenType = tokenType; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public Long getOrganizationId() { return organizationId; }
        public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
        public String getOrganizationName() { return organizationName; }
        public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }
        public Set<String> getRoles() { return roles; }
        public void setRoles(Set<String> roles) { this.roles = roles; }
        public String getPortal() { return portal; }
        public void setPortal(String portal) { this.portal = portal; }
        public Boolean getEmailVerified() { return emailVerified; }
        public void setEmailVerified(Boolean emailVerified) { this.emailVerified = emailVerified; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public static AuthResponseBuilder builder() { return new AuthResponseBuilder(); }

        public static class AuthResponseBuilder {
            private String accessToken;
            private String refreshToken;
            private String tokenType = "Bearer";
            private Long userId;
            private String username;
            private String email;
            private String fullName;
            private Long organizationId;
            private String organizationName;
            private Set<String> roles;
            private String portal;
            private Boolean emailVerified;
            private String message;

            public AuthResponseBuilder accessToken(String v) { this.accessToken = v; return this; }
            public AuthResponseBuilder refreshToken(String v) { this.refreshToken = v; return this; }
            public AuthResponseBuilder tokenType(String v) { this.tokenType = v; return this; }
            public AuthResponseBuilder userId(Long v) { this.userId = v; return this; }
            public AuthResponseBuilder username(String v) { this.username = v; return this; }
            public AuthResponseBuilder email(String v) { this.email = v; return this; }
            public AuthResponseBuilder fullName(String v) { this.fullName = v; return this; }
            public AuthResponseBuilder organizationId(Long v) { this.organizationId = v; return this; }
            public AuthResponseBuilder organizationName(String v) { this.organizationName = v; return this; }
            public AuthResponseBuilder roles(Set<String> v) { this.roles = v; return this; }
            public AuthResponseBuilder portal(String v) { this.portal = v; return this; }
            public AuthResponseBuilder emailVerified(Boolean v) { this.emailVerified = v; return this; }
            public AuthResponseBuilder message(String v) { this.message = v; return this; }

            public AuthResponse build() {
                AuthResponse obj = new AuthResponse();
                obj.setAccessToken(accessToken);
                obj.setRefreshToken(refreshToken);
                obj.setTokenType(tokenType);
                obj.setUserId(userId);
                obj.setUsername(username);
                obj.setEmail(email);
                obj.setFullName(fullName);
                obj.setOrganizationId(organizationId);
                obj.setOrganizationName(organizationName);
                obj.setRoles(roles);
                obj.setPortal(portal);
                obj.setEmailVerified(emailVerified);
                obj.setMessage(message);
                return obj;
            }
        }
    }

    public static class MeResponse {
        private Long id;
        private String username;
        private String email;
        private String fullName;
        private Long organizationId;
        private String organizationName;
        private Set<String> roles;
        private Set<String> permissions;
        private Boolean emailVerified;
        private LocalDateTime lastLoginAt;

        public static MeResponseBuilder builder() { return new MeResponseBuilder(); }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public Long getOrganizationId() { return organizationId; }
        public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
        public String getOrganizationName() { return organizationName; }
        public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }
        public Set<String> getRoles() { return roles; }
        public void setRoles(Set<String> roles) { this.roles = roles; }
        public Set<String> getPermissions() { return permissions; }
        public void setPermissions(Set<String> permissions) { this.permissions = permissions; }
        public Boolean getEmailVerified() { return emailVerified; }
        public void setEmailVerified(Boolean emailVerified) { this.emailVerified = emailVerified; }
        public LocalDateTime getLastLoginAt() { return lastLoginAt; }
        public void setLastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }

        public static class MeResponseBuilder {
            private Long id;
            private String username;
            private String email;
            private String fullName;
            private Long organizationId;
            private String organizationName;
            private Set<String> roles;
            private Set<String> permissions;
            private Boolean emailVerified;
            private LocalDateTime lastLoginAt;

            public MeResponseBuilder id(Long v) { this.id = v; return this; }
            public MeResponseBuilder username(String v) { this.username = v; return this; }
            public MeResponseBuilder email(String v) { this.email = v; return this; }
            public MeResponseBuilder fullName(String v) { this.fullName = v; return this; }
            public MeResponseBuilder organizationId(Long v) { this.organizationId = v; return this; }
            public MeResponseBuilder organizationName(String v) { this.organizationName = v; return this; }
            public MeResponseBuilder roles(Set<String> v) { this.roles = v; return this; }
            public MeResponseBuilder permissions(Set<String> v) { this.permissions = v; return this; }
            public MeResponseBuilder emailVerified(Boolean v) { this.emailVerified = v; return this; }
            public MeResponseBuilder lastLoginAt(LocalDateTime v) { this.lastLoginAt = v; return this; }

            public MeResponse build() {
                MeResponse obj = new MeResponse();
                obj.setId(id);
                obj.setUsername(username);
                obj.setEmail(email);
                obj.setFullName(fullName);
                obj.setOrganizationId(organizationId);
                obj.setOrganizationName(organizationName);
                obj.setRoles(roles);
                obj.setPermissions(permissions);
                obj.setEmailVerified(emailVerified);
                obj.setLastLoginAt(lastLoginAt);
                return obj;
            }
        }
    }
}
