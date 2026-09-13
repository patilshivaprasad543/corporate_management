package com.corporate.travel.service;

import com.corporate.travel.dto.PolicyDto;
import com.corporate.travel.entity.EmployeeProfile;
import com.corporate.travel.entity.TravelPolicy;
import com.corporate.travel.entity.enums.HotelCategory;
import com.corporate.travel.entity.enums.PolicyComplianceStatus;
import com.corporate.travel.entity.enums.TravelClass;
import com.corporate.travel.entity.enums.ViolationSeverity;
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

    private final TravelPolicyRepository policyRepository;
    private final EmployeeProfileRepository employeeProfileRepository;

    public PolicyEvaluationService(TravelPolicyRepository policyRepository,
                                   EmployeeProfileRepository employeeProfileRepository) {
        this.policyRepository = policyRepository;
        this.employeeProfileRepository = employeeProfileRepository;
    }

    /** @deprecated Use {@link #evaluate(PolicyDto.EvaluateRequest)} */
    @Deprecated
    public static class EvaluationResult {
        public PolicyComplianceStatus status;
        public List<String> reasons = new ArrayList<>();
        public boolean requiresFinanceApproval;
        public boolean requiresAdminApproval;
        public List<PolicyDto.ViolationDetail> violations = new ArrayList<>();
    }

    public EvaluationResult evaluateTravelRequest(Long organizationId, Long userId, BigDecimal estimatedBudget,
                                                 TravelClass requestedClass, LocalDate departureDate,
                                                 boolean isInternational) {
        PolicyDto.EvaluateRequest request = new PolicyDto.EvaluateRequest();
        request.setOrganizationId(organizationId);
        request.setUserId(userId);
        request.setEstimatedBudget(estimatedBudget);
        request.setFlightAmount(estimatedBudget);
        request.setTravelClass(requestedClass);
        request.setDepartureDate(departureDate);
        request.setInternational(isInternational);

        PolicyDto.EvaluationResponse response = evaluate(request);

        EvaluationResult result = new EvaluationResult();
        result.status = response.getStatus();
        result.requiresFinanceApproval = response.isRequiresFinanceApproval();
        result.requiresAdminApproval = response.isRequiresAdminApproval();
        result.violations = response.getViolations();
        response.getViolations().forEach(v ->
                result.reasons.add(v.getExplanation()));
        response.getWarnings().forEach(result.reasons::add);
        return result;
    }

    public PolicyDto.EvaluationResponse evaluate(PolicyDto.EvaluateRequest request) {
        PolicyDto.EvaluationResponse response = new PolicyDto.EvaluationResponse();
        response.setRequiresManagerApproval(true);

        TravelPolicy policy = policyRepository.findFirstByOrganizationIdAndActiveTrue(request.getOrganizationId())
                .orElse(null);
        if (policy == null) {
            response.setStatus(PolicyComplianceStatus.COMPLIANT);
            response.setSummary("No active travel policy configured — request allowed.");
            return response;
        }

        EmployeeProfile profile = request.getUserId() != null
                ? employeeProfileRepository.findByUserId(request.getUserId()).orElse(null)
                : null;

        boolean isInternational = Boolean.TRUE.equals(request.getInternational());

        evaluateAdvanceBooking(policy, request.getDepartureDate(), response);
        evaluateFlightBudget(policy, request.getFlightAmount(), isInternational, response);
        evaluateHotelBudget(policy, request.getHotelAmountPerNight(), response);
        evaluateHotelRooms(policy, request.getHotelRooms(), response);
        evaluateTransportBudget(policy, request.getTransportAmount(), response);
        evaluateCabinClass(policy, profile, request.getTravelClass(), response);
        evaluateHotelCategory(policy, profile, request.getHotelCategory(), response);
        evaluateApprovalThresholds(policy, request.getEstimatedBudget(), isInternational, response);

        boolean hasHardViolations = response.getViolations().stream()
                .anyMatch(v -> v.getSeverity() == ViolationSeverity.VIOLATION);

        if (!hasHardViolations && response.getWarnings().isEmpty()
                && response.getViolations().stream().noneMatch(v -> v.getSeverity() == ViolationSeverity.WARNING)) {
            response.setStatus(PolicyComplianceStatus.COMPLIANT);
            response.setSummary("Request is fully compliant with company travel policy.");
        } else if (hasHardViolations) {
            response.setStatus(PolicyComplianceStatus.POLICY_VIOLATION);
            response.setSummary("Policy violations detected — additional approval may be required.");
        } else {
            response.setStatus(PolicyComplianceStatus.WARNING);
            response.setSummary("Policy warnings detected — review before submitting.");
        }

        return response;
    }

    private void evaluateAdvanceBooking(TravelPolicy policy, LocalDate departureDate,
                                       PolicyDto.EvaluationResponse response) {
        if (departureDate == null || policy.getAdvanceBookingDays() == null) {
            return;
        }
        long daysInAdvance = ChronoUnit.DAYS.between(LocalDate.now(), departureDate);
        if (daysInAdvance < policy.getAdvanceBookingDays()) {
            String msg = String.format("Trip booked %d days in advance; policy requires at least %d days.",
                    daysInAdvance, policy.getAdvanceBookingDays());
            response.getWarnings().add(msg);
            addWarningViolation(response, "ADVANCE_BOOKING", "Minimum Advance Booking",
                    null, BigDecimal.valueOf(policy.getAdvanceBookingDays()), msg);
        }
    }

    private void evaluateFlightBudget(TravelPolicy policy, BigDecimal flightAmount, boolean isInternational,
                                      PolicyDto.EvaluationResponse response) {
        if (flightAmount == null) {
            return;
        }
        BigDecimal maxAllowed = isInternational
                ? policy.getMaxInternationalFlightPrice()
                : policy.getMaxDomesticFlightPrice();
        if (maxAllowed == null) {
            return;
        }
        if (flightAmount.compareTo(maxAllowed) > 0) {
            BigDecimal diff = flightAmount.subtract(maxAllowed);
            String msg = String.format("Flight amount ₹%s exceeds allowed limit ₹%s (difference ₹%s).",
                    flightAmount, maxAllowed, diff);
            addViolation(response, "FLIGHT_BUDGET",
                    isInternational ? "Max International Flight" : "Max Domestic Flight",
                    flightAmount, maxAllowed, diff, msg);
        }
    }

    private void evaluateHotelBudget(TravelPolicy policy, BigDecimal hotelAmount, PolicyDto.EvaluationResponse response) {
        if (hotelAmount == null || policy.getMaxHotelPricePerNight() == null) {
            return;
        }
        if (hotelAmount.compareTo(policy.getMaxHotelPricePerNight()) > 0) {
            BigDecimal diff = hotelAmount.subtract(policy.getMaxHotelPricePerNight());
            String msg = String.format("Hotel rate ₹%s/night exceeds limit ₹%s/night (difference ₹%s).",
                    hotelAmount, policy.getMaxHotelPricePerNight(), diff);
            addViolation(response, "HOTEL_BUDGET", "Max Hotel Per Night",
                    hotelAmount, policy.getMaxHotelPricePerNight(), diff, msg);
        }
    }

    private void evaluateHotelRooms(TravelPolicy policy, Integer rooms, PolicyDto.EvaluationResponse response) {
        if (rooms == null || policy.getHotelRoomLimit() == null) {
            return;
        }
        if (rooms > policy.getHotelRoomLimit()) {
            String msg = String.format("Requested %d hotel rooms exceeds policy limit of %d.",
                    rooms, policy.getHotelRoomLimit());
            addViolation(response, "HOTEL_ROOM_LIMIT", "Hotel Room Limit",
                    BigDecimal.valueOf(rooms), BigDecimal.valueOf(policy.getHotelRoomLimit()),
                    BigDecimal.valueOf(rooms - policy.getHotelRoomLimit()), msg);
        }
    }

    private void evaluateTransportBudget(TravelPolicy policy, BigDecimal transportAmount,
                                         PolicyDto.EvaluationResponse response) {
        if (transportAmount == null) {
            return;
        }
        BigDecimal limit = policy.getMaxTransportAmount() != null
                ? policy.getMaxTransportAmount()
                : policy.getDailyTaxiAllowance();
        if (limit == null) {
            return;
        }
        if (transportAmount.compareTo(limit) > 0) {
            BigDecimal diff = transportAmount.subtract(limit);
            String msg = String.format("Transport cost ₹%s exceeds limit ₹%s (difference ₹%s).",
                    transportAmount, limit, diff);
            addViolation(response, "TRANSPORT_BUDGET", "Max Transport Amount",
                    transportAmount, limit, diff, msg);
        }
    }

    private void evaluateCabinClass(TravelPolicy policy, EmployeeProfile profile, TravelClass requested,
                                    PolicyDto.EvaluationResponse response) {
        if (requested == null || requested == TravelClass.ECONOMY) {
            return;
        }
        TravelClass allowed = profile != null && profile.getAllowedTravelClass() != null
                ? profile.getAllowedTravelClass()
                : policy.getAllowedFlightClass();
        if (allowed == null) {
            allowed = TravelClass.ECONOMY;
        }
        if (requested.ordinal() > allowed.ordinal()) {
            String msg = String.format("%s class requested but employee eligibility is %s.",
                    requested, allowed);
            addViolation(response, "CABIN_CLASS", "Allowed Cabin Class",
                    null, null, null, msg);
        }
    }

    private void evaluateHotelCategory(TravelPolicy policy, EmployeeProfile profile, HotelCategory requested,
                                       PolicyDto.EvaluationResponse response) {
        if (requested == null) {
            return;
        }
        HotelCategory allowed = profile != null && profile.getAllowedHotelCategory() != null
                ? profile.getAllowedHotelCategory()
                : policy.getAllowedHotelCategory();
        if (allowed != null && requested.ordinal() > allowed.ordinal()) {
            String msg = String.format("%s hotel requested but policy allows up to %s.",
                    requested, allowed);
            addViolation(response, "HOTEL_CATEGORY", "Allowed Hotel Category",
                    null, null, null, msg);
        }
    }

    private void evaluateApprovalThresholds(TravelPolicy policy, BigDecimal estimatedBudget,
                                            boolean isInternational, PolicyDto.EvaluationResponse response) {
        if (Boolean.TRUE.equals(isInternational) && Boolean.TRUE.equals(policy.getInternationalRequiresFinance())) {
            response.setRequiresFinanceApproval(true);
        }
        if (estimatedBudget == null) {
            return;
        }
        if (policy.getAdminApprovalThreshold() != null
                && estimatedBudget.compareTo(policy.getAdminApprovalThreshold()) >= 0) {
            response.setRequiresAdminApproval(true);
            response.setRequiresFinanceApproval(true);
        } else if (policy.getFinanceApprovalThreshold() != null
                && estimatedBudget.compareTo(policy.getFinanceApprovalThreshold()) >= 0) {
            response.setRequiresFinanceApproval(true);
        }
        if (Boolean.TRUE.equals(policy.getRequireManagerApproval())) {
            response.setRequiresManagerApproval(true);
        }
    }

    private void addViolation(PolicyDto.EvaluationResponse response, String type, String ruleName,
                              BigDecimal requested, BigDecimal allowed, BigDecimal diff, String explanation) {
        PolicyDto.ViolationDetail detail = new PolicyDto.ViolationDetail();
        detail.setViolationType(type);
        detail.setRuleName(ruleName);
        detail.setRequestedAmount(requested);
        detail.setAllowedAmount(allowed);
        detail.setDifferenceAmount(diff);
        detail.setCurrencyCode("INR");
        detail.setSeverity(ViolationSeverity.VIOLATION);
        detail.setExplanation(explanation);
        response.getViolations().add(detail);
    }

    private void addWarningViolation(PolicyDto.EvaluationResponse response, String type, String ruleName,
                                     BigDecimal requested, BigDecimal allowed, String explanation) {
        PolicyDto.ViolationDetail detail = new PolicyDto.ViolationDetail();
        detail.setViolationType(type);
        detail.setRuleName(ruleName);
        detail.setRequestedAmount(requested);
        detail.setAllowedAmount(allowed);
        detail.setSeverity(ViolationSeverity.WARNING);
        detail.setExplanation(explanation);
        response.getViolations().add(detail);
    }
}
