import { post } from '@/utils/request'

export function loginApi(data: LoginRequest): Promise<LoginResponse> {
  return post<LoginResponse>('/core/v1/auth/login', data)
}

export function refreshTokenApi(refreshToken: string): Promise<string> {
  return post<string>('/core/v1/auth/refresh', { refreshToken })
}

export function logoutApi(): Promise<void> {
  return post<void>('/core/v1/auth/logout')
}

export function changePasswordApi(data: ChangePasswordForm): Promise<void> {
  return post<void>('/core/v1/auth/change-password', data)
}

export function impersonateLoginApi(schoolId: number): Promise<ImpersonateLoginResponse> {
  return post<ImpersonateLoginResponse>('/core/v1/auth/impersonate', { schoolId })
}
