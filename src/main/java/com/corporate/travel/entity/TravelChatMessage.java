package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "travel_chat_messages")
public class TravelChatMessage extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "travel_request_id", nullable = false)
    private TravelRequest travelRequest;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_user_id", nullable = false)
    private User sender;

    @Column(nullable = false, length = 4000) private String message;
    @Column(name = "attachment_name", length = 255) private String attachmentName;
    @Column(name = "attachment_key", length = 500) private String attachmentKey;
    @Column(name = "system_message", nullable = false) private boolean systemMessage = false;

    public TravelRequest getTravelRequest() { return travelRequest; }
    public void setTravelRequest(TravelRequest travelRequest) { this.travelRequest = travelRequest; }
    public User getSender() { return sender; }
    public void setSender(User sender) { this.sender = sender; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getAttachmentName() { return attachmentName; }
    public void setAttachmentName(String attachmentName) { this.attachmentName = attachmentName; }
    public String getAttachmentKey() { return attachmentKey; }
    public void setAttachmentKey(String attachmentKey) { this.attachmentKey = attachmentKey; }
    public boolean isSystemMessage() { return systemMessage; }
    public void setSystemMessage(boolean systemMessage) { this.systemMessage = systemMessage; }
}
