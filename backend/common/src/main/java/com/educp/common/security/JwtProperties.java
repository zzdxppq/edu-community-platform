package com.educp.common.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret;
    private long accessTokenExpiry = 900;
    private long refreshTokenExpiry = 604800;
    private String issuer = "edu-community-platform";
}
