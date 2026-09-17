package com.corporate.travel.repository;

import com.corporate.travel.entity.TravelBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TravelBudgetRepository extends JpaRepository<TravelBudget, Long> {
    Optional<TravelBudget> findByTravelRequestId(Long travelRequestId);
}
