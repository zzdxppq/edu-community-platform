package com.educp.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionTest {

    @Test
    void businessException_shouldHaveCorrectCodeAndStatus() {
        BusinessException ex = new BusinessException("用户名已存在");
        assertEquals("BUSINESS_ERROR", ex.getCode());
        assertEquals(422, ex.getHttpStatus());
        assertEquals("用户名已存在", ex.getMessage());
    }

    @Test
    void businessException_withCause_shouldPreserveCause() {
        RuntimeException cause = new RuntimeException("root cause");
        BusinessException ex = new BusinessException("业务错误", cause);
        assertEquals("BUSINESS_ERROR", ex.getCode());
        assertEquals(422, ex.getHttpStatus());
        assertSame(cause, ex.getCause());
    }

    @Test
    void resourceNotFoundException_withResourceAndId_shouldFormatMessage() {
        ResourceNotFoundException ex = new ResourceNotFoundException("学校", 123);
        assertEquals("NOT_FOUND", ex.getCode());
        assertEquals(404, ex.getHttpStatus());
        assertEquals("学校不存在: 123", ex.getMessage());
    }

    @Test
    void resourceNotFoundException_withMessage_shouldUseDirectMessage() {
        ResourceNotFoundException ex = new ResourceNotFoundException("资源不存在");
        assertEquals("NOT_FOUND", ex.getCode());
        assertEquals(404, ex.getHttpStatus());
        assertEquals("资源不存在", ex.getMessage());
    }

    @Test
    void unauthorizedException_default_shouldHaveDefaultMessage() {
        UnauthorizedException ex = new UnauthorizedException();
        assertEquals("UNAUTHORIZED", ex.getCode());
        assertEquals(401, ex.getHttpStatus());
        assertEquals("未登录或登录已过期", ex.getMessage());
    }

    @Test
    void unauthorizedException_withMessage_shouldUseCustomMessage() {
        UnauthorizedException ex = new UnauthorizedException("Token已过期");
        assertEquals("UNAUTHORIZED", ex.getCode());
        assertEquals(401, ex.getHttpStatus());
        assertEquals("Token已过期", ex.getMessage());
    }

    @Test
    void forbiddenException_default_shouldHaveDefaultMessage() {
        ForbiddenException ex = new ForbiddenException();
        assertEquals("FORBIDDEN", ex.getCode());
        assertEquals(403, ex.getHttpStatus());
        assertEquals("无访问权限", ex.getMessage());
    }

    @Test
    void forbiddenException_withMessage_shouldUseCustomMessage() {
        ForbiddenException ex = new ForbiddenException("无管理员权限");
        assertEquals("FORBIDDEN", ex.getCode());
        assertEquals(403, ex.getHttpStatus());
        assertEquals("无管理员权限", ex.getMessage());
    }

    @Test
    void allExceptions_shouldExtendBaseException() {
        assertTrue(BaseException.class.isAssignableFrom(BusinessException.class));
        assertTrue(BaseException.class.isAssignableFrom(ResourceNotFoundException.class));
        assertTrue(BaseException.class.isAssignableFrom(UnauthorizedException.class));
        assertTrue(BaseException.class.isAssignableFrom(ForbiddenException.class));
    }

    @Test
    void allExceptions_shouldExtendRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(BaseException.class));
    }
}
