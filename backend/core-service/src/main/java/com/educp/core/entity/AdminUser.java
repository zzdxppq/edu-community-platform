package com.educp.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.educp.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@TableName("admin_users")
@EqualsAndHashCode(callSuper = true)
public class AdminUser extends BaseEntity {

    private String username;
    private String password;
    private String salt;
    private Integer role;
    private Long schoolId;
    private String phone;
    private String email;
    private String avatar;
    private Integer isFirstLogin = 1;
    private Integer status = 1;
    private LocalDateTime lastLoginAt;
    private String lastLoginIp;
    private Integer loginFailCount = 0;
    private LocalDateTime lockedUntil;
    private Long createdBy;
}
