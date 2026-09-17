package com.corporate.travel.controller;

import com.corporate.travel.common.ApiResponse;
import com.corporate.travel.dto.AuthDto;
import com.corporate.travel.security.UserPrincipal;
import com.corporate.travel.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User registration, login, JWT token issuance, and credential management")
public class AuthController {
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and issue JWT token")
    public ResponseEntity<ApiResponse<AuthDto.AuthResponse>> login(@Valid @RequestBody AuthDto.LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(authService.login(request), "Login successful"));
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new corporate employee / manager")
    public ResponseEntity<ApiResponse<AuthDto.AuthResponse>> register(@Valid @RequestBody AuthDto.RegisterRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(authService.register(request), "User registered successfully"));
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change the authenticated user's password")
    public ResponseEntity<ApiResponse<Object>> changePassword(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody Map<String, String> request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Authentication is required"));
        }
        String currentPassword = request != null ? request.get("currentPassword") : null;
        String newPassword = request != null ? request.get("newPassword") : null;
        authService.changePassword(principal.getId(), currentPassword, newPassword);
        return ResponseEntity.ok(ApiResponse.ok(null, "Password changed successfully. Please sign in again."));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated user profile")
    public ResponseEntity<ApiResponse<Object>> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.ok(ApiResponse.ok(null, "Not authenticated"));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", principal.getId());
        map.put("username", principal.getUsername());
        map.put("email", principal.getEmail());
        map.put("fullName", principal.getFullName());
        map.put("organizationId", principal.getOrganizationId());
        map.put("authorities", principal.getAuthorities());
        return ResponseEntity.ok(ApiResponse.ok(map, "Profile retrieved"));
    }
}
