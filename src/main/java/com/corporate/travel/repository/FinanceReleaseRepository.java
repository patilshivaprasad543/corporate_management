package com.corporate.travel.repository;

import com.corporate.travel.entity.FinanceRelease;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FinanceReleaseRepository extends JpaRepository<FinanceRelease, Long> {
    List<FinanceRelease> findByTravelRequestIdOrderByCreatedAtDesc(Long travelRequestId);
}
