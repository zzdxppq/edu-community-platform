import type { Router } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import { usePermissionStore } from '@/stores/permission'

const whiteList = ['/login']
const firstLoginWhiteList = ['/login', '/change-password']

export function setupRouterGuards(router: Router) {
  router.beforeEach((to, _from, next) => {
    const authStore = useAuthStore()
    const permissionStore = usePermissionStore()

    document.title = `${to.meta.title || ''} - ${import.meta.env.VITE_APP_TITLE}`

    if (authStore.isLoggedIn) {
      // 页面刷新时恢复路由权限
      if (!permissionStore.isRoutesGenerated && authStore.user?.role) {
        permissionStore.generateRoutes(authStore.user.role)
      }

      if (to.path === '/login') {
        next({ path: '/dashboard' })
      } else if (authStore.user?.isFirstLogin === 1 && !firstLoginWhiteList.includes(to.path)) {
        next({ path: '/change-password' })
      } else if (to.meta.roles && !(to.meta.roles as number[]).includes(authStore.user?.role ?? 0)) {
        ElMessage.warning('无权限访问该页面')
        next({ path: '/dashboard' })
      } else {
        next()
      }
    } else {
      // 未登录时清除权限路由
      if (permissionStore.isRoutesGenerated) {
        permissionStore.resetRoutes()
      }

      if (whiteList.includes(to.path)) {
        next()
      } else {
        next({ path: '/login', query: { redirect: to.fullPath } })
      }
    }
  })
}
