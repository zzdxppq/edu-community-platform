package com.educp.common.security;

import lombok.Data;

@Data
public class UserContext {

    private Long userId;
    private Integer role;
    private Long schoolId;
    private String username;

    private static final ThreadLocal<UserContext> HOLDER = new ThreadLocal<>();

    public static UserContext current() {
        return HOLDER.get();
    }

    public static void set(UserContext ctx) {
        HOLDER.set(ctx);
    }

    public static void clear() {
        HOLDER.remove();
    }
}
