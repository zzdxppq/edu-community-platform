<template>
  <div class="change-password-page">
    <div class="change-password-card">
      <h2 class="card-title">修改密码</h2>
      <p class="card-subtitle">首次登录请修改默认密码</p>

      <el-form
        ref="formRef"
        :model="form"
        :rules="formRules"
        label-width="0"
        size="large"
        @keyup.enter="handleSubmit"
      >
        <el-form-item prop="oldPassword">
          <el-input
            v-model="form.oldPassword"
            type="password"
            placeholder="请输入原密码"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item prop="newPassword">
          <el-input
            v-model="form.newPassword"
            type="password"
            placeholder="请输入新密码"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input
            v-model="form.confirmPassword"
            type="password"
            placeholder="请再次输入新密码"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>

        <div class="password-hint">8-20位，必须包含大写字母、小写字母和数字</div>

        <el-form-item>
          <el-button
            type="primary"
            class="submit-btn"
            :loading="loading"
            @click="handleSubmit"
          >
            确认修改
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { Lock } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { changePasswordApi } from '@/api/auth'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive<ChangePasswordForm>({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const PASSWORD_REGEX = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d).{8,20}$/

const validateNewPassword = (_rule: unknown, value: string, callback: (error?: Error) => void) => {
  if (!value) {
    callback(new Error('请输入新密码'))
  } else if (!PASSWORD_REGEX.test(value)) {
    callback(new Error('密码必须为8-20位，包含大写字母、小写字母和数字'))
  } else {
    if (form.confirmPassword) {
      formRef.value?.validateField('confirmPassword')
    }
    callback()
  }
}

const validateConfirmPassword = (_rule: unknown, value: string, callback: (error?: Error) => void) => {
  if (!value) {
    callback(new Error('请再次输入新密码'))
  } else if (value !== form.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const formRules: FormRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'change' }],
  newPassword: [{ validator: validateNewPassword, trigger: 'change' }],
  confirmPassword: [{ validator: validateConfirmPassword, trigger: 'change' }]
}

async function handleSubmit() {
  if (!formRef.value) return
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    await changePasswordApi(form)
    authStore.updateIsFirstLogin(0)
    ElMessage.success('密码修改成功')
    router.push('/dashboard')
  } catch {
    // Error already handled by request interceptor (ElMessage)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.change-password-page {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1a3d6e 0%, #2C5AA0 100%);
}

.change-password-card {
  width: 420px;
  padding: 40px;
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.card-title {
  text-align: center;
  font-size: 20px;
  color: #1a1a1a;
  margin-bottom: 8px;
}

.card-subtitle {
  text-align: center;
  font-size: 14px;
  color: #909399;
  margin-bottom: 24px;
}

.password-hint {
  font-size: 12px;
  color: #909399;
  margin-bottom: 16px;
  padding-left: 4px;
}

.submit-btn {
  width: 100%;
}
</style>
