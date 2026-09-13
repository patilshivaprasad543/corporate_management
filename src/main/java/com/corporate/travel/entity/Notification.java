package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import com.corporate.travel.entity.enums.NotificationType;
import jakarta.persistence.*;

@Entity
@Table(name = "notifications")

public class Notification extends BaseEntity {

    @com.fasterxml.jackson.annotation.JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 1000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType notificationType;

    @Column(name = "reference_link", length = 255)
    private String referenceLink;

    @Column(name = "is_read")

    private Boolean read = false;


    public Notification() {}

    public Notification(User user, String title, String message, NotificationType notificationType, String referenceLink, Boolean read) {
        this.user = user;
        this.title = title;
        this.message = message;
        this.notificationType = notificationType;
        this.referenceLink = referenceLink;
        this.read = read;
    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public NotificationType getNotificationType() { return notificationType; }

    public void setNotificationType(NotificationType notificationType) { this.notificationType = notificationType; }

    public String getReferenceLink() { return referenceLink; }

    public void setReferenceLink(String referenceLink) { this.referenceLink = referenceLink; }

    public Boolean isRead() { return read; }

    public Boolean getRead() { return read; }

    public void setRead(Boolean read) { this.read = read; }

    public static NotificationBuilder builder() { return new NotificationBuilder(); }

    public static class NotificationBuilder {
        private Long id;
        private User user;
        private String title;
        private String message;
        private NotificationType notificationType;
        private String referenceLink;
        private Boolean read = false;

        public NotificationBuilder id(Long id) { this.id = id; return this; }
        public NotificationBuilder user(User user) { this.user = user; return this; }
        public NotificationBuilder title(String title) { this.title = title; return this; }
        public NotificationBuilder message(String message) { this.message = message; return this; }
        public NotificationBuilder notificationType(NotificationType notificationType) { this.notificationType = notificationType; return this; }
        public NotificationBuilder referenceLink(String referenceLink) { this.referenceLink = referenceLink; return this; }
        public NotificationBuilder read(Boolean read) { this.read = read; return this; }

        public Notification build() {
            Notification obj = new Notification();
            obj.setId(this.id);
            obj.setUser(this.user);
            obj.setTitle(this.title);
            obj.setMessage(this.message);
            obj.setNotificationType(this.notificationType);
            obj.setReferenceLink(this.referenceLink);
            obj.setRead(this.read);
            return obj;
        }
    }
}
