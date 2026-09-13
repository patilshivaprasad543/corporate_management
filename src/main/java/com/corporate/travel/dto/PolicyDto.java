package com.corporate.travel.dto;

import com.corporate.travel.entity.enums.HotelCategory;
import com.corporate.travel.entity.enums.PolicyComplianceStatus;
import com.corporate.travel.entity.enums.TravelClass;
import com.corporate.travel.entity.enums.ViolationSeverity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PolicyDto {

    public static class PolicyResponse {
        private Long id;
        private String name;
        private String description;
        private Long organizationId;
        private String organizationName;
        private BigDecimal maxDomesticFlightPrice;
        private BigDecimal maxInternationalFlightPrice;
        private BigDecimal maxHotelPricePerNight;
        private Integer hotelRoomLimit;
        private BigDecimal dailyMealAllowance;
        private BigDecimal dailyTaxiAllowance;
        private BigDecimal maxTransportAmount;
        private Integer advanceBookingDays;
        private TravelClass allowedFlightClass;
        private HotelCategory allowedHotelCategory;
        private Boolean requireManagerApproval;
        private BigDecimal financeApprovalThreshold;
        private BigDecimal adminApprovalThreshold;
        private Boolean internationalRequiresFinance;
        private Boolean active;
        private LocalDateTime createdAt;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Long getOrganizationId() { return organizationId; }
        public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
        public String getOrganizationName() { return organizationName; }
        public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }
        public BigDecimal getMaxDomesticFlightPrice() { return maxDomesticFlightPrice; }
        public void setMaxDomesticFlightPrice(BigDecimal maxDomesticFlightPrice) { this.maxDomesticFlightPrice = maxDomesticFlightPrice; }
        public BigDecimal getMaxInternationalFlightPrice() { return maxInternationalFlightPrice; }
        public void setMaxInternationalFlightPrice(BigDecimal maxInternationalFlightPrice) { this.maxInternationalFlightPrice = maxInternationalFlightPrice; }
        public BigDecimal getMaxHotelPricePerNight() { return maxHotelPricePerNight; }
        public void setMaxHotelPricePerNight(BigDecimal maxHotelPricePerNight) { this.maxHotelPricePerNight = maxHotelPricePerNight; }
        public Integer getHotelRoomLimit() { return hotelRoomLimit; }
        public void setHotelRoomLimit(Integer hotelRoomLimit) { this.hotelRoomLimit = hotelRoomLimit; }
        public BigDecimal getDailyMealAllowance() { return dailyMealAllowance; }
        public void setDailyMealAllowance(BigDecimal dailyMealAllowance) { this.dailyMealAllowance = dailyMealAllowance; }
        public BigDecimal getDailyTaxiAllowance() { return dailyTaxiAllowance; }
        public void setDailyTaxiAllowance(BigDecimal dailyTaxiAllowance) { this.dailyTaxiAllowance = dailyTaxiAllowance; }
        public BigDecimal getMaxTransportAmount() { return maxTransportAmount; }
        public void setMaxTransportAmount(BigDecimal maxTransportAmount) { this.maxTransportAmount = maxTransportAmount; }
        public Integer getAdvanceBookingDays() { return advanceBookingDays; }
        public void setAdvanceBookingDays(Integer advanceBookingDays) { this.advanceBookingDays = advanceBookingDays; }
        public TravelClass getAllowedFlightClass() { return allowedFlightClass; }
        public void setAllowedFlightClass(TravelClass allowedFlightClass) { this.allowedFlightClass = allowedFlightClass; }
        public HotelCategory getAllowedHotelCategory() { return allowedHotelCategory; }
        public void setAllowedHotelCategory(HotelCategory allowedHotelCategory) { this.allowedHotelCategory = allowedHotelCategory; }
        public Boolean getRequireManagerApproval() { return requireManagerApproval; }
        public void setRequireManagerApproval(Boolean requireManagerApproval) { this.requireManagerApproval = requireManagerApproval; }
        public BigDecimal getFinanceApprovalThreshold() { return financeApprovalThreshold; }
        public void setFinanceApprovalThreshold(BigDecimal financeApprovalThreshold) { this.financeApprovalThreshold = financeApprovalThreshold; }
        public BigDecimal getAdminApprovalThreshold() { return adminApprovalThreshold; }
        public void setAdminApprovalThreshold(BigDecimal adminApprovalThreshold) { this.adminApprovalThreshold = adminApprovalThreshold; }
        public Boolean getInternationalRequiresFinance() { return internationalRequiresFinance; }
        public void setInternationalRequiresFinance(Boolean internationalRequiresFinance) { this.internationalRequiresFinance = internationalRequiresFinance; }
        public Boolean getActive() { return active; }
        public void setActive(Boolean active) { this.active = active; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }

    public static class CreatePolicyRequest {
        @NotBlank private String name;
        private String description;
        @NotNull private Long organizationId;
        private BigDecimal maxDomesticFlightPrice;
        private BigDecimal maxInternationalFlightPrice;
        private BigDecimal maxHotelPricePerNight;
        private Integer hotelRoomLimit;
        private BigDecimal dailyMealAllowance;
        private BigDecimal dailyTaxiAllowance;
        private BigDecimal maxTransportAmount;
        private Integer advanceBookingDays;
        private TravelClass allowedFlightClass;
        private HotelCategory allowedHotelCategory;
        private Boolean requireManagerApproval;
        private BigDecimal financeApprovalThreshold;
        private BigDecimal adminApprovalThreshold;
        private Boolean internationalRequiresFinance;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Long getOrganizationId() { return organizationId; }
        public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
        public BigDecimal getMaxDomesticFlightPrice() { return maxDomesticFlightPrice; }
        public void setMaxDomesticFlightPrice(BigDecimal v) { this.maxDomesticFlightPrice = v; }
        public BigDecimal getMaxInternationalFlightPrice() { return maxInternationalFlightPrice; }
        public void setMaxInternationalFlightPrice(BigDecimal v) { this.maxInternationalFlightPrice = v; }
        public BigDecimal getMaxHotelPricePerNight() { return maxHotelPricePerNight; }
        public void setMaxHotelPricePerNight(BigDecimal v) { this.maxHotelPricePerNight = v; }
        public Integer getHotelRoomLimit() { return hotelRoomLimit; }
        public void setHotelRoomLimit(Integer hotelRoomLimit) { this.hotelRoomLimit = hotelRoomLimit; }
        public BigDecimal getDailyMealAllowance() { return dailyMealAllowance; }
        public void setDailyMealAllowance(BigDecimal v) { this.dailyMealAllowance = v; }
        public BigDecimal getDailyTaxiAllowance() { return dailyTaxiAllowance; }
        public void setDailyTaxiAllowance(BigDecimal v) { this.dailyTaxiAllowance = v; }
        public BigDecimal getMaxTransportAmount() { return maxTransportAmount; }
        public void setMaxTransportAmount(BigDecimal v) { this.maxTransportAmount = v; }
        public Integer getAdvanceBookingDays() { return advanceBookingDays; }
        public void setAdvanceBookingDays(Integer advanceBookingDays) { this.advanceBookingDays = advanceBookingDays; }
        public TravelClass getAllowedFlightClass() { return allowedFlightClass; }
        public void setAllowedFlightClass(TravelClass allowedFlightClass) { this.allowedFlightClass = allowedFlightClass; }
        public HotelCategory getAllowedHotelCategory() { return allowedHotelCategory; }
        public void setAllowedHotelCategory(HotelCategory allowedHotelCategory) { this.allowedHotelCategory = allowedHotelCategory; }
        public Boolean getRequireManagerApproval() { return requireManagerApproval; }
        public void setRequireManagerApproval(Boolean requireManagerApproval) { this.requireManagerApproval = requireManagerApproval; }
        public BigDecimal getFinanceApprovalThreshold() { return financeApprovalThreshold; }
        public void setFinanceApprovalThreshold(BigDecimal v) { this.financeApprovalThreshold = v; }
        public BigDecimal getAdminApprovalThreshold() { return adminApprovalThreshold; }
        public void setAdminApprovalThreshold(BigDecimal v) { this.adminApprovalThreshold = v; }
        public Boolean getInternationalRequiresFinance() { return internationalRequiresFinance; }
        public void setInternationalRequiresFinance(Boolean internationalRequiresFinance) { this.internationalRequiresFinance = internationalRequiresFinance; }
    }

    public static class UpdatePolicyRequest extends CreatePolicyRequest {}

    public static class EvaluateRequest {
        private Long organizationId;
        private Long userId;
        private BigDecimal estimatedBudget;
        private BigDecimal flightAmount;
        private BigDecimal hotelAmountPerNight;
        private BigDecimal transportAmount;
        private Integer hotelRooms;
        private TravelClass travelClass;
        private HotelCategory hotelCategory;
        private LocalDate departureDate;
        private Boolean international;
        private Integer numberOfTravelers;

        public Long getOrganizationId() { return organizationId; }
        public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public BigDecimal getEstimatedBudget() { return estimatedBudget; }
        public void setEstimatedBudget(BigDecimal estimatedBudget) { this.estimatedBudget = estimatedBudget; }
        public BigDecimal getFlightAmount() { return flightAmount; }
        public void setFlightAmount(BigDecimal flightAmount) { this.flightAmount = flightAmount; }
        public BigDecimal getHotelAmountPerNight() { return hotelAmountPerNight; }
        public void setHotelAmountPerNight(BigDecimal hotelAmountPerNight) { this.hotelAmountPerNight = hotelAmountPerNight; }
        public BigDecimal getTransportAmount() { return transportAmount; }
        public void setTransportAmount(BigDecimal transportAmount) { this.transportAmount = transportAmount; }
        public Integer getHotelRooms() { return hotelRooms; }
        public void setHotelRooms(Integer hotelRooms) { this.hotelRooms = hotelRooms; }
        public TravelClass getTravelClass() { return travelClass; }
        public void setTravelClass(TravelClass travelClass) { this.travelClass = travelClass; }
        public HotelCategory getHotelCategory() { return hotelCategory; }
        public void setHotelCategory(HotelCategory hotelCategory) { this.hotelCategory = hotelCategory; }
        public LocalDate getDepartureDate() { return departureDate; }
        public void setDepartureDate(LocalDate departureDate) { this.departureDate = departureDate; }
        public Boolean getInternational() { return international; }
        public void setInternational(Boolean international) { this.international = international; }
        public Integer getNumberOfTravelers() { return numberOfTravelers; }
        public void setNumberOfTravelers(Integer numberOfTravelers) { this.numberOfTravelers = numberOfTravelers; }
    }

    public static class ViolationDetail {
        private String violationType;
        private String ruleName;
        private BigDecimal requestedAmount;
        private BigDecimal allowedAmount;
        private BigDecimal differenceAmount;
        private String currencyCode;
        private ViolationSeverity severity;
        private String explanation;

        public String getViolationType() { return violationType; }
        public void setViolationType(String violationType) { this.violationType = violationType; }
        public String getRuleName() { return ruleName; }
        public void setRuleName(String ruleName) { this.ruleName = ruleName; }
        public BigDecimal getRequestedAmount() { return requestedAmount; }
        public void setRequestedAmount(BigDecimal requestedAmount) { this.requestedAmount = requestedAmount; }
        public BigDecimal getAllowedAmount() { return allowedAmount; }
        public void setAllowedAmount(BigDecimal allowedAmount) { this.allowedAmount = allowedAmount; }
        public BigDecimal getDifferenceAmount() { return differenceAmount; }
        public void setDifferenceAmount(BigDecimal differenceAmount) { this.differenceAmount = differenceAmount; }
        public String getCurrencyCode() { return currencyCode; }
        public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
        public ViolationSeverity getSeverity() { return severity; }
        public void setSeverity(ViolationSeverity severity) { this.severity = severity; }
        public String getExplanation() { return explanation; }
        public void setExplanation(String explanation) { this.explanation = explanation; }
    }

    public static class EvaluationResponse {
        private PolicyComplianceStatus status;
        private List<ViolationDetail> violations = new ArrayList<>();
        private List<String> warnings = new ArrayList<>();
        private boolean requiresFinanceApproval;
        private boolean requiresAdminApproval;
        private boolean requiresManagerApproval;
        private String summary;

        public PolicyComplianceStatus getStatus() { return status; }
        public void setStatus(PolicyComplianceStatus status) { this.status = status; }
        public List<ViolationDetail> getViolations() { return violations; }
        public void setViolations(List<ViolationDetail> violations) { this.violations = violations; }
        public List<String> getWarnings() { return warnings; }
        public void setWarnings(List<String> warnings) { this.warnings = warnings; }
        public boolean isRequiresFinanceApproval() { return requiresFinanceApproval; }
        public void setRequiresFinanceApproval(boolean requiresFinanceApproval) { this.requiresFinanceApproval = requiresFinanceApproval; }
        public boolean isRequiresAdminApproval() { return requiresAdminApproval; }
        public void setRequiresAdminApproval(boolean requiresAdminApproval) { this.requiresAdminApproval = requiresAdminApproval; }
        public boolean isRequiresManagerApproval() { return requiresManagerApproval; }
        public void setRequiresManagerApproval(boolean requiresManagerApproval) { this.requiresManagerApproval = requiresManagerApproval; }
        public String getSummary() { return summary; }
        public void setSummary(String summary) { this.summary = summary; }
    }

    public static class ViolationResponse {
        private Long id;
        private Long travelRequestId;
        private String violationType;
        private String ruleName;
        private BigDecimal requestedAmount;
        private BigDecimal allowedAmount;
        private BigDecimal differenceAmount;
        private ViolationSeverity severity;
        private String explanation;
        private Boolean resolved;
        private LocalDateTime createdAt;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getTravelRequestId() { return travelRequestId; }
        public void setTravelRequestId(Long travelRequestId) { this.travelRequestId = travelRequestId; }
        public String getViolationType() { return violationType; }
        public void setViolationType(String violationType) { this.violationType = violationType; }
        public String getRuleName() { return ruleName; }
        public void setRuleName(String ruleName) { this.ruleName = ruleName; }
        public BigDecimal getRequestedAmount() { return requestedAmount; }
        public void setRequestedAmount(BigDecimal requestedAmount) { this.requestedAmount = requestedAmount; }
        public BigDecimal getAllowedAmount() { return allowedAmount; }
        public void setAllowedAmount(BigDecimal allowedAmount) { this.allowedAmount = allowedAmount; }
        public BigDecimal getDifferenceAmount() { return differenceAmount; }
        public void setDifferenceAmount(BigDecimal differenceAmount) { this.differenceAmount = differenceAmount; }
        public ViolationSeverity getSeverity() { return severity; }
        public void setSeverity(ViolationSeverity severity) { this.severity = severity; }
        public String getExplanation() { return explanation; }
        public void setExplanation(String explanation) { this.explanation = explanation; }
        public Boolean getResolved() { return resolved; }
        public void setResolved(Boolean resolved) { this.resolved = resolved; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }
}
