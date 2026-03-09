package com.educp.core.dto.request;

import com.educp.common.result.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminUserQueryRequest extends PageRequest {

    private String keyword;

    private Integer role;

    private Integer status;

    private Long schoolId;
}
