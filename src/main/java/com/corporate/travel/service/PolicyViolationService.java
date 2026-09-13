package com.corporate.travel.service;

import com.corporate.travel.dto.PolicyDto;
import com.corporate.travel.entity.*;
import com.corporate.travel.repository.PolicyViolationRepository;
import com.corporate.travel.security.TenantAccessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PolicyViolationService {

    private final PolicyViolationRepository violationRepository;
    private final TenantAccessService tenantAccessService;

    public PolicyViolationService(PolicyViolationRepository violationRepository,
                                  TenantAccessService tenantAccessService) {
        this.violationRepository = violationRepository;
        this.tenantAccessService = tenantAccessService;
    }

    @Transactional
    public void persistViolations(Organization organization, TravelPolicy policy, User user,
                                TravelRequest travelRequest, List<PolicyDto.ViolationDetail> violations) {
        if (violations == null || violations.isEmpty()) {
            return;
        }
        for (PolicyDto.ViolationDetail detail : violations) {
            PolicyViolation record = new PolicyViolation();
            record.setOrganization(organization);
            record.setPolicy(policy);
            record.setUser(user);
            record.setTravelRequest(travelRequest);
            record.setViolationType(detail.getViolationType());
            record.setRuleName(detail.getRuleName());
            record.setRequestedAmount(detail.getRequestedAmount());
            record.setAllowedAmount(detail.getAllowedAmount());
            record.setDifferenceAmount(detail.getDifferenceAmount());
            record.setCurrencyCode(detail.getCurrencyCode() != null ? detail.getCurrencyCode() : "INR");
            record.setSeverity(detail.getSeverity());
            record.setExplanation(detail.getExplanation());
            record.setResolved(false);
            violationRepository.save(record);
        }
    }

    @Transactional(readOnly = true)
    public List<PolicyDto.ViolationResponse> listViolations() {
        Long scope = tenantAccessService.resolveOrganizationScope();
        List<PolicyViolation> records = scope == null
                ? violationRepository.findAll()
                : violationRepository.findByOrganization_IdOrderByCreatedAtDesc(scope);
        return records.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PolicyDto.ViolationResponse> listViolationsForRequest(Long travelRequestId) {
        return violationRepository.findByTravelRequest_IdOrderByCreatedAtDesc(travelRequestId)
                .stream().map(this::toResponse).toList();
    }

    private PolicyDto.ViolationResponse toResponse(PolicyViolation v) {
        PolicyDto.ViolationResponse r = new PolicyDto.ViolationResponse();
        r.setId(v.getId());
        r.setTravelRequestId(v.getTravelRequest() != null ? v.getTravelRequest().getId() : null);
        r.setViolationType(v.getViolationType());
        r.setRuleName(v.getRuleName());
        r.setRequestedAmount(v.getRequestedAmount());
        r.setAllowedAmount(v.getAllowedAmount());
        r.setDifferenceAmount(v.getDifferenceAmount());
        r.setSeverity(v.getSeverity());
        r.setExplanation(v.getExplanation());
        r.setResolved(v.getResolved());
        r.setCreatedAt(v.getCreatedAt());
        return r;
    }
}
