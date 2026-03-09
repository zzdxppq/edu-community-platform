<template>
  <el-dialog
    :model-value="modelValue"
    @update:model-value="$emit('update:modelValue', $event)"
    :title="`二级菜单管理 - ${parentColumn?.name ?? ''}`"
    width="760px"
    destroy-on-close
  >
    <!-- Toolbar -->
    <div class="toolbar">
      <el-button type="primary" size="small" @click="handleCreate">
        <el-icon><Plus /></el-icon>
        新增
      </el-button>
      <el-button
        size="small"
        :loading="sortSaving"
        :disabled="!sortChanged"
        @click="handleSaveSort"
      >
        保存排序
      </el-button>
    </div>

    <!-- Table -->
    <el-table :data="tableData" v-loading="loading" stripe size="small" row-key="id">
      <el-table-column prop="name" label="名称" min-width="120" />
      <el-table-column prop="code" label="代码" min-width="120" />
      <el-table-column prop="categoryCode" label="分类代码" min-width="100" />
      <el-table-column label="排序" width="110" align="center">
        <template #default="{ row }">
          <el-input-number
            v-model="row.sortOrder"
            :min="0"
            :max="9999"
            size="small"
            controls-position="right"
            @change="onSortChange"
          />
        </template>
      </el-table-column>
      <el-table-column label="类型" width="80" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.isSystem" type="warning" size="small">系统</el-tag>
          <el-tag v-else size="small">自定义</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="140" align="center" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button
            type="danger"
            link
            size="small"
            :disabled="row.isSystem"
            :loading="deletingId === row.id"
            @click="handleDelete(row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty v-if="!loading && tableData.length === 0" description="暂无二级菜单" />

    <!-- Sub-form Dialog -->
    <el-dialog
      v-model="formDialogVisible"
      :title="editingItem ? '编辑二级菜单' : '新增二级菜单'"
      width="480px"
      append-to-body
      destroy-on-close
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="90px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入名称" maxlength="50" />
        </el-form-item>
        <el-form-item label="代码" prop="code">
          <el-input
            v-model="formData.code"
            placeholder="如 national-policy"
            maxlength="50"
            :disabled="editingItem?.isSystem === true"
          />
        </el-form-item>
        <el-form-item label="分类代码" prop="categoryCode">
          <el-input
            v-model="formData.categoryCode"
            placeholder="关联内容分类（可选）"
            maxlength="50"
            :disabled="editingItem?.isSystem === true"
          />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="formData.sortOrder" :min="0" :max="9999" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="formDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import {
  getSubColumnList,
  createSubColumn,
  updateSubColumn,
  deleteSubColumn,
  sortSubColumns
} from '@/api/subColumn'
import type { SubColumn } from '@/types/subColumn'
import type { Column } from '@/types/column'

const props = defineProps<{
  modelValue: boolean
  parentColumn: Column | null
}>()

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
}>()

// --- List State ---
const loading = ref(false)
const tableData = ref<SubColumn[]>([])
const sortChanged = ref(false)
const sortSaving = ref(false)
const deletingId = ref<number | null>(null)

// --- Form State ---
const formDialogVisible = ref(false)
const editingItem = ref<SubColumn | null>(null)
const submitting = ref(false)
const formRef = ref<FormInstance>()
const formData = ref({
  name: '',
  code: '',
  categoryCode: '',
  sortOrder: 0
})

const formRules: FormRules = {
  name: [
    { required: true, message: '二级菜单名称不能为空', trigger: 'blur' },
    { max: 50, message: '不能超过50字符', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '二级菜单代码不能为空', trigger: 'blur' },
    { max: 50, message: '不能超过50字符', trigger: 'blur' },
    {
      pattern: /^[a-z][a-z0-9_-]*$/,
      message: '仅支持小写字母、数字、下划线和连字符，以字母开头',
      trigger: 'blur'
    }
  ],
  categoryCode: [{ max: 50, message: '不能超过50字符', trigger: 'blur' }]
}

let originalSortMap: Record<number, number> = {}

watch(
  () => props.modelValue,
  (val) => {
    if (val && props.parentColumn) {
      fetchList()
    }
  }
)

async function fetchList() {
  if (!props.parentColumn) return
  loading.value = true
  try {
    const data = await getSubColumnList(props.parentColumn.id)
    tableData.value = data
    originalSortMap = {}
    data.forEach((item) => { originalSortMap[item.id] = item.sortOrder })
    sortChanged.value = false
  } catch {
    // handled by interceptor
  } finally {
    loading.value = false
  }
}

function onSortChange() {
  sortChanged.value = tableData.value.some(
    (item) => item.sortOrder !== originalSortMap[item.id]
  )
}

async function handleSaveSort() {
  if (!props.parentColumn) return
  const items = tableData.value.map((item) => ({
    id: item.id,
    sortOrder: item.sortOrder
  }))
  sortSaving.value = true
  try {
    await sortSubColumns(props.parentColumn.id, { items })
    ElMessage.success('排序已保存')
    fetchList()
  } finally {
    sortSaving.value = false
  }
}

function handleCreate() {
  editingItem.value = null
  formData.value = { name: '', code: '', categoryCode: '', sortOrder: 0 }
  formDialogVisible.value = true
}

function handleEdit(row: SubColumn) {
  editingItem.value = row
  formData.value = {
    name: row.name,
    code: row.code,
    categoryCode: row.categoryCode ?? '',
    sortOrder: row.sortOrder
  }
  formDialogVisible.value = true
}

async function handleSubmit() {
  if (!props.parentColumn) return
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    if (editingItem.value) {
      await updateSubColumn(props.parentColumn.id, editingItem.value.id, formData.value)
      ElMessage.success('更新成功')
    } else {
      await createSubColumn(props.parentColumn.id, formData.value)
      ElMessage.success('创建成功')
    }
    formDialogVisible.value = false
    fetchList()
  } finally {
    submitting.value = false
  }
}

async function handleDelete(row: SubColumn) {
  if (row.isSystem || !props.parentColumn) return
  try {
    await ElMessageBox.confirm('确定要删除该二级菜单吗？', '操作确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
  } catch {
    return
  }

  deletingId.value = row.id
  try {
    await deleteSubColumn(props.parentColumn.id, row.id)
    ElMessage.success('删除成功')
    fetchList()
  } finally {
    deletingId.value = null
  }
}
</script>

<style scoped>
.toolbar {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}
</style>
