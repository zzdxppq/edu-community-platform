package com.educp.core.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminUserListResponse {

    private Long id;
    private String username;
    private Integer role;
    private Long schoolId;
    private Integer status;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
}
