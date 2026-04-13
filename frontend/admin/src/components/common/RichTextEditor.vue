<script lang="ts">
/**
 * RichTextEditor - Story 4.3
 * WangEditor 5.x integration for admin CMS content editing.
 */

// Named config exports for testing and reuse
export const TOOLBAR_KEYS: (string | Record<string, unknown>)[] = [
  'headerSelect',
  '|',
  'bold', 'italic', 'underline', 'through',
  '|',
  'fontFamily', 'fontSize',
  'color', 'bgColor',
  '|',
  'lineHeight',
  '|',
  'justifyLeft', 'justifyCenter', 'justifyRight', 'justifyFull',
  'indent', 'delIndent',
  '|',
  'bulletedList', 'numberedList',
  'insertTable',
  'divider',
  '|',
  'insertLink',
  'uploadImage',
  'uploadVideo',
  '|',
  'undo', 'redo',
  '|',
  'fullScreen',
]

export const UPLOAD_IMAGE_SERVER = '/api/v1/files/upload/image'
export const UPLOAD_IMAGE_MAX_SIZE = 5 * 1024 * 1024
export const UPLOAD_IMAGE_ALLOWED_TYPES = ['image/jpeg', 'image/png', 'image/gif']

export const LINE_HEIGHT_LIST = ['1', '1.5', '2', '2.5', '3']

export const FONT_FAMILY_LIST = [
  { name: '微软雅黑', value: '微软雅黑' },
  { name: '宋体', value: '宋体' },
  { name: '黑体', value: '黑体' },
  { name: '楷体', value: '楷体' },
  { name: 'Arial', value: 'Arial' },
  { name: 'Times New Roman', value: 'Times New Roman' },
]

export const FONT_SIZE_LIST = [
  '12px', '13px', '14px', '15px', '16px', '18px',
  '20px', '24px', '28px', '32px', '36px', '40px', '48px',
]

export const COLOR_PRESETS = [
  '#000000', '#262626', '#595959', '#8c8c8c', '#bfbfbf',
  '#d9d9d9', '#e8e8e8', '#f5f5f5', '#fafafa', '#ffffff',
  '#f5222d', '#fa541c', '#fa8c16', '#fadb14', '#52c41a',
  '#13c2c2', '#1890ff', '#2f54eb', '#722ed1', '#eb2f96',
  '#fbe9e7', '#fff3e0', '#fffde7', '#e8f5e9', '#e0f7fa',
]

export const DEFAULT_FONT_FAMILY = '微软雅黑'
export const DEFAULT_FONT_SIZE = '14px'
export const DEFAULT_LINE_HEIGHT = '1.5'
export const DEFAULT_HEIGHT = 500
export const DEFAULT_PLACEHOLDER = '请输入内容...'
</script>

<script setup lang="ts">
import { ref, computed, shallowRef, onBeforeUnmount, watch } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import type { IDomEditor, IEditorConfig, IToolbarConfig } from '@wangeditor/editor'
import '@wangeditor/editor/dist/css/style.css'
import { getToken } from '@/utils/auth'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const props = withDefaults(defineProps<{
  modelValue?: string
  height?: number | string
  readOnly?: boolean
  disabled?: boolean
  placeholder?: string
  maxLength?: number
}>(), {
  modelValue: '',
  height: DEFAULT_HEIGHT,
  readOnly: false,
  disabled: false,
  placeholder: DEFAULT_PLACEHOLDER,
})

const emit = defineEmits<{
  'update:modelValue': [value: string]
  'change': [value: string]
}>()

const router = useRouter()
const editorRef = shallowRef<IDomEditor | null>(null)
const isSourceMode = ref(false)
const isFullscreen = ref(false)
const sourceHtml = ref('')

const normalizedHeight = computed(() => {
  const h = props.height
  if (typeof h === 'number') {
    return h > 0 ? `${h}px` : `${DEFAULT_HEIGHT}px`
  }
  return h || `${DEFAULT_HEIGHT}px`
})

const toolbarConfig: Partial<IToolbarConfig> = {
  toolbarKeys: TOOLBAR_KEYS,
}

const editorConfig: Partial<IEditorConfig> = {
  placeholder: props.placeholder,
  readOnly: props.readOnly || props.disabled,
  maxLength: props.maxLength,
  MENU_CONF: {
    fontFamily: {
      fontFamilyList: FONT_FAMILY_LIST,
    },
    fontSize: {
      fontSizeList: FONT_SIZE_LIST,
    },
    lineHeight: {
      lineHeightList: LINE_HEIGHT_LIST,
    },
    color: {
      colors: COLOR_PRESETS,
    },
    bgColor: {
      colors: COLOR_PRESETS,
    },
    uploadImage: {
      server: UPLOAD_IMAGE_SERVER,
      fieldName: 'file',
      maxFileSize: UPLOAD_IMAGE_MAX_SIZE,
      allowedFileTypes: UPLOAD_IMAGE_ALLOWED_TYPES,
      headers: {
        Authorization: `Bearer ${getToken() || ''}`,
      },
      customInsert(res: any, insertFn: (url: string, alt: string, href: string) => void) {
        const url = res?.data?.url || res?.url
        if (!url) {
          ElMessage.error('上传失败：响应中缺少图片地址')
          return
        }
        insertFn(url, '', '')
      },
      onError(file: File, err: Error, res: any) {
        if (err?.message?.includes('401')) {
          ElMessage.error('登录已过期，请重新登录')
          router.push('/login')
          return
        }
        ElMessage.error('上传服务暂时不可用，请稍后重试')
      },
      onFailed(file: File, res: any) {
        ElMessage.error('图片上传失败')
      },
      timeout: 30000,
    },
    uploadVideo: {
      server: '/api/v1/files/upload/video',
      fieldName: 'file',
      maxFileSize: 100 * 1024 * 1024,
      headers: {
        Authorization: `Bearer ${getToken() || ''}`,
      },
    },
  },
}

