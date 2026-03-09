package com.educp.core.service;

import com.educp.core.dto.request.ChangePasswordRequest;
import com.educp.core.dto.request.LoginRequest;
import com.educp.core.dto.request.RefreshTokenRequest;
import com.educp.core.dto.response.ImpersonateLoginResponse;
import com.educp.core.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request, String clientIp);

    String refresh(RefreshTokenRequest request);

    void logout(String accessToken);

    void changePassword(Long userId, ChangePasswordRequest request);

    void invalidateUserTokens(Long userId);

    void clearUserDisabledFlag(Long userId);

    ImpersonateLoginResponse impersonateLogin(Long schoolId, Long operatorId, String operatorUsername, String ip);
}
