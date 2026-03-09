package com.educp.common.exception;

public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(String resource, Object id) {
        super(404, "NOT_FOUND", resource + "不存在: " + id);
    }

    public ResourceNotFoundException(String message) {
        super(404, "NOT_FOUND", message);
    }
}
