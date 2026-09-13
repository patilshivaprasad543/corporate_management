package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import com.corporate.travel.entity.enums.HotelCategory;
import com.corporate.travel.entity.enums.TravelClass;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "travel_policies")

public class TravelPolicy extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "max_domestic_flight_price", precision = 12, scale = 2)

    private BigDecimal maxDomesticFlightPrice = BigDecimal.valueOf(15000);

    @Column(name = "max_international_flight_price", precision = 12, scale = 2)

    private BigDecimal maxInternationalFlightPrice = BigDecimal.valueOf(75000);

    @Column(name = "max_hotel_price_per_night", precision = 12, scale = 2)

    private BigDecimal maxHotelPricePerNight = BigDecimal.valueOf(6000);

    @Column(name = "daily_meal_allowance", precision = 12, scale = 2)

    private BigDecimal dailyMealAllowance = BigDecimal.valueOf(2000);

    @Column(name = "daily_taxi_allowance", precision = 12, scale = 2)

    private BigDecimal dailyTaxiAllowance = BigDecimal.valueOf(1500);

    @Column(name = "advance_booking_days")

    private Integer advanceBookingDays = 7;

    @Enumerated(EnumType.STRING)
    @Column(name = "allowed_flight_class")

    private TravelClass allowedFlightClass = TravelClass.ECONOMY;

    @Enumerated(EnumType.STRING)
    @Column(name = "allowed_hotel_category")

    private HotelCategory allowedHotelCategory = HotelCategory.STANDARD_3_STAR;

    @Column(name = "require_manager_approval")

    private Boolean requireManagerApproval = true;

    @Column(name = "finance_approval_threshold", precision = 12, scale = 2)

    private BigDecimal financeApprovalThreshold = BigDecimal.valueOf(25000);

    @Column(name = "admin_approval_threshold", precision = 12, scale = 2)

    private BigDecimal adminApprovalThreshold = BigDecimal.valueOf(100000);

    @Column(name = "is_active")

    private Boolean active = true;


    public TravelPolicy() {}

    public TravelPolicy(String name, String description, Organization organization, BigDecimal maxDomesticFlightPrice, BigDecimal maxInternationalFlightPrice, BigDecimal maxHotelPricePerNight, BigDecimal dailyMealAllowance, BigDecimal dailyTaxiAllowance, Integer advanceBookingDays, TravelClass allowedFlightClass, HotelCategory allowedHotelCategory, Boolean requireManagerApproval, BigDecimal financeApprovalThreshold, BigDecimal adminApprovalThreshold, Boolean active) {
        this.name = name;
        this.description = description;
        this.organization = organization;
        this.maxDomesticFlightPrice = maxDomesticFlightPrice;
        this.maxInternationalFlightPrice = maxInternationalFlightPrice;
        this.maxHotelPricePerNight = maxHotelPricePerNight;
        this.dailyMealAllowance = dailyMealAllowance;
        this.dailyTaxiAllowance = dailyTaxiAllowance;
        this.advanceBookingDays = advanceBookingDays;
        this.allowedFlightClass = allowedFlightClass;
        this.allowedHotelCategory = allowedHotelCategory;
        this.requireManagerApproval = requireManagerApproval;
        this.financeApprovalThreshold = financeApprovalThreshold;
        this.adminApprovalThreshold = adminApprovalThreshold;
        this.active = active;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Organization getOrganization() { return organization; }

    public void setOrganization(Organization organization) { this.organization = organization; }

    public BigDecimal getMaxDomesticFlightPrice() { return maxDomesticFlightPrice; }

    public void setMaxDomesticFlightPrice(BigDecimal maxDomesticFlightPrice) { this.maxDomesticFlightPrice = maxDomesticFlightPrice; }

    public BigDecimal getMaxInternationalFlightPrice() { return maxInternationalFlightPrice; }

    public void setMaxInternationalFlightPrice(BigDecimal maxInternationalFlightPrice) { this.maxInternationalFlightPrice = maxInternationalFlightPrice; }

    public BigDecimal getMaxHotelPricePerNight() { return maxHotelPricePerNight; }

    public void setMaxHotelPricePerNight(BigDecimal maxHotelPricePerNight) { this.maxHotelPricePerNight = maxHotelPricePerNight; }

    public BigDecimal getDailyMealAllowance() { return dailyMealAllowance; }

    public void setDailyMealAllowance(BigDecimal dailyMealAllowance) { this.dailyMealAllowance = dailyMealAllowance; }

    public BigDecimal getDailyTaxiAllowance() { return dailyTaxiAllowance; }

    public void setDailyTaxiAllowance(BigDecimal dailyTaxiAllowance) { this.dailyTaxiAllowance = dailyTaxiAllowance; }

    public Integer getAdvanceBookingDays() { return advanceBookingDays; }

    public void setAdvanceBookingDays(Integer advanceBookingDays) { this.advanceBookingDays = advanceBookingDays; }

    public TravelClass getAllowedFlightClass() { return allowedFlightClass; }

    public void setAllowedFlightClass(TravelClass allowedFlightClass) { this.allowedFlightClass = allowedFlightClass; }

    public HotelCategory getAllowedHotelCategory() { return allowedHotelCategory; }

    public void setAllowedHotelCategory(HotelCategory allowedHotelCategory) { this.allowedHotelCategory = allowedHotelCategory; }

    public Boolean isRequireManagerApproval() { return requireManagerApproval; }

    public Boolean getRequireManagerApproval() { return requireManagerApproval; }

    public void setRequireManagerApproval(Boolean requireManagerApproval) { this.requireManagerApproval = requireManagerApproval; }

    public BigDecimal getFinanceApprovalThreshold() { return financeApprovalThreshold; }

    public void setFinanceApprovalThreshold(BigDecimal financeApprovalThreshold) { this.financeApprovalThreshold = financeApprovalThreshold; }

    public BigDecimal getAdminApprovalThreshold() { return adminApprovalThreshold; }

    public void setAdminApprovalThreshold(BigDecimal adminApprovalThreshold) { this.adminApprovalThreshold = adminApprovalThreshold; }

    public Boolean isActive() { return active; }

    public Boolean getActive() { return active; }

    public void setActive(Boolean active) { this.active = active; }

    public static TravelPolicyBuilder builder() { return new TravelPolicyBuilder(); }

    public static class TravelPolicyBuilder {
        private Long id;
        private String name;
        private String description;
        private Organization organization;
        private BigDecimal maxDomesticFlightPrice = BigDecimal.valueOf(15000);
        private BigDecimal maxInternationalFlightPrice = BigDecimal.valueOf(75000);
        private BigDecimal maxHotelPricePerNight = BigDecimal.valueOf(6000);
        private BigDecimal dailyMealAllowance = BigDecimal.valueOf(2000);
        private BigDecimal dailyTaxiAllowance = BigDecimal.valueOf(1500);
        private Integer advanceBookingDays = 7;
        private TravelClass allowedFlightClass = TravelClass.ECONOMY;
        private HotelCategory allowedHotelCategory = HotelCategory.STANDARD_3_STAR;
        private Boolean requireManagerApproval = true;
        private BigDecimal financeApprovalThreshold = BigDecimal.valueOf(25000);
        private BigDecimal adminApprovalThreshold = BigDecimal.valueOf(100000);
        private Boolean active = true;

        public TravelPolicyBuilder id(Long id) { this.id = id; return this; }
        public TravelPolicyBuilder name(String name) { this.name = name; return this; }
        public TravelPolicyBuilder description(String description) { this.description = description; return this; }
        public TravelPolicyBuilder organization(Organization organization) { this.organization = organization; return this; }
        public TravelPolicyBuilder maxDomesticFlightPrice(BigDecimal maxDomesticFlightPrice) { this.maxDomesticFlightPrice = maxDomesticFlightPrice; return this; }
        public TravelPolicyBuilder maxInternationalFlightPrice(BigDecimal maxInternationalFlightPrice) { this.maxInternationalFlightPrice = maxInternationalFlightPrice; return this; }
        public TravelPolicyBuilder maxHotelPricePerNight(BigDecimal maxHotelPricePerNight) { this.maxHotelPricePerNight = maxHotelPricePerNight; return this; }
        public TravelPolicyBuilder dailyMealAllowance(BigDecimal dailyMealAllowance) { this.dailyMealAllowance = dailyMealAllowance; return this; }
        public TravelPolicyBuilder dailyTaxiAllowance(BigDecimal dailyTaxiAllowance) { this.dailyTaxiAllowance = dailyTaxiAllowance; return this; }
        public TravelPolicyBuilder advanceBookingDays(Integer advanceBookingDays) { this.advanceBookingDays = advanceBookingDays; return this; }
        public TravelPolicyBuilder allowedFlightClass(TravelClass allowedFlightClass) { this.allowedFlightClass = allowedFlightClass; return this; }
        public TravelPolicyBuilder allowedHotelCategory(HotelCategory allowedHotelCategory) { this.allowedHotelCategory = allowedHotelCategory; return this; }
        public TravelPolicyBuilder requireManagerApproval(Boolean requireManagerApproval) { this.requireManagerApproval = requireManagerApproval; return this; }
        public TravelPolicyBuilder financeApprovalThreshold(BigDecimal financeApprovalThreshold) { this.financeApprovalThreshold = financeApprovalThreshold; return this; }
        public TravelPolicyBuilder adminApprovalThreshold(BigDecimal adminApprovalThreshold) { this.adminApprovalThreshold = adminApprovalThreshold; return this; }
        public TravelPolicyBuilder active(Boolean active) { this.active = active; return this; }

        public TravelPolicy build() {
            TravelPolicy obj = new TravelPolicy();
            obj.setId(this.id);
            obj.setName(this.name);
            obj.setDescription(this.description);
            obj.setOrganization(this.organization);
            obj.setMaxDomesticFlightPrice(this.maxDomesticFlightPrice);
            obj.setMaxInternationalFlightPrice(this.maxInternationalFlightPrice);
            obj.setMaxHotelPricePerNight(this.maxHotelPricePerNight);
            obj.setDailyMealAllowance(this.dailyMealAllowance);
            obj.setDailyTaxiAllowance(this.dailyTaxiAllowance);
            obj.setAdvanceBookingDays(this.advanceBookingDays);
            obj.setAllowedFlightClass(this.allowedFlightClass);
            obj.setAllowedHotelCategory(this.allowedHotelCategory);
            obj.setRequireManagerApproval(this.requireManagerApproval);
            obj.setFinanceApprovalThreshold(this.financeApprovalThreshold);
            obj.setAdminApprovalThreshold(this.adminApprovalThreshold);
            obj.setActive(this.active);
            return obj;
        }
    }
}
