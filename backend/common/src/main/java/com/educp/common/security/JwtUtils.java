package com.educp.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtProperties jwtProperties;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(TokenPayload payload) {
        String jti = UUID.randomUUID().toString().replace("-", "");
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.getAccessTokenExpiry() * 1000);

        return Jwts.builder()
                .id(jti)
                .subject(String.valueOf(payload.getUserId()))
                .claim("username", payload.getUsername())
                .claim("role", payload.getRole())
                .claim("schoolId", payload.getSchoolId())
                .claim("type", "access")
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    public String generateImpersonateToken(TokenPayload payload) {
        String jti = UUID.randomUUID().toString().replace("-", "");
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 1800 * 1000L);

        var builder = Jwts.builder()
                .id(jti)
                .subject(String.valueOf(payload.getUserId()))
                .claim("username", payload.getUsername())
                .claim("role", payload.getRole())
                .claim("schoolId", payload.getSchoolId())
                .claim("type", "access")
                .claim("impersonated", true)
                .claim("originalUserId", payload.getOriginalUserId())
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey());

        return builder.compact();
    }

    public String generateRefreshToken(TokenPayload payload) {
        String jti = UUID.randomUUID().toString().replace("-", "");
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.getRefreshTokenExpiry() * 1000);

        return Jwts.builder()
                .id(jti)
                .subject(String.valueOf(payload.getUserId()))
                .claim("type", "refresh")
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    public TokenPayload parseToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return buildPayloadFromClaims(claims);
    }

    public TokenPayload parseTokenIgnoreExpiry(String token) {
        try {
            return parseToken(token);
        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
            return buildPayloadFromClaims(claims);
        }
    }

    public String extractJti(String token) {
        return parseToken(token).getJti();
    }

    public String extractJtiIgnoreExpiry(String token) {
        return parseTokenIgnoreExpiry(token).getJti();
    }

    public long getTokenRemainingSeconds(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            long expMillis = claims.getExpiration().getTime();
            long remaining = (expMillis - System.currentTimeMillis()) / 1000;
            return Math.max(remaining, 0);
        } catch (ExpiredJwtException e) {
            return 0;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private TokenPayload buildPayloadFromClaims(Claims claims) {
        TokenPayload.TokenPayloadBuilder builder = TokenPayload.builder()
                .userId(Long.parseLong(claims.getSubject()))
                .username(claims.get("username", String.class))
                .role(claims.get("role", Integer.class))
                .schoolId(claims.get("schoolId", Long.class))
                .jti(claims.getId())
                .type(claims.get("type", String.class));

        Boolean impersonated = claims.get("impersonated", Boolean.class);
        if (Boolean.TRUE.equals(impersonated)) {
            builder.impersonated(true);
            Object originalUserId = claims.get("originalUserId");
            if (originalUserId instanceof Number) {
                builder.originalUserId(((Number) originalUserId).longValue());
            }
        }

        return builder.build();
    }
}
