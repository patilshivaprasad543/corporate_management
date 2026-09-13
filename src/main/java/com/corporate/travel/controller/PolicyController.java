package com.corporate.travel.controller;

import com.corporate.travel.common.ApiResponse;
import com.corporate.travel.dto.PolicyDto;
import com.corporate.travel.security.AuthenticatedUser;
import com.corporate.travel.security.UserPrincipal;
import com.corporate.travel.service.PolicyViolationService;
import com.corporate.travel.service.TravelPolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
@Tag(name = "Travel Policies", description = "Company travel policy management and evaluation")
public class PolicyController {

    private final TravelPolicyService travelPolicyService;
    private final PolicyViolationService policyViolationService;

    public PolicyController(TravelPolicyService travelPolicyService,
                              PolicyViolationService policyViolationService) {
        this.travelPolicyService = travelPolicyService;
        this.policyViolationService = policyViolationService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PERM_POLICY_VIEW')")
    @Operation(summary = "List travel policies for the current company scope")
    public ResponseEntity<ApiResponse<List<PolicyDto.PolicyResponse>>> listPolicies() {
        return ResponseEntity.ok(ApiResponse.ok(travelPolicyService.listPolicies(), "Policies retrieved"));
    }

    @GetMapping("/violations")
    @PreAuthorize("hasAuthority('PERM_POLICY_VIEW')")
    @Operation(summary = "List policy violations for the current company scope")
    public ResponseEntity<ApiResponse<List<PolicyDto.ViolationResponse>>> listViolations() {
        return ResponseEntity.ok(ApiResponse.ok(policyViolationService.listViolations(), "Violations retrieved"));
    }

    @GetMapping("/violations/request/{travelRequestId}")
    @PreAuthorize("hasAuthority('PERM_POLICY_VIEW')")
    @Operation(summary = "List policy violations for a specific travel request")
    public ResponseEntity<ApiResponse<List<PolicyDto.ViolationResponse>>> listRequestViolations(
            @PathVariable Long travelRequestId) {
        return ResponseEntity.ok(ApiResponse.ok(
                policyViolationService.listViolationsForRequest(travelRequestId), "Violations retrieved"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PERM_POLICY_VIEW')")
    @Operation(summary = "Get travel policy by ID")
    public ResponseEntity<ApiResponse<PolicyDto.PolicyResponse>> getPolicy(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(travelPolicyService.getPolicy(id), "Policy retrieved"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PERM_POLICY_MANAGE')")
    @Operation(summary = "Create or replace active travel policy for a company")
    public ResponseEntity<ApiResponse<PolicyDto.PolicyResponse>> createPolicy(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody PolicyDto.CreatePolicyRequest request) {
        String actor = principal != null ? principal.getEmail() : "system";
        return ResponseEntity.ok(ApiResponse.ok(travelPolicyService.createPolicy(request, actor), "Policy created"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PERM_POLICY_MANAGE')")
    @Operation(summary = "Update travel policy")
    public ResponseEntity<ApiResponse<PolicyDto.PolicyResponse>> updatePolicy(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody PolicyDto.UpdatePolicyRequest request) {
        String actor = principal != null ? principal.getEmail() : "system";
        return ResponseEntity.ok(ApiResponse.ok(travelPolicyService.updatePolicy(id, request, actor), "Policy updated"));
    }

    @PostMapping("/evaluate")
    @PreAuthorize("hasAuthority('PERM_POLICY_VIEW')")
    @Operation(summary = "Evaluate a travel request against company policy before submission")
    public ResponseEntity<ApiResponse<PolicyDto.EvaluationResponse>> evaluate(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody PolicyDto.EvaluateRequest request) {
        Long userId = AuthenticatedUser.requireId(principal);
        return ResponseEntity.ok(ApiResponse.ok(
                travelPolicyService.evaluate(request, userId), "Policy evaluation complete"));
    }

}
