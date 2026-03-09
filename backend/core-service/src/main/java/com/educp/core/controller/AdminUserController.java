package com.educp.core.controller;

import com.educp.common.result.PageResult;
import com.educp.common.result.Result;
import com.educp.core.dto.request.AdminUserQueryRequest;
import com.educp.core.dto.request.AdminStatusRequest;
import com.educp.core.dto.request.CreateAdminUserRequest;
import com.educp.core.dto.request.UpdateAdminUserRequest;
import com.educp.core.dto.response.AdminUserDetailResponse;
import com.educp.core.dto.response.AdminUserListResponse;
import com.educp.common.security.RequireRole;
import com.educp.core.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理员管理")
@RestController
@RequestMapping("/v1/admin-users")
@RequiredArgsConstructor
@RequireRole(1)
public class AdminUserController {

    private final AdminUserService adminUserService;

    @Operation(summary = "新增管理员")
    @PostMapping
    public Result<Long> create(@Valid @RequestBody CreateAdminUserRequest request) {
        Long id = adminUserService.create(request);
        return Result.success(id);
    }

    @Operation(summary = "查询管理员详情")
    @GetMapping("/{id}")
    public Result<AdminUserDetailResponse> getById(@PathVariable Long id) {
        AdminUserDetailResponse detail = adminUserService.getById(id);
        return Result.success(detail);
    }

    @Operation(summary = "分页查询管理员列表")
    @GetMapping
    public Result<PageResult<AdminUserListResponse>> page(@Valid AdminUserQueryRequest request) {
        PageResult<AdminUserListResponse> page = adminUserService.page(request);
        return Result.success(page);
    }

    @Operation(summary = "更新管理员信息")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody UpdateAdminUserRequest request) {
        adminUserService.update(id, request);
        return Result.success();
    }

    @Operation(summary = "禁用/启用管理员账号")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody AdminStatusRequest request,
            @RequestHeader("X-User-Id") Long operatorId) {
        adminUserService.updateStatus(id, request.getStatus(), operatorId);
        return Result.success();
    }

    @Operation(summary = "重置管理员密码")
    @PostMapping("/{id}/reset-password")
    public Result<Void> resetPassword(@PathVariable Long id) {
        adminUserService.resetPassword(id);
        return Result.success();
    }

    @Operation(summary = "删除管理员")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        adminUserService.delete(id);
        return Result.success();
    }
}
