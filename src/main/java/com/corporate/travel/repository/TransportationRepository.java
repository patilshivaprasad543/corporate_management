package com.corporate.travel.repository;

import com.corporate.travel.entity.Transportation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportationRepository extends JpaRepository<Transportation, Long> {
    List<Transportation> findByPickupLocationContainingIgnoreCase(String query);
}
