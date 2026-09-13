package com.corporate.travel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corporate.travel.dto.TravelRequestDto;
import com.corporate.travel.entity.*;
import com.corporate.travel.entity.enums.*;
import com.corporate.travel.exception.ForbiddenException;
import com.corporate.travel.exception.ResourceNotFoundException;
import com.corporate.travel.repository.*;
import com.corporate.travel.security.TenantAccessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TravelRequestService {
    private static final Logger log = LoggerFactory.getLogger(TravelRequestService.class);

    public TravelRequestService(TravelRequestRepository requestRepository, UserRepository userRepository, OrganizationRepository organizationRepository, CostCenterRepository costCenterRepository, ApprovalStepRepository approvalStepRepository, PolicyEvaluationService policyEvaluationService, PolicyViolationService policyViolationService, TravelPolicyRepository travelPolicyRepository, NotificationService notificationService, AuditService auditService, TenantAccessService tenantAccessService) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.costCenterRepository = costCenterRepository;
        this.approvalStepRepository = approvalStepRepository;
        this.policyEvaluationService = policyEvaluationService;
        this.policyViolationService = policyViolationService;
        this.travelPolicyRepository = travelPolicyRepository;
        this.notificationService = notificationService;
        this.auditService = auditService;
        this.tenantAccessService = tenantAccessService;
    }


    private final TravelRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final CostCenterRepository costCenterRepository;
    private final ApprovalStepRepository approvalStepRepository;
    private final PolicyEvaluationService policyEvaluationService;
    private final PolicyViolationService policyViolationService;
    private final TravelPolicyRepository travelPolicyRepository;
    private final NotificationService notificationService;
    private final AuditService auditService;
    private final TenantAccessService tenantAccessService;

    @Transactional
    public TravelRequestDto.Response createRequest(Long userId, TravelRequestDto.CreateRequest dto) {
        User employee = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Organization org = employee.getOrganization();
        if (org == null) {
            throw new ForbiddenException("User is not assigned to a company");
        }
        tenantAccessService.assertCanAccessOrganization(org.getId());

        CostCenter costCenter = null;
        if (dto.getCostCenterId() != null) {
            costCenter = costCenterRepository.findById(dto.getCostCenterId()).orElse(null);
            if (costCenter != null && costCenter.getOrganization() != null
                    && !costCenter.getOrganization().getId().equals(org.getId())) {
                throw new ForbiddenException("Cost center does not belong to your company");
            }
        }

        String reqNum = "TR-" + (1000 + (int)(Math.random() * 9000));

        // Evaluate Policy
        PolicyEvaluationService.EvaluationResult eval = policyEvaluationService.evaluateTravelRequest(
                org.getId(),
                userId,
                dto.getEstimatedBudget(),
                dto.getPreferredTravelClass() != null ? dto.getPreferredTravelClass() : TravelClass.ECONOMY,
                dto.getDepartureDate(),
                false
        );

        TravelRequest request = TravelRequest.builder()
                .requestNumber(reqNum)
                .tripName(dto.getTripName())
                .tripType(dto.getTripType())
                .origin(dto.getOrigin())
                .destination(dto.getDestination())
                .departureDate(dto.getDepartureDate())
                .returnDate(dto.getReturnDate())
                .roundTrip(dto.getRoundTrip() != null ? dto.getRoundTrip() : true)
                .personalTrip(dto.getPersonalTrip() != null ? dto.getPersonalTrip() : false)
                .numberOfTravelers(dto.getNumberOfTravelers() != null ? dto.getNumberOfTravelers() : 1)
                .businessJustification(dto.getBusinessJustification())
                .clientOrEventName(dto.getClientOrEventName())
                .estimatedBudget(dto.getEstimatedBudget())
                .preferredTravelClass(dto.getPreferredTravelClass() != null ? dto.getPreferredTravelClass() : TravelClass.ECONOMY)
                .preferredHotelCategory(dto.getPreferredHotelCategory() != null ? dto.getPreferredHotelCategory() : HotelCategory.STANDARD_3_STAR)
                .status(RequestStatus.SUBMITTED)
                .complianceStatus(eval.status)
                .policyViolationReason(eval.reasons.isEmpty() ? null : String.join("; ", eval.reasons))
                .employee(employee)
                .costCenter(costCenter)
                .organization(org)
                .build();

        TravelRequest saved = requestRepository.save(request);

        TravelPolicy activePolicy = travelPolicyRepository.findFirstByOrganizationIdAndActiveTrue(org.getId()).orElse(null);
        if (!eval.violations.isEmpty()) {
            policyViolationService.persistViolations(org, activePolicy, employee, saved, eval.violations);
        }

        // Build Approval Steps
        List<ApprovalStep> steps = new ArrayList<>();
        int order = 1;

        // Step 1: Manager Review
        steps.add(ApprovalStep.builder()
                .travelRequest(saved)
                .stepOrder(order++)
                .approverRole("ROLE_APPROVER")
                .status(ApprovalStatus.PENDING)
                .comments("Awaiting Line Manager review")
                .build());

        // Step 2: Finance Review if required
        if (eval.requiresFinanceApproval) {
            steps.add(ApprovalStep.builder()
                    .travelRequest(saved)
                    .stepOrder(order++)
                    .approverRole("ROLE_FINANCE")
                    .status(ApprovalStatus.PENDING)
                    .comments("Finance budget compliance check required (> ₹25,000 threshold)")
                    .build());
        }

        // Step 3: Admin Review if large budget
        if (eval.requiresAdminApproval) {
            steps.add(ApprovalStep.builder()
                    .travelRequest(saved)
                    .stepOrder(order++)
                    .approverRole("ROLE_COMPANY_ADMIN")
                    .status(ApprovalStatus.PENDING)
                    .comments("Executive authorization required (> ₹1,00,000 threshold)")
                    .build());
        }

        approvalStepRepository.saveAll(steps);
        saved.setApprovalSteps(steps);

        notificationService.sendNotification(employee.getId(), "Travel Request Submitted",
                "Your travel request " + reqNum + " for '" + dto.getTripName() + "' has been submitted for approval.",
                NotificationType.REQUEST_SUBMITTED, "/requests");

        auditService.logAction(employee.getEmail(), "CREATE_TRAVEL_REQUEST", "TravelRequest", saved.getId(),
                "Submitted travel request " + reqNum + " for " + dto.getDestination(), null);

        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<TravelRequestDto.Response> getMyRequests(Long userId) {
        return requestRepository.findByEmployeeIdOrderByCreatedAtDesc(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TravelRequestDto.Response> getAllRequests() {
        Long scope = tenantAccessService.resolveOrganizationScope();
        List<TravelRequest> requests = scope == null
                ? requestRepository.findAll()
                : requestRepository.findByOrganizationIdOrderByCreatedAtDesc(scope);
        return requests.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TravelRequestDto.Response getRequestById(Long id) {
        TravelRequest req = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TravelRequest", "id", id));
        if (req.getOrganization() != null) {
            tenantAccessService.assertCanAccessResource(req.getOrganization().getId());
        }
        return mapToResponse(req);
    }

    public TravelRequestDto.Response mapToResponse(TravelRequest req) {
        List<TravelRequestDto.ApprovalStepDto> stepDtos = req.getApprovalSteps() != null ?
                req.getApprovalSteps().stream().map(s -> TravelRequestDto.ApprovalStepDto.builder()
                        .id(s.getId())
                        .stepOrder(s.getStepOrder())
                        .approverRole(s.getApproverRole())
                        .approverName(s.getApprover() != null ? s.getApprover().getFullName() : s.getApproverRole())
                        .status(s.getStatus())
                        .comments(s.getComments())
                        .actionTimestamp(s.getActionTimestamp())
                        .build()).collect(Collectors.toList()) : new ArrayList<>();

        return TravelRequestDto.Response.builder()
                .id(req.getId())
                .requestNumber(req.getRequestNumber())
                .tripName(req.getTripName())
                .tripType(req.getTripType())
                .origin(req.getOrigin())
                .destination(req.getDestination())
                .departureDate(req.getDepartureDate())
                .returnDate(req.getReturnDate())
                .roundTrip(req.getRoundTrip())
                .personalTrip(req.getPersonalTrip())
                .numberOfTravelers(req.getNumberOfTravelers())
                .businessJustification(req.getBusinessJustification())
                .clientOrEventName(req.getClientOrEventName())
                .estimatedBudget(req.getEstimatedBudget())
                .preferredTravelClass(req.getPreferredTravelClass())
                .preferredHotelCategory(req.getPreferredHotelCategory())
                .status(req.getStatus())
                .complianceStatus(req.getComplianceStatus())
                .policyViolationReason(req.getPolicyViolationReason())
                .employeeId(req.getEmployee() != null ? req.getEmployee().getId() : null)
                .employeeName(req.getEmployee() != null ? req.getEmployee().getFullName() : "")
                .employeeEmail(req.getEmployee() != null ? req.getEmployee().getEmail() : "")
                .departmentName(req.getDepartment() != null ? req.getDepartment().getName() : "Enterprise Corporate")
                .costCenterCode(req.getCostCenter() != null ? req.getCostCenter().getCode() : "CC-GENERAL")
                .createdAt(req.getCreatedAt())
                .approvalSteps(stepDtos)
                .build();
    }
}
