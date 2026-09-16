package com.corporate.travel.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/index.html", "/app.js", "/styles.css", "/favicon.ico", "/static/**", "/assets/**",
                        "/swagger-ui/**", "/swagger-ui.html", "/api-docs/**", "/api/auth/**", "/actuator/health").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/search/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/travel-requests/my").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/travel-requests", "/api/travel-requests/*")
                    .hasAnyAuthority("ROLE_TRAVEL_MANAGER", "ROLE_FINANCE", "ROLE_COMPANY_ADMIN", "ROLE_SUPER_ADMIN", "ROLE_AUDITOR")
                .requestMatchers("/api/approvals/**").hasAnyAuthority("ROLE_APPROVER", "ROLE_FINANCE", "ROLE_COMPANY_ADMIN", "ROLE_SUPER_ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/bookings").hasAnyAuthority("ROLE_EMPLOYEE", "ROLE_TRAVEL_MANAGER", "ROLE_COMPANY_ADMIN", "ROLE_SUPER_ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/bookings/my").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/bookings").hasAnyAuthority("ROLE_TRAVEL_MANAGER", "ROLE_FINANCE", "ROLE_COMPANY_ADMIN", "ROLE_SUPER_ADMIN", "ROLE_AUDITOR")
                .requestMatchers("/api/expenses/*/approve", "/api/expenses/*/reject",
                        "/api/expenses/*/reimbursement/start", "/api/expenses/*/reimbursement/complete",
                        "/api/expenses/*/reimbursement/fail")
                    .hasAnyAuthority("ROLE_FINANCE", "ROLE_COMPANY_ADMIN", "ROLE_SUPER_ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/expenses").hasAnyAuthority("ROLE_FINANCE", "ROLE_COMPANY_ADMIN", "ROLE_SUPER_ADMIN", "ROLE_AUDITOR")
                .requestMatchers("/api/admin/**").hasAnyAuthority("ROLE_SUPER_ADMIN", "ROLE_COMPANY_ADMIN", "ROLE_HR")
                .requestMatchers("/api/analytics/**").hasAnyAuthority("ROLE_SUPER_ADMIN", "ROLE_COMPANY_ADMIN", "ROLE_APPROVER", "ROLE_FINANCE", "ROLE_TRAVEL_MANAGER", "ROLE_EMPLOYEE", "ROLE_FINANCE_APPROVER", "ROLE_DEPARTMENT_HEAD", "ROLE_AUDITOR")
                .requestMatchers("/api/audit/**").hasAnyAuthority("ROLE_SUPER_ADMIN", "ROLE_COMPANY_ADMIN", "ROLE_FINANCE")
                .requestMatchers("/api/risk/**").hasAnyAuthority("ROLE_SUPER_ADMIN", "ROLE_COMPANY_ADMIN", "ROLE_TRAVEL_MANAGER", "ROLE_APPROVER", "ROLE_EMPLOYEE")
                .anyRequest().authenticated());
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
