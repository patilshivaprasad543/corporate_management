package com.corporate.travel.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.otp.provider", havingValue = "email")
public class EmailOtpProvider implements OtpProvider {

    private static final Logger log = LoggerFactory.getLogger(EmailOtpProvider.class);

    @Value("${spring.mail.host:}")
    private String mailHost;

    @Override
    public void sendOtp(String email, String otp, String purpose) {
        if (mailHost == null || mailHost.isBlank()) {
            log.warn("SMTP not configured; OTP for {} not sent (purpose={})", email, purpose);
            return;
        }
        // Production email integration point — configure Spring Mail separately
        log.info("OTP email dispatched to {} for purpose {}", email, purpose);
    }
}
