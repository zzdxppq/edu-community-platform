package com.educp.core.service;

import com.educp.common.exception.BusinessException;
import com.educp.common.exception.ResourceNotFoundException;
import com.educp.common.exception.UnauthorizedException;
import com.educp.common.security.JwtUtils;
import com.educp.common.security.TokenPayload;
import com.educp.core.dto.request.ChangePasswordRequest;
import com.educp.core.dto.request.LoginRequest;
import com.educp.core.dto.request.RefreshTokenRequest;
import com.educp.core.dto.response.LoginResponse;
import com.educp.core.entity.AdminUser;
import com.educp.core.mapper.AdminUserMapper;
import com.educp.core.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AdminUserMapper adminUserMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private AuthServiceImpl authService;

    private AdminUser user;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        user = new AdminUser();
        user.setId(1L);
        user.setUsername("13800138000");
        user.setPassword("$2a$10$encodedPassword");
        user.setRole(1);
        user.setStatus(1);
        user.setIsFirstLogin(1);
        user.setLoginFailCount(0);
        user.setLockedUntil(null);

        loginRequest = new LoginRequest();
        loginRequest.setUsername("13800138000");
        loginRequest.setPassword("138000");
    }

    // === login() tests ===

    @Test
    void login_shouldSucceed_whenValidCredentials() {
        when(adminUserMapper.findByUsername("13800138000")).thenReturn(user);
        when(passwordEncoder.matches("138000", "$2a$10$encodedPassword")).thenReturn(true);

        TokenPayload payload = TokenPayload.builder().userId(1L).username("13800138000").role(1).build();
        when(jwtUtils.generateAccessToken(any())).thenReturn("access-token");
        when(jwtUtils.generateRefreshToken(any())).thenReturn("refresh-token");
        when(jwtUtils.extractJti("refresh-token")).thenReturn("refresh-jti");
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(adminUserMapper.updateById(any())).thenReturn(1);

        LoginResponse response = authService.login(loginRequest, "127.0.0.1");

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
        assertEquals(1L, response.getUserId());
        assertEquals("13800138000", response.getUsername());
        assertEquals(1, response.getIsFirstLogin());
        verify(valueOperations).set(eq("auth:refresh:1"), eq("refresh-jti"), eq(7L), any());
    }

    @Test
    void login_shouldThrowBusinessException_whenUsernameNotFound() {
        when(adminUserMapper.findByUsername("99999999999")).thenReturn(null);
        loginRequest.setUsername("99999999999");

        BusinessException ex = assertThrows(BusinessException.class,
                () -> authService.login(loginRequest, "127.0.0.1"));
        assertEquals("账号不存在", ex.getMessage());
    }

    @Test
    void login_shouldThrowBusinessException_whenAccountDisabled() {
        user.setStatus(0);
        when(adminUserMapper.findByUsername("13800138000")).thenReturn(user);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> authService.login(loginRequest, "127.0.0.1"));
        assertEquals("该账号已被禁用，请联系管理员", ex.getMessage());
    }

    @Test
    void login_shouldThrowBusinessException_whenAccountLocked() {
        user.setLockedUntil(LocalDateTime.now().plusMinutes(20));
        when(adminUserMapper.findByUsername("13800138000")).thenReturn(user);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> authService.login(loginRequest, "127.0.0.1"));
        assertTrue(ex.getMessage().contains("账号已锁定"));
    }

    @Test
    void login_shouldThrowBusinessException_whenPasswordWrong() {
        when(adminUserMapper.findByUsername("13800138000")).thenReturn(user);
        when(passwordEncoder.matches("138000", "$2a$10$encodedPassword")).thenReturn(false);
        when(adminUserMapper.updateById(any())).thenReturn(1);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> authService.login(loginRequest, "127.0.0.1"));
        assertEquals("密码错误", ex.getMessage());
    }

    @Test
    void login_shouldIncrementFailCount_whenPasswordWrong() {
        when(adminUserMapper.findByUsername("13800138000")).thenReturn(user);
        when(passwordEncoder.matches("138000", "$2a$10$encodedPassword")).thenReturn(false);
        when(adminUserMapper.updateById(any())).thenReturn(1);

        assertThrows(BusinessException.class, () -> authService.login(loginRequest, "127.0.0.1"));
        assertEquals(1, user.getLoginFailCount());
    }

    @Test
    void login_shouldLockAccount_whenFailCount5() {
        user.setLoginFailCount(4);
        when(adminUserMapper.findByUsername("13800138000")).thenReturn(user);
        when(passwordEncoder.matches("138000", "$2a$10$encodedPassword")).thenReturn(false);
        when(adminUserMapper.updateById(any())).thenReturn(1);

        assertThrows(BusinessException.class, () -> authService.login(loginRequest, "127.0.0.1"));
        assertEquals(5, user.getLoginFailCount());
        assertNotNull(user.getLockedUntil());
    }

    @Test
    void login_shouldAllow_whenLockExpired() {
        user.setLockedUntil(LocalDateTime.now().minusMinutes(1));
        when(adminUserMapper.findByUsername("13800138000")).thenReturn(user);
        when(passwordEncoder.matches("138000", "$2a$10$encodedPassword")).thenReturn(true);
        when(jwtUtils.generateAccessToken(any())).thenReturn("access-token");
        when(jwtUtils.generateRefreshToken(any())).thenReturn("refresh-token");
        when(jwtUtils.extractJti("refresh-token")).thenReturn("jti");
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(adminUserMapper.updateById(any())).thenReturn(1);

        LoginResponse response = authService.login(loginRequest, "127.0.0.1");
        assertNotNull(response);
    }

    // === refresh() tests ===

    @Test
    void refresh_shouldReturnNewAccessToken_whenValid() {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("valid-refresh-token");

        TokenPayload payload = TokenPayload.builder()
                .userId(1L).jti("refresh-jti").type("refresh").build();
        when(jwtUtils.parseToken("valid-refresh-token")).thenReturn(payload);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("auth:refresh:1")).thenReturn("refresh-jti");
        when(adminUserMapper.selectById(1L)).thenReturn(user);
        when(jwtUtils.generateAccessToken(any())).thenReturn("new-access-token");

        String newToken = authService.refresh(request);
        assertEquals("new-access-token", newToken);
    }

    @Test
    void refresh_shouldThrowUnauthorized_whenTokenInvalid() {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("invalid-token");
        when(jwtUtils.parseToken("invalid-token")).thenThrow(new RuntimeException("invalid"));

        assertThrows(UnauthorizedException.class, () -> authService.refresh(request));
    }

    @Test
    void refresh_shouldThrowUnauthorized_whenJtiMismatch() {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("refresh-token");

        TokenPayload payload = TokenPayload.builder()
                .userId(1L).jti("old-jti").type("refresh").build();
        when(jwtUtils.parseToken("refresh-token")).thenReturn(payload);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("auth:refresh:1")).thenReturn("new-jti");

        UnauthorizedException ex = assertThrows(UnauthorizedException.class,
                () -> authService.refresh(request));
        assertEquals("登录已失效，请重新登录", ex.getMessage());
    }

    @Test
    void refresh_shouldThrowUnauthorized_whenUserDisabled() {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("refresh-token");

        user.setStatus(0);
        TokenPayload payload = TokenPayload.builder()
                .userId(1L).jti("jti").type("refresh").build();
        when(jwtUtils.parseToken("refresh-token")).thenReturn(payload);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("auth:refresh:1")).thenReturn("jti");
        when(adminUserMapper.selectById(1L)).thenReturn(user);

        assertThrows(UnauthorizedException.class, () -> authService.refresh(request));
    }

    // === logout() tests ===

    @Test
    void logout_shouldBlacklistAndDeleteRefresh() {
        TokenPayload payload = TokenPayload.builder()
                .userId(1L).jti("access-jti").type("access").build();
        when(jwtUtils.parseTokenIgnoreExpiry("access-token")).thenReturn(payload);
        when(jwtUtils.getTokenRemainingSeconds("access-token")).thenReturn(300L);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(stringRedisTemplate.delete("auth:refresh:1")).thenReturn(true);

        assertDoesNotThrow(() -> authService.logout("access-token"));
        verify(valueOperations).set(eq("auth:blacklist:access-jti"), eq("1"), eq(300L), any());
        verify(stringRedisTemplate).delete("auth:refresh:1");
    }

    @Test
    void logout_shouldHandleExpiredToken() {
        TokenPayload payload = TokenPayload.builder()
                .userId(1L).jti("expired-jti").type("access").build();
        when(jwtUtils.parseTokenIgnoreExpiry("expired-token")).thenReturn(payload);
        when(jwtUtils.getTokenRemainingSeconds("expired-token")).thenReturn(0L);
        when(stringRedisTemplate.delete("auth:refresh:1")).thenReturn(true);

        assertDoesNotThrow(() -> authService.logout("expired-token"));
        verify(stringRedisTemplate).delete("auth:refresh:1");
    }

    // === changePassword() tests ===

    private ChangePasswordRequest buildChangePasswordRequest(String oldPwd, String newPwd, String confirmPwd) {
        ChangePasswordRequest req = new ChangePasswordRequest();
        req.setOldPassword(oldPwd);
        req.setNewPassword(newPwd);
        req.setConfirmPassword(confirmPwd);
        return req;
    }

    @Test
    void changePassword_shouldSucceed_whenValid() {
        ChangePasswordRequest request = buildChangePasswordRequest("138000", "NewPass123", "NewPass123");
        when(adminUserMapper.findByIdWithPassword(1L)).thenReturn(user);
        when(passwordEncoder.matches("138000", "$2a$10$encodedPassword")).thenReturn(true);
        when(passwordEncoder.matches("NewPass123", "$2a$10$encodedPassword")).thenReturn(false);
        when(passwordEncoder.encode("NewPass123")).thenReturn("$2a$10$newEncodedPassword");
        when(adminUserMapper.updateById(any())).thenReturn(1);

        assertDoesNotThrow(() -> authService.changePassword(1L, request));
        assertEquals("$2a$10$newEncodedPassword", user.getPassword());
        assertEquals(0, user.getIsFirstLogin());
        verify(adminUserMapper).updateById(user);
    }

    @Test
    void changePassword_shouldThrow_whenConfirmMismatch() {
        ChangePasswordRequest request = buildChangePasswordRequest("138000", "NewPass123", "DifferentPass1");

        BusinessException ex = assertThrows(BusinessException.class,
                () -> authService.changePassword(1L, request));
        assertEquals("两次输入的密码不一致", ex.getMessage());
    }

    @Test
    void changePassword_shouldThrow_whenUserNotFound() {
        ChangePasswordRequest request = buildChangePasswordRequest("138000", "NewPass123", "NewPass123");
        when(adminUserMapper.findByIdWithPassword(999L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> authService.changePassword(999L, request));
    }

    @Test
    void changePassword_shouldThrow_whenOldPasswordWrong() {
        ChangePasswordRequest request = buildChangePasswordRequest("wrongPassword", "NewPass123", "NewPass123");
        when(adminUserMapper.findByIdWithPassword(1L)).thenReturn(user);
        when(passwordEncoder.matches("wrongPassword", "$2a$10$encodedPassword")).thenReturn(false);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> authService.changePassword(1L, request));
        assertEquals("原密码错误", ex.getMessage());
    }

    @Test
    void changePassword_shouldThrow_whenNewSameAsOld() {
        ChangePasswordRequest request = buildChangePasswordRequest("138000", "138000", "138000");
        when(adminUserMapper.findByIdWithPassword(1L)).thenReturn(user);
        when(passwordEncoder.matches("138000", "$2a$10$encodedPassword")).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> authService.changePassword(1L, request));
        assertEquals("新密码不能与原密码相同", ex.getMessage());
    }

    @Test
    void changePassword_shouldThrow_whenPolicyViolation_noUppercase() {
        ChangePasswordRequest request = buildChangePasswordRequest("138000", "newpass123", "newpass123");
        when(adminUserMapper.findByIdWithPassword(1L)).thenReturn(user);
        when(passwordEncoder.matches("138000", "$2a$10$encodedPassword")).thenReturn(true);
        when(passwordEncoder.matches("newpass123", "$2a$10$encodedPassword")).thenReturn(false);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> authService.changePassword(1L, request));
        assertEquals("密码必须为8-20位，包含大写字母、小写字母和数字", ex.getMessage());
    }

    @Test
    void changePassword_shouldThrow_whenPolicyViolation_noLowercase() {
        ChangePasswordRequest request = buildChangePasswordRequest("138000", "NEWPASS123", "NEWPASS123");
        when(adminUserMapper.findByIdWithPassword(1L)).thenReturn(user);
        when(passwordEncoder.matches("138000", "$2a$10$encodedPassword")).thenReturn(true);
        when(passwordEncoder.matches("NEWPASS123", "$2a$10$encodedPassword")).thenReturn(false);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> authService.changePassword(1L, request));
        assertEquals("密码必须为8-20位，包含大写字母、小写字母和数字", ex.getMessage());
    }

    @Test
    void changePassword_shouldThrow_whenPolicyViolation_noDigit() {
        ChangePasswordRequest request = buildChangePasswordRequest("138000", "NewPasswd", "NewPasswd");
        when(adminUserMapper.findByIdWithPassword(1L)).thenReturn(user);
        when(passwordEncoder.matches("138000", "$2a$10$encodedPassword")).thenReturn(true);
        when(passwordEncoder.matches("NewPasswd", "$2a$10$encodedPassword")).thenReturn(false);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> authService.changePassword(1L, request));
        assertEquals("密码必须为8-20位，包含大写字母、小写字母和数字", ex.getMessage());
    }

    @Test
    void changePassword_shouldThrow_whenPolicyViolation_tooShort() {
        ChangePasswordRequest request = buildChangePasswordRequest("138000", "Pass1", "Pass1");
        when(adminUserMapper.findByIdWithPassword(1L)).thenReturn(user);
        when(passwordEncoder.matches("138000", "$2a$10$encodedPassword")).thenReturn(true);
        when(passwordEncoder.matches("Pass1", "$2a$10$encodedPassword")).thenReturn(false);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> authService.changePassword(1L, request));
        assertEquals("密码必须为8-20位，包含大写字母、小写字母和数字", ex.getMessage());
    }
}
