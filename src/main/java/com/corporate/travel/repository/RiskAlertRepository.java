package com.corporate.travel.repository;

import com.corporate.travel.entity.RiskAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskAlertRepository extends JpaRepository<RiskAlert, Long> {
    List<RiskAlert> findByActiveTrueOrderByCreatedAtDesc();
    List<RiskAlert> findByDestinationIgnoreCaseAndActiveTrue(String destination);
}
