package com.educp.common.security;

import com.educp.common.exception.ForbiddenException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class RoleCheckAspect {

    @Before("@within(com.educp.common.security.RequireRole) || @annotation(com.educp.common.security.RequireRole)")
    public void checkRole(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 方法级注解优先于类级别注解
        RequireRole requireRole = method.getAnnotation(RequireRole.class);
        if (requireRole == null) {
            requireRole = joinPoint.getTarget().getClass().getAnnotation(RequireRole.class);
        }

        if (requireRole == null) {
            return;
        }

        UserContext ctx = UserContext.current();
        if (ctx == null || ctx.getRole() == null) {
            log.warn("权限校验失败: UserContext 为空");
            throw new ForbiddenException("无访问权限");
        }

        int currentRole = ctx.getRole();
        for (int allowedRole : requireRole.value()) {
            if (currentRole == allowedRole) {
                return;
            }
        }

        log.warn("权限校验失败: userId={}, role={}, requiredRoles={}",
                ctx.getUserId(), currentRole, requireRole.value());
        throw new ForbiddenException("无访问权限");
    }
}
