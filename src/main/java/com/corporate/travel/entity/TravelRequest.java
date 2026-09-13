package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import com.corporate.travel.entity.enums.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "travel_requests")

public class TravelRequest extends BaseEntity {

    @Column(name = "request_number", nullable = false, unique = true, length = 50)
    private String requestNumber;

    @Column(name = "trip_name", nullable = false, length = 200)
    private String tripName;

    @Enumerated(EnumType.STRING)
    @Column(name = "trip_type", nullable = false)
    private TripType tripType;

    @Column(nullable = false, length = 100)
    private String origin;

    @Column(nullable = false, length = 100)
    private String destination;

    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "is_round_trip")

    private Boolean roundTrip = true;

    @Column(name = "is_personal_trip")

    private Boolean personalTrip = false;

    @Column(name = "number_of_travelers")

    private Integer numberOfTravelers = 1;

    @Column(name = "business_justification", length = 1000)
    private String businessJustification;

    @Column(name = "client_or_event_name", length = 200)
    private String clientOrEventName;

    @Column(name = "estimated_budget", precision = 12, scale = 2)
    private BigDecimal estimatedBudget;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_travel_class")

    private TravelClass preferredTravelClass = TravelClass.ECONOMY;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_hotel_category")

    private HotelCategory preferredHotelCategory = HotelCategory.STANDARD_3_STAR;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)

    private RequestStatus status = RequestStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "compliance_status")

    private PolicyComplianceStatus complianceStatus = PolicyComplianceStatus.COMPLIANT;

    @Column(name = "policy_violation_reason", length = 1000)
    private String policyViolationReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cost_center_id")
    private CostCenter costCenter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @OneToMany(mappedBy = "travelRequest", cascade = CascadeType.ALL, orphanRemoval = true)

    private List<ApprovalStep> approvalSteps = new ArrayList<>();


    public TravelRequest() {}

    public TravelRequest(String requestNumber, String tripName, TripType tripType, String origin, String destination, LocalDate departureDate, LocalDate returnDate, Boolean roundTrip, Boolean personalTrip, Integer numberOfTravelers, String businessJustification, String clientOrEventName, BigDecimal estimatedBudget, TravelClass preferredTravelClass, HotelCategory preferredHotelCategory, RequestStatus status, PolicyComplianceStatus complianceStatus, String policyViolationReason, User employee, Department department, CostCenter costCenter, Organization organization, List<ApprovalStep> approvalSteps) {
        this.requestNumber = requestNumber;
        this.tripName = tripName;
        this.tripType = tripType;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.roundTrip = roundTrip;
        this.personalTrip = personalTrip;
        this.numberOfTravelers = numberOfTravelers;
        this.businessJustification = businessJustification;
        this.clientOrEventName = clientOrEventName;
        this.estimatedBudget = estimatedBudget;
        this.preferredTravelClass = preferredTravelClass;
        this.preferredHotelCategory = preferredHotelCategory;
        this.status = status;
        this.complianceStatus = complianceStatus;
        this.policyViolationReason = policyViolationReason;
        this.employee = employee;
        this.department = department;
        this.costCenter = costCenter;
        this.organization = organization;
        this.approvalSteps = approvalSteps;
    }

    public String getRequestNumber() { return requestNumber; }

    public void setRequestNumber(String requestNumber) { this.requestNumber = requestNumber; }

    public String getTripName() { return tripName; }

    public void setTripName(String tripName) { this.tripName = tripName; }

    public TripType getTripType() { return tripType; }

    public void setTripType(TripType tripType) { this.tripType = tripType; }

    public String getOrigin() { return origin; }

    public void setOrigin(String origin) { this.origin = origin; }

    public String getDestination() { return destination; }

    public void setDestination(String destination) { this.destination = destination; }

    public LocalDate getDepartureDate() { return departureDate; }

    public void setDepartureDate(LocalDate departureDate) { this.departureDate = departureDate; }

    public LocalDate getReturnDate() { return returnDate; }

    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public Boolean isRoundTrip() { return roundTrip; }

    public Boolean getRoundTrip() { return roundTrip; }

    public void setRoundTrip(Boolean roundTrip) { this.roundTrip = roundTrip; }

    public Boolean isPersonalTrip() { return personalTrip; }

    public Boolean getPersonalTrip() { return personalTrip; }

    public void setPersonalTrip(Boolean personalTrip) { this.personalTrip = personalTrip; }

    public Integer getNumberOfTravelers() { return numberOfTravelers; }

    public void setNumberOfTravelers(Integer numberOfTravelers) { this.numberOfTravelers = numberOfTravelers; }

    public String getBusinessJustification() { return businessJustification; }

    public void setBusinessJustification(String businessJustification) { this.businessJustification = businessJustification; }

    public String getClientOrEventName() { return clientOrEventName; }

    public void setClientOrEventName(String clientOrEventName) { this.clientOrEventName = clientOrEventName; }

    public BigDecimal getEstimatedBudget() { return estimatedBudget; }

    public void setEstimatedBudget(BigDecimal estimatedBudget) { this.estimatedBudget = estimatedBudget; }

    public TravelClass getPreferredTravelClass() { return preferredTravelClass; }

    public void setPreferredTravelClass(TravelClass preferredTravelClass) { this.preferredTravelClass = preferredTravelClass; }

    public HotelCategory getPreferredHotelCategory() { return preferredHotelCategory; }

    public void setPreferredHotelCategory(HotelCategory preferredHotelCategory) { this.preferredHotelCategory = preferredHotelCategory; }

    public RequestStatus getStatus() { return status; }

    public void setStatus(RequestStatus status) { this.status = status; }

    public PolicyComplianceStatus getComplianceStatus() { return complianceStatus; }

    public void setComplianceStatus(PolicyComplianceStatus complianceStatus) { this.complianceStatus = complianceStatus; }

    public String getPolicyViolationReason() { return policyViolationReason; }

    public void setPolicyViolationReason(String policyViolationReason) { this.policyViolationReason = policyViolationReason; }

    public User getEmployee() { return employee; }

    public void setEmployee(User employee) { this.employee = employee; }

    public Department getDepartment() { return department; }

    public void setDepartment(Department department) { this.department = department; }

    public CostCenter getCostCenter() { return costCenter; }

    public void setCostCenter(CostCenter costCenter) { this.costCenter = costCenter; }

    public Organization getOrganization() { return organization; }

    public void setOrganization(Organization organization) { this.organization = organization; }

    public List<ApprovalStep> getApprovalSteps() { return approvalSteps; }

    public void setApprovalSteps(List<ApprovalStep> approvalSteps) { this.approvalSteps = approvalSteps; }

    public static TravelRequestBuilder builder() { return new TravelRequestBuilder(); }

    public static class TravelRequestBuilder {
        private Long id;
        private String requestNumber;
        private String tripName;
        private TripType tripType;
        private String origin;
        private String destination;
        private LocalDate departureDate;
        private LocalDate returnDate;
        private Boolean roundTrip = true;
        private Boolean personalTrip = false;
        private Integer numberOfTravelers = 1;
        private String businessJustification;
        private String clientOrEventName;
        private BigDecimal estimatedBudget;
        private TravelClass preferredTravelClass = TravelClass.ECONOMY;
        private HotelCategory preferredHotelCategory = HotelCategory.STANDARD_3_STAR;
        private RequestStatus status = RequestStatus.DRAFT;
        private PolicyComplianceStatus complianceStatus = PolicyComplianceStatus.COMPLIANT;
        private String policyViolationReason;
        private User employee;
        private Department department;
        private CostCenter costCenter;
        private Organization organization;
        private List<ApprovalStep> approvalSteps = new ArrayList<>();

        public TravelRequestBuilder id(Long id) { this.id = id; return this; }
        public TravelRequestBuilder requestNumber(String requestNumber) { this.requestNumber = requestNumber; return this; }
        public TravelRequestBuilder tripName(String tripName) { this.tripName = tripName; return this; }
        public TravelRequestBuilder tripType(TripType tripType) { this.tripType = tripType; return this; }
        public TravelRequestBuilder origin(String origin) { this.origin = origin; return this; }
        public TravelRequestBuilder destination(String destination) { this.destination = destination; return this; }
        public TravelRequestBuilder departureDate(LocalDate departureDate) { this.departureDate = departureDate; return this; }
        public TravelRequestBuilder returnDate(LocalDate returnDate) { this.returnDate = returnDate; return this; }
        public TravelRequestBuilder roundTrip(Boolean roundTrip) { this.roundTrip = roundTrip; return this; }
        public TravelRequestBuilder personalTrip(Boolean personalTrip) { this.personalTrip = personalTrip; return this; }
        public TravelRequestBuilder numberOfTravelers(Integer numberOfTravelers) { this.numberOfTravelers = numberOfTravelers; return this; }
        public TravelRequestBuilder businessJustification(String businessJustification) { this.businessJustification = businessJustification; return this; }
        public TravelRequestBuilder clientOrEventName(String clientOrEventName) { this.clientOrEventName = clientOrEventName; return this; }
        public TravelRequestBuilder estimatedBudget(BigDecimal estimatedBudget) { this.estimatedBudget = estimatedBudget; return this; }
        public TravelRequestBuilder preferredTravelClass(TravelClass preferredTravelClass) { this.preferredTravelClass = preferredTravelClass; return this; }
        public TravelRequestBuilder preferredHotelCategory(HotelCategory preferredHotelCategory) { this.preferredHotelCategory = preferredHotelCategory; return this; }
        public TravelRequestBuilder status(RequestStatus status) { this.status = status; return this; }
        public TravelRequestBuilder complianceStatus(PolicyComplianceStatus complianceStatus) { this.complianceStatus = complianceStatus; return this; }
        public TravelRequestBuilder policyViolationReason(String policyViolationReason) { this.policyViolationReason = policyViolationReason; return this; }
        public TravelRequestBuilder employee(User employee) { this.employee = employee; return this; }
        public TravelRequestBuilder department(Department department) { this.department = department; return this; }
        public TravelRequestBuilder costCenter(CostCenter costCenter) { this.costCenter = costCenter; return this; }
        public TravelRequestBuilder organization(Organization organization) { this.organization = organization; return this; }
        public TravelRequestBuilder approvalSteps(List<ApprovalStep> approvalSteps) { this.approvalSteps = approvalSteps; return this; }

        public TravelRequest build() {
            TravelRequest obj = new TravelRequest();
            obj.setId(this.id);
            obj.setRequestNumber(this.requestNumber);
            obj.setTripName(this.tripName);
            obj.setTripType(this.tripType);
            obj.setOrigin(this.origin);
            obj.setDestination(this.destination);
            obj.setDepartureDate(this.departureDate);
            obj.setReturnDate(this.returnDate);
            obj.setRoundTrip(this.roundTrip);
            obj.setPersonalTrip(this.personalTrip);
            obj.setNumberOfTravelers(this.numberOfTravelers);
            obj.setBusinessJustification(this.businessJustification);
            obj.setClientOrEventName(this.clientOrEventName);
            obj.setEstimatedBudget(this.estimatedBudget);
            obj.setPreferredTravelClass(this.preferredTravelClass);
            obj.setPreferredHotelCategory(this.preferredHotelCategory);
            obj.setStatus(this.status);
            obj.setComplianceStatus(this.complianceStatus);
            obj.setPolicyViolationReason(this.policyViolationReason);
            obj.setEmployee(this.employee);
            obj.setDepartment(this.department);
            obj.setCostCenter(this.costCenter);
            obj.setOrganization(this.organization);
            obj.setApprovalSteps(this.approvalSteps);
            return obj;
        }
    }
}
