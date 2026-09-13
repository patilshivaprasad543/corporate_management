package com.corporate.travel.service;

import com.corporate.travel.entity.OtpVerification;
import com.corporate.travel.entity.User;
import com.corporate.travel.exception.BadRequestException;
import com.corporate.travel.provider.OtpProvider;
import com.corporate.travel.repository.OtpVerificationRepository;
import com.corporate.travel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private static final String PURPOSE_EMAIL_VERIFICATION = "EMAIL_VERIFICATION";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final OtpVerificationRepository otpRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpProvider otpProvider;

    @Value("${app.otp.expiration-minutes:10}")
    private int expirationMinutes;

    @Value("${app.otp.max-attempts:5}")
    private int maxAttempts;

    @Value("${app.otp.resend-cooldown-seconds:60}")
    private int resendCooldownSeconds;

    private final Map<String, LocalDateTime> lastSentAt = new ConcurrentHashMap<>();

    public OtpService(OtpVerificationRepository otpRepository, UserRepository userRepository,
                      PasswordEncoder passwordEncoder, OtpProvider otpProvider) {
        this.otpRepository = otpRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpProvider = otpProvider;
    }

    @Transactional
    public void generateAndSendOtp(User user) {
        String email = user.getEmail();
        LocalDateTime last = lastSentAt.get(email);
        if (last != null && last.plusSeconds(resendCooldownSeconds).isAfter(LocalDateTime.now())) {
            throw new BadRequestException("Please wait before requesting another OTP");
        }

        String otp = String.format("%06d", RANDOM.nextInt(1_000_000));
        OtpVerification record = new OtpVerification();
        record.setUser(user);
        record.setEmail(email);
        record.setOtpHash(passwordEncoder.encode(otp));
        record.setExpiresAt(LocalDateTime.now().plusMinutes(expirationMinutes));
        record.setPurpose(PURPOSE_EMAIL_VERIFICATION);
        otpRepository.save(record);

        otpProvider.sendOtp(email, otp, PURPOSE_EMAIL_VERIFICATION);
        lastSentAt.put(email, LocalDateTime.now());
    }

    @Transactional
    public void verifyOtp(String email, String otp) {
        OtpVerification record = otpRepository
                .findTopByEmailAndPurposeAndUsedFalseOrderByCreatedAtDesc(email, PURPOSE_EMAIL_VERIFICATION)
                .orElseThrow(() -> new BadRequestException("No active OTP found for this email"));

        if (record.isExpired()) {
            throw new BadRequestException("OTP has expired");
        }

        if (record.getAttemptCount() >= maxAttempts) {
            throw new BadRequestException("Maximum OTP attempts exceeded");
        }

        record.setAttemptCount(record.getAttemptCount() + 1);
        otpRepository.save(record);

        if (!passwordEncoder.matches(otp, record.getOtpHash())) {
            throw new BadRequestException("Invalid OTP");
        }

        record.setUsed(true);
        record.setUsedAt(LocalDateTime.now());
        otpRepository.save(record);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));
        user.setEmailVerified(true);
        user.setStatus(com.corporate.travel.entity.enums.UserStatus.ACTIVE);
        userRepository.save(user);
    }
}
