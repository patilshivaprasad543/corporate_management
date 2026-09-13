package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "chat_messages")

public class ChatMessage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private ChatConversation conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(name = "sender_type", length = 50)
    private String senderType;

    @Column(nullable = false, length = 2000)
    private String message;

    @Column(name = "attachment_url", length = 500)
    private String attachmentUrl;

    @Column(name = "is_read")

    private Boolean read = false;


    public ChatMessage() {}

    public ChatMessage(ChatConversation conversation, User sender, String senderType, String message, String attachmentUrl, Boolean read) {
        this.conversation = conversation;
        this.sender = sender;
        this.senderType = senderType;
        this.message = message;
        this.attachmentUrl = attachmentUrl;
        this.read = read;
    }

    public ChatConversation getConversation() { return conversation; }

    public void setConversation(ChatConversation conversation) { this.conversation = conversation; }

    public User getSender() { return sender; }

    public void setSender(User sender) { this.sender = sender; }

    public String getSenderType() { return senderType; }

    public void setSenderType(String senderType) { this.senderType = senderType; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public String getAttachmentUrl() { return attachmentUrl; }

    public void setAttachmentUrl(String attachmentUrl) { this.attachmentUrl = attachmentUrl; }

    public Boolean isRead() { return read; }

    public Boolean getRead() { return read; }

    public void setRead(Boolean read) { this.read = read; }

    public static ChatMessageBuilder builder() { return new ChatMessageBuilder(); }

    public static class ChatMessageBuilder {
        private Long id;
        private ChatConversation conversation;
        private User sender;
        private String senderType;
        private String message;
        private String attachmentUrl;
        private Boolean read = false;

        public ChatMessageBuilder id(Long id) { this.id = id; return this; }
        public ChatMessageBuilder conversation(ChatConversation conversation) { this.conversation = conversation; return this; }
        public ChatMessageBuilder sender(User sender) { this.sender = sender; return this; }
        public ChatMessageBuilder senderType(String senderType) { this.senderType = senderType; return this; }
        public ChatMessageBuilder message(String message) { this.message = message; return this; }
        public ChatMessageBuilder attachmentUrl(String attachmentUrl) { this.attachmentUrl = attachmentUrl; return this; }
        public ChatMessageBuilder read(Boolean read) { this.read = read; return this; }

        public ChatMessage build() {
            ChatMessage obj = new ChatMessage();
            obj.setId(this.id);
            obj.setConversation(this.conversation);
            obj.setSender(this.sender);
            obj.setSenderType(this.senderType);
            obj.setMessage(this.message);
            obj.setAttachmentUrl(this.attachmentUrl);
            obj.setRead(this.read);
            return obj;
        }
    }
}
