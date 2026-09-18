package com.corporate.travel.dto;

import com.corporate.travel.entity.ChatConversation;
import com.corporate.travel.entity.ChatMessage;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public final class ChatDto {

    private ChatDto() {}

    public static Map<String, Object> toConversationMap(ChatConversation conv) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", conv.getId());
        map.put("roomId", conv.getRoomId());
        map.put("conversationType", conv.getConversationType());
        map.put("closed", conv.getClosed());
        return map;
    }

    public static Map<String, Object> toMessageMap(ChatMessage msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", msg.getId());
        map.put("message", msg.getMessage());
        map.put("senderType", msg.getSenderType());
        map.put("read", msg.getRead());
        map.put("createdAt", msg.getCreatedAt() != null ? msg.getCreatedAt().toString() : LocalDateTime.now().toString());
        map.put("attachmentUrl", msg.getAttachmentUrl());
        return map;
    }
}
