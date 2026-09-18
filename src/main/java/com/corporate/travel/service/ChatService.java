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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ChatService {
    private static final Logger log = LoggerFactory.getLogger(ChatService.class);

    private static final Map<String, String> HUB_AGENTS = Map.of(
            "SUPPORT", "James Wilson — 24/7 Travel Care",
            "HR", "Rachel Green — HR & Duty of Care",
            "MANAGER", "Robert Vance — Line Manager"
    );

    private static final Map<String, String> HUB_GREETINGS = Map.of(
            "SUPPORT", "Hi! I'm from Global Travel Support. Share your PNR or trip issue and I'll assist immediately.",
            "HR", "Hello from HR Travel Desk. I can help with onboarding travel, visa letters, and duty-of-care updates.",
            "MANAGER", "Hi, I'm your line manager channel. Ask about budget approvals or policy exceptions here."
    );

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

    @Transactional(readOnly = true)
    public List<Map<String, Object>> listHubs(Long userId) {
        return HUB_AGENTS.entrySet().stream().map(entry -> {
            String type = entry.getKey();
            ChatConversation conv = conversationRepository
                    .findFirstByUserIdAndConversationTypeAndClosedFalseOrderByCreatedAtDesc(userId, type)
                    .orElse(null);
            long unread = 0;
            if (conv != null) {
                unread = messageRepository.findByConversationIdOrderByCreatedAtAsc(conv.getId()).stream()
                        .filter(m -> "AGENT".equals(m.getSenderType()) && !Boolean.TRUE.equals(m.isRead()))
                        .count();
            }
            Map<String, Object> hub = new LinkedHashMap<>();
            hub.put("type", type);
            hub.put("label", entry.getValue());
            hub.put("conversationId", conv != null ? conv.getId() : null);
            hub.put("roomId", conv != null ? conv.getRoomId() : null);
            hub.put("unreadCount", unread);
            return hub;
        }).toList();
    }

    @Transactional
    public ChatConversation getOrCreateConversation(Long userId, String type) {
        String hubType = type != null ? type.toUpperCase() : "SUPPORT";
        return conversationRepository
                .findFirstByUserIdAndConversationTypeAndClosedFalseOrderByCreatedAtDesc(userId, hubType)
                .orElseGet(() -> createConversation(userId, hubType));
    }

    private ChatConversation createConversation(Long userId, String hubType) {
        User user = userRepository.findById(userId).orElseThrow();
        ChatConversation conv = conversationRepository.save(ChatConversation.builder()
                .roomId("ROOM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .conversationType(hubType)
                .user(user)
                .closed(false)
                .build());

        String greeting = HUB_GREETINGS.getOrDefault(hubType, HUB_GREETINGS.get("SUPPORT"));
        ChatMessage welcome = messageRepository.save(ChatMessage.builder()
                .conversation(conv)
                .sender(null)
                .senderType("AGENT")
                .message(greeting)
                .read(false)
                .build());
        publishMessage(conv.getRoomId(), welcome);
        return conv;
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
        publishMessage(roomId, saved);

        if ("USER".equals(saved.getSenderType())) {
            scheduleAgentReply(conv, text);
        }

        return saved;
    }

    private void scheduleAgentReply(ChatConversation conv, String userText) {
        String hubType = conv.getConversationType() != null ? conv.getConversationType() : "SUPPORT";
        String agentLabel = HUB_AGENTS.getOrDefault(hubType, "Travel Support");
        String reply = buildAgentReply(hubType, userText);

        ChatMessage agentMsg = messageRepository.save(ChatMessage.builder()
                .conversation(conv)
                .sender(null)
                .senderType("AGENT")
                .message(agentLabel.split("—")[0].trim() + ": " + reply)
                .read(false)
                .build());
        publishMessage(conv.getRoomId(), agentMsg);
    }

    private String buildAgentReply(String hubType, String userText) {
        String lower = userText.toLowerCase();
        if (lower.contains("pnr") || lower.contains("flight") || lower.contains("delay")) {
            return "I checked PNR683921 — your Delhi flight is on time. Would you like me to push a gate change alert to your email?";
        }
        if (lower.contains("hotel") || lower.contains("room")) {
            return "Corporate rate at Taj Diplomatic Enclave is confirmed. I can email the voucher PDF to you now.";
        }
        if ("HR".equals(hubType) && (lower.contains("visa") || lower.contains("letter"))) {
            return "HR can issue a business travel authorization letter within 2 hours. Shall I start the request?";
        }
        if ("MANAGER".equals(hubType) && (lower.contains("approve") || lower.contains("budget"))) {
            return "Your request is within the ₹25k auto-approval band. I can approve once you confirm the updated itinerary.";
        }
        return "Thanks for the update. I've logged this in your travel record and notified the relevant team via email.";
    }

    @Transactional
    public int markMessagesRead(Long conversationId, Long userId) {
        ChatConversation conv = conversationRepository.findById(conversationId).orElseThrow();
        if (!conv.getUser().getId().equals(userId)) {
            return 0;
        }
        List<ChatMessage> messages = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
        int count = 0;
        for (ChatMessage msg : messages) {
            if ("AGENT".equals(msg.getSenderType()) && !Boolean.TRUE.equals(msg.isRead())) {
                msg.setRead(true);
                count++;
            }
        }
        messageRepository.saveAll(messages);
        return count;
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> getMessages(Long conversationId) {
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }

    private void publishMessage(String roomId, ChatMessage saved) {
        try {
            messagingTemplate.convertAndSend("/topic/messages/" + roomId, saved);
        } catch (Exception ex) {
            log.debug("WebSocket publish skipped: {}", ex.getMessage());
        }
    }
}
