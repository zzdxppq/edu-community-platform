package com.educp.core.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImpersonateLoginResponse {

    private String accessToken;
    private Long userId;
    private String username;
    private Integer role;
    private Long schoolId;
    private String schoolName;
}
