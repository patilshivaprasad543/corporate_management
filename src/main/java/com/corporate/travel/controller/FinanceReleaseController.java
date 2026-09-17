package com.corporate.travel.controller;

import com.corporate.travel.common.ApiResponse;
import com.corporate.travel.dto.FinanceReleaseDto;
import com.corporate.travel.entity.FinanceRelease;
import com.corporate.travel.entity.TravelBudget;
import com.corporate.travel.entity.TravelRequest;
import com.corporate.travel.repository.FinanceReleaseRepository;
import com.corporate.travel.repository.TravelBudgetRepository;
import com.corporate.travel.repository.TravelRequestRepository;
import com.corporate.travel.repository.UserRepository;
import com.corporate.travel.security.AuthenticatedUser;
import com.corporate.travel.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/finance/releases")
public class FinanceReleaseController {
    private final FinanceReleaseRepository releases;
    private final TravelRequestRepository requests;
    private final TravelBudgetRepository budgets;
    private final UserRepository users;

    public FinanceReleaseController(FinanceReleaseRepository releases, TravelRequestRepository requests,
                                    TravelBudgetRepository budgets, UserRepository users) {
        this.releases=releases; this.requests=requests; this.budgets=budgets; this.users=users;
    }

    @PostMapping("/{travelRequestId}/request")
    public ResponseEntity<ApiResponse<FinanceReleaseDto.Release>> requestRelease(
            @AuthenticationPrincipal UserPrincipal principal, @PathVariable Long travelRequestId,
            @Valid @RequestBody FinanceReleaseDto.Request body) {
        Long uid=AuthenticatedUser.requireId(principal); require(principal,"ROLE_HR","ROLE_COMPANY_ADMIN","ROLE_SUPER_ADMIN");
        TravelRequest r=requests.findById(travelRequestId).orElseThrow();
        TravelBudget b=budgets.findByTravelRequestId(travelRequestId).orElseThrow(() -> new IllegalStateException("HR budget is not allocated"));
        if (b.getStatus()!=TravelBudget.BudgetStatus.ALLOCATED && b.getStatus()!=TravelBudget.BudgetStatus.HR_APPROVED)
            throw new IllegalStateException("Budget is not authorized for release");
        if(body.amount().compareTo(b.getAllocatedAmount())>0) throw new IllegalArgumentException("Release exceeds HR allocated budget");
        FinanceRelease x=new FinanceRelease(); x.setTravelRequest(r); x.setRequestedBy(users.findById(uid).orElseThrow());
        x.setRequestedAmount(body.amount()); x.setRemarks(body.remarks()); x.setStatus(FinanceRelease.ReleaseStatus.AUTHORIZED);
        return ResponseEntity.ok(ApiResponse.ok(to(releases.save(x)),"Finance release authorized by HR command"));
    }

    @PostMapping("/{releaseId}/release")
    public ResponseEntity<ApiResponse<FinanceReleaseDto.Release>> release(
            @AuthenticationPrincipal UserPrincipal principal,@PathVariable Long releaseId) {
        Long uid=AuthenticatedUser.requireId(principal); require(principal,"ROLE_FINANCE","ROLE_COMPANY_ADMIN","ROLE_SUPER_ADMIN");
        FinanceRelease x=releases.findById(releaseId).orElseThrow();
        if(x.getStatus()!=FinanceRelease.ReleaseStatus.AUTHORIZED) throw new IllegalStateException("Finance can release only an HR-authorized request");
        x.setReleasedAmount(x.getRequestedAmount()); x.setReleasedBy(users.findById(uid).orElseThrow());
        x.setPaymentReference("PAY-"+UUID.randomUUID().toString().replace("-","").substring(0,12).toUpperCase());
        x.setStatus(FinanceRelease.ReleaseStatus.RELEASED);
        return ResponseEntity.ok(ApiResponse.ok(to(releases.save(x)),"Amount released by Finance"));
    }

    @GetMapping("/{travelRequestId}")
    public ResponseEntity<ApiResponse<List<FinanceReleaseDto.Release>>> list(@PathVariable Long travelRequestId) {
        return ResponseEntity.ok(ApiResponse.ok(releases.findByTravelRequestIdOrderByCreatedAtDesc(travelRequestId).stream().map(this::to).collect(Collectors.toList()),"Finance releases retrieved"));
    }
    private void require(UserPrincipal p,String... allowed){if(p.getAuthorities().stream().noneMatch(a->List.of(allowed).contains(a.getAuthority())))throw new AccessDeniedException("Role not authorized");}
    private FinanceReleaseDto.Release to(FinanceRelease x){return new FinanceReleaseDto.Release(x.getId(),x.getTravelRequest().getId(),x.getRequestedAmount(),x.getReleasedAmount(),x.getStatus().name(),x.getPaymentReference(),x.getRemarks());}
}
