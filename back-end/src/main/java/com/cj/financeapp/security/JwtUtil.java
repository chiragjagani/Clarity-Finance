package com.cj.financeapp.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    public String generateToken(Long userId) {
        log.info("🔐 Generating JWT for user ID: {}", userId);

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        log.info("📅 JWT issued at: {}", now);
        log.info("📅 JWT will expire at: {}", expiryDate);

        String jwt = Jwts.builder()
                .setSubject(Long.toString(userId))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();

        log.info("✅ JWT generated successfully");
        return jwt;
    }

    public String getUserIdFromJWT(String token) {
        try {
            log.info("🔍 Extracting user ID from JWT");
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            log.info("✅ User ID extracted: {}", claims.getSubject());
            return claims.getSubject();
        } catch (JwtException ex) {
            log.warn("⚠️ Failed to extract user ID from token: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    public boolean validateToken(String authToken) {
        try {
            log.info("🔒 Validating JWT token");
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            log.info("✅ Token is valid");
            return true;
        } catch (ExpiredJwtException ex) {
            log.warn("⏰ Token expired: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.warn("❌ Unsupported JWT: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.warn("⚠️ Malformed JWT: {}", ex.getMessage());
        } catch (SignatureException ex) {
            log.warn("🔐 Invalid signature: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.warn("❗ JWT claims string is empty: {}", ex.getMessage());
        }
        return false;
    }

    private Key getSigningKey() {
        try {
            byte[] secretBytes = jwtSecret.getBytes();
            Key key = Keys.hmacShaKeyFor(secretBytes);
            log.debug("🔑 Signing key generated successfully");
            return key;
        } catch (Exception ex) {
            log.error("❌ Error generating signing key: {}", ex.getMessage(), ex);
            throw new RuntimeException("Invalid secret key for JWT", ex);
        }
    }
}
