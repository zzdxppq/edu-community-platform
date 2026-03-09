package com.educp.common.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

    private final String code;
    private final int httpStatus;

    protected BaseException(int httpStatus, String code, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.code = code;
    }

    protected BaseException(int httpStatus, String code, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.code = code;
    }
}
