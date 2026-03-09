package com.educp.common.handler;

import com.educp.common.exception.BusinessException;
import com.educp.common.exception.ForbiddenException;
import com.educp.common.exception.ResourceNotFoundException;
import com.educp.common.exception.UnauthorizedException;
import com.educp.common.result.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleBusinessException_shouldReturn422() {
        BusinessException ex = new BusinessException("用户名已存在");
        ResponseEntity<Result<Void>> response = handler.handleBusinessException(ex);

        assertEquals(422, response.getStatusCode().value());
        assertEquals(422, response.getBody().getCode());
        assertEquals("用户名已存在", response.getBody().getMessage());
    }

    @Test
    void handleResourceNotFoundException_shouldReturn404() {
        ResourceNotFoundException ex = new ResourceNotFoundException("学校", 123);
        ResponseEntity<Result<Void>> response = handler.handleResourceNotFoundException(ex);

        assertEquals(404, response.getStatusCode().value());
        assertEquals(404, response.getBody().getCode());
        assertEquals("学校不存在: 123", response.getBody().getMessage());
    }

    @Test
    void handleUnauthorizedException_shouldReturn401() {
        UnauthorizedException ex = new UnauthorizedException();
        ResponseEntity<Result<Void>> response = handler.handleUnauthorizedException(ex);

        assertEquals(401, response.getStatusCode().value());
        assertEquals(401, response.getBody().getCode());
        assertEquals("未登录或登录已过期", response.getBody().getMessage());
    }

    @Test
    void handleForbiddenException_shouldReturn403() {
        ForbiddenException ex = new ForbiddenException();
        ResponseEntity<Result<Void>> response = handler.handleForbiddenException(ex);

        assertEquals(403, response.getStatusCode().value());
        assertEquals(403, response.getBody().getCode());
        assertEquals("无访问权限", response.getBody().getMessage());
    }

    @Test
    void handleException_shouldReturn500WithGenericMessage() {
        Exception ex = new NullPointerException("unexpected null");
        ResponseEntity<Result<Void>> response = handler.handleException(ex);

        assertEquals(500, response.getStatusCode().value());
        assertEquals(500, response.getBody().getCode());
        assertEquals("系统繁忙，请稍后重试", response.getBody().getMessage());
    }

    @Test
    void handleException_shouldNotExposeInternalDetails() {
        Exception ex = new RuntimeException("SQL syntax error near...");
        ResponseEntity<Result<Void>> response = handler.handleException(ex);

        assertFalse(response.getBody().getMessage().contains("SQL"));
        assertEquals("系统繁忙，请稍后重试", response.getBody().getMessage());
    }
}
