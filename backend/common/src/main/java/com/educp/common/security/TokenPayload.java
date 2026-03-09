package com.educp.common.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenPayload {

    private Long userId;
    private String username;
    private Integer role;
    private Long schoolId;
    private String jti;
    private String type;
    private Boolean impersonated;
    private Long originalUserId;
}
