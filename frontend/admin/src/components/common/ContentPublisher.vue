<template>
  <div class="content-publisher">
    <!-- Mode selector (hidden when single mode) -->
    <div v-if="showModeSelector" class="content-publisher__mode-selector">
      <el-radio-group
        :model-value="currentMode"
        :disabled="disabled"
        @update:model-value="handleModeChange"
      >
        <el-radio
          v-for="mode in effectiveModes"
          :key="mode"
          :value="mode"
        >
          {{ PUBLISH_MODE_LABELS[mode] }}
        </el-radio>
      </el-radio-group>
    </div>

    <!-- Rich Text Mode -->
    <div v-show="currentMode === 'richtext'" class="content-publisher__panel">
      <div
        class="content-publisher__editor-wrapper"
        :class="{ 'is-error': validationErrors.richtext }"
      >
        <RichTextEditor
          :model-value="internalData.content"
          :height="editorHeight"
          :disabled="disabled"
          @update:model-value="handleContentChange"
        />
      </div>
      <div v-if="validationErrors.richtext" class="content-publisher__error">
        {{ validationErrors.richtext }}
      </div>
    </div>

    <!-- Attachment Mode -->
    <div v-show="currentMode === 'attachment'" class="content-publisher__panel">
      <AttachmentUpload
        :model-value="currentAttachment"
        :accept="accept"
        :max-size="maxSize"
        :disabled="disabled"
        @update:model-value="handleAttachmentChange"
      />
    </div>

    <!-- Link Mode -->
    <div v-show="currentMode === 'link'" class="content-publisher__panel">
      <el-form
        label-position="top"
        :disabled="disabled"
      >
        <el-form-item
          label="链接地址"
          :error="validationErrors.externalLink"
          required
        >
          <el-input
            :model-value="internalData.externalLink"
            placeholder="请输入链接地址（http:// 或 https://）"
            maxlength="500"
            show-word-limit
            clearable
            @update:model-value="handleLinkFieldChange('externalLink', $event)"
          />
        </el-form-item>

        <el-form-item label="封面图（可选）">
          <el-input
            :model-value="internalData.coverImage"
            placeholder="输入封面图 URL 或通过上传获取"
            clearable
            @update:model-value="handleLinkFieldChange('coverImage', $event)"
          />
        </el-form-item>

        <el-form-item
          label="摘要（可选）"
          :error="validationErrors.summary"
        >
          <el-input
            type="textarea"
            :model-value="internalData.summary"
            placeholder="请输入内容摘要"
            maxlength="500"
            show-word-limit
            :rows="4"
            @update:model-value="handleLinkFieldChange('summary', $event)"
          />
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, reactive } from 'vue'
import RichTextEditor from './RichTextEditor.vue'
import AttachmentUpload from './AttachmentUpload.vue'
import type {
  PublishMode,
  ContentPublishData,
  AttachmentInfo,
} from '@/types/content'
import {
  PUBLISH_MODE_LABELS,
  DEFAULT_ACCEPT,
  DEFAULT_MAX_SIZE,
  MAX_RICHTEXT_SIZE,
} from '@/types/content'

const props = withDefaults(defineProps<{
  modelValue?: ContentPublishData | null
  modes?: PublishMode[]
  defaultMode?: PublishMode
  accept?: string
  maxSize?: number
  editorHeight?: number | string
  disabled?: boolean
}>(), {
  modelValue: null,
  modes: () => ['richtext', 'attachment', 'link'] as PublishMode[],
  defaultMode: 'richtext',
  accept: DEFAULT_ACCEPT,
  maxSize: DEFAULT_MAX_SIZE,
  editorHeight: 500,
  disabled: false,
})

const emit = defineEmits<{
  'update:modelValue': [value: ContentPublishData]
}>()

// Effective modes: fall back to all three if empty array
const effectiveModes = computed(() => {
  return props.modes.length > 0
    ? props.modes
    : (['richtext', 'attachment', 'link'] as PublishMode[])
})

const showModeSelector = computed(() => effectiveModes.value.length > 1)

// Internal data store — preserves all mode data across switches
const internalData = reactive({
  content: '',
  attachments: [] as AttachmentInfo[],
  externalLink: '',
  coverImage: '',
  summary: '',
})

// Current mode
const currentMode = ref<PublishMode>(props.defaultMode)

// Validation errors
const validationErrors = reactive<Record<string, string>>({
  richtext: '',
  externalLink: '',
  summary: '',
})

// Computed: current attachment (first item, single-file UI)
const currentAttachment = computed<AttachmentInfo | null>(() => {
  return internalData.attachments.length > 0 ? internalData.attachments[0] : null
})

