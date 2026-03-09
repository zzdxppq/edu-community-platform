<template>
  <el-dialog
    v-model="visible"
    :title="isEdit ? '编辑栏目' : '新增栏目'"
    width="520px"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="90px"
    >
      <el-form-item label="栏目名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入栏目名称" maxlength="50" />
      </el-form-item>
      <el-form-item label="栏目代码" prop="code">
        <el-input
          v-model="form.code"
          placeholder="小写字母开头，如 news"
          maxlength="50"
          :disabled="isSystemColumn"
        />
      </el-form-item>
      <el-form-item label="图标" prop="icon">
        <el-input v-model="form.icon" placeholder="图标名称（可选）" maxlength="100" />
      </el-form-item>
      <el-form-item label="路由路径" prop="routePath">
        <el-input v-model="form.routePath" placeholder="前端路由路径（可选）" maxlength="200" />
      </el-form-item>
      <el-form-item label="排序" prop="sortOrder">
        <el-input-number v-model="form.sortOrder" :min="0" :max="9999" />
      </el-form-item>
      <el-form-item v-if="isEdit" label="是否显示">
        <el-switch v-model="form.isVisible" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { reactive, ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { createColumn, updateColumn } from '@/api/column'
import type { Column } from '@/types/column'

const props = defineProps<{
  modelValue: boolean
  column: Column | null
}>()

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
  'success': []
}>()

const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

const isEdit = computed(() => !!props.column)
const isSystemColumn = computed(() => props.column?.isSystem === true)

const formRef = ref<FormInstance>()
const submitting = ref(false)

const form = reactive({
  name: '',
  code: '',
  icon: '',
  routePath: '',
  sortOrder: 0,
  isVisible: true
})

const rules: FormRules = {
  name: [
    { required: true, message: '栏目名称不能为空', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '栏目代码不能为空', trigger: 'blur' },
    { pattern: /^[a-z][a-z0-9_-]*$/, message: '只能包含小写字母、数字、下划线和连字符，且以小写字母开头', trigger: 'blur' }
  ]
}

watch(() => props.modelValue, (val) => {
  if (val && props.column) {
    form.name = props.column.name
    form.code = props.column.code
    form.icon = props.column.icon || ''
    form.routePath = props.column.routePath || ''
    form.sortOrder = props.column.sortOrder
    form.isVisible = props.column.isVisible
  }
})

function handleClose() {
  form.name = ''
  form.code = ''
  form.icon = ''
  form.routePath = ''
  form.sortOrder = 0
  form.isVisible = true
  formRef.value?.resetFields()
}

async function handleSubmit() {
  if (!formRef.value) return
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    if (isEdit.value && props.column) {
      const data: Record<string, unknown> = {
        name: form.name,
        icon: form.icon || undefined,
        routePath: form.routePath || undefined,
        isVisible: form.isVisible,
        sortOrder: form.sortOrder
      }
      if (!isSystemColumn.value) {
        data.code = form.code
      }
      await updateColumn(props.column.id, data)
      ElMessage.success('更新成功')
    } else {
      await createColumn({
        name: form.name,
        code: form.code,
        icon: form.icon || undefined,
        routePath: form.routePath || undefined,
        sortOrder: form.sortOrder
      })
      ElMessage.success('创建成功')
    }
    visible.value = false
    emit('success')
  } finally {
    submitting.value = false
  }
}
</script>
