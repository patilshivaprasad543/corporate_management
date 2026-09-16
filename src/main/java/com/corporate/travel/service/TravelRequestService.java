package com.corporate.travel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corporate.travel.dto.TravelRequestDto;
import com.corporate.travel.entity.*;
import com.corporate.travel.entity.enums.*;
import com.corporate.travel.exception.ResourceNotFoundException;
import com.corporate.travel.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TravelRequestService {
    private static final Logger log = LoggerFactory.getLogger(TravelRequestService.class);

    private final TravelRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final CostCenterRepository costCenterRepository;
    private final ApprovalStepRepository approvalStepRepository;
    private final PolicyEvaluationService policyEvaluationService;
    private final NotificationService notificationService;
    private final AuditService auditService;

    public TravelRequestService(TravelRequestRepository requestRepository, UserRepository userRepository,
                                OrganizationRepository organizationRepository, CostCenterRepository costCenterRepository,
                                ApprovalStepRepository approvalStepRepository, PolicyEvaluationService policyEvaluationService,
                                NotificationService notificationService, AuditService auditService) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.costCenterRepository = costCenterRepository;
        this.approvalStepRepository = approvalStepRepository;
        this.policyEvaluationService = policyEvaluationService;
        this.notificationService = notificationService;
        this.auditService = auditService;
    }

    @Transactional
    public TravelRequestDto.Response createRequest(Long userId, TravelRequestDto.CreateRequest dto) {
        User employee = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Organization org = employee.getOrganization() != null ? employee.getOrganization()
                : organizationRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No organization is configured"));

        if (dto.getDepartureDate() == null || dto.getDepartureDate().isBefore(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("Departure date cannot be in the past");
        }
        if (dto.getReturnDate() != null && dto.getReturnDate().isBefore(dto.getDepartureDate())) {
            throw new IllegalArgumentException("Return date cannot be before departure date");
        }
        if (dto.getNumberOfTravelers() != null && dto.getNumberOfTravelers() < 1) {
            throw new IllegalArgumentException("At least one traveler is required");
        }
        if (dto.getEstimatedBudget() != null && dto.getEstimatedBudget().signum() < 0) {
            throw new IllegalArgumentException("Estimated budget cannot be negative");
        }

        CostCenter costCenter = dto.getCostCenterId() == null ? null :
                costCenterRepository.findById(dto.getCostCenterId()).orElse(null);
        String reqNum = "TR-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        TravelClass travelClass = dto.getPreferredTravelClass() != null ? dto.getPreferredTravelClass() : TravelClass.ECONOMY;
        PolicyEvaluationService.EvaluationResult eval = policyEvaluationService.evaluateTravelRequest(
                org.getId(), userId, dto.getEstimatedBudget(), travelClass, dto.getDepartureDate(), false);

        TravelRequest request = TravelRequest.builder()
                .requestNumber(reqNum)
                .tripName(dto.getTripName()).tripType(dto.getTripType())
                .origin(dto.getOrigin()).destination(dto.getDestination())
                .departureDate(dto.getDepartureDate()).returnDate(dto.getReturnDate())
                .roundTrip(dto.getRoundTrip() != null ? dto.getRoundTrip() : true)
                .personalTrip(dto.getPersonalTrip() != null ? dto.getPersonalTrip() : false)
                .numberOfTravelers(dto.getNumberOfTravelers() != null ? dto.getNumberOfTravelers() : 1)
                .businessJustification(dto.getBusinessJustification()).clientOrEventName(dto.getClientOrEventName())
                .estimatedBudget(dto.getEstimatedBudget()).preferredTravelClass(travelClass)
                .preferredHotelCategory(dto.getPreferredHotelCategory() != null ? dto.getPreferredHotelCategory() : HotelCategory.STANDARD_3_STAR)
                .status(RequestStatus.MANAGER_REVIEW).complianceStatus(eval.status)
                .policyViolationReason(eval.reasons.isEmpty() ? null : String.join("; ", eval.reasons))
                .employee(employee).costCenter(costCenter).organization(org).build();

        TravelRequest saved = requestRepository.save(request);
        List<ApprovalStep> steps = new ArrayList<>();
        int order = 1;
        steps.add(ApprovalStep.builder().travelRequest(saved).stepOrder(order++)
                .approverRole("ROLE_APPROVER").status(ApprovalStatus.PENDING)
                .comments("Awaiting line manager review").build());
        if (eval.requiresFinanceApproval) {
            steps.add(ApprovalStep.builder().travelRequest(saved).stepOrder(order++)
                    .approverRole("ROLE_FINANCE").status(ApprovalStatus.PENDING)
                    .comments("Finance budget compliance review required").build());
        }
        if (eval.requiresAdminApproval) {
            steps.add(ApprovalStep.builder().travelRequest(saved).stepOrder(order)
                    .approverRole("ROLE_COMPANY_ADMIN").status(ApprovalStatus.PENDING)
                    .comments("Executive authorization required").build());
        }
        approvalStepRepository.saveAll(steps);
        saved.setApprovalSteps(steps);

        notificationService.sendNotification(employee.getId(), "Travel Request Submitted",
                "Request " + reqNum + " is awaiting approval.", NotificationType.REQUEST_SUBMITTED, "/requests");
        auditService.logAction(employee.getEmail(), "CREATE_TRAVEL_REQUEST", "TravelRequest", saved.getId(),
                "Submitted " + reqNum + " for " + dto.getDestination(), null);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<TravelRequestDto.Response> getMyRequests(Long userId) {
        return requestRepository.findByEmployeeIdOrderByCreatedAtDesc(userId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TravelRequestDto.Response> getAllRequests() {
        return requestRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TravelRequestDto.Response getRequestById(Long id) {
        return mapToResponse(requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TravelRequest", "id", id)));
    }

    public TravelRequestDto.Response mapToResponse(TravelRequest req) {
        List<TravelRequestDto.ApprovalStepDto> stepDtos = req.getApprovalSteps() != null ?
                req.getApprovalSteps().stream().map(s -> TravelRequestDto.ApprovalStepDto.builder()
                        .id(s.getId()).stepOrder(s.getStepOrder()).approverRole(s.getApproverRole())
                        .approverName(s.getApprover() != null ? s.getApprover().getFullName() : s.getApproverRole())
                        .status(s.getStatus()).comments(s.getComments()).actionTimestamp(s.getActionTimestamp()).build())
                .collect(Collectors.toList()) : new ArrayList<>();
        return TravelRequestDto.Response.builder().id(req.getId()).requestNumber(req.getRequestNumber())
                .tripName(req.getTripName()).tripType(req.getTripType()).origin(req.getOrigin()).destination(req.getDestination())
                .departureDate(req.getDepartureDate()).returnDate(req.getReturnDate()).roundTrip(req.getRoundTrip())
                .personalTrip(req.getPersonalTrip()).numberOfTravelers(req.getNumberOfTravelers())
                .businessJustification(req.getBusinessJustification()).clientOrEventName(req.getClientOrEventName())
                .estimatedBudget(req.getEstimatedBudget()).preferredTravelClass(req.getPreferredTravelClass())
                .preferredHotelCategory(req.getPreferredHotelCategory()).status(req.getStatus()).complianceStatus(req.getComplianceStatus())
                .policyViolationReason(req.getPolicyViolationReason())
                .employeeId(req.getEmployee() != null ? req.getEmployee().getId() : null)
                .employeeName(req.getEmployee() != null ? req.getEmployee().getFullName() : "")
                .employeeEmail(req.getEmployee() != null ? req.getEmployee().getEmail() : "")
                .departmentName(req.getDepartment() != null ? req.getDepartment().getName() : "Enterprise Corporate")
                .costCenterCode(req.getCostCenter() != null ? req.getCostCenter().getCode() : "CC-GENERAL")
                .createdAt(req.getCreatedAt()).approvalSteps(stepDtos).build();
    }
}
