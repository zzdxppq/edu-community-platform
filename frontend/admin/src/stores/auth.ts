import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import {
  getToken,
  setToken as saveToken,
  removeToken,
  getRefreshToken,
  setRefreshToken as saveRefreshToken,
  removeRefreshToken,
  getUserInfo,
  setUserInfo as saveUserInfo,
  removeUserInfo
} from '@/utils/auth'

const ORIGINAL_SESSION_KEY = 'originalSession'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(getToken())
  const refreshToken = ref<string | null>(getRefreshToken())
  const user = ref<UserInfo | null>(getUserInfo())
  const isImpersonated = ref(!!localStorage.getItem(ORIGINAL_SESSION_KEY))
  const impersonatedSchoolName = ref<string | null>(null)

  // 页面刷新恢复越权学校名称
  if (isImpersonated.value) {
    try {
      const stored = JSON.parse(localStorage.getItem(ORIGINAL_SESSION_KEY) || '{}')
      impersonatedSchoolName.value = stored.schoolName || null
    } catch {
      // ignore
    }
  }

  const isLoggedIn = computed(() => !!token.value)
  const isSuperAdmin = computed(() => user.value?.role === 1)
  const isSchoolAdmin = computed(() => user.value?.role === 2)

  function setToken(newToken: string) {
    token.value = newToken
    saveToken(newToken)
  }

  function setRefreshTokenValue(newToken: string) {
    refreshToken.value = newToken
    saveRefreshToken(newToken)
  }

  function setUser(userInfo: UserInfo) {
    user.value = userInfo
    saveUserInfo(userInfo)
  }

  function updateIsFirstLogin(value: number) {
    if (user.value) {
      user.value = { ...user.value, isFirstLogin: value }
      saveUserInfo(user.value)
    }
  }

  function startImpersonation(response: ImpersonateLoginResponse) {
    // 保存原始 session
    const originalSession = {
      token: token.value,
      refreshToken: refreshToken.value,
      user: user.value,
      schoolName: response.schoolName
    }
    localStorage.setItem(ORIGINAL_SESSION_KEY, JSON.stringify(originalSession))

    // 设置越权 token + user
    token.value = response.accessToken
    saveToken(response.accessToken)
    refreshToken.value = null
    removeRefreshToken()

    const impersonatedUser: UserInfo = {
      id: response.userId,
      username: response.username,
      role: response.role,
      schoolId: response.schoolId,
      isFirstLogin: 0
    }
    user.value = impersonatedUser
    saveUserInfo(impersonatedUser)

    isImpersonated.value = true
    impersonatedSchoolName.value = response.schoolName
  }

  function exitImpersonation() {
    const stored = localStorage.getItem(ORIGINAL_SESSION_KEY)
    if (!stored) return

    try {
      const originalSession = JSON.parse(stored)

      // 恢复原始 token
      token.value = originalSession.token
      if (originalSession.token) saveToken(originalSession.token)

      refreshToken.value = originalSession.refreshToken
      if (originalSession.refreshToken) saveRefreshToken(originalSession.refreshToken)

      user.value = originalSession.user
      if (originalSession.user) saveUserInfo(originalSession.user)
    } catch {
      // fallback: logout
      token.value = null
      refreshToken.value = null
      user.value = null
      removeToken()
      removeRefreshToken()
      removeUserInfo()
    }

    isImpersonated.value = false
    impersonatedSchoolName.value = null
    localStorage.removeItem(ORIGINAL_SESSION_KEY)
  }

  function logout() {
    token.value = null
    refreshToken.value = null
    user.value = null
    isImpersonated.value = false
    impersonatedSchoolName.value = null
    removeToken()
    removeRefreshToken()
    removeUserInfo()
    localStorage.removeItem(ORIGINAL_SESSION_KEY)
  }

  return {
    token,
    refreshToken,
    user,
    isLoggedIn,
    isSuperAdmin,
    isSchoolAdmin,
    isImpersonated,
    impersonatedSchoolName,
    setToken,
    setRefreshToken: setRefreshTokenValue,
    setUser,
    updateIsFirstLogin,
    startImpersonation,
    exitImpersonation,
    logout
  }
})
