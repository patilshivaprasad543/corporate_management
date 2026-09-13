package com.corporate.travel.repository;

import com.corporate.travel.entity.ChatConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatConversationRepository extends JpaRepository<ChatConversation, Long> {
    Optional<ChatConversation> findByRoomId(String roomId);
    List<ChatConversation> findByUserIdOrderByCreatedAtDesc(Long userId);
}
