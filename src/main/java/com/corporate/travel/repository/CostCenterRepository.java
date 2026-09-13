package com.corporate.travel.repository;

import com.corporate.travel.entity.CostCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CostCenterRepository extends JpaRepository<CostCenter, Long> {
    List<CostCenter> findByOrganizationId(Long organizationId);
}
