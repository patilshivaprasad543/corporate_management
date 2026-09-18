package com.corporate.travel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.corporate.travel.entity.User;
import com.corporate.travel.entity.enums.NotificationType;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${app.mail.enabled:false}")
    private boolean mailEnabled;

    @Value("${app.mail.from:noreply@corporatetravel360.com}")
    private String fromAddress;

    public EmailService(org.springframework.beans.factory.ObjectProvider<JavaMailSender> mailSenderProvider) {
        this.mailSender = mailSenderProvider.getIfAvailable();
    }

    public void sendTravelNotification(User user, String title, String body, NotificationType type) {
        if (user == null || user.getEmail() == null) {
            return;
        }
        String subject = "[CorporateTravel360] " + title;
        String fullBody = buildBody(user, body, type);
        dispatch(user.getEmail(), subject, fullBody);
    }

    private String buildBody(User user, String message, NotificationType type) {
        String greeting = user.getFullName() != null ? user.getFullName() : user.getEmail();
        return """
                Hello %s,

                %s

                Notification type: %s

                —
                CorporateTravel360 Enterprise Travel Platform
                Acme Global Technologies Inc.
                """.formatted(greeting, message, type != null ? type.name() : "ALERT");
    }

    private void dispatch(String to, String subject, String body) {
        if (!mailEnabled || mailSender == null) {
            log.info("[EMAIL-DEMO] To: {} | Subject: {} | Body: {}", to, subject, body.replace("\n", " "));
            return;
        }
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(fromAddress);
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(body);
            mailSender.send(msg);
            log.info("Email sent to {}", to);
        } catch (Exception ex) {
            log.warn("Failed to send email to {}: {}", to, ex.getMessage());
        }
    }
}
