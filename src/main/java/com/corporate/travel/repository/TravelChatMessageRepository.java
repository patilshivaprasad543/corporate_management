package com.corporate.travel.repository;

import com.corporate.travel.entity.TravelChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TravelChatMessageRepository extends JpaRepository<TravelChatMessage, Long> {
    List<TravelChatMessage> findByTravelRequestIdOrderByCreatedAtAsc(Long travelRequestId);
}
