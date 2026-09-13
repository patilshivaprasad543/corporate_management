package com.corporate.travel.service;

import com.corporate.travel.dto.OrganizationDto;
import com.corporate.travel.entity.Organization;
import com.corporate.travel.exception.BadRequestException;
import com.corporate.travel.exception.ForbiddenException;
import com.corporate.travel.exception.ResourceNotFoundException;
import com.corporate.travel.repository.BookingRepository;
import com.corporate.travel.repository.DepartmentRepository;
import com.corporate.travel.repository.OrganizationRepository;
import com.corporate.travel.repository.TravelRequestRepository;
import com.corporate.travel.repository.UserRepository;
import com.corporate.travel.security.TenantAccessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CompanyService {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final TravelRequestRepository travelRequestRepository;
    private final BookingRepository bookingRepository;
    private final TenantAccessService tenantAccessService;
    private final AuditService auditService;

    public CompanyService(OrganizationRepository organizationRepository,
                          UserRepository userRepository,
                          DepartmentRepository departmentRepository,
                          TravelRequestRepository travelRequestRepository,
                          BookingRepository bookingRepository,
                          TenantAccessService tenantAccessService,
                          AuditService auditService) {
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.travelRequestRepository = travelRequestRepository;
        this.bookingRepository = bookingRepository;
        this.tenantAccessService = tenantAccessService;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public List<OrganizationDto.CompanyResponse> listCompanies() {
        Long scope = tenantAccessService.resolveOrganizationScope();
        List<Organization> organizations = scope == null
                ? organizationRepository.findAllByOrderByNameAsc()
                : List.of(organizationRepository.findById(scope)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", scope)));
        return organizations.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public OrganizationDto.CompanyResponse getCompany(Long id) {
        Organization org = loadAccessibleCompany(id);
        return toResponse(org);
    }

    @Transactional
    public OrganizationDto.CompanyResponse createCompany(OrganizationDto.CreateCompanyRequest request, String actorEmail) {
        if (!tenantAccessService.isSuperAdmin()) {
            throw new ForbiddenException("Only super administrators can create companies");
        }
        if (organizationRepository.existsByCode(request.getCode())) {
            throw new BadRequestException("Company code already exists");
        }

        Organization org = Organization.builder()
                .name(request.getName())
                .code(request.getCode().trim().toUpperCase())
                .registrationNumber(request.getRegistrationNumber())
                .taxNumber(request.getTaxNumber())
                .email(request.getEmail())
                .phone(request.getPhone())
                .domainName(request.getDomainName())
                .addressLine1(request.getAddressLine1())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .currencyCode(request.getCurrencyCode())
                .timezone(request.getTimezone() != null ? request.getTimezone() : "UTC")
                .annualTravelBudget(request.getAnnualTravelBudget() != null
                        ? request.getAnnualTravelBudget() : BigDecimal.valueOf(5_000_000))
                .active(true)
                .build();

        Organization saved = organizationRepository.save(org);
        auditService.logAction(actorEmail, "CREATE_COMPANY", "Organization", saved.getId(),
                "Created company " + saved.getName(), null);
        return toResponse(saved);
    }

    @Transactional
    public OrganizationDto.CompanyResponse updateCompany(Long id, OrganizationDto.UpdateCompanyRequest request, String actorEmail) {
        Organization org = loadAccessibleCompany(id);
        if (!tenantAccessService.isSuperAdmin()) {
            throw new ForbiddenException("Only super administrators can update companies");
        }

        org.setName(request.getName());
        org.setRegistrationNumber(request.getRegistrationNumber());
        org.setTaxNumber(request.getTaxNumber());
        org.setEmail(request.getEmail());
        org.setPhone(request.getPhone());
        org.setDomainName(request.getDomainName());
        org.setAddressLine1(request.getAddressLine1());
        org.setCity(request.getCity());
        org.setState(request.getState());
        org.setCountry(request.getCountry());
        org.setCurrencyCode(request.getCurrencyCode());
        org.setTimezone(request.getTimezone() != null ? request.getTimezone() : org.getTimezone());
        if (request.getAnnualTravelBudget() != null) {
            org.setAnnualTravelBudget(request.getAnnualTravelBudget());
        }

        Organization saved = organizationRepository.save(org);
        auditService.logAction(actorEmail, "UPDATE_COMPANY", "Organization", saved.getId(),
                "Updated company " + saved.getName(), null);
        return toResponse(saved);
    }

    @Transactional
    public OrganizationDto.CompanyResponse activateCompany(Long id, String actorEmail) {
        return setActive(id, true, actorEmail);
    }

    @Transactional
    public OrganizationDto.CompanyResponse deactivateCompany(Long id, String actorEmail) {
        return setActive(id, false, actorEmail);
    }

    @Transactional(readOnly = true)
    public OrganizationDto.SystemStats getSystemStats() {
        if (!tenantAccessService.isSuperAdmin()) {
            throw new ForbiddenException("System statistics require super administrator access");
        }
        OrganizationDto.SystemStats stats = new OrganizationDto.SystemStats();
        stats.setTotalCompanies(organizationRepository.count());
        stats.setActiveCompanies(organizationRepository.countByActiveTrue());
        stats.setTotalUsers(userRepository.count());
        stats.setTotalDepartments(departmentRepository.count());
        stats.setTotalTravelRequests(travelRequestRepository.count());
        stats.setTotalBookings(bookingRepository.count());
        return stats;
    }

    private OrganizationDto.CompanyResponse setActive(Long id, boolean active, String actorEmail) {
        if (!tenantAccessService.isSuperAdmin()) {
            throw new ForbiddenException("Only super administrators can change company status");
        }
        Organization org = loadAccessibleCompany(id);
        org.setActive(active);
        Organization saved = organizationRepository.save(org);
        auditService.logAction(actorEmail, active ? "ACTIVATE_COMPANY" : "DEACTIVATE_COMPANY",
                "Organization", saved.getId(), (active ? "Activated " : "Deactivated ") + saved.getName(), null);
        return toResponse(saved);
    }

    private Organization loadAccessibleCompany(Long id) {
        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", id));
        tenantAccessService.assertCanAccessOrganization(org.getId());
        return org;
    }

    private OrganizationDto.CompanyResponse toResponse(Organization org) {
        OrganizationDto.CompanyResponse response = new OrganizationDto.CompanyResponse();
        response.setId(org.getId());
        response.setName(org.getName());
        response.setCode(org.getCode());
        response.setRegistrationNumber(org.getRegistrationNumber());
        response.setTaxNumber(org.getTaxNumber());
        response.setEmail(org.getEmail());
        response.setPhone(org.getPhone());
        response.setDomainName(org.getDomainName());
        response.setAddressLine1(org.getAddressLine1());
        response.setCity(org.getCity());
        response.setState(org.getState());
        response.setCountry(org.getCountry());
        response.setCurrencyCode(org.getCurrencyCode());
        response.setTimezone(org.getTimezone());
        response.setAnnualTravelBudget(org.getAnnualTravelBudget());
        response.setActive(org.getActive());
        response.setEmployeeCount(userRepository.countByOrganization_Id(org.getId()));
        response.setDepartmentCount(departmentRepository.countByOrganization_Id(org.getId()));
        response.setCreatedAt(org.getCreatedAt());
        return response;
    }
}
