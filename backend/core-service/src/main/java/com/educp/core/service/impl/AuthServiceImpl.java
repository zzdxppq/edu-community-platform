package com.educp.core.service.impl;

import com.educp.common.exception.BusinessException;
import com.educp.common.exception.ResourceNotFoundException;
import com.educp.common.exception.UnauthorizedException;
import com.educp.common.security.JwtProperties;
import com.educp.common.security.JwtUtils;
import com.educp.common.security.SecurityConstants;
import com.educp.common.security.TokenPayload;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.educp.core.dto.request.ChangePasswordRequest;
import com.educp.core.dto.request.LoginRequest;
import com.educp.core.dto.request.RefreshTokenRequest;
import com.educp.core.dto.response.ImpersonateLoginResponse;
import com.educp.core.dto.response.LoginResponse;
import com.educp.core.entity.AdminUser;
import com.educp.core.entity.OperationLog;
import com.educp.core.mapper.AdminUserMapper;
import com.educp.core.mapper.SchoolMapper;
import com.educp.core.service.AuthService;
import com.educp.core.service.OperationLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AdminUserMapper adminUserMapper;
    private final SchoolMapper schoolMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final JwtProperties jwtProperties;
    private final StringRedisTemplate stringRedisTemplate;
    private final OperationLogService operationLogService;

    private static final int MAX_FAIL_COUNT = 5;
    private static final int LOCK_MINUTES = 30;
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,20}$";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(LoginRequest request, String clientIp) {
        // 查询用户（含密码）
        AdminUser user = adminUserMapper.findByUsername(request.getUsername());
        if (user == null) {
            log.warn("登录失败: 账号不存在 username={}", request.getUsername());
            throw new BusinessException("账号不存在");
        }

        // 检查账号状态
        if (user.getStatus() == 0) {
            log.warn("登录失败: 账号已禁用 userId={}", user.getId());
            throw new BusinessException("该账号已被禁用，请联系管理员");
        }

        // 检查锁定状态
        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(LocalDateTime.now())) {
            long remainingMinutes = Duration.between(LocalDateTime.now(), user.getLockedUntil()).toMinutes() + 1;
            log.warn("登录失败: 账号已锁定 userId={}, lockedUntil={}", user.getId(), user.getLockedUntil());
            throw new BusinessException("账号已锁定，请" + remainingMinutes + "分钟后重试");
        }

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            handleLoginFailure(user);
            log.warn("登录失败: 密码错误 userId={}, failCount={}", user.getId(), user.getLoginFailCount());
            throw new BusinessException("密码错误");
        }

        // 登录成功 — 生成双令牌
        TokenPayload payload = TokenPayload.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .schoolId(user.getSchoolId())
                .build();

        String accessToken = jwtUtils.generateAccessToken(payload);
        String refreshToken = jwtUtils.generateRefreshToken(payload);

        // 提取 refresh token 的 jti 存入 Redis
        String refreshJti = jwtUtils.extractJti(refreshToken);
        String redisKey = SecurityConstants.REDIS_REFRESH_PREFIX + user.getId();
        stringRedisTemplate.opsForValue().set(redisKey, refreshJti, 7, TimeUnit.DAYS);

        // 更新登录信息
        user.setLastLoginAt(LocalDateTime.now());
        user.setLastLoginIp(clientIp);
        user.setLoginFailCount(0);
        user.setLockedUntil(null);
        adminUserMapper.updateById(user);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .schoolId(user.getSchoolId())
                .isFirstLogin(user.getIsFirstLogin())
                .build();
    }

    @Override
    public String refresh(RefreshTokenRequest request) {
        // 解析 refresh token
        TokenPayload payload;
        try {
            payload = jwtUtils.parseToken(request.getRefreshToken());
        } catch (Exception e) {
            log.warn("刷新令牌解析失败: {}", e.getMessage());
            throw new UnauthorizedException("无效的刷新令牌");
        }

        // 验证 type
        if (!"refresh".equals(payload.getType())) {
            throw new UnauthorizedException("无效的刷新令牌");
        }

        // 验证 Redis 一致性
        String redisKey = SecurityConstants.REDIS_REFRESH_PREFIX + payload.getUserId();
        String storedJti = stringRedisTemplate.opsForValue().get(redisKey);
        if (storedJti == null || !storedJti.equals(payload.getJti())) {
            log.warn("刷新令牌与Redis不一致 userId={}", payload.getUserId());
            throw new UnauthorizedException("登录已失效，请重新登录");
        }

        // 检查用户状态
        AdminUser user = adminUserMapper.selectById(payload.getUserId());
        if (user == null || user.getStatus() == 0) {
            log.warn("刷新令牌用户已被禁用 userId={}", payload.getUserId());
            throw new UnauthorizedException("账号已被禁用");
        }

        // 生成新的 access token
        TokenPayload newPayload = TokenPayload.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .schoolId(user.getSchoolId())
                .build();

        return jwtUtils.generateAccessToken(newPayload);
    }

    @Override
    public void logout(String accessToken) {
        // 解析 accessToken (忽略过期)
        TokenPayload payload;
        try {
            payload = jwtUtils.parseTokenIgnoreExpiry(accessToken);
        } catch (Exception e) {
            log.warn("登出解析token失败: {}", e.getMessage());
            throw new UnauthorizedException("无效的认证令牌");
        }

        // 将 access token 的 jti 加入黑名单
        String blacklistKey = SecurityConstants.REDIS_BLACKLIST_PREFIX + payload.getJti();
        long remaining = jwtUtils.getTokenRemainingSeconds(accessToken);
        if (remaining > 0) {
            stringRedisTemplate.opsForValue().set(blacklistKey, "1", remaining, TimeUnit.SECONDS);
        }

        // 删除 refresh token
        String refreshKey = SecurityConstants.REDIS_REFRESH_PREFIX + payload.getUserId();
        stringRedisTemplate.delete(refreshKey);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(Long userId, ChangePasswordRequest request) {
        // 校验确认密码
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("两次输入的密码不一致");
        }

        // 查询用户（含密码）
        AdminUser user = adminUserMapper.findByIdWithPassword(userId);
        if (user == null) {
            throw new ResourceNotFoundException("用户不存在");
        }

        // 验证原密码
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BusinessException("原密码错误");
        }

        // 检查新密码与原密码不同
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new BusinessException("新密码不能与原密码相同");
        }

        // 密码策略校验
        if (!request.getNewPassword().matches(PASSWORD_PATTERN)) {
            throw new BusinessException("密码必须为8-20位，包含大写字母、小写字母和数字");
        }

        // 更新密码和 isFirstLogin
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setIsFirstLogin(0);
        adminUserMapper.updateById(user);

        log.info("密码修改成功 userId={}", userId);
    }

    @Override
    public void invalidateUserTokens(Long userId) {
        // 删除 refresh token，阻止 token 刷新
        String refreshKey = SecurityConstants.REDIS_REFRESH_PREFIX + userId;
        stringRedisTemplate.delete(refreshKey);

        // 设置 user_disabled 标记，Gateway 拦截所有活跃 session
        String disabledKey = SecurityConstants.REDIS_USER_DISABLED_PREFIX + userId;
        stringRedisTemplate.opsForValue().set(disabledKey, "1",
                jwtProperties.getAccessTokenExpiry(), TimeUnit.SECONDS);

        log.info("用户Token已失效 userId={}", userId);
    }

    @Override
    public void clearUserDisabledFlag(Long userId) {
        String disabledKey = SecurityConstants.REDIS_USER_DISABLED_PREFIX + userId;
        stringRedisTemplate.delete(disabledKey);

        log.info("用户禁用标记已清除 userId={}", userId);
    }

    @Override
    public ImpersonateLoginResponse impersonateLogin(Long schoolId, Long operatorId, String operatorUsername, String ip) {
        // 验证学校存在且活跃
        String schoolName = schoolMapper.findNameById(schoolId);
        if (schoolName == null) {
            throw new BusinessException("目标学校不存在或已禁用");
        }

        // 查找目标学校的第一个活跃 school_admin
        AdminUser targetAdmin = adminUserMapper.selectOne(
                new QueryWrapper<AdminUser>()
                        .eq("school_id", schoolId)
                        .eq("role", 2)
                        .eq("status", 1)
                        .isNull("deleted_at")
                        .orderByAsc("id")
                        .last("LIMIT 1")
        );
        if (targetAdmin == null) {
            throw new BusinessException("该学校暂无活跃管理员账号，无法越权登录");
        }

        // 构建越权 Token
        TokenPayload payload = TokenPayload.builder()
                .userId(targetAdmin.getId())
                .username(targetAdmin.getUsername())
                .role(targetAdmin.getRole())
                .schoolId(schoolId)
                .impersonated(true)
                .originalUserId(operatorId)
                .build();

        String accessToken = jwtUtils.generateImpersonateToken(payload);

        // 记录操作日志
        OperationLog operationLog = new OperationLog();
        operationLog.setUserId(operatorId);
        operationLog.setUsername(operatorUsername);
        operationLog.setModule("auth");
        operationLog.setAction("impersonate");
        operationLog.setTargetType("School");
        operationLog.setTargetId(schoolId);
        operationLog.setDescription("越权登录至 " + schoolName + "（目标管理员: " + targetAdmin.getUsername() + "）");
        operationLog.setIp(ip);
        operationLogService.log(operationLog);

        log.info("越权登录成功: operatorId={}, targetUserId={}, schoolId={}", operatorId, targetAdmin.getId(), schoolId);

        return ImpersonateLoginResponse.builder()
                .accessToken(accessToken)
                .userId(targetAdmin.getId())
                .username(targetAdmin.getUsername())
                .role(targetAdmin.getRole())
                .schoolId(schoolId)
                .schoolName(schoolName)
                .build();
    }

    private void handleLoginFailure(AdminUser user) {
        int failCount = (user.getLoginFailCount() == null ? 0 : user.getLoginFailCount()) + 1;
        user.setLoginFailCount(failCount);

        if (failCount >= MAX_FAIL_COUNT) {
            user.setLockedUntil(LocalDateTime.now().plusMinutes(LOCK_MINUTES));
            log.warn("账号已锁定 userId={}, failCount={}", user.getId(), failCount);
        }

        adminUserMapper.updateById(user);
    }
}