// Initialize from modelValue
function syncFromModelValue(val: ContentPublishData | null | undefined) {
  if (!val) return
  if (val.publishType && effectiveModes.value.includes(val.publishType)) {
    currentMode.value = val.publishType
  }
  if (val.content !== undefined) internalData.content = val.content
  if (val.attachments !== undefined) internalData.attachments = [...val.attachments]
  if (val.externalLink !== undefined) internalData.externalLink = val.externalLink
  if (val.coverImage !== undefined) internalData.coverImage = val.coverImage ?? ''
  if (val.summary !== undefined) internalData.summary = val.summary ?? ''
}

// Watch modelValue for external changes (edit mode / async data)
watch(() => props.modelValue, (val) => {
  syncFromModelValue(val)
}, { immediate: true, deep: true })

// Emit current state
function emitUpdate() {
  const data: ContentPublishData = {
    publishType: currentMode.value,
  }

  switch (currentMode.value) {
    case 'richtext':
      data.content = internalData.content
      break
    case 'attachment':
      data.attachments = [...internalData.attachments]
      break
    case 'link':
      data.externalLink = internalData.externalLink
      if (internalData.coverImage) data.coverImage = internalData.coverImage
      if (internalData.summary) data.summary = internalData.summary
      break
  }

  emit('update:modelValue', data)
}

// Mode change handler
function handleModeChange(mode: string | number | boolean | undefined) {
  currentMode.value = mode as PublishMode
  clearErrors()
  emitUpdate()
}

// Richtext content change
function handleContentChange(value: string) {
  internalData.content = value
  validationErrors.richtext = ''
  emitUpdate()
}

// Attachment change
function handleAttachmentChange(value: AttachmentInfo | null) {
  if (value) {
    internalData.attachments = [value]
  } else {
    internalData.attachments = []
  }
  emitUpdate()
}

// Link field changes
function handleLinkFieldChange(field: 'externalLink' | 'coverImage' | 'summary', value: string) {
  internalData[field] = value ?? ''
  if (field === 'externalLink') validationErrors.externalLink = ''
  if (field === 'summary') validationErrors.summary = ''
  emitUpdate()
}

// Clear all validation errors
function clearErrors() {
  validationErrors.richtext = ''
  validationErrors.externalLink = ''
  validationErrors.summary = ''
}

// Utility: check if HTML content is effectively empty
function isHtmlEmpty(html: string): boolean {
  if (!html) return true
  // Strip tags and check for real content
  const stripped = html
    .replace(/<br\s*\/?>/gi, '')
    .replace(/<\/?(p|div|span|br|h[1-6]|ul|ol|li|blockquote|pre|table|tr|td|th|thead|tbody|em|strong|b|i|u|s|a|img|hr)[^>]*>/gi, '')
    .replace(/&nbsp;/gi, ' ')
    .trim()
  return stripped.length === 0
}

// URL protocol validation
function isValidUrl(url: string): boolean {
  return /^https?:\/\/.+/.test(url)
}

// Validate current mode
async function validate(): Promise<boolean> {
  clearErrors()

  switch (currentMode.value) {
    case 'richtext': {
      if (isHtmlEmpty(internalData.content)) {
        validationErrors.richtext = '请输入图文内容'
        return false
      }
      const contentSize = new Blob([internalData.content]).size
      if (contentSize > MAX_RICHTEXT_SIZE) {
        validationErrors.richtext = '内容超出大小限制'
        return false
      }
      return true
    }

    case 'attachment':
      // Attachments are optional per BR-3.3
      return true

    case 'link': {
      if (!internalData.externalLink) {
        validationErrors.externalLink = '请输入链接地址'
        return false
      }
      if (!isValidUrl(internalData.externalLink)) {
        validationErrors.externalLink = '请输入有效的链接地址（http/https）'
        return false
      }
      if (internalData.externalLink.length > 500) {
        validationErrors.externalLink = '链接地址不能超过 500 个字符'
        return false
      }
      if (internalData.summary && internalData.summary.length > 500) {
        validationErrors.summary = '摘要不能超过 500 字'
        return false
      }
      return true
    }

    default:
      return false
  }
}

// Reset all mode data
function reset() {
  internalData.content = ''
  internalData.attachments = []
  internalData.externalLink = ''
  internalData.coverImage = ''
  internalData.summary = ''
  currentMode.value = props.defaultMode
  clearErrors()
  emitUpdate()
}

defineExpose({
  validate,
  reset,
})
</script>

<style scoped>
.content-publisher__mode-selector {
  margin-bottom: 16px;
}

.content-publisher__panel {
  min-height: 100px;
}

.content-publisher__editor-wrapper {
  border-radius: 4px;
  transition: border-color 0.2s;
}

.content-publisher__editor-wrapper.is-error {
  border: 1px solid #f56c6c;
  border-radius: 4px;
}

.content-publisher__error {
  color: #f56c6c;
  font-size: 12px;
  margin-top: 4px;
}
</style>
