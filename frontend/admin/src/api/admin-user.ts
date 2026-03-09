import { get, post, put } from '@/utils/request'

export function getAdminUserList(params: {
  page?: number
  size?: number
  role?: number
  keyword?: string
  schoolId?: number
}): Promise<PageResult<AdminUserListItem>> {
  return get<PageResult<AdminUserListItem>>('/core/v1/admin-users', params)
}

export function createAdminUser(data: CreateAdminUserForm): Promise<number> {
  return post<number>('/core/v1/admin-users', data)
}

export function updateAdminStatus(id: number, status: number): Promise<void> {
  return put<void>(`/core/v1/admin-users/${id}/status`, { status })
}

export function resetAdminPassword(id: number): Promise<void> {
  return post<void>(`/core/v1/admin-users/${id}/reset-password`)
}
