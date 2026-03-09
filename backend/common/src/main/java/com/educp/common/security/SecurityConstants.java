package com.educp.common.security;

public final class SecurityConstants {

    private SecurityConstants() {
    }

    public static final String REDIS_REFRESH_PREFIX = "auth:refresh:";
    public static final String REDIS_BLACKLIST_PREFIX = "auth:blacklist:";
    public static final String REDIS_USER_DISABLED_PREFIX = "auth:user_disabled:";
    public static final String HEADER_USER_ID = "X-User-Id";
    public static final String HEADER_USER_ROLE = "X-User-Role";
    public static final String HEADER_USER_SCHOOL_ID = "X-User-School-Id";
    public static final String HEADER_USER_NAME = "X-User-Name";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_USER_IMPERSONATED = "X-User-Impersonated";
}
