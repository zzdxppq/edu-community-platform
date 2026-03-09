package com.educp.common.exception;

public class ForbiddenException extends BaseException {

    public ForbiddenException(String message) {
        super(403, "FORBIDDEN", message);
    }

    public ForbiddenException() {
        super(403, "FORBIDDEN", "无访问权限");
    }
}
