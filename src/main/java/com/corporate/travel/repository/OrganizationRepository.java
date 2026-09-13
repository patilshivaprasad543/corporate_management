package com.corporate.travel.repository;

import com.corporate.travel.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> findByCode(String code);
    List<Organization> findByActiveTrueOrderByNameAsc();
    List<Organization> findAllByOrderByNameAsc();
    boolean existsByCode(String code);
    long countByActiveTrue();
}
