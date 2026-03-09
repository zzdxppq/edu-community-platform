package com.educp.common.exception;

public class BusinessException extends BaseException {

    public BusinessException(String message) {
        super(422, "BUSINESS_ERROR", message);
    }

    public BusinessException(String message, Throwable cause) {
        super(422, "BUSINESS_ERROR", message, cause);
    }
}
