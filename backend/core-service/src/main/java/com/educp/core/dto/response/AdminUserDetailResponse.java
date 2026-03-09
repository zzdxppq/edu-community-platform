package com.educp.core.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminUserDetailResponse {

    private Long id;
    private String username;
    private Integer role;
    private Long schoolId;
    private String phone;
    private String email;
    private String avatar;
    private Integer isFirstLogin;
    private Integer status;
    private LocalDateTime lastLoginAt;
    private String lastLoginIp;
    private Integer loginFailCount;
    private LocalDateTime lockedUntil;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
