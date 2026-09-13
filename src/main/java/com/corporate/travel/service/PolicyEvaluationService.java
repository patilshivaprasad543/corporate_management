package com.corporate.travel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corporate.travel.entity.EmployeeProfile;
import com.corporate.travel.entity.TravelPolicy;
import com.corporate.travel.entity.enums.PolicyComplianceStatus;
import com.corporate.travel.entity.enums.TravelClass;
import com.corporate.travel.repository.EmployeeProfileRepository;
import com.corporate.travel.repository.TravelPolicyRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class PolicyEvaluationService {
    private static final Logger log = LoggerFactory.getLogger(PolicyEvaluationService.class);

    public PolicyEvaluationService(TravelPolicyRepository policyRepository, EmployeeProfileRepository employeeProfileRepository) {
        this.policyRepository = policyRepository;
        this.employeeProfileRepository = employeeProfileRepository;
    }


    private final TravelPolicyRepository policyRepository;
    private final EmployeeProfileRepository employeeProfileRepository;

    public static class EvaluationResult {
        public PolicyComplianceStatus status;
        public List<String> reasons = new ArrayList<>();
        public boolean requiresFinanceApproval;
        public boolean requiresAdminApproval;
    }

    public EvaluationResult evaluateTravelRequest(Long organizationId, Long userId, BigDecimal estimatedBudget,
                                                 TravelClass requestedClass, LocalDate departureDate, boolean isInternational) {
        EvaluationResult result = new EvaluationResult();
        TravelPolicy policy = policyRepository.findFirstByOrganizationIdAndActiveTrue(organizationId)
                .orElse(null);

        if (policy == null) {
            result.status = PolicyComplianceStatus.COMPLIANT;
            return result;
        }

        EmployeeProfile profile = employeeProfileRepository.findByUserId(userId).orElse(null);

        // 1. Advance booking check
        if (departureDate != null) {
            long daysInAdvance = ChronoUnit.DAYS.between(LocalDate.now(), departureDate);
            if (daysInAdvance < policy.getAdvanceBookingDays()) {
                result.reasons.add(String.format("Trip booked %d days in advance; policy requires at least %d days.",
                        daysInAdvance, policy.getAdvanceBookingDays()));
            }
        }

        // 2. Budget limits check
        BigDecimal maxPrice = isInternational ? policy.getMaxInternationalFlightPrice() : policy.getMaxDomesticFlightPrice();
        if (estimatedBudget != null && maxPrice != null && estimatedBudget.compareTo(maxPrice) > 0) {
            result.reasons.add(String.format("Estimated budget (₹%s) exceeds standard allowed limit (₹%s).",
                    estimatedBudget, maxPrice));
        }

        // 3. Cabin class check
        if (requestedClass != null && requestedClass == TravelClass.BUSINESS) {
            TravelClass allowed = profile != null ? profile.getAllowedTravelClass() : policy.getAllowedFlightClass();
            if (allowed != TravelClass.BUSINESS && allowed != TravelClass.FIRST) {
                result.reasons.add("Business Class requested but employee eligibility is Economy / Premium Economy.");
            }
        }

        // 4. Determine Approval Levels
        if (estimatedBudget != null) {
            if (policy.getAdminApprovalThreshold() != null && estimatedBudget.compareTo(policy.getAdminApprovalThreshold()) >= 0) {
                result.requiresAdminApproval = true;
                result.requiresFinanceApproval = true;
            } else if (policy.getFinanceApprovalThreshold() != null && estimatedBudget.compareTo(policy.getFinanceApprovalThreshold()) >= 0) {
                result.requiresFinanceApproval = true;
            }
        }

        if (result.reasons.isEmpty()) {
            result.status = PolicyComplianceStatus.COMPLIANT;
        } else if (result.reasons.size() == 1 && result.reasons.get(0).contains("advance")) {
            result.status = PolicyComplianceStatus.WARNING;
        } else {
            result.status = PolicyComplianceStatus.POLICY_VIOLATION;
        }

        return result;
    }
}
