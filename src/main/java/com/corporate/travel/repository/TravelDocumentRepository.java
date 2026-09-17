package com.corporate.travel.repository;

import com.corporate.travel.entity.TravelDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TravelDocumentRepository extends JpaRepository<TravelDocument, Long> {
    List<TravelDocument> findByTravelRequestIdOrderByCreatedAtDesc(Long travelRequestId);
}
