<template>
  <div class="attachment-upload">
    <!-- Upload area -->
    <el-upload
      v-if="!fileInfo && !uploadError"
      ref="uploadRef"
      class="attachment-upload__dragger"
      drag
      :accept="accept"
      :show-file-list="false"
      :before-upload="handleBeforeUpload"
      :http-request="handleUpload"
      :disabled="disabled"
    >
      <el-icon class="attachment-upload__icon"><UploadFilled /></el-icon>
      <div class="el-upload__text">
        将文件拖到此处，或<em>点击上传</em>
      </div>
      <template #tip>
        <div class="el-upload__tip">
          支持 {{ acceptLabel }} 格式，单文件不超过 {{ maxSize }}MB
        </div>
      </template>
    </el-upload>

    <!-- Upload progress -->
    <div v-if="uploading" class="attachment-upload__progress">
      <el-icon class="attachment-upload__file-icon"><Document /></el-icon>
      <span class="attachment-upload__filename">{{ uploadingFileName }}</span>
      <el-progress :percentage="uploadProgress" :stroke-width="6" />
    </div>

    <!-- File info display (after successful upload) -->
    <div v-if="fileInfo && !uploading" class="attachment-upload__file">
      <el-icon class="attachment-upload__file-icon"><Document /></el-icon>
      <div class="attachment-upload__file-info">
        <span class="attachment-upload__filename">{{ fileInfo.fileName }}</span>
        <span class="attachment-upload__filesize">{{ formatFileSize(fileInfo.fileSize) }}</span>
      </div>
      <el-button
        type="danger"
        :icon="Delete"
        circle
        size="small"
        :disabled="disabled"
        @click="handleDelete"
      />
    </div>

    <!-- Upload error with retry -->
    <div v-if="uploadError" class="attachment-upload__error">
      <el-icon class="attachment-upload__error-icon"><WarningFilled /></el-icon>
      <span class="attachment-upload__error-msg">{{ uploadError }}</span>
      <el-button type="primary" size="small" @click="handleRetry">重试</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onBeforeUnmount } from 'vue'
import { ElMessage } from 'element-plus'
import { UploadFilled, Document, Delete, WarningFilled } from '@element-plus/icons-vue'
import type { UploadRequestOptions } from 'element-plus'
import type { AttachmentInfo, FileUploadResponse } from '@/types/content'
import { DEFAULT_ACCEPT, DEFAULT_MAX_SIZE } from '@/types/content'
import service from '@/utils/request'

const props = withDefaults(defineProps<{
  modelValue?: AttachmentInfo | null
  accept?: string
  maxSize?: number
  disabled?: boolean
  uploadUrl?: string
}>(), {
  modelValue: null,
  accept: DEFAULT_ACCEPT,
  maxSize: DEFAULT_MAX_SIZE,
  disabled: false,
  uploadUrl: '/api/v1/files/upload',
})

const emit = defineEmits<{
  'update:modelValue': [value: AttachmentInfo | null]
}>()

const uploadRef = ref()
const uploading = ref(false)
const uploadProgress = ref(0)
const uploadingFileName = ref('')
const uploadError = ref('')
let abortController: AbortController | null = null
let lastFile: File | null = null

const fileInfo = computed(() => props.modelValue)

const acceptLabel = computed(() => {
  return props.accept
    .split(',')
    .map(ext => ext.trim().replace('.', '').toUpperCase())
    .join('/')
})

function formatFileSize(bytes: number): string {
  if (bytes < 1024) return bytes + ' B'
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB'
  return (bytes / (1024 * 1024)).toFixed(1) + ' MB'
}

function getFileExtension(filename: string): string {
  const dotIndex = filename.lastIndexOf('.')
  return dotIndex >= 0 ? filename.slice(dotIndex).toLowerCase() : ''
}

function handleBeforeUpload(file: File): boolean {
  const ext = getFileExtension(file.name)
  const acceptExts = props.accept.split(',').map(e => e.trim().toLowerCase())

  if (!acceptExts.includes(ext)) {
    ElMessage.error(`仅支持 ${acceptLabel.value} 格式文件`)
    return false
  }

  const maxBytes = props.maxSize * 1024 * 1024
  if (file.size > maxBytes) {
    ElMessage.error(`文件大小不能超过 ${props.maxSize}MB`)
    return false
  }

  return true
}

async function handleUpload(options: UploadRequestOptions) {
  const file = options.file
  lastFile = file
  uploading.value = true
  uploadProgress.value = 0
  uploadingFileName.value = file.name
  uploadError.value = ''

  abortController = new AbortController()
  const formData = new FormData()
  formData.append('file', file)

  try {
    const response = await service.post<FileUploadResponse>(props.uploadUrl, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
      signal: abortController.signal,
      onUploadProgress(progressEvent) {
        if (progressEvent.total) {
          uploadProgress.value = Math.round((progressEvent.loaded / progressEvent.total) * 100)
        }
      },
    })

    // response is already unwrapped by the interceptor (returns data directly)
    const data = response as unknown as FileUploadResponse
    const attachment: AttachmentInfo = {
      fileId: data.id,
      fileName: data.filename,
      fileSize: data.size,
      fileUrl: data.url,
      mimeType: data.mime_type,
    }

    emit('update:modelValue', attachment)
  } catch (error: unknown) {
    if ((error as { name?: string })?.name === 'CanceledError') return
    const isNetworkError = (error as { code?: string })?.code === 'ERR_NETWORK'
      || (error as { message?: string })?.message?.includes('Network Error')
    uploadError.value = isNetworkError
      ? '网络异常，请检查网络连接'
      : '文件上传失败，请重试'
  } finally {
    uploading.value = false
    abortController = null
  }
}

function handleDelete() {
  emit('update:modelValue', null)
}

function handleRetry() {
  uploadError.value = ''
  if (lastFile) {
    handleUpload({ file: lastFile } as UploadRequestOptions)
  }
}

onBeforeUnmount(() => {
  if (abortController) {
    abortController.abort()
  }
})

defineExpose({
  /** Exposed for parent component to call before-upload validation */
  beforeUpload: handleBeforeUpload,
})
</script>

<style scoped>
.attachment-upload__dragger {
  width: 100%;
}

.attachment-upload__dragger :deep(.el-upload-dragger) {
  width: 100%;
}

.attachment-upload__icon {
  font-size: 48px;
  color: #c0c4cc;
  margin-bottom: 8px;
}

.attachment-upload__progress {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 4px;
}

.attachment-upload__progress .el-progress {
  flex: 1;
}

.attachment-upload__file {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 4px;
}

.attachment-upload__file-icon {
  font-size: 24px;
  color: #409eff;
}

.attachment-upload__file-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.attachment-upload__filename {
  font-size: 14px;
  color: #303133;
  word-break: break-all;
}

.attachment-upload__filesize {
  font-size: 12px;
  color: #909399;
}

.attachment-upload__error {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: #fef0f0;
  border-radius: 4px;
}

.attachment-upload__error-icon {
  font-size: 24px;
  color: #f56c6c;
}

.attachment-upload__error-msg {
  flex: 1;
  font-size: 14px;
  color: #f56c6c;
}
</style>
