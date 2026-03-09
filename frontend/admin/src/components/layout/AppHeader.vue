<template>
  <div class="app-header">
    <div class="app-header__left">
      <el-icon class="app-header__toggle" @click="appStore.toggleSidebar">
        <Fold v-if="!appStore.sidebarCollapsed" />
        <Expand v-else />
      </el-icon>
      <AppBreadcrumb />
    </div>
    <div class="app-header__right">
      <span class="app-header__user">管理员</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Fold, Expand } from '@element-plus/icons-vue'
import { useAppStore } from '@/stores/app'
import AppBreadcrumb from './AppBreadcrumb.vue'

const appStore = useAppStore()
</script>

<style scoped>
.app-header {
  height: 80px;
  background: #ffffff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}

.app-header__left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.app-header__toggle {
  font-size: 20px;
  cursor: pointer;
  color: #333333;
}

.app-header__toggle:hover {
  color: #2C5AA0;
}

.app-header__right {
  display: flex;
  align-items: center;
}

.app-header__user {
  font-size: 14px;
  color: #333333;
}
</style>
