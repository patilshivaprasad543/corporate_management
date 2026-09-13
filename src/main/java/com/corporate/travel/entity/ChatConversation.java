package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_conversations")

public class ChatConversation extends BaseEntity {

    @Column(name = "room_id", nullable = false, unique = true, length = 100)
    private String roomId;

    @Column(name = "conversation_type", length = 50)
    private String conversationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_agent_id")
    private User assignedAgent;

    @Column(name = "is_closed")

    private Boolean closed = false;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")

    private List<ChatMessage> messages = new ArrayList<>();


    public ChatConversation() {}

    public ChatConversation(String roomId, String conversationType, User user, User assignedAgent, Boolean closed, List<ChatMessage> messages) {
        this.roomId = roomId;
        this.conversationType = conversationType;
        this.user = user;
        this.assignedAgent = assignedAgent;
        this.closed = closed;
        this.messages = messages;
    }

    public String getRoomId() { return roomId; }

    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getConversationType() { return conversationType; }

    public void setConversationType(String conversationType) { this.conversationType = conversationType; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public User getAssignedAgent() { return assignedAgent; }

    public void setAssignedAgent(User assignedAgent) { this.assignedAgent = assignedAgent; }

    public Boolean isClosed() { return closed; }

    public Boolean getClosed() { return closed; }

    public void setClosed(Boolean closed) { this.closed = closed; }

    public List<ChatMessage> getMessages() { return messages; }

    public void setMessages(List<ChatMessage> messages) { this.messages = messages; }

    public static ChatConversationBuilder builder() { return new ChatConversationBuilder(); }

    public static class ChatConversationBuilder {
        private Long id;
        private String roomId;
        private String conversationType;
        private User user;
        private User assignedAgent;
        private Boolean closed = false;
        private List<ChatMessage> messages = new ArrayList<>();

        public ChatConversationBuilder id(Long id) { this.id = id; return this; }
        public ChatConversationBuilder roomId(String roomId) { this.roomId = roomId; return this; }
        public ChatConversationBuilder conversationType(String conversationType) { this.conversationType = conversationType; return this; }
        public ChatConversationBuilder user(User user) { this.user = user; return this; }
        public ChatConversationBuilder assignedAgent(User assignedAgent) { this.assignedAgent = assignedAgent; return this; }
        public ChatConversationBuilder closed(Boolean closed) { this.closed = closed; return this; }
        public ChatConversationBuilder messages(List<ChatMessage> messages) { this.messages = messages; return this; }

        public ChatConversation build() {
            ChatConversation obj = new ChatConversation();
            obj.setId(this.id);
            obj.setRoomId(this.roomId);
            obj.setConversationType(this.conversationType);
            obj.setUser(this.user);
            obj.setAssignedAgent(this.assignedAgent);
            obj.setClosed(this.closed);
            obj.setMessages(this.messages);
            return obj;
        }
    }
}
