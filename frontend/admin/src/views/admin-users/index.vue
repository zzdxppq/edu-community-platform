<template>
  <div class="admin-users-page">
    <!-- Search Bar -->
    <div class="search-bar">
      <div class="search-bar__left">
        <el-input
          v-model="queryParams.keyword"
          placeholder="请输入手机号搜索"
          clearable
          style="width: 220px"
          @keyup.enter="handleSearch"
          @clear="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-select
          v-model="queryParams.schoolId"
          placeholder="全部学校"
          clearable
          style="width: 200px; margin-left: 12px"
          @change="handleSearch"
        >
          <el-option
            v-for="school in schoolOptions"
            :key="school.id"
            :label="school.name"
            :value="school.id"
          />
        </el-select>
        <el-button type="primary" style="margin-left: 12px" @click="handleSearch">
          <el-icon><Search /></el-icon>
          搜索
        </el-button>
        <el-button @click="handleReset">重置</el-button>
      </div>
      <div class="search-bar__right">
        <el-button type="primary" @click="dialogVisible = true">
          <el-icon><Plus /></el-icon>
          新增管理员
        </el-button>
      </div>
    </div>

    <!-- Table -->
    <el-table :data="tableData" v-loading="loading" stripe>
      <el-table-column prop="username" label="账号（手机号）" min-width="140" />
      <el-table-column label="所属学校" min-width="160">
        <template #default="{ row }">
          {{ getSchoolName(row.schoolId) }}
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.status === 1" type="success">启用</el-tag>
          <el-tag v-else type="danger">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="最后登录时间" min-width="170">
        <template #default="{ row }">
          {{ formatDateTime(row.lastLoginAt) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" align="center" fixed="right">
        <template #default="{ row }">
          <el-button
            v-if="row.status === 1"
            type="danger"
            link
            :loading="operatingId === row.id && operatingType === 'status'"
            @click="handleToggleStatus(row)"
          >禁用</el-button>
          <el-button
            v-else
            type="success"
            link
            :loading="operatingId === row.id && operatingType === 'status'"
            @click="handleToggleStatus(row)"
          >启用</el-button>
          <el-button
            type="warning"
            link
            :loading="operatingId === row.id && operatingType === 'reset'"
            @click="handleResetPassword(row)"
          >重置密码</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Empty State -->
    <el-empty v-if="!loading && tableData.length === 0" description="暂无数据" />

    <!-- Pagination -->
    <div class="pagination-wrapper" v-if="total > 0">
      <el-pagination
        v-model:current-page="queryParams.page"
        v-model:page-size="queryParams.size"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next"
        @size-change="fetchList"
        @current-change="fetchList"
      />
    </div>

    <!-- Create Dialog -->
    <el-dialog
      v-model="dialogVisible"
      title="新增管理员"
      width="480px"
      @close="resetForm"
    >
      <el-form
        ref="createFormRef"
        :model="createForm"
        :rules="createRules"
        label-width="90px"
      >
        <el-form-item label="账号" prop="username">
          <el-input v-model="createForm.username" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="关联学校" prop="schoolId">
          <el-select
            v-model="createForm.schoolId"
            placeholder="请选择关联学校"
            style="width: 100%"
          >
            <el-option
              v-for="school in schoolOptions"
              :key="school.id"
              :label="school.name"
              :value="school.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="creating" @click="handleCreate">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import { Search, Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { getAdminUserList, createAdminUser, updateAdminStatus, resetAdminPassword } from '@/api/admin-user'
import { getSchoolOptions } from '@/api/school'

// --- List State ---
const loading = ref(false)
const tableData = ref<AdminUserListItem[]>([])
const total = ref(0)
const schoolOptions = ref<SchoolOption[]>([])

const queryParams = reactive({
  page: 1,
  size: 10,
  keyword: '',
  schoolId: undefined as number | undefined
})

function getSchoolName(schoolId: number | null): string {
  if (!schoolId) return '—'
  const school = schoolOptions.value.find(s => s.id === schoolId)
  return school ? school.name : '—'
}

function formatDateTime(dateStr: string | null): string {
  if (!dateStr) return '—'
  const d = new Date(dateStr)
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const h = String(d.getHours()).padStart(2, '0')
  const min = String(d.getMinutes()).padStart(2, '0')
  return `${y}-${m}-${day} ${h}:${min}`
}

async function fetchList() {
  loading.value = true
  try {
    const res = await getAdminUserList({
      page: queryParams.page,
      size: queryParams.size,
      role: 2,
      keyword: queryParams.keyword || undefined,
      schoolId: queryParams.schoolId
    })
    tableData.value = res.list
    total.value = res.total
  } catch {
    // Error handled by request interceptor
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  queryParams.page = 1
  fetchList()
}

function handleReset() {
  queryParams.keyword = ''
  queryParams.schoolId = undefined
  queryParams.page = 1
  fetchList()
}

async function fetchSchoolOptions() {
  try {
    schoolOptions.value = await getSchoolOptions()
  } catch {
    ElMessage.error('学校列表加载失败')
  }
}

// --- Create Dialog State ---
const dialogVisible = ref(false)
const creating = ref(false)
const createFormRef = ref<FormInstance>()

const createForm = reactive<CreateAdminUserForm>({
  username: '',
  role: 2,
  schoolId: null
})

const createRules: FormRules = {
  username: [
    { required: true, message: '请输入手机号', trigger: 'change' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'change' }
  ],
  schoolId: [
    { required: true, message: '请选择关联学校', trigger: 'change' }
  ]
}

function resetForm() {
  createForm.username = ''
  createForm.schoolId = null
  createFormRef.value?.resetFields()
}

async function handleCreate() {
  if (!createFormRef.value) return
  const valid = await createFormRef.value.validate().catch(() => false)
  if (!valid) return

  creating.value = true
  try {
    await createAdminUser(createForm)
    ElMessage.success('创建成功，默认密码为手机号后6位')
    dialogVisible.value = false
    fetchList()
  } catch {
    // Error handled by request interceptor
  } finally {
    creating.value = false
  }
}

// --- Account Control ---
const operatingId = ref<number | null>(null)
const operatingType = ref<'status' | 'reset' | null>(null)

async function handleToggleStatus(row: AdminUserListItem) {
  const isDisabling = row.status === 1
  const newStatus = isDisabling ? 0 : 1
  const actionText = isDisabling ? '禁用' : '启用'
  const confirmMsg = isDisabling
    ? '确定要禁用该管理员账号吗？禁用后该账号将无法登录。'
    : '确定要启用该管理员账号吗？'

  try {
    await ElMessageBox.confirm(confirmMsg, '操作确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
  } catch {
    return
  }

  operatingId.value = row.id
  operatingType.value = 'status'
  try {
    await updateAdminStatus(row.id, newStatus)
    ElMessage.success(`已${actionText}`)
    fetchList()
  } catch {
    // Error handled by request interceptor
  } finally {
    operatingId.value = null
    operatingType.value = null
  }
}

async function handleResetPassword(row: AdminUserListItem) {
  try {
    await ElMessageBox.confirm(
      '确定要重置该管理员密码吗？重置后密码将恢复为手机号后6位。',
      '操作确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
  } catch {
    return
  }

  operatingId.value = row.id
  operatingType.value = 'reset'
  try {
    await resetAdminPassword(row.id)
    ElMessage.success('密码已重置为手机号后6位')
  } catch {
    // Error handled by request interceptor
  } finally {
    operatingId.value = null
    operatingType.value = null
  }
}

// --- Init ---
onMounted(() => {
  fetchSchoolOptions()
  fetchList()
})
</script>

<style scoped>
.admin-users-page {
  padding: 20px;
}

.search-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.search-bar__left {
  display: flex;
  align-items: center;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
