package com.corporate.travel.controller;

import com.corporate.travel.common.ApiResponse;
import com.corporate.travel.dto.ChatDto;
import com.corporate.travel.entity.ChatMessage;
import com.corporate.travel.security.UserPrincipal;
import com.corporate.travel.security.AuthenticatedUser;
import com.corporate.travel.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public ResponseEntity<ApiResponse<Map<String, Object>>> getConversation(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(name = "type", required = false, defaultValue = "SUPPORT") String type) {
        Long userId = AuthenticatedUser.requireId(principal);
        return ResponseEntity.ok(ApiResponse.ok(
                ChatDto.toConversationMap(chatService.getOrCreateConversation(userId, type)),
                "Conversation retrieved"));
    }

    @PostMapping("/messages")
    @Operation(summary = "Send a message in chat room")
    public ResponseEntity<ApiResponse<Map<String, Object>>> sendMessage(
            @RequestParam("roomId") String roomId,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(name = "senderType", required = false, defaultValue = "USER") String senderType,
            @RequestBody String message) {
        Long senderId = AuthenticatedUser.requireId(principal);
        ChatMessage saved = chatService.sendMessage(roomId, senderId, senderType, message);
        return ResponseEntity.ok(ApiResponse.ok(ChatDto.toMessageMap(saved), "Message sent"));
    }

    @GetMapping("/messages/{conversationId}")
    @Operation(summary = "Get conversation message history")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMessages(@PathVariable("conversationId") Long conversationId) {
        List<Map<String, Object>> payload = chatService.getMessages(conversationId).stream()
                .map(ChatDto::toMessageMap)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(payload, "Messages retrieved"));
    }

    @GetMapping("/hubs")
    @Operation(summary = "List multihub chat channels for current user")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> listHubs(
            @AuthenticationPrincipal UserPrincipal principal) {
        Long userId = AuthenticatedUser.requireId(principal);
        return ResponseEntity.ok(ApiResponse.ok(chatService.listHubs(userId), "Chat hubs retrieved"));
    }

    @PostMapping("/messages/{conversationId}/read")
    @Operation(summary = "Mark agent messages as read")
    public ResponseEntity<ApiResponse<Integer>> markRead(
            @PathVariable("conversationId") Long conversationId,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long userId = AuthenticatedUser.requireId(principal);
        return ResponseEntity.ok(ApiResponse.ok(chatService.markMessagesRead(conversationId, userId), "Messages marked read"));
    }

}
