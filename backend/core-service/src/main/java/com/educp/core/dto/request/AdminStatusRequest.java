package com.educp.core.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminStatusRequest {

    @NotNull(message = "状态不能为空")
    private Integer status;
}
