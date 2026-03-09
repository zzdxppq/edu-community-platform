<template>
  <div class="login-page">
    <div class="login-card">
      <h2 class="login-title">{{ appTitle }}</h2>

      <div class="role-selector">
        <el-radio-group v-model="selectedRole" size="large">
          <el-radio-button :value="1">超级管理员</el-radio-button>
          <el-radio-button :value="2">学校管理员</el-radio-button>
        </el-radio-group>
      </div>

      <el-form
        ref="formRef"
        :model="loginForm"
        :rules="loginRules"
        label-width="0"
        size="large"
        @keyup.enter="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入手机号"
            :prefix-icon="Phone"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            class="login-btn"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Phone, Lock } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import { loginApi } from '@/api/auth'
import { useAuthStore } from '@/stores/auth'
import { usePermissionStore } from '@/stores/permission'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()
const permissionStore = usePermissionStore()

const appTitle = import.meta.env.VITE_APP_TITLE
const formRef = ref<FormInstance>()
const loading = ref(false)
const selectedRole = ref(1)

const loginForm = reactive<LoginRequest>({
  username: '',
  password: ''
})

const loginRules: FormRules = {
  username: [{ required: true, message: '请输入手机号', trigger: 'change' }],
  password: [{ required: true, message: '请输入密码', trigger: 'change' }]
}

async function handleLogin() {
  if (!formRef.value) return
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const res = await loginApi(loginForm)
    authStore.setToken(res.accessToken)
    authStore.setRefreshToken(res.refreshToken)
    authStore.setUser({
      id: res.userId,
      username: res.username,
      role: res.role,
      schoolId: res.schoolId,
      isFirstLogin: res.isFirstLogin
    })

    permissionStore.generateRoutes(res.role)

    const redirect = (route.query.redirect as string) || '/dashboard'
    router.push(redirect)
  } catch {
    // Error already handled by request interceptor (ElMessage)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1a3d6e 0%, #2C5AA0 100%);
}

.login-card {
  width: 420px;
  padding: 40px;
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.login-title {
  text-align: center;
  font-size: 20px;
  color: #1a1a1a;
  margin-bottom: 24px;
}

.role-selector {
  display: flex;
  justify-content: center;
  margin-bottom: 24px;
}

.role-selector .el-radio-group {
  width: 100%;
  display: flex;
}

.role-selector .el-radio-button {
  flex: 1;
}

.role-selector :deep(.el-radio-button__inner) {
  width: 100%;
}

.login-btn {
  width: 100%;
}
</style>
