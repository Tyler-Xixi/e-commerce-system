package com.ecommerce.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.header}")
    private String header;

    private SecretKey getSigningKey() {
        byte[] keyBytes = new byte[32];
        byte[] originalBytes = secret.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < 32; i++) {
            if (i < originalBytes.length) {
                keyBytes[i] = originalBytes[i];
            } else {
                keyBytes[i] = (byte) ('A' + i);
            }
        }
        // HS256 算法生成密钥
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Long userId, String username, String roleCode) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("roleCode", roleCode);
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + expiration);
        return Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(issuedAt)
                .setExpiration(expiresAt)
                .setSubject(username)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(String token) {
        return parseToken(token).getSubject();
    }

    public Long getUserIdFromToken(String token) {
        Object userId = parseToken(token).get("userId");
        if (userId instanceof Number number) {
            return number.longValue();
        }
        if (userId != null) {
            return Long.valueOf(userId.toString());
        }
        return null;
    }

    public String getRoleCodeFromToken(String token) {
        return parseToken(token).get("roleCode", String.class);
    }

    public String getTokenIdFromToken(String token) {
        return parseToken(token).getId();
    }

    public Date getExpirationDateFromToken(String token) {
        return parseToken(token).getExpiration();
    }

    public String getHeader() {
        return header;
    }
}
