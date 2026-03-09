package com.educp.common.security;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private JwtProperties jwtProperties;

    @BeforeEach
    void setUp() {
        jwtProperties = new JwtProperties();
        jwtProperties.setSecret("edu-community-platform-jwt-secret-key-at-least-32-chars");
        jwtProperties.setAccessTokenExpiry(900);
        jwtProperties.setRefreshTokenExpiry(604800);
        jwtProperties.setIssuer("edu-community-platform");
        jwtUtils = new JwtUtils(jwtProperties);
    }

    @Test
    void generateAccessToken_shouldReturnValidJwt() {
        TokenPayload payload = TokenPayload.builder()
                .userId(1L).username("13800138000").role(1).schoolId(null).build();

        String token = jwtUtils.generateAccessToken(payload);

        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3);
    }

    @Test
    void generateRefreshToken_shouldReturnValidJwt() {
        TokenPayload payload = TokenPayload.builder()
                .userId(1L).username("13800138000").role(1).build();

        String token = jwtUtils.generateRefreshToken(payload);

        assertNotNull(token);
        TokenPayload parsed = jwtUtils.parseToken(token);
        assertEquals("refresh", parsed.getType());
        assertEquals(1L, parsed.getUserId());
    }

    @Test
    void parseToken_shouldExtractAllClaims() {
        TokenPayload payload = TokenPayload.builder()
                .userId(42L).username("15912345678").role(2).schoolId(5L).build();

        String token = jwtUtils.generateAccessToken(payload);
        TokenPayload parsed = jwtUtils.parseToken(token);

        assertEquals(42L, parsed.getUserId());
        assertEquals("15912345678", parsed.getUsername());
        assertEquals(2, parsed.getRole());
        assertEquals(5L, parsed.getSchoolId());
        assertEquals("access", parsed.getType());
        assertNotNull(parsed.getJti());
    }

    @Test
    void parseToken_shouldFailForInvalidSignature() {
        JwtProperties otherProps = new JwtProperties();
        otherProps.setSecret("another-secret-key-that-is-at-least-32-chars-long");
        otherProps.setAccessTokenExpiry(900);
        otherProps.setIssuer("edu-community-platform");
        JwtUtils otherJwtUtils = new JwtUtils(otherProps);

        TokenPayload payload = TokenPayload.builder().userId(1L).username("test").role(1).build();
        String token = otherJwtUtils.generateAccessToken(payload);

        assertThrows(JwtException.class, () -> jwtUtils.parseToken(token));
    }

    @Test
    void parseToken_shouldFailForExpiredToken() {
        JwtProperties shortProps = new JwtProperties();
        shortProps.setSecret("edu-community-platform-jwt-secret-key-at-least-32-chars");
        shortProps.setAccessTokenExpiry(0);
        shortProps.setIssuer("edu-community-platform");
        JwtUtils shortJwtUtils = new JwtUtils(shortProps);

        TokenPayload payload = TokenPayload.builder().userId(1L).username("test").role(1).build();
        String token = shortJwtUtils.generateAccessToken(payload);

        assertFalse(jwtUtils.validateToken(token));
    }

    @Test
    void validateToken_shouldReturnTrueForValidToken() {
        TokenPayload payload = TokenPayload.builder().userId(1L).username("test").role(1).build();
        String token = jwtUtils.generateAccessToken(payload);

        assertTrue(jwtUtils.validateToken(token));
    }

    @Test
    void validateToken_shouldReturnFalseForGarbageString() {
        assertFalse(jwtUtils.validateToken("not.a.jwt"));
    }

    @Test
    void extractJti_shouldReturnUniqueIds() {
        TokenPayload payload = TokenPayload.builder().userId(1L).username("test").role(1).build();
        String token1 = jwtUtils.generateAccessToken(payload);
        String token2 = jwtUtils.generateAccessToken(payload);

        assertNotEquals(jwtUtils.extractJti(token1), jwtUtils.extractJti(token2));
    }

    @Test
    void parseTokenIgnoreExpiry_shouldReturnClaimsForExpiredToken() {
        JwtProperties shortProps = new JwtProperties();
        shortProps.setSecret("edu-community-platform-jwt-secret-key-at-least-32-chars");
        shortProps.setAccessTokenExpiry(0);
        shortProps.setIssuer("edu-community-platform");
        JwtUtils shortJwtUtils = new JwtUtils(shortProps);

        TokenPayload payload = TokenPayload.builder().userId(99L).username("expired").role(1).build();
        String token = shortJwtUtils.generateAccessToken(payload);

        TokenPayload parsed = shortJwtUtils.parseTokenIgnoreExpiry(token);
        assertEquals(99L, parsed.getUserId());
    }

    @Test
    void getTokenRemainingSeconds_shouldReturnPositiveForValidToken() {
        TokenPayload payload = TokenPayload.builder().userId(1L).username("test").role(1).build();
        String token = jwtUtils.generateAccessToken(payload);

        long remaining = jwtUtils.getTokenRemainingSeconds(token);
        assertTrue(remaining > 0);
        assertTrue(remaining <= 900);
    }
}
