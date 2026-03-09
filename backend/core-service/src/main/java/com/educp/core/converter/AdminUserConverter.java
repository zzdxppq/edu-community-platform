package com.educp.core.converter;

import com.educp.core.dto.request.CreateAdminUserRequest;
import com.educp.core.dto.response.AdminUserDetailResponse;
import com.educp.core.dto.response.AdminUserListResponse;
import com.educp.core.entity.AdminUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminUserConverter {

    AdminUserConverter INSTANCE = Mappers.getMapper(AdminUserConverter.class);

    AdminUser toEntity(CreateAdminUserRequest request);

    AdminUserDetailResponse toDetailResponse(AdminUser entity);

    AdminUserListResponse toListResponse(AdminUser entity);

    List<AdminUserListResponse> toListResponse(List<AdminUser> entities);
}
