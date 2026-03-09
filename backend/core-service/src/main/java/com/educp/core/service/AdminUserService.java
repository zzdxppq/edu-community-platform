package com.educp.core.service;

import com.educp.common.result.PageResult;
import com.educp.core.dto.request.AdminUserQueryRequest;
import com.educp.core.dto.request.CreateAdminUserRequest;
import com.educp.core.dto.request.UpdateAdminUserRequest;
import com.educp.core.dto.response.AdminUserDetailResponse;
import com.educp.core.dto.response.AdminUserListResponse;

public interface AdminUserService {

    Long create(CreateAdminUserRequest request);

    AdminUserDetailResponse getById(Long id);

    PageResult<AdminUserListResponse> page(AdminUserQueryRequest request);

    void update(Long id, UpdateAdminUserRequest request);

    void delete(Long id);

    void updateStatus(Long id, Integer status, Long operatorId);

    void resetPassword(Long id);
}
