package com.educp.core.controller;

import com.educp.common.exception.UnauthorizedException;
import com.educp.common.result.Result;
import com.educp.common.security.RequireRole;
import com.educp.common.security.SecurityConstants;
import com.educp.common.security.UserContext;
import com.educp.core.dto.request.ChangePasswordRequest;
import com.educp.core.dto.request.ImpersonateRequest;
import com.educp.core.dto.request.LoginRequest;
import com.educp.core.dto.request.RefreshTokenRequest;
import com.educp.core.dto.response.ImpersonateLoginResponse;
import com.educp.core.dto.response.LoginResponse;
import com.educp.core.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "认证管理")
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "管理员登录")
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        String clientIp = getClientIp(httpRequest);
        LoginResponse response = authService.login(request, clientIp);
        return Result.success(response);
    }

    @Operation(summary = "刷新令牌")
    @PostMapping("/refresh")
    public Result<String> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        String newAccessToken = authService.refresh(request);
        return Result.success(newAccessToken);
    }

    @Operation(summary = "登出")
    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest httpRequest) {
        String authHeader = httpRequest.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith(SecurityConstants.BEARER_PREFIX)) {
            throw new UnauthorizedException("未提供认证令牌");
        }
        String accessToken = authHeader.substring(SecurityConstants.BEARER_PREFIX.length());
        authService.logout(accessToken);
        return Result.success();
    }

    @Operation(summary = "修改密码")
    @PostMapping("/change-password")
    public Result<Void> changePassword(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(userId, request);
        return Result.success();
    }

    @Operation(summary = "越权登录")
    @RequireRole(1)
    @PostMapping("/impersonate")
    public Result<ImpersonateLoginResponse> impersonate(
            @Valid @RequestBody ImpersonateRequest request,
            HttpServletRequest httpRequest) {
        UserContext ctx = UserContext.current();
        String clientIp = getClientIp(httpRequest);
        ImpersonateLoginResponse response = authService.impersonateLogin(
                request.getSchoolId(), ctx.getUserId(), ctx.getUsername(), clientIp);
        return Result.success(response);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0].trim();
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }
}
