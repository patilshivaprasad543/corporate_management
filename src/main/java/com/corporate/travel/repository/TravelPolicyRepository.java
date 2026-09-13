package com.corporate.travel.repository;

import com.corporate.travel.entity.TravelPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TravelPolicyRepository extends JpaRepository<TravelPolicy, Long> {
    List<TravelPolicy> findByOrganizationIdAndActiveTrue(Long organizationId);
    Optional<TravelPolicy> findFirstByOrganizationIdAndActiveTrue(Long organizationId);
}
