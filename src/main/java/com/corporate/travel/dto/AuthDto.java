package com.corporate.travel.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public class AuthDto {

    public static class LoginRequest {
        @NotBlank
        private String usernameOrEmail;
        @NotBlank
        private String password;

    public LoginRequest() {}

    public LoginRequest(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }

    public String getUsernameOrEmail() { return usernameOrEmail; }

    public void setUsernameOrEmail(String usernameOrEmail) { this.usernameOrEmail = usernameOrEmail; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public static LoginRequestBuilder builder() { return new LoginRequestBuilder(); }

    public static class LoginRequestBuilder {
        private String usernameOrEmail;
        private String password;

        public LoginRequestBuilder usernameOrEmail(String usernameOrEmail) { this.usernameOrEmail = usernameOrEmail; return this; }
        public LoginRequestBuilder password(String password) { this.password = password; return this; }

        public LoginRequest build() {
            LoginRequest obj = new LoginRequest();
            obj.setUsernameOrEmail(this.usernameOrEmail);
            obj.setPassword(this.password);
            return obj;
        }
    }
    }

    public static class RegisterRequest {
        @NotBlank
        private String username;
        @NotBlank
        @Email
        private String email;
        @NotBlank
        private String password;
        @NotBlank
        private String firstName;
        @NotBlank
        private String lastName;
        private String phone;
        private String role;
        private Long organizationId;

    public RegisterRequest() {}

    public RegisterRequest(String username, String email, String password, String firstName, String lastName, String phone, String role, Long organizationId) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
        this.organizationId = organizationId;
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

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public Long getOrganizationId() { return organizationId; }

    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }

    public static RegisterRequestBuilder builder() { return new RegisterRequestBuilder(); }

    public static class RegisterRequestBuilder {
        private String username;
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private String phone;
        private String role;
        private Long organizationId;

        public RegisterRequestBuilder username(String username) { this.username = username; return this; }
        public RegisterRequestBuilder email(String email) { this.email = email; return this; }
        public RegisterRequestBuilder password(String password) { this.password = password; return this; }
        public RegisterRequestBuilder firstName(String firstName) { this.firstName = firstName; return this; }
        public RegisterRequestBuilder lastName(String lastName) { this.lastName = lastName; return this; }
        public RegisterRequestBuilder phone(String phone) { this.phone = phone; return this; }
        public RegisterRequestBuilder role(String role) { this.role = role; return this; }
        public RegisterRequestBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }

        public RegisterRequest build() {
            RegisterRequest obj = new RegisterRequest();
            obj.setUsername(this.username);
            obj.setEmail(this.email);
            obj.setPassword(this.password);
            obj.setFirstName(this.firstName);
            obj.setLastName(this.lastName);
            obj.setPhone(this.phone);
            obj.setRole(this.role);
            obj.setOrganizationId(this.organizationId);
            return obj;
        }
    }
    }

    public static class AuthResponse {
        private String accessToken;

        private String tokenType = "Bearer";
        private Long userId;
        private String username;
        private String email;
        private String fullName;
        private Long organizationId;
        private String organizationName;
        private Set<String> roles;

    public AuthResponse() {}

    public AuthResponse(String accessToken, String tokenType, Long userId, String username, String email, String fullName, Long organizationId, String organizationName, Set<String> roles) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.roles = roles;
    }

    public String getAccessToken() { return accessToken; }

    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

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

    public static AuthResponseBuilder builder() { return new AuthResponseBuilder(); }

    public static class AuthResponseBuilder {
        private String accessToken;
        private String tokenType = "Bearer";
        private Long userId;
        private String username;
        private String email;
        private String fullName;
        private Long organizationId;
        private String organizationName;
        private Set<String> roles;

        public AuthResponseBuilder accessToken(String accessToken) { this.accessToken = accessToken; return this; }
        public AuthResponseBuilder tokenType(String tokenType) { this.tokenType = tokenType; return this; }
        public AuthResponseBuilder userId(Long userId) { this.userId = userId; return this; }
        public AuthResponseBuilder username(String username) { this.username = username; return this; }
        public AuthResponseBuilder email(String email) { this.email = email; return this; }
        public AuthResponseBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public AuthResponseBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public AuthResponseBuilder organizationName(String organizationName) { this.organizationName = organizationName; return this; }
        public AuthResponseBuilder roles(Set<String> roles) { this.roles = roles; return this; }

        public AuthResponse build() {
            AuthResponse obj = new AuthResponse();
            obj.setAccessToken(this.accessToken);
            obj.setTokenType(this.tokenType);
            obj.setUserId(this.userId);
            obj.setUsername(this.username);
            obj.setEmail(this.email);
            obj.setFullName(this.fullName);
            obj.setOrganizationId(this.organizationId);
            obj.setOrganizationName(this.organizationName);
            obj.setRoles(this.roles);
            return obj;
        }
    }
    }

    public static class SwitchRoleRequest {
        private String role;

    public SwitchRoleRequest() {}

    public SwitchRoleRequest(String role) {
        this.role = role;
    }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public static SwitchRoleRequestBuilder builder() { return new SwitchRoleRequestBuilder(); }

    public static class SwitchRoleRequestBuilder {
        private String role;

        public SwitchRoleRequestBuilder role(String role) { this.role = role; return this; }

        public SwitchRoleRequest build() {
            SwitchRoleRequest obj = new SwitchRoleRequest();
            obj.setRole(this.role);
            return obj;
        }
    }
    }


    public AuthDto() {}

    public static AuthDtoBuilder builder() { return new AuthDtoBuilder(); }

    public static class AuthDtoBuilder {


        public AuthDto build() {
            AuthDto obj = new AuthDto();
            return obj;
        }
    }
}