function handleCreated(editor: IDomEditor) {
  editorRef.value = editor
  if (props.disabled) {
    editor.disable()
  }
}

function handleChange(editor: IDomEditor) {
  const html = editor.getHtml()
  emit('update:modelValue', html)
  emit('change', html)
}

watch(() => props.disabled, (val) => {
  const editor = editorRef.value
  if (!editor) return
  val ? editor.disable() : (!props.readOnly && editor.enable())
})

watch(() => props.readOnly, (val) => {
  const editor = editorRef.value
  if (!editor) return
  ;(val || props.disabled) ? editor.disable() : editor.enable()
})

function toggleSourceMode() {
  if (isSourceMode.value) {
    emit('update:modelValue', sourceHtml.value)
    isSourceMode.value = false
  } else {
    sourceHtml.value = props.modelValue || ''
    isSourceMode.value = true
  }
}

function handleSourceInput(e: Event) {
  sourceHtml.value = (e.target as HTMLTextAreaElement).value
}

function toggleFullscreen() {
  isFullscreen.value = !isFullscreen.value
}

onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor) {
    editor.destroy()
  }
})

defineExpose({
  getEditorInstance: () => editorRef.value,
  toolbarConfig,
  editorConfig,
  isSourceMode,
  isFullscreen,
  sourceHtml,
  toggleSourceMode,
  toggleFullscreen,
})
</script>

<template>
  <div
    class="rich-text-editor"
    :class="{
      'is-disabled': disabled,
      'is-readonly': readOnly,
      'is-fullscreen': isFullscreen,
    }"
  >
    <div class="rich-text-editor__toolbar-wrapper">
      <Toolbar
        v-if="editorRef"
        :editor="editorRef"
        :defaultConfig="toolbarConfig"
        mode="default"
        class="rich-text-editor__toolbar"
      />
      <div class="rich-text-editor__extra-buttons">
        <button
          type="button"
          class="rich-text-editor__btn"
          :disabled="disabled"
          @click="toggleSourceMode"
        >
          {{ isSourceMode ? '富文本' : '源码' }}
        </button>
        <button
          type="button"
          class="rich-text-editor__btn"
          @click="toggleFullscreen"
        >
          {{ isFullscreen ? '退出全屏' : '全屏' }}
        </button>
      </div>
    </div>

    <Editor
      v-show="!isSourceMode"
      :modelValue="modelValue"
      :defaultConfig="editorConfig"
      mode="default"
      :style="{ height: normalizedHeight, overflowY: 'auto' }"
      class="rich-text-editor__editor"
      @onCreated="handleCreated"
      @onChange="handleChange"
    />

    <textarea
      v-if="isSourceMode"
      :value="sourceHtml"
      class="rich-text-editor__source"
      :style="{ height: normalizedHeight }"
      @input="handleSourceInput"
    />
  </div>
</template>

<style scoped>
.rich-text-editor {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}

.rich-text-editor.is-fullscreen {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 9999;
  background: #fff;
  border-radius: 0;
}

.rich-text-editor__toolbar-wrapper {
  display: flex;
  align-items: center;
  border-bottom: 1px solid #dcdfe6;
}

.rich-text-editor__toolbar {
  flex: 1;
}

.rich-text-editor__extra-buttons {
  display: flex;
  gap: 4px;
  padding: 0 8px;
  flex-shrink: 0;
}

.rich-text-editor__btn {
  padding: 4px 8px;
  border: 1px solid #dcdfe6;
  border-radius: 3px;
  background: #fff;
  cursor: pointer;
  font-size: 12px;
  white-space: nowrap;
}

.rich-text-editor__btn:hover {
  background: #f5f7fa;
}

.rich-text-editor__btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.rich-text-editor__editor {
  font-family: '微软雅黑', 'Microsoft YaHei', sans-serif;
  font-size: 14px;
  line-height: 1.5;
}

.rich-text-editor__source {
  width: 100%;
  border: none;
  padding: 12px;
  resize: none;
  font-family: 'Courier New', monospace;
  font-size: 13px;
  line-height: 1.6;
  box-sizing: border-box;
  outline: none;
}

.rich-text-editor.is-disabled :deep(.w-e-toolbar) {
  opacity: 0.5;
  pointer-events: none;
}
</style>
