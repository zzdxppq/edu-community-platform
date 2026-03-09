package com.educp.core.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateAdminUserRequest {

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "联系电话必须为11位手机号格式")
    private String phone;

    @Email(message = "邮箱格式不正确")
    private String email;

    private String avatar;

    private Integer status;
}
