<template>
  <div class="schools-page">
    <el-card shadow="never">
      <template #header>
        <span>示范校管理</span>
      </template>
      <el-table
        v-loading="loading"
        :data="schoolList"
        border
        stripe
      >
        <el-table-column prop="name" label="学校名称" min-width="200" />
        <el-table-column label="操作" width="150" align="center">
          <template #default="{ row }">
            <el-button
              type="warning"
              size="small"
              :loading="operatingId === row.id"
              @click="handleImpersonate(row)"
            >
              登录后台
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { getSchoolOptions } from '@/api/school'
import { impersonateLoginApi } from '@/api/auth'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const loading = ref(false)
const schoolList = ref<SchoolOption[]>([])
const operatingId = ref<number | null>(null)

async function fetchSchools() {
  loading.value = true
  try {
    schoolList.value = await getSchoolOptions()
  } finally {
    loading.value = false
  }
}

async function handleImpersonate(school: SchoolOption) {
  try {
    await ElMessageBox.confirm(
      `确定要以越权方式登录 ${school.name} 的管理后台吗？越权登录有效期30分钟。`,
      '越权登录确认',
      { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }
    )
  } catch {
    return
  }

  operatingId.value = school.id
  try {
    const response = await impersonateLoginApi(school.id)
    authStore.startImpersonation(response)
    ElMessage.success(`已越权登录至 ${school.name}`)
    router.push('/dashboard')
  } finally {
    operatingId.value = null
  }
}

onMounted(() => {
  fetchSchools()
})
</script>

<style scoped>
.schools-page {
  max-width: 800px;
}
</style>
