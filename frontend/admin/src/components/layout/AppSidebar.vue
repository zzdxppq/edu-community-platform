<template>
  <div class="app-sidebar" :class="{ 'app-sidebar--collapsed': appStore.sidebarCollapsed }">
    <div class="app-sidebar__logo">
      <span v-if="!appStore.sidebarCollapsed">管理后台</span>
      <span v-else>后台</span>
    </div>
    <el-menu
      :default-active="route.path"
      :collapse="appStore.sidebarCollapsed"
      :collapse-transition="false"
      background-color="#1a3d6e"
      text-color="#a0aec0"
      active-text-color="#ffffff"
      router
    >
      <el-menu-item
        v-for="item in permissionStore.menuRoutes"
        :key="item.path"
        :index="item.path"
      >
        <el-icon><component :is="iconMap[item.meta?.icon ?? '']" /></el-icon>
        <template #title>{{ item.meta?.title }}</template>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'
import { Odometer, User, School } from '@element-plus/icons-vue'
import { useAppStore } from '@/stores/app'
import { usePermissionStore } from '@/stores/permission'
import type { Component } from 'vue'

const route = useRoute()
const appStore = useAppStore()
const permissionStore = usePermissionStore()

const iconMap: Record<string, Component> = {
  Odometer,
  User,
  School
}
</script>

<style scoped>
.app-sidebar {
  width: 220px;
  height: 100%;
  position: fixed;
  left: 0;
  top: 0;
  background-color: #1a3d6e;
  transition: width 0.3s;
  z-index: 1001;
  overflow: hidden;
}

.app-sidebar--collapsed {
  width: 64px;
}

.app-sidebar__logo {
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ffffff;
  font-size: 18px;
  font-weight: 700;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.app-sidebar :deep(.el-menu) {
  border-right: none;
}
</style>
