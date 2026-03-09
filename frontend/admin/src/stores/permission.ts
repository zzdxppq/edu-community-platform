import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { RouteRecordRaw } from 'vue-router'
import routes from '@/router/routes'

export const usePermissionStore = defineStore('permission', () => {
  const accessibleRoutes = ref<RouteRecordRaw[]>([])
  const isRoutesGenerated = ref(false)

  const menuRoutes = computed(() => {
    return accessibleRoutes.value
      .filter(route => !route.meta?.hidden && route.meta?.title)
      .sort((a, b) => (a.meta?.order ?? 99) - (b.meta?.order ?? 99))
  })

  function generateRoutes(role: number) {
    const filtered = routes.filter(route => {
      // No roles meta → accessible to all authenticated users
      if (!route.meta?.roles) return true
      // Has roles meta → check if current role is included
      return route.meta.roles.includes(role)
    })
    accessibleRoutes.value = filtered
    isRoutesGenerated.value = true
  }

  function resetRoutes() {
    accessibleRoutes.value = []
    isRoutesGenerated.value = false
  }

  return {
    accessibleRoutes,
    isRoutesGenerated,
    menuRoutes,
    generateRoutes,
    resetRoutes
  }
})
