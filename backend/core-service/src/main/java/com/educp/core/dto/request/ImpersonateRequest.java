package com.educp.core.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ImpersonateRequest {

    @NotNull(message = "学校ID不能为空")
    @Min(value = 1, message = "学校ID无效")
    private Long schoolId;
}
