package com.corporate.travel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corporate.travel.entity.ChatConversation;
import com.corporate.travel.entity.ChatMessage;
import com.corporate.travel.entity.User;
import com.corporate.travel.repository.ChatConversationRepository;
import com.corporate.travel.repository.ChatMessageRepository;
import com.corporate.travel.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ChatService {
    private static final Logger log = LoggerFactory.getLogger(ChatService.class);

    public ChatService(ChatConversationRepository conversationRepository, ChatMessageRepository messageRepository, UserRepository userRepository, SimpMessagingTemplate messagingTemplate) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }


    private final ChatConversationRepository conversationRepository;
    private final ChatMessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public ChatConversation getOrCreateConversation(Long userId, String type) {
        return conversationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream().findFirst()
                .orElseGet(() -> {
                    User user = userRepository.findById(userId).orElseThrow();
                    return conversationRepository.save(ChatConversation.builder()
                            .roomId("ROOM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                            .conversationType(type != null ? type : "SUPPORT")
                            .user(user)
                            .closed(false)
                            .build());
                });
    }

    @Transactional
    public ChatMessage sendMessage(String roomId, Long senderId, String senderType, String text) {
        ChatConversation conv = conversationRepository.findByRoomId(roomId)
                .orElseThrow();

        User sender = senderId != null ? userRepository.findById(senderId).orElse(null) : null;

        ChatMessage message = ChatMessage.builder()
                .conversation(conv)
                .sender(sender)
                .senderType(senderType != null ? senderType : "USER")
                .message(text)
                .read(false)
                .build();

        ChatMessage saved = messageRepository.save(message);

        try {
            messagingTemplate.convertAndSend("/topic/messages/" + roomId, saved);
        } catch (Exception ignored) {}

        return saved;
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> getMessages(Long conversationId) {
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }
}
