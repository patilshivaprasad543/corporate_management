package com.corporate.travel.repository;

import com.corporate.travel.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByOriginCityIgnoreCaseAndDestinationCityIgnoreCase(String origin, String destination);
}
