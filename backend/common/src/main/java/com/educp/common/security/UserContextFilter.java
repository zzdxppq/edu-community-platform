package com.educp.common.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class UserContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            String userIdHeader = httpRequest.getHeader(SecurityConstants.HEADER_USER_ID);
            if (StringUtils.hasText(userIdHeader)) {
                UserContext ctx = new UserContext();
                ctx.setUserId(Long.valueOf(userIdHeader));

                String roleHeader = httpRequest.getHeader(SecurityConstants.HEADER_USER_ROLE);
                if (StringUtils.hasText(roleHeader)) {
                    ctx.setRole(Integer.valueOf(roleHeader));
                }

                String schoolIdHeader = httpRequest.getHeader(SecurityConstants.HEADER_USER_SCHOOL_ID);
                if (StringUtils.hasText(schoolIdHeader)) {
                    ctx.setSchoolId(Long.valueOf(schoolIdHeader));
                }

                String usernameHeader = httpRequest.getHeader(SecurityConstants.HEADER_USER_NAME);
                if (StringUtils.hasText(usernameHeader)) {
                    ctx.setUsername(usernameHeader);
                }

                UserContext.set(ctx);
            }

            chain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }
}
