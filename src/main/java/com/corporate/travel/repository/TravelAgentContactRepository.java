package com.corporate.travel.repository;

import com.corporate.travel.entity.TravelAgentContact;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TravelAgentContactRepository extends JpaRepository<TravelAgentContact, Long> {
    Optional<TravelAgentContact> findByTravelRequestId(Long travelRequestId);
}
