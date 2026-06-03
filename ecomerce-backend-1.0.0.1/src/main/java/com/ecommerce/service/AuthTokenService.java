package com.ecommerce.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthTokenService {
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenService.class);
    private static final String TOKEN_KEY_PREFIX = "ecommerce:auth:token:";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public void storeToken(String tokenId, Long userId, String username, String roleCode, Date expiresAt) {
        if (tokenId == null || tokenId.isBlank() || expiresAt == null) {
            return;
        }
        Duration ttl = Duration.between(Instant.now(), expiresAt.toInstant());
        if (ttl.isNegative() || ttl.isZero()) {
            return;
        }
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("userId", userId);
        payload.put("username", username);
        payload.put("roleCode", roleCode);
        payload.put("expiresAt", expiresAt.getTime());
        try {
            redisTemplate.opsForValue().set(tokenKey(tokenId), objectMapper.writeValueAsString(payload), ttl);
        } catch (Exception e) {
            logger.warn("Redis token write failed, stateless JWT fallback will be used: {}", e.getMessage());
        }
    }

    public boolean isTokenActive(String tokenId) {
        if (tokenId == null || tokenId.isBlank()) {
            return false;
        }
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(tokenKey(tokenId)));
        } catch (Exception e) {
            logger.warn("Redis token check failed, accepting signed JWT fallback: {}", e.getMessage());
            return true;
        }
    }

    public void revokeToken(String tokenId) {
        if (tokenId == null || tokenId.isBlank()) {
            return;
        }
        try {
            redisTemplate.delete(tokenKey(tokenId));
        } catch (Exception e) {
            logger.warn("Redis token revoke failed: {}", e.getMessage());
        }
    }

    private String tokenKey(String tokenId) {
        return TOKEN_KEY_PREFIX + tokenId;
    }
}
