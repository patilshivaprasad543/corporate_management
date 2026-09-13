package com.corporate.travel.controller;

import com.corporate.travel.common.ApiResponse;
import com.corporate.travel.entity.ChatConversation;
import com.corporate.travel.entity.ChatMessage;
import com.corporate.travel.security.UserPrincipal;
import com.corporate.travel.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@Tag(name = "Real-Time Support Chat", description = "Traveler ↔ Support Agent, Travel Manager, and Vendor messaging")
public class ChatController {
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }


    private final ChatService chatService;

    @GetMapping("/conversation")
    @Operation(summary = "Get or create active support conversation")
    public ResponseEntity<ApiResponse<ChatConversation>> getConversation(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(name = "type", required = false, defaultValue = "SUPPORT") String type) {
        Long userId = principal != null ? principal.getId() : 5L;
        return ResponseEntity.ok(ApiResponse.ok(chatService.getOrCreateConversation(userId, type), "Conversation retrieved"));
    }

    @PostMapping("/messages")
    @Operation(summary = "Send a message in chat room")
    public ResponseEntity<ApiResponse<ChatMessage>> sendMessage(
            @RequestParam("roomId") String roomId,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(name = "senderType", required = false, defaultValue = "USER") String senderType,
            @RequestBody String message) {
        Long senderId = principal != null ? principal.getId() : 5L;
        return ResponseEntity.ok(ApiResponse.ok(chatService.sendMessage(roomId, senderId, senderType, message), "Message sent"));
    }

    @GetMapping("/messages/{conversationId}")
    @Operation(summary = "Get conversation message history")
    public ResponseEntity<ApiResponse<List<ChatMessage>>> getMessages(@PathVariable("conversationId") Long conversationId) {
        return ResponseEntity.ok(ApiResponse.ok(chatService.getMessages(conversationId), "Messages retrieved"));
    }

}
