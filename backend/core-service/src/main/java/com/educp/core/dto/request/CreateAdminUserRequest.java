package com.educp.core.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateAdminUserRequest {

    @NotBlank(message = "账号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "账号必须为11位手机号格式")
    private String username;

    @NotNull(message = "角色不能为空")
    private Integer role;

    private Long schoolId;
}
