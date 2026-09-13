package com.corporate.travel.dto;

import com.corporate.travel.entity.enums.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TravelRequestDto {

    public static class CreateRequest {
        @NotBlank
        private String tripName;
        @NotNull
        private TripType tripType;
        @NotBlank
        private String origin;
        @NotBlank
        private String destination;
        @NotNull
        private LocalDate departureDate;
        private LocalDate returnDate;
        private Boolean roundTrip;
        private Boolean personalTrip;
        private Integer numberOfTravelers;
        private String businessJustification;
        private String clientOrEventName;
        private BigDecimal estimatedBudget;
        private TravelClass preferredTravelClass;
        private HotelCategory preferredHotelCategory;
        private Long costCenterId;

    public CreateRequest() {}

    public CreateRequest(String tripName, TripType tripType, String origin, String destination, LocalDate departureDate, LocalDate returnDate, Boolean roundTrip, Boolean personalTrip, Integer numberOfTravelers, String businessJustification, String clientOrEventName, BigDecimal estimatedBudget, TravelClass preferredTravelClass, HotelCategory preferredHotelCategory, Long costCenterId) {
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
        this.costCenterId = costCenterId;
    }

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

    public Long getCostCenterId() { return costCenterId; }

    public void setCostCenterId(Long costCenterId) { this.costCenterId = costCenterId; }

    public static CreateRequestBuilder builder() { return new CreateRequestBuilder(); }

    public static class CreateRequestBuilder {
        private String tripName;
        private TripType tripType;
        private String origin;
        private String destination;
        private LocalDate departureDate;
        private LocalDate returnDate;
        private Boolean roundTrip;
        private Boolean personalTrip;
        private Integer numberOfTravelers;
        private String businessJustification;
        private String clientOrEventName;
        private BigDecimal estimatedBudget;
        private TravelClass preferredTravelClass;
        private HotelCategory preferredHotelCategory;
        private Long costCenterId;

        public CreateRequestBuilder tripName(String tripName) { this.tripName = tripName; return this; }
        public CreateRequestBuilder tripType(TripType tripType) { this.tripType = tripType; return this; }
        public CreateRequestBuilder origin(String origin) { this.origin = origin; return this; }
        public CreateRequestBuilder destination(String destination) { this.destination = destination; return this; }
        public CreateRequestBuilder departureDate(LocalDate departureDate) { this.departureDate = departureDate; return this; }
        public CreateRequestBuilder returnDate(LocalDate returnDate) { this.returnDate = returnDate; return this; }
        public CreateRequestBuilder roundTrip(Boolean roundTrip) { this.roundTrip = roundTrip; return this; }
        public CreateRequestBuilder personalTrip(Boolean personalTrip) { this.personalTrip = personalTrip; return this; }
        public CreateRequestBuilder numberOfTravelers(Integer numberOfTravelers) { this.numberOfTravelers = numberOfTravelers; return this; }
        public CreateRequestBuilder businessJustification(String businessJustification) { this.businessJustification = businessJustification; return this; }
        public CreateRequestBuilder clientOrEventName(String clientOrEventName) { this.clientOrEventName = clientOrEventName; return this; }
        public CreateRequestBuilder estimatedBudget(BigDecimal estimatedBudget) { this.estimatedBudget = estimatedBudget; return this; }
        public CreateRequestBuilder preferredTravelClass(TravelClass preferredTravelClass) { this.preferredTravelClass = preferredTravelClass; return this; }
        public CreateRequestBuilder preferredHotelCategory(HotelCategory preferredHotelCategory) { this.preferredHotelCategory = preferredHotelCategory; return this; }
        public CreateRequestBuilder costCenterId(Long costCenterId) { this.costCenterId = costCenterId; return this; }

        public CreateRequest build() {
            CreateRequest obj = new CreateRequest();
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
            obj.setCostCenterId(this.costCenterId);
            return obj;
        }
    }
    }

    public static class ApprovalActionRequest {
        @NotNull
        private ApprovalStatus status;
        private String comments;

    public ApprovalActionRequest() {}

    public ApprovalActionRequest(ApprovalStatus status, String comments) {
        this.status = status;
        this.comments = comments;
    }

    public ApprovalStatus getStatus() { return status; }

    public void setStatus(ApprovalStatus status) { this.status = status; }

    public String getComments() { return comments; }

    public void setComments(String comments) { this.comments = comments; }

    public static ApprovalActionRequestBuilder builder() { return new ApprovalActionRequestBuilder(); }

    public static class ApprovalActionRequestBuilder {
        private ApprovalStatus status;
        private String comments;

        public ApprovalActionRequestBuilder status(ApprovalStatus status) { this.status = status; return this; }
        public ApprovalActionRequestBuilder comments(String comments) { this.comments = comments; return this; }

        public ApprovalActionRequest build() {
            ApprovalActionRequest obj = new ApprovalActionRequest();
            obj.setStatus(this.status);
            obj.setComments(this.comments);
            return obj;
        }
    }
    }

    public static class Response {
        private Long id;
        private String requestNumber;
        private String tripName;
        private TripType tripType;
        private String origin;
        private String destination;
        private LocalDate departureDate;
        private LocalDate returnDate;
        private Boolean roundTrip;
        private Boolean personalTrip;
        private Integer numberOfTravelers;
        private String businessJustification;
        private String clientOrEventName;
        private BigDecimal estimatedBudget;
        private TravelClass preferredTravelClass;
        private HotelCategory preferredHotelCategory;
        private RequestStatus status;
        private PolicyComplianceStatus complianceStatus;
        private String policyViolationReason;
        private Long employeeId;
        private String employeeName;
        private String employeeEmail;
        private String departmentName;
        private String costCenterCode;
        private LocalDateTime createdAt;
        private List<ApprovalStepDto> approvalSteps;

    public Response() {}

    public Response(Long id, String requestNumber, String tripName, TripType tripType, String origin, String destination, LocalDate departureDate, LocalDate returnDate, Boolean roundTrip, Boolean personalTrip, Integer numberOfTravelers, String businessJustification, String clientOrEventName, BigDecimal estimatedBudget, TravelClass preferredTravelClass, HotelCategory preferredHotelCategory, RequestStatus status, PolicyComplianceStatus complianceStatus, String policyViolationReason, Long employeeId, String employeeName, String employeeEmail, String departmentName, String costCenterCode, LocalDateTime createdAt, List<ApprovalStepDto> approvalSteps) {
        this.id = id;
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
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeEmail = employeeEmail;
        this.departmentName = departmentName;
        this.costCenterCode = costCenterCode;
        this.createdAt = createdAt;
        this.approvalSteps = approvalSteps;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

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

    public Long getEmployeeId() { return employeeId; }

    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public String getEmployeeName() { return employeeName; }

    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getEmployeeEmail() { return employeeEmail; }

    public void setEmployeeEmail(String employeeEmail) { this.employeeEmail = employeeEmail; }

    public String getDepartmentName() { return departmentName; }

    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public String getCostCenterCode() { return costCenterCode; }

    public void setCostCenterCode(String costCenterCode) { this.costCenterCode = costCenterCode; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<ApprovalStepDto> getApprovalSteps() { return approvalSteps; }

    public void setApprovalSteps(List<ApprovalStepDto> approvalSteps) { this.approvalSteps = approvalSteps; }

    public static ResponseBuilder builder() { return new ResponseBuilder(); }

    public static class ResponseBuilder {
        private Long id;
        private String requestNumber;
        private String tripName;
        private TripType tripType;
        private String origin;
        private String destination;
        private LocalDate departureDate;
        private LocalDate returnDate;
        private Boolean roundTrip;
        private Boolean personalTrip;
        private Integer numberOfTravelers;
        private String businessJustification;
        private String clientOrEventName;
        private BigDecimal estimatedBudget;
        private TravelClass preferredTravelClass;
        private HotelCategory preferredHotelCategory;
        private RequestStatus status;
        private PolicyComplianceStatus complianceStatus;
        private String policyViolationReason;
        private Long employeeId;
        private String employeeName;
        private String employeeEmail;
        private String departmentName;
        private String costCenterCode;
        private LocalDateTime createdAt;
        private List<ApprovalStepDto> approvalSteps;

        public ResponseBuilder id(Long id) { this.id = id; return this; }
        public ResponseBuilder requestNumber(String requestNumber) { this.requestNumber = requestNumber; return this; }
        public ResponseBuilder tripName(String tripName) { this.tripName = tripName; return this; }
        public ResponseBuilder tripType(TripType tripType) { this.tripType = tripType; return this; }
        public ResponseBuilder origin(String origin) { this.origin = origin; return this; }
        public ResponseBuilder destination(String destination) { this.destination = destination; return this; }
        public ResponseBuilder departureDate(LocalDate departureDate) { this.departureDate = departureDate; return this; }
        public ResponseBuilder returnDate(LocalDate returnDate) { this.returnDate = returnDate; return this; }
        public ResponseBuilder roundTrip(Boolean roundTrip) { this.roundTrip = roundTrip; return this; }
        public ResponseBuilder personalTrip(Boolean personalTrip) { this.personalTrip = personalTrip; return this; }
        public ResponseBuilder numberOfTravelers(Integer numberOfTravelers) { this.numberOfTravelers = numberOfTravelers; return this; }
        public ResponseBuilder businessJustification(String businessJustification) { this.businessJustification = businessJustification; return this; }
        public ResponseBuilder clientOrEventName(String clientOrEventName) { this.clientOrEventName = clientOrEventName; return this; }
        public ResponseBuilder estimatedBudget(BigDecimal estimatedBudget) { this.estimatedBudget = estimatedBudget; return this; }
        public ResponseBuilder preferredTravelClass(TravelClass preferredTravelClass) { this.preferredTravelClass = preferredTravelClass; return this; }
        public ResponseBuilder preferredHotelCategory(HotelCategory preferredHotelCategory) { this.preferredHotelCategory = preferredHotelCategory; return this; }
        public ResponseBuilder status(RequestStatus status) { this.status = status; return this; }
        public ResponseBuilder complianceStatus(PolicyComplianceStatus complianceStatus) { this.complianceStatus = complianceStatus; return this; }
        public ResponseBuilder policyViolationReason(String policyViolationReason) { this.policyViolationReason = policyViolationReason; return this; }
        public ResponseBuilder employeeId(Long employeeId) { this.employeeId = employeeId; return this; }
        public ResponseBuilder employeeName(String employeeName) { this.employeeName = employeeName; return this; }
        public ResponseBuilder employeeEmail(String employeeEmail) { this.employeeEmail = employeeEmail; return this; }
        public ResponseBuilder departmentName(String departmentName) { this.departmentName = departmentName; return this; }
        public ResponseBuilder costCenterCode(String costCenterCode) { this.costCenterCode = costCenterCode; return this; }
        public ResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ResponseBuilder approvalSteps(List<ApprovalStepDto> approvalSteps) { this.approvalSteps = approvalSteps; return this; }

        public Response build() {
            Response obj = new Response();
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
            obj.setEmployeeId(this.employeeId);
            obj.setEmployeeName(this.employeeName);
            obj.setEmployeeEmail(this.employeeEmail);
            obj.setDepartmentName(this.departmentName);
            obj.setCostCenterCode(this.costCenterCode);
            obj.setCreatedAt(this.createdAt);
            obj.setApprovalSteps(this.approvalSteps);
            return obj;
        }
    }
    }

    public static class ApprovalStepDto {
        private Long id;
        private Integer stepOrder;
        private String approverRole;
        private String approverName;
        private ApprovalStatus status;
        private String comments;
        private LocalDateTime actionTimestamp;

    public ApprovalStepDto() {}

    public ApprovalStepDto(Long id, Integer stepOrder, String approverRole, String approverName, ApprovalStatus status, String comments, LocalDateTime actionTimestamp) {
        this.id = id;
        this.stepOrder = stepOrder;
        this.approverRole = approverRole;
        this.approverName = approverName;
        this.status = status;
        this.comments = comments;
        this.actionTimestamp = actionTimestamp;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Integer getStepOrder() { return stepOrder; }

    public void setStepOrder(Integer stepOrder) { this.stepOrder = stepOrder; }

    public String getApproverRole() { return approverRole; }

    public void setApproverRole(String approverRole) { this.approverRole = approverRole; }

    public String getApproverName() { return approverName; }

    public void setApproverName(String approverName) { this.approverName = approverName; }

    public ApprovalStatus getStatus() { return status; }

    public void setStatus(ApprovalStatus status) { this.status = status; }

    public String getComments() { return comments; }

    public void setComments(String comments) { this.comments = comments; }

    public LocalDateTime getActionTimestamp() { return actionTimestamp; }

    public void setActionTimestamp(LocalDateTime actionTimestamp) { this.actionTimestamp = actionTimestamp; }

    public static ApprovalStepDtoBuilder builder() { return new ApprovalStepDtoBuilder(); }

    public static class ApprovalStepDtoBuilder {
        private Long id;
        private Integer stepOrder;
        private String approverRole;
        private String approverName;
        private ApprovalStatus status;
        private String comments;
        private LocalDateTime actionTimestamp;

        public ApprovalStepDtoBuilder id(Long id) { this.id = id; return this; }
        public ApprovalStepDtoBuilder stepOrder(Integer stepOrder) { this.stepOrder = stepOrder; return this; }
        public ApprovalStepDtoBuilder approverRole(String approverRole) { this.approverRole = approverRole; return this; }
        public ApprovalStepDtoBuilder approverName(String approverName) { this.approverName = approverName; return this; }
        public ApprovalStepDtoBuilder status(ApprovalStatus status) { this.status = status; return this; }
        public ApprovalStepDtoBuilder comments(String comments) { this.comments = comments; return this; }
        public ApprovalStepDtoBuilder actionTimestamp(LocalDateTime actionTimestamp) { this.actionTimestamp = actionTimestamp; return this; }

        public ApprovalStepDto build() {
            ApprovalStepDto obj = new ApprovalStepDto();
            obj.setId(this.id);
            obj.setStepOrder(this.stepOrder);
            obj.setApproverRole(this.approverRole);
            obj.setApproverName(this.approverName);
            obj.setStatus(this.status);
            obj.setComments(this.comments);
            obj.setActionTimestamp(this.actionTimestamp);
            return obj;
        }
    }
    }


    public TravelRequestDto() {}

    public static TravelRequestDtoBuilder builder() { return new TravelRequestDtoBuilder(); }

    public static class TravelRequestDtoBuilder {


        public TravelRequestDto build() {
            TravelRequestDto obj = new TravelRequestDto();
            return obj;
        }
    }
}
