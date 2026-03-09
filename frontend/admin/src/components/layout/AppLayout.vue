<template>
  <div class="app-layout">
    <AppSidebar />
    <div
      class="app-layout__main"
      :class="{ 'app-layout__main--collapsed': appStore.sidebarCollapsed }"
    >
      <div v-if="authStore.isImpersonated" class="impersonation-banner">
        <span>🔑 越权登录中 — {{ authStore.impersonatedSchoolName }}</span>
        <el-button size="small" type="warning" plain @click="handleExitImpersonation">
          退出越权登录
        </el-button>
      </div>
      <AppHeader />
      <div class="app-layout__content">
        <slot />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import AppSidebar from './AppSidebar.vue'
import AppHeader from './AppHeader.vue'
import { useAppStore } from '@/stores/app'
import { useAuthStore } from '@/stores/auth'
import { usePermissionStore } from '@/stores/permission'

const router = useRouter()
const appStore = useAppStore()
const authStore = useAuthStore()
const permissionStore = usePermissionStore()

function handleExitImpersonation() {
  authStore.exitImpersonation()
  permissionStore.generateRoutes(1)
  router.push('/schools')
  ElMessage.success('已退出越权登录')
}
</script>

<style scoped>
.app-layout {
  display: flex;
  height: 100%;
}

.app-layout__main {
  flex: 1;
  display: flex;
  flex-direction: column;
  margin-left: 220px;
  transition: margin-left 0.3s;
}

.app-layout__main--collapsed {
  margin-left: 64px;
}

.impersonation-banner {
  height: 40px;
  background-color: #E6A23C;
  color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  font-size: 14px;
  font-weight: 500;
  flex-shrink: 0;
}

.app-layout__content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}
</style>
