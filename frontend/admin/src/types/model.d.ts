interface UserInfo {
  id: number
  username: string
  role: number // 1=super_admin, 2=school_admin
  schoolId: number | null
  isFirstLogin: number // 1=true, 0=false
}

interface LoginRequest {
  username: string
  password: string
}

interface LoginResponse {
  accessToken: string
  refreshToken: string
  userId: number
  username: string
  role: number
  schoolId: number | null
  isFirstLogin: number
}

interface RefreshTokenRequest {
  refreshToken: string
}

interface ChangePasswordForm {
  oldPassword: string
  newPassword: string
  confirmPassword: string
}

interface AdminUserListItem {
  id: number
  username: string
  role: number
  schoolId: number | null
  status: number
  lastLoginAt: string | null
  createdAt: string
}

interface CreateAdminUserForm {
  username: string
  role: number
  schoolId: number | null
}

interface SchoolOption {
  id: number
  name: string
}

interface ImpersonateLoginResponse {
  accessToken: string
  userId: number
  username: string
  role: number
  schoolId: number
  schoolName: string
}
