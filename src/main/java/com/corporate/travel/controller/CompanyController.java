package com.corporate.travel.controller;

import com.corporate.travel.common.ApiResponse;
import com.corporate.travel.dto.OrganizationDto;
import com.corporate.travel.security.UserPrincipal;
import com.corporate.travel.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@Tag(name = "Companies", description = "Company management for super administrators and company viewers")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAuthority('PERM_SYSTEM_ADMIN')")
    @Operation(summary = "System-wide company statistics (super admin)")
    public ResponseEntity<ApiResponse<OrganizationDto.SystemStats>> getSystemStats() {
        return ResponseEntity.ok(ApiResponse.ok(companyService.getSystemStats(), "System statistics retrieved"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PERM_COMPANY_VIEW')")
    @Operation(summary = "List companies (all for super admin, own company for others)")
    public ResponseEntity<ApiResponse<List<OrganizationDto.CompanyResponse>>> listCompanies() {
        return ResponseEntity.ok(ApiResponse.ok(companyService.listCompanies(), "Companies retrieved"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PERM_COMPANY_VIEW')")
    @Operation(summary = "Get company details by ID")
    public ResponseEntity<ApiResponse<OrganizationDto.CompanyResponse>> getCompany(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(companyService.getCompany(id), "Company retrieved"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PERM_COMPANY_MANAGE')")
    @Operation(summary = "Create a new company (super admin)")
    public ResponseEntity<ApiResponse<OrganizationDto.CompanyResponse>> createCompany(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody OrganizationDto.CreateCompanyRequest request) {
        String actor = principal != null ? principal.getEmail() : "system";
        return ResponseEntity.ok(ApiResponse.ok(companyService.createCompany(request, actor), "Company created"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PERM_COMPANY_MANAGE')")
    @Operation(summary = "Update company details (super admin)")
    public ResponseEntity<ApiResponse<OrganizationDto.CompanyResponse>> updateCompany(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody OrganizationDto.UpdateCompanyRequest request) {
        String actor = principal != null ? principal.getEmail() : "system";
        return ResponseEntity.ok(ApiResponse.ok(companyService.updateCompany(id, request, actor), "Company updated"));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('PERM_COMPANY_MANAGE')")
    @Operation(summary = "Activate a company")
    public ResponseEntity<ApiResponse<OrganizationDto.CompanyResponse>> activateCompany(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        String actor = principal != null ? principal.getEmail() : "system";
        return ResponseEntity.ok(ApiResponse.ok(companyService.activateCompany(id, actor), "Company activated"));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('PERM_COMPANY_MANAGE')")
    @Operation(summary = "Deactivate a company")
    public ResponseEntity<ApiResponse<OrganizationDto.CompanyResponse>> deactivateCompany(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        String actor = principal != null ? principal.getEmail() : "system";
        return ResponseEntity.ok(ApiResponse.ok(companyService.deactivateCompany(id, actor), "Company deactivated"));
    }
}
