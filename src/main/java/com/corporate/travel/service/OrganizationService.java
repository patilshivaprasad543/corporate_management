package com.corporate.travel.service;

import com.corporate.travel.dto.OrganizationDto;
import com.corporate.travel.entity.Department;
import com.corporate.travel.entity.Organization;
import com.corporate.travel.exception.BadRequestException;
import com.corporate.travel.exception.ResourceNotFoundException;
import com.corporate.travel.repository.DepartmentRepository;
import com.corporate.travel.repository.OrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final DepartmentRepository departmentRepository;

    public OrganizationService(OrganizationRepository organizationRepository,
                               DepartmentRepository departmentRepository) {
        this.organizationRepository = organizationRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<OrganizationDto.CompanyOption> listActiveCompanies() {
        return organizationRepository.findByActiveTrueOrderByNameAsc()
                .stream()
                .map(this::toCompanyOption)
                .toList();
    }

    public List<OrganizationDto.DepartmentOption> listDepartments(Long organizationId) {
        Organization org = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        if (!Boolean.TRUE.equals(org.getActive())) {
            throw new BadRequestException("Company is not active");
        }
        return departmentRepository.findByOrganizationId(organizationId)
                .stream()
                .map(d -> new OrganizationDto.DepartmentOption(d.getId(), d.getName(), d.getCode()))
                .toList();
    }

    public Organization requireActiveOrganization(Long organizationId) {
        Organization org = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new BadRequestException("Company not found"));
        if (!Boolean.TRUE.equals(org.getActive())) {
            throw new BadRequestException("Company is not active");
        }
        return org;
    }

    private OrganizationDto.CompanyOption toCompanyOption(Organization org) {
        return new OrganizationDto.CompanyOption(
                org.getId(),
                org.getName(),
                org.getCode(),
                org.getCountry(),
                org.getCurrencyCode()
        );
    }
}
