package com.corporate.travel.controller;

import com.corporate.travel.common.ApiResponse;
import com.corporate.travel.dto.AuthDto;
import com.corporate.travel.dto.OrganizationDto;
import com.corporate.travel.security.UserPrincipal;
import com.corporate.travel.service.AuthService;
import com.corporate.travel.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Portal-based login, registration, OTP, JWT refresh, and logout")
public class AuthController {

    private final AuthService authService;
    private final OrganizationService organizationService;

    public AuthController(AuthService authService, OrganizationService organizationService) {
        this.authService = authService;
        this.organizationService = organizationService;
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user for a specific portal and issue JWT + refresh token")
    public ResponseEntity<ApiResponse<AuthDto.AuthResponse>> login(@Valid @RequestBody AuthDto.LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(authService.login(request), "Login successful"));
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new employee (OTP verification required before login)")
    public ResponseEntity<ApiResponse<AuthDto.AuthResponse>> register(@Valid @RequestBody AuthDto.RegisterRequest request) {
        AuthDto.AuthResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.ok(response, response.getMessage()));
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Verify email OTP after registration")
    public ResponseEntity<ApiResponse<Void>> verifyOtp(@Valid @RequestBody AuthDto.OtpVerifyRequest request) {
        authService.verifyOtp(request);
        return ResponseEntity.ok(ApiResponse.ok(null, "Email verified successfully"));
    }

    @PostMapping("/resend-otp")
    @Operation(summary = "Resend OTP for email verification")
    public ResponseEntity<ApiResponse<Void>> resendOtp(@RequestBody java.util.Map<String, String> body) {
        authService.resendOtp(body.get("email"));
        return ResponseEntity.ok(ApiResponse.ok(null, "OTP sent"));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token using refresh token")
    public ResponseEntity<ApiResponse<AuthDto.AuthResponse>> refresh(@Valid @RequestBody AuthDto.RefreshRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(authService.refresh(request), "Token refreshed"));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout and revoke refresh token")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestBody(required = false) AuthDto.LogoutRequest request) {
        String token = request != null ? request.getRefreshToken() : null;
        authService.logout(token);
        return ResponseEntity.ok(ApiResponse.ok(null, "Logged out successfully"));
    }

    @GetMapping("/companies")
    @Operation(summary = "List active companies for employee registration")
    public ResponseEntity<ApiResponse<java.util.List<OrganizationDto.CompanyOption>>> listCompanies() {
        return ResponseEntity.ok(ApiResponse.ok(organizationService.listActiveCompanies(), "Companies retrieved"));
    }

    @GetMapping("/companies/{organizationId}/departments")
    @Operation(summary = "List departments for a company during registration")
    public ResponseEntity<ApiResponse<java.util.List<OrganizationDto.DepartmentOption>>> listDepartments(
            @PathVariable Long organizationId) {
        return ResponseEntity.ok(ApiResponse.ok(organizationService.listDepartments(organizationId), "Departments retrieved"));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated user profile with permissions")
    public ResponseEntity<ApiResponse<AuthDto.MeResponse>> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.ok(ApiResponse.ok(null, "Not authenticated"));
        }
        return ResponseEntity.ok(ApiResponse.ok(authService.getCurrentUser(principal), "Profile retrieved"));
    }
}
