package com.corporate.travel.controller;

import com.corporate.travel.common.ApiResponse;
import com.corporate.travel.entity.Notification;
import com.corporate.travel.security.UserPrincipal;
import com.corporate.travel.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "In-app and real-time push alerts")
public class NotificationController {
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Get recent notifications for logged in user")
    public ResponseEntity<ApiResponse<List<Notification>>> getMyNotifications(@AuthenticationPrincipal UserPrincipal principal) {
        Long userId = principal != null ? principal.getId() : 5L;
        return ResponseEntity.ok(ApiResponse.ok(notificationService.getMyNotifications(userId), "Notifications retrieved"));
    }

}
