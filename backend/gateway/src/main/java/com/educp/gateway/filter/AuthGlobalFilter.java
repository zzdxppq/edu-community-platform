package com.educp.gateway.filter;

import com.educp.gateway.config.AuthProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final AuthProperties authProperties;
    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String REDIS_BLACKLIST_PREFIX = "auth:blacklist:";
    private static final String REDIS_USER_DISABLED_PREFIX = "auth:user_disabled:";
    private static final String HEADER_USER_ID = "X-User-Id";
    private static final String HEADER_USER_ROLE = "X-User-Role";
    private static final String HEADER_USER_SCHOOL_ID = "X-User-School-Id";
    private static final String HEADER_USER_NAME = "X-User-Name";
    private static final String HEADER_USER_IMPERSONATED = "X-User-Impersonated";

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // 白名单检查
        if (isWhitelisted(path)) {
            return chain.filter(exchange);
        }

        // 提取 Authorization header
        String authHeader = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION_HEADER);
        if (authHeader == null || authHeader.isBlank()) {
            return unauthorizedResponse(exchange, "未提供认证令牌");
        }

        if (!authHeader.startsWith(BEARER_PREFIX)) {
            return unauthorizedResponse(exchange, "认证令牌格式错误");
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        // 解析 JWT
        Claims claims;
        try {
            claims = parseJwt(token);
        } catch (ExpiredJwtException e) {
            return unauthorizedResponse(exchange, "认证令牌已过期");
        } catch (JwtException | IllegalArgumentException e) {
            return unauthorizedResponse(exchange, "无效的认证令牌");
        }

        String jti = claims.getId();
        String userId = claims.getSubject();

        // 检查用户是否被禁用（优先于 JTI 黑名单检查）
        return reactiveRedisTemplate.hasKey(REDIS_USER_DISABLED_PREFIX + userId)
                .flatMap(isDisabled -> {
                    if (Boolean.TRUE.equals(isDisabled)) {
                        return unauthorizedResponse(exchange, "账号已被禁用");
                    }

                    // 检查 Redis 黑名单
                    return reactiveRedisTemplate.hasKey(REDIS_BLACKLIST_PREFIX + jti)
                            .flatMap(inBlacklist -> {
                                if (Boolean.TRUE.equals(inBlacklist)) {
                                    return unauthorizedResponse(exchange, "认证令牌已失效");
                                }

                                // 清除客户端传入的 X-User-* 头（防止伪造），注入真实值
                                ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate()
                                        .headers(headers -> {
                                            headers.remove(HEADER_USER_ID);
                                            headers.remove(HEADER_USER_ROLE);
                                            headers.remove(HEADER_USER_SCHOOL_ID);
                                            headers.remove(HEADER_USER_NAME);
                                            headers.remove(HEADER_USER_IMPERSONATED);
                                        })
                                        .header(HEADER_USER_ID, userId)
                                        .header(HEADER_USER_ROLE, String.valueOf(claims.get("role", Integer.class)))
                                        .header(HEADER_USER_SCHOOL_ID, claims.get("schoolId") != null ? String.valueOf(claims.get("schoolId")) : "")
                                        .header(HEADER_USER_NAME, claims.get("username", String.class));

                                // 注入越权登录标记
                                Boolean impersonated = claims.get("impersonated", Boolean.class);
                                if (Boolean.TRUE.equals(impersonated)) {
                                    requestBuilder.header(HEADER_USER_IMPERSONATED, "true");
                                }

                                ServerHttpRequest mutatedRequest = requestBuilder.build();

                                return chain.filter(exchange.mutate().request(mutatedRequest).build());
                            });
                })
                .onErrorResume(e -> {
                    log.error("Redis 连接异常: {}", e.getMessage());
                    return unauthorizedResponse(exchange, "系统繁忙，请稍后重试");
                });
    }

    private boolean isWhitelisted(String path) {
        if (authProperties.getWhitelist() == null || authProperties.getWhitelist().isEmpty()) {
            log.warn("Auth whitelist is empty — all requests require authentication");
            return false;
        }
        return authProperties.getWhitelist().stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    private Claims parseJwt(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "code", 401,
                "message", message,
                "data", ""
        );

        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(body);
        } catch (JsonProcessingException e) {
            bytes = "{\"code\":401,\"message\":\"认证失败\",\"data\":null}".getBytes(StandardCharsets.UTF_8);
        }

        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
}
