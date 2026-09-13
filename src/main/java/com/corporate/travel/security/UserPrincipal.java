package com.corporate.travel.security;

import com.corporate.travel.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private Long id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private String fullName;
    private Long organizationId;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal() {}

    public UserPrincipal(Long id, String username, String email, String password, String fullName, Long organizationId, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.organizationId = organizationId;
        this.authorities = authorities;
    }

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserPrincipal(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getFullName(),
                user.getOrganization() != null ? user.getOrganization().getId() : null,
                authorities
        );
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }

    @Override
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @Override
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) { this.authorities = authorities; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    public static UserPrincipalBuilder builder() { return new UserPrincipalBuilder(); }

    public static class UserPrincipalBuilder {
        private Long id;
        private String username;
        private String email;
        private String password;
        private String fullName;
        private Long organizationId;
        private Collection<? extends GrantedAuthority> authorities;

        public UserPrincipalBuilder id(Long id) { this.id = id; return this; }
        public UserPrincipalBuilder username(String username) { this.username = username; return this; }
        public UserPrincipalBuilder email(String email) { this.email = email; return this; }
        public UserPrincipalBuilder password(String password) { this.password = password; return this; }
        public UserPrincipalBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public UserPrincipalBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public UserPrincipalBuilder authorities(Collection<? extends GrantedAuthority> authorities) { this.authorities = authorities; return this; }

        public UserPrincipal build() {
            return new UserPrincipal(id, username, email, password, fullName, organizationId, authorities);
        }
    }
}
