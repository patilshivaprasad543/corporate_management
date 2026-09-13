package com.corporate.travel.service;

import com.corporate.travel.dto.PolicyDto;
import com.corporate.travel.entity.Organization;
import com.corporate.travel.entity.TravelPolicy;
import com.corporate.travel.entity.User;
import com.corporate.travel.exception.BadRequestException;
import com.corporate.travel.exception.ForbiddenException;
import com.corporate.travel.exception.ResourceNotFoundException;
import com.corporate.travel.repository.OrganizationRepository;
import com.corporate.travel.repository.TravelPolicyRepository;
import com.corporate.travel.repository.UserRepository;
import com.corporate.travel.security.TenantAccessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TravelPolicyService {

    private final TravelPolicyRepository policyRepository;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final TenantAccessService tenantAccessService;
    private final PolicyEvaluationService policyEvaluationService;
    private final AuditService auditService;

    public TravelPolicyService(TravelPolicyRepository policyRepository,
                               OrganizationRepository organizationRepository,
                               UserRepository userRepository,
                               TenantAccessService tenantAccessService,
                               PolicyEvaluationService policyEvaluationService,
                               AuditService auditService) {
        this.policyRepository = policyRepository;
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.tenantAccessService = tenantAccessService;
        this.policyEvaluationService = policyEvaluationService;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public List<PolicyDto.PolicyResponse> listPolicies() {
        Long scope = tenantAccessService.resolveOrganizationScope();
        List<TravelPolicy> policies = scope == null
                ? policyRepository.findAll()
                : policyRepository.findByOrganizationIdAndActiveTrue(scope);
        if (scope != null && policies.isEmpty()) {
            policies = policyRepository.findByOrganizationId(scope);
        }
        return policies.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PolicyDto.PolicyResponse getPolicy(Long id) {
        TravelPolicy policy = loadAccessiblePolicy(id);
        return toResponse(policy);
    }

    @Transactional
    public PolicyDto.PolicyResponse createPolicy(PolicyDto.CreatePolicyRequest request, String actorEmail) {
        Long orgId = resolveTargetOrganizationId(request.getOrganizationId());
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", orgId));

        policyRepository.findFirstByOrganizationIdAndActiveTrue(orgId).ifPresent(existing -> {
            existing.setActive(false);
            policyRepository.save(existing);
        });

        TravelPolicy policy = mapToEntity(request, org);
        policy.setActive(true);
        TravelPolicy saved = policyRepository.save(policy);

        auditService.logAction(actorEmail, "CREATE_TRAVEL_POLICY", "TravelPolicy", saved.getId(),
                "Created policy " + saved.getName() + " for " + org.getName(), null);
        return toResponse(saved);
    }

    @Transactional
    public PolicyDto.PolicyResponse updatePolicy(Long id, PolicyDto.UpdatePolicyRequest request, String actorEmail) {
        TravelPolicy policy = loadAccessiblePolicy(id);
        if (!tenantAccessService.isSuperAdmin()) {
            Long scope = tenantAccessService.resolveOrganizationScope();
            if (scope == null || !policy.getOrganization().getId().equals(scope)) {
                throw new ForbiddenException("Cannot update policy for another company");
            }
        }

        applyUpdate(policy, request);
        TravelPolicy saved = policyRepository.save(policy);

        auditService.logAction(actorEmail, "UPDATE_TRAVEL_POLICY", "TravelPolicy", saved.getId(),
                "Updated policy " + saved.getName(), null);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PolicyDto.EvaluationResponse evaluate(PolicyDto.EvaluateRequest request, Long currentUserId) {
        Long orgId = request.getOrganizationId();
        if (orgId == null) {
            orgId = tenantAccessService.resolveOrganizationScope();
        }
        if (orgId == null && currentUserId != null) {
            User user = userRepository.findById(currentUserId).orElse(null);
            if (user != null && user.getOrganization() != null) {
                orgId = user.getOrganization().getId();
            }
        }
        if (orgId == null) {
            throw new BadRequestException("Organization is required for policy evaluation");
        }
        tenantAccessService.assertCanAccessOrganization(orgId);
        request.setOrganizationId(orgId);
        if (request.getUserId() == null) {
            request.setUserId(currentUserId);
        }
        return policyEvaluationService.evaluate(request);
    }

    private Long resolveTargetOrganizationId(Long requestedOrgId) {
        if (tenantAccessService.isSuperAdmin()) {
            if (requestedOrgId == null) {
                throw new BadRequestException("Organization is required");
            }
            return requestedOrgId;
        }
        Long scope = tenantAccessService.resolveOrganizationScope();
        if (requestedOrgId != null && !requestedOrgId.equals(scope)) {
            throw new ForbiddenException("Cannot manage policy for another company");
        }
        return scope;
    }

    private TravelPolicy loadAccessiblePolicy(Long id) {
        TravelPolicy policy = policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TravelPolicy", "id", id));
        if (policy.getOrganization() != null) {
            tenantAccessService.assertCanAccessOrganization(policy.getOrganization().getId());
        }
        return policy;
    }

    private TravelPolicy mapToEntity(PolicyDto.CreatePolicyRequest request, Organization org) {
        TravelPolicy policy = new TravelPolicy();
        policy.setName(request.getName());
        policy.setDescription(request.getDescription());
        policy.setOrganization(org);
        applyUpdate(policy, request);
        return policy;
    }

    private void applyUpdate(TravelPolicy policy, PolicyDto.CreatePolicyRequest request) {
        policy.setName(request.getName());
        policy.setDescription(request.getDescription());
        if (request.getMaxDomesticFlightPrice() != null) policy.setMaxDomesticFlightPrice(request.getMaxDomesticFlightPrice());
        if (request.getMaxInternationalFlightPrice() != null) policy.setMaxInternationalFlightPrice(request.getMaxInternationalFlightPrice());
        if (request.getMaxHotelPricePerNight() != null) policy.setMaxHotelPricePerNight(request.getMaxHotelPricePerNight());
        if (request.getHotelRoomLimit() != null) policy.setHotelRoomLimit(request.getHotelRoomLimit());
        if (request.getDailyMealAllowance() != null) policy.setDailyMealAllowance(request.getDailyMealAllowance());
        if (request.getDailyTaxiAllowance() != null) policy.setDailyTaxiAllowance(request.getDailyTaxiAllowance());
        if (request.getMaxTransportAmount() != null) policy.setMaxTransportAmount(request.getMaxTransportAmount());
        if (request.getAdvanceBookingDays() != null) policy.setAdvanceBookingDays(request.getAdvanceBookingDays());
        if (request.getAllowedFlightClass() != null) policy.setAllowedFlightClass(request.getAllowedFlightClass());
        if (request.getAllowedHotelCategory() != null) policy.setAllowedHotelCategory(request.getAllowedHotelCategory());
        if (request.getRequireManagerApproval() != null) policy.setRequireManagerApproval(request.getRequireManagerApproval());
        if (request.getFinanceApprovalThreshold() != null) policy.setFinanceApprovalThreshold(request.getFinanceApprovalThreshold());
        if (request.getAdminApprovalThreshold() != null) policy.setAdminApprovalThreshold(request.getAdminApprovalThreshold());
        if (request.getInternationalRequiresFinance() != null) policy.setInternationalRequiresFinance(request.getInternationalRequiresFinance());
    }

    private PolicyDto.PolicyResponse toResponse(TravelPolicy policy) {
        PolicyDto.PolicyResponse r = new PolicyDto.PolicyResponse();
        r.setId(policy.getId());
        r.setName(policy.getName());
        r.setDescription(policy.getDescription());
        if (policy.getOrganization() != null) {
            r.setOrganizationId(policy.getOrganization().getId());
            r.setOrganizationName(policy.getOrganization().getName());
        }
        r.setMaxDomesticFlightPrice(policy.getMaxDomesticFlightPrice());
        r.setMaxInternationalFlightPrice(policy.getMaxInternationalFlightPrice());
        r.setMaxHotelPricePerNight(policy.getMaxHotelPricePerNight());
        r.setHotelRoomLimit(policy.getHotelRoomLimit());
        r.setDailyMealAllowance(policy.getDailyMealAllowance());
        r.setDailyTaxiAllowance(policy.getDailyTaxiAllowance());
        r.setMaxTransportAmount(policy.getMaxTransportAmount());
        r.setAdvanceBookingDays(policy.getAdvanceBookingDays());
        r.setAllowedFlightClass(policy.getAllowedFlightClass());
        r.setAllowedHotelCategory(policy.getAllowedHotelCategory());
        r.setRequireManagerApproval(policy.getRequireManagerApproval());
        r.setFinanceApprovalThreshold(policy.getFinanceApprovalThreshold());
        r.setAdminApprovalThreshold(policy.getAdminApprovalThreshold());
        r.setInternationalRequiresFinance(policy.getInternationalRequiresFinance());
        r.setActive(policy.getActive());
        r.setCreatedAt(policy.getCreatedAt());
        return r;
    }
}
