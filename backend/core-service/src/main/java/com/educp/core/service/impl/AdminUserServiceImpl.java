package com.educp.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.educp.common.exception.BusinessException;
import com.educp.common.exception.ResourceNotFoundException;
import com.educp.common.result.PageResult;
import com.educp.core.converter.AdminUserConverter;
import com.educp.core.dto.request.AdminUserQueryRequest;
import com.educp.core.dto.request.CreateAdminUserRequest;
import com.educp.core.dto.request.UpdateAdminUserRequest;
import com.educp.core.dto.response.AdminUserDetailResponse;
import com.educp.core.dto.response.AdminUserListResponse;
import com.educp.core.entity.AdminUser;
import com.educp.core.enums.AdminRoleEnum;
import com.educp.core.mapper.AdminUserMapper;
import com.educp.core.service.AdminUserService;
import com.educp.core.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserMapper adminUserMapper;
    private final AdminUserConverter adminUserConverter;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(CreateAdminUserRequest request) {
        // 手机号唯一性校验
        if (adminUserMapper.existsByUsername(request.getUsername())) {
            throw new BusinessException("该手机号已被注册");
        }

        // 学校管理员 schoolId 校验
        if (AdminRoleEnum.SCHOOL_ADMIN.getValue() == request.getRole()) {
            if (request.getSchoolId() == null) {
                throw new BusinessException("学校管理员必须指定学校");
            }
            if (!adminUserMapper.existsSchoolById(request.getSchoolId())) {
                throw new BusinessException("指定的学校不存在");
            }
        }

        AdminUser entity = adminUserConverter.toEntity(request);

        // 默认密码: 手机号后6位
        String defaultPassword = request.getUsername().substring(request.getUsername().length() - 6);
        entity.setPassword(passwordEncoder.encode(defaultPassword));

        // salt: UUID 32位（BCrypt 内部已含盐，此字段为 DDL 兼容保留）
        entity.setSalt(UUID.randomUUID().toString().replace("-", ""));

        // 默认值
        entity.setIsFirstLogin(1);
        entity.setStatus(1);
        entity.setLoginFailCount(0);

        adminUserMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public AdminUserDetailResponse getById(Long id) {
        AdminUser entity = adminUserMapper.selectById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("管理员", id);
        }
        return adminUserConverter.toDetailResponse(entity);
    }

    @Override
    public PageResult<AdminUserListResponse> page(AdminUserQueryRequest request) {
        Page<AdminUser> page = request.toPage();

        LambdaQueryWrapper<AdminUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(request.getKeyword()), AdminUser::getUsername, request.getKeyword())
               .eq(request.getRole() != null, AdminUser::getRole, request.getRole())
               .eq(request.getStatus() != null, AdminUser::getStatus, request.getStatus())
               .eq(request.getSchoolId() != null, AdminUser::getSchoolId, request.getSchoolId())
               .orderByDesc(AdminUser::getCreatedAt);

        Page<AdminUser> result = adminUserMapper.selectPage(page, wrapper);

        return PageResult.of(
                adminUserConverter.toListResponse(result.getRecords()),
                result.getTotal(),
                request.getPage(),
                request.getSize()
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, UpdateAdminUserRequest request) {
        AdminUser entity = adminUserMapper.selectById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("管理员", id);
        }

        if (request.getPhone() != null) {
            entity.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            entity.setEmail(request.getEmail());
        }
        if (request.getAvatar() != null) {
            entity.setAvatar(request.getAvatar());
        }
        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }

        adminUserMapper.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AdminUser entity = adminUserMapper.selectById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("管理员", id);
        }
        adminUserMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status, Long operatorId) {
        AdminUser entity = adminUserMapper.selectById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("管理员不存在");
        }

        // 不能操作超级管理员
        if (AdminRoleEnum.SUPER_ADMIN.getValue() == entity.getRole()) {
            throw new BusinessException("不能禁用超级管理员账号");
        }

        // 不能操作自己
        if (id.equals(operatorId)) {
            throw new BusinessException("不能禁用自己的账号");
        }

        entity.setStatus(status);
        adminUserMapper.updateById(entity);

        // 禁用时失效 Token，启用时清除禁用标记
        if (status == 0) {
            authService.invalidateUserTokens(id);
        } else if (status == 1) {
            authService.clearUserDisabledFlag(id);
        }

        log.info("管理员状态更新 id={}, status={}, operatorId={}", id, status, operatorId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long id) {
        AdminUser entity = adminUserMapper.selectById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("管理员不存在");
        }

        // 不能重置超级管理员密码
        if (AdminRoleEnum.SUPER_ADMIN.getValue() == entity.getRole()) {
            throw new BusinessException("不能重置超级管理员密码");
        }

        // 默认密码 = 手机号后6位
        String defaultPassword = entity.getUsername().substring(entity.getUsername().length() - 6);
        entity.setPassword(passwordEncoder.encode(defaultPassword));
        entity.setIsFirstLogin(1);
        adminUserMapper.updateById(entity);

        log.info("管理员密码已重置 id={}", id);
    }
}
