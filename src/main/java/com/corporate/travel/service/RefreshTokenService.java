package com.corporate.travel.service;

import com.corporate.travel.entity.RefreshToken;
import com.corporate.travel.entity.User;
import com.corporate.travel.entity.enums.PortalType;
import com.corporate.travel.exception.UnauthorizedException;
import com.corporate.travel.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class RefreshTokenService {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.jwt.refresh-expiration-ms:604800000}")
    private long refreshExpirationMs;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public String createRefreshToken(User user, PortalType portal) {
        byte[] bytes = new byte[48];
        RANDOM.nextBytes(bytes);
        String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setTokenHash(hashToken(rawToken));
        token.setExpiresAt(LocalDateTime.now().plusSeconds(refreshExpirationMs / 1000));
        token.setPortal(portal != null ? portal.name() : null);
        refreshTokenRepository.save(token);

        return rawToken;
    }

    @Transactional(readOnly = true)
    public RefreshToken validateRefreshToken(String rawToken) {
        return refreshTokenRepository.findByTokenHash(hashToken(rawToken))
                .filter(RefreshToken::isValid)
                .orElseThrow(() -> new UnauthorizedException("Invalid or expired refresh token"));
    }

    @Transactional
    public void revokeAllForUser(User user) {
        refreshTokenRepository.revokeAllByUser(user);
    }

    @Transactional
    public void revokeToken(RefreshToken token) {
        token.setRevoked(true);
        token.setRevokedAt(LocalDateTime.now());
        refreshTokenRepository.save(token);
    }

    private String hashToken(String raw) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
