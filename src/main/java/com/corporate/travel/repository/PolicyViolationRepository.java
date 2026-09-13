package com.corporate.travel.repository;

import com.corporate.travel.entity.PolicyViolation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyViolationRepository extends JpaRepository<PolicyViolation, Long> {
    List<PolicyViolation> findByOrganization_IdOrderByCreatedAtDesc(Long organizationId);
    List<PolicyViolation> findByTravelRequest_IdOrderByCreatedAtDesc(Long travelRequestId);
}
