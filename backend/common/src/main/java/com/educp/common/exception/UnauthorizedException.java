package com.educp.common.exception;

public class UnauthorizedException extends BaseException {

    public UnauthorizedException(String message) {
        super(401, "UNAUTHORIZED", message);
    }

    public UnauthorizedException() {
        super(401, "UNAUTHORIZED", "未登录或登录已过期");
    }
}
