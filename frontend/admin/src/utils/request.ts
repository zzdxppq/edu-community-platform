import axios from 'axios'
import type { AxiosInstance, AxiosResponse, InternalAxiosRequestConfig } from 'axios'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import { usePermissionStore } from '@/stores/permission'
import { getRefreshToken } from '@/utils/auth'
import router from '@/router'

const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// Standalone axios instance for refresh (no interceptors — avoids infinite loop)
const refreshClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' }
})

// Token refresh state
let isRefreshing = false
let failedQueue: Array<{
  resolve: (token: string) => void
  reject: (error: unknown) => void
}> = []

function processQueue(error: unknown, token: string | null) {
  failedQueue.forEach(({ resolve, reject }) => {
    if (token) {
      resolve(token)
    } else {
      reject(error)
    }
  })
  failedQueue = []
}

service.interceptors.request.use(
  (config) => {
    const authStore = useAuthStore()

    if (authStore.token) {
      config.headers.Authorization = `Bearer ${authStore.token}`
    }

    config.headers['X-Request-Id'] = crypto.randomUUID()

    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  (response: AxiosResponse<ApiResponse>) => {
    const { code, message, data } = response.data

    if (code === 200) {
      return data as AxiosResponse<ApiResponse>
    }

    ElMessage.error(message || '请求失败')
    return Promise.reject(new Error(message))
  },
  (error) => {
    const { status, data } = error.response || {}
    const originalRequest = error.config as InternalAxiosRequestConfig & { _retry?: boolean }

    // 401 — check impersonation first, then attempt token refresh
    if (status === 401 && !originalRequest._retry) {
      const authStore401 = useAuthStore()

      // 越权登录 token 过期：跳过 refresh，恢复原始会话
      if (authStore401.isImpersonated) {
        authStore401.exitImpersonation()
        const permissionStore = usePermissionStore()
        permissionStore.generateRoutes(1)
        ElMessage.warning('越权登录已过期，已恢复原始会话')
        router.push('/schools')
        return Promise.reject(error)
      }

      const refreshTokenValue = getRefreshToken()

      if (refreshTokenValue) {
        if (isRefreshing) {
          // Queue this request until refresh completes
          return new Promise<string>((resolve, reject) => {
            failedQueue.push({ resolve, reject })
          }).then((newToken) => {
            originalRequest.headers.Authorization = `Bearer ${newToken}`
            return service(originalRequest)
          })
        }

        originalRequest._retry = true
        isRefreshing = true

        return refreshClient
          .post<ApiResponse<string>>('/core/v1/auth/refresh', {
            refreshToken: refreshTokenValue
          })
          .then((res) => {
            const newAccessToken = res.data.data
            authStore401.setToken(newAccessToken)

            processQueue(null, newAccessToken)

            originalRequest.headers.Authorization = `Bearer ${newAccessToken}`
            return service(originalRequest)
          })
          .catch((refreshError) => {
            processQueue(refreshError, null)

            authStore401.logout()
            router.push('/login')
            return Promise.reject(refreshError)
          })
          .finally(() => {
            isRefreshing = false
          })
      }

      // No refresh token — logout
      authStore401.logout()
      router.push('/login')
      return Promise.reject(error)
    }

    switch (status) {
      case 403:
        ElMessage.error('无权限访问')
        break

      case 404:
        ElMessage.error('资源不存在')
        break

      case 422:
        ElMessage.error(data?.message || '操作失败')
        break

      case 500:
        ElMessage.error('服务器异常，请稍后重试')
        break

      default:
        if (status !== 401) {
          ElMessage.error(error.message || '网络异常')
        }
    }

    return Promise.reject(error)
  }
)

export function get<T>(url: string, params?: object): Promise<T> {
  return service.get(url, { params }) as Promise<T>
}

export function post<T>(url: string, data?: object): Promise<T> {
  return service.post(url, data) as Promise<T>
}

export function put<T>(url: string, data?: object): Promise<T> {
  return service.put(url, data) as Promise<T>
}

export function del<T>(url: string, params?: object): Promise<T> {
  return service.delete(url, { params }) as Promise<T>
}

export default service
