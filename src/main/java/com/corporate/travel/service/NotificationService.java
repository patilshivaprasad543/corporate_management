package com.corporate.travel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corporate.travel.entity.Notification;
import com.corporate.travel.entity.User;
import com.corporate.travel.entity.enums.NotificationType;
import com.corporate.travel.repository.NotificationRepository;
import com.corporate.travel.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository, SimpMessagingTemplate messagingTemplate, EmailService emailService) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
        this.emailService = emailService;
    }


    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final EmailService emailService;

    @Transactional
    public Notification sendNotification(Long userId, String title, String message, NotificationType type, String refLink) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return null;

        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .message(message)
                .notificationType(type)
                .referenceLink(refLink)
                .read(false)
                .build();

        Notification saved = notificationRepository.save(notification);

        try {
            messagingTemplate.convertAndSend("/topic/alerts/" + userId, saved);
        } catch (Exception ignored) {}

        try {
            emailService.sendTravelNotification(user, title, message, type);
        } catch (Exception ex) {
            log.warn("Email dispatch skipped for user {}: {}", userId, ex.getMessage());
        }

        return saved;
    }

    @Transactional
    public Notification markAsRead(Long notificationId, Long userId) {
        return notificationRepository.findById(notificationId)
                .filter(n -> n.getUser() != null && n.getUser().getId().equals(userId))
                .map(n -> {
                    n.setRead(true);
                    return notificationRepository.save(n);
                })
                .orElse(null);
    }

    @Transactional
    public int markAllRead(Long userId) {
        List<Notification> unread = notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
        return unread.size();
    }

    @Transactional(readOnly = true)
    public List<Notification> getMyNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
