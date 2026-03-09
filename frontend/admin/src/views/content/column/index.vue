<template>
  <div class="column-page">
    <!-- Toolbar -->
    <div class="toolbar">
      <el-button type="primary" @click="handleCreate">
        <el-icon><Plus /></el-icon>
        新增栏目
      </el-button>
      <el-button :loading="sortSaving" :disabled="!sortChanged" @click="handleSaveSort">
        保存排序
      </el-button>
    </div>

    <!-- Table -->
    <el-table :data="tableData" v-loading="loading" stripe row-key="id">
      <el-table-column prop="name" label="栏目名称" min-width="140" />
      <el-table-column prop="code" label="栏目代码" min-width="120" />
      <el-table-column label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.isVisible" type="success">显示</el-tag>
          <el-tag v-else type="info">隐藏</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="二级菜单" width="110" align="center">
        <template #default="{ row }">
          <el-switch
            v-model="row.subMenuEnabled"
            :disabled="row.code === 'schools'"
            @change="(val: boolean) => handleSubMenuToggle(row, val)"
          />
        </template>
      </el-table-column>
      <el-table-column label="排序" width="130" align="center">
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
      <el-table-column label="类型" width="100" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.isSystem" type="warning" size="small">系统</el-tag>
          <el-tag v-else size="small">自定义</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="240" align="center" fixed="right">
        <template #default="{ row }">
          <el-button
            v-if="row.subMenuEnabled"
            type="success"
            link
            @click="handleManageSubColumns(row)"
          >管理二级菜单</el-button>
          <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
          <el-button
            type="danger"
            link
            :disabled="row.isSystem"
            :loading="deletingId === row.id"
            @click="handleDelete(row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Empty State -->
    <el-empty v-if="!loading && tableData.length === 0" description="暂无栏目" />

    <!-- Form Dialog -->
    <ColumnFormDialog
      v-model="dialogVisible"
      :column="editingColumn"
      @success="fetchList"
    />

    <!-- Sub Column Manage Dialog -->
    <SubColumnManage
      v-model="subColumnDialogVisible"
      :parent-column="managingColumn"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getColumnList, deleteColumn, sortColumns, updateColumn } from '@/api/column'
import type { Column } from '@/types/column'
import ColumnFormDialog from './ColumnFormDialog.vue'
import SubColumnManage from './SubColumnManage.vue'

// --- List State ---
const loading = ref(false)
const tableData = ref<Column[]>([])
const sortChanged = ref(false)
const sortSaving = ref(false)
const deletingId = ref<number | null>(null)

// --- Dialog State ---
const dialogVisible = ref(false)
const editingColumn = ref<Column | null>(null)

// --- Sub Column Dialog State ---
const subColumnDialogVisible = ref(false)
const managingColumn = ref<Column | null>(null)

// Keep a snapshot of original sort values to detect changes
let originalSortMap: Record<number, number> = {}

async function fetchList() {
  loading.value = true
  try {
    const data = await getColumnList()
    tableData.value = data
    // Snapshot sort values
    originalSortMap = {}
    data.forEach(col => { originalSortMap[col.id] = col.sortOrder })
    sortChanged.value = false
  } catch {
    // Error handled by request interceptor
  } finally {
    loading.value = false
  }
}

function onSortChange() {
  sortChanged.value = tableData.value.some(
    col => col.sortOrder !== originalSortMap[col.id]
  )
}

async function handleSaveSort() {
  const items = tableData.value.map(col => ({
    id: col.id,
    sortOrder: col.sortOrder
  }))
  sortSaving.value = true
  try {
    await sortColumns({ items })
    ElMessage.success('排序已保存')
    fetchList()
  } finally {
    sortSaving.value = false
  }
}

async function handleSubMenuToggle(row: Column, val: boolean) {
  try {
    await updateColumn(row.id, { subMenuEnabled: val })
    ElMessage.success(val ? '已启用二级菜单' : '已关闭二级菜单')
  } catch {
    // Revert on failure
    row.subMenuEnabled = !val
  }
}

function handleManageSubColumns(row: Column) {
  managingColumn.value = row
  subColumnDialogVisible.value = true
}

function handleCreate() {
  editingColumn.value = null
  dialogVisible.value = true
}

function handleEdit(row: Column) {
  editingColumn.value = { ...row }
  dialogVisible.value = true
}

async function handleDelete(row: Column) {
  if (row.isSystem) return

  try {
    await ElMessageBox.confirm(
      '确定要删除该栏目吗？删除后可通过隐藏代替。',
      '操作确认',
      { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }
    )
  } catch {
    return
  }

  deletingId.value = row.id
  try {
    await deleteColumn(row.id)
    ElMessage.success('删除成功')
    fetchList()
  } finally {
    deletingId.value = null
  }
}

// --- Init ---
onMounted(() => {
  fetchList()
})
</script>

<style scoped>
.column-page {
  padding: 20px;
}

.toolbar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}
</style>
