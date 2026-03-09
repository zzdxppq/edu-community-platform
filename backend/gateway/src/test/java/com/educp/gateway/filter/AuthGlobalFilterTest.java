package com.educp.gateway.filter;

import com.educp.gateway.config.AuthProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthGlobalFilterTest {

    private AuthGlobalFilter filter;

    @Mock
    private ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    @Mock
    private GatewayFilterChain chain;

    private AuthProperties authProperties;
    private static final String JWT_SECRET = "edu-community-platform-jwt-secret-key-at-least-32-chars";

    @BeforeEach
    void setUp() {
        authProperties = new AuthProperties();
        authProperties.setWhitelist(List.of(
                "/api/core/v1/auth/login",
                "/api/core/v1/auth/refresh",
                "/actuator/**"
        ));

        filter = new AuthGlobalFilter(authProperties, reactiveRedisTemplate);
        ReflectionTestUtils.setField(filter, "jwtSecret", JWT_SECRET);
    }

    @Test
    void order_shouldBeHighestPrecedence() {
        assertEquals(Ordered.HIGHEST_PRECEDENCE, filter.getOrder());
    }

    @Test
    void filter_shouldPassWhitelistedPath() {
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/core/v1/auth/login").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(chain.filter(exchange)).thenReturn(Mono.empty());

        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();
    }

    @Test
    void filter_shouldPassActuatorPath() {
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/actuator/health").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(chain.filter(exchange)).thenReturn(Mono.empty());

        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();
    }

    @Test
    void filter_shouldReturn401_whenNoAuthorizationHeader() {
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/core/v1/admin-users").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    void filter_shouldReturn401_whenInvalidBearerFormat() {
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/core/v1/admin-users")
                .header("Authorization", "Basic abc123").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    void filter_shouldReturn401_whenInvalidJwt() {
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/core/v1/admin-users")
                .header("Authorization", "Bearer not.a.valid.jwt").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    void filter_shouldReturn401_whenExpiredJwt() {
        String expiredToken = generateTestToken(1L, "testuser", 1, null, -1000);

        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/core/v1/admin-users")
                .header("Authorization", "Bearer " + expiredToken).build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    void filter_shouldPass_whenValidTokenAndNotBlacklisted() {
        String validToken = generateTestToken(1L, "testuser", 1, 5L, 900000);

        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/core/v1/admin-users")
                .header("Authorization", "Bearer " + validToken).build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(reactiveRedisTemplate.hasKey(anyString())).thenReturn(Mono.just(false));
        when(chain.filter(org.mockito.ArgumentMatchers.any())).thenReturn(Mono.empty());

        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();
    }

    @Test
    void filter_shouldReturn401_whenTokenInBlacklist() {
        String validToken = generateTestToken(1L, "testuser", 1, null, 900000);

        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/core/v1/admin-users")
                .header("Authorization", "Bearer " + validToken).build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(reactiveRedisTemplate.hasKey(anyString())).thenReturn(Mono.just(true));

        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    private String generateTestToken(Long userId, String username, Integer role, Long schoolId, long expiryMillis) {
        SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        Date now = new Date();
        Date exp = new Date(now.getTime() + expiryMillis);

        var builder = Jwts.builder()
                .id("test-jti-" + System.nanoTime())
                .subject(String.valueOf(userId))
                .claim("username", username)
                .claim("role", role)
                .claim("type", "access")
                .issuedAt(now)
                .expiration(exp)
                .signWith(key);

        if (schoolId != null) {
            builder.claim("schoolId", schoolId);
        }

        return builder.compact();
    }
}
