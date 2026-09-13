package com.corporate.travel.controller;

import com.corporate.travel.common.ApiResponse;
import com.corporate.travel.dto.TravelRequestDto;
import com.corporate.travel.security.UserPrincipal;
import com.corporate.travel.security.AuthenticatedUser;
import com.corporate.travel.service.TravelRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/travel-requests")
@Tag(name = "Travel Requests", description = "Create, view, and track corporate travel requests with policy evaluation")
public class TravelRequestController {
    public TravelRequestController(TravelRequestService travelRequestService) {
        this.travelRequestService = travelRequestService;
    }


    private final TravelRequestService travelRequestService;

    @PostMapping
    @PreAuthorize("hasAuthority('PERM_TRAVEL_REQUEST_CREATE')")
    @Operation(summary = "Create and submit a new travel request")
    public ResponseEntity<ApiResponse<TravelRequestDto.Response>> createRequest(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody TravelRequestDto.CreateRequest request) {
        Long userId = AuthenticatedUser.requireId(principal);
        return ResponseEntity.ok(ApiResponse.ok(travelRequestService.createRequest(userId, request), "Travel request submitted"));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('PERM_TRAVEL_REQUEST_VIEW')")
    @Operation(summary = "List current user's travel requests")
    public ResponseEntity<ApiResponse<List<TravelRequestDto.Response>>> getMyRequests(
            @AuthenticationPrincipal UserPrincipal principal) {
        Long userId = AuthenticatedUser.requireId(principal);
        return ResponseEntity.ok(ApiResponse.ok(travelRequestService.getMyRequests(userId), "User requests retrieved"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PERM_TRAVEL_REQUEST_VIEW')")
    @Operation(summary = "List all organizational travel requests (Manager/Finance/Admin)")
    public ResponseEntity<ApiResponse<List<TravelRequestDto.Response>>> getAllRequests() {
        return ResponseEntity.ok(ApiResponse.ok(travelRequestService.getAllRequests(), "All requests retrieved"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PERM_TRAVEL_REQUEST_VIEW')")
    @Operation(summary = "Get travel request details by ID")
    public ResponseEntity<ApiResponse<TravelRequestDto.Response>> getRequestById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ApiResponse.ok(travelRequestService.getRequestById(id), "Request details retrieved"));
    }

}
