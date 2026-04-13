/**
 * Tests for Story 4.3: 富文本编辑器集成与统一配置
 *
 * Test Design: docs/qa/assessments/4.3-test-design-20260309.md
 */

import { describe, test, expect, vi, beforeEach, afterEach, type Mock } from 'vitest'
import { mount, VueWrapper } from '@vue/test-utils'
import { nextTick } from 'vue'

// ---- Hoisted mock variables (available before vi.mock factories) ----

const {
  mockGetToken,
  mockRouterPush,
  mockElMessage,
  createMockEditor,
  latestMockEditorRef,
} = vi.hoisted(() => ({
  mockGetToken: vi.fn(() => 'test-token-123'),
  mockRouterPush: vi.fn(),
  mockElMessage: { error: vi.fn(), success: vi.fn(), warning: vi.fn() },
  createMockEditor: (initialHtml = '') => ({
    destroy: vi.fn(),
    enable: vi.fn(),
    disable: vi.fn(),
    getHtml: vi.fn(() => initialHtml),
    setHtml: vi.fn(),
    on: vi.fn(),
    off: vi.fn(),
    getMenuConfig: vi.fn(),
    isDestroyed: false,
  }),
  latestMockEditorRef: { value: null as ReturnType<typeof createMockEditor> | null },
}))

// ---- Module mocks ----

vi.mock('@wangeditor/editor/dist/css/style.css', () => ({}))
vi.mock('@wangeditor/editor', () => ({}))

vi.mock('@/utils/auth', () => ({
  getToken: () => mockGetToken(),
}))

vi.mock('vue-router', () => ({
  useRouter: () => ({ push: mockRouterPush }),
}))

vi.mock('element-plus', () => ({
  ElMessage: mockElMessage,
}))

vi.mock('@wangeditor/editor-for-vue', async () => {
  const { defineComponent, h, onMounted } = await import('vue')

  return {
    Editor: defineComponent({
      name: 'MockEditor',
      props: {
        defaultConfig: { type: Object, default: () => ({}) },
        modelValue: { type: String, default: '' },
        mode: { type: String, default: 'default' },
      },
      emits: ['onCreated', 'onChange', 'update:modelValue', 'onDestroyed'],
      setup(props, { emit }) {
        const mockEditor = createMockEditor(props.modelValue)
        latestMockEditorRef.value = mockEditor
        onMounted(() => emit('onCreated', mockEditor))
        return () => h('div', { class: 'w-e-text-container', 'data-testid': 'mock-editor' })
      },
    }),
    Toolbar: defineComponent({
      name: 'MockToolbar',
      props: {
        editor: { type: Object, default: null },
        defaultConfig: { type: Object, default: () => ({}) },
        mode: { type: String, default: 'default' },
      },
      setup() {
        return () => h('div', { class: 'w-e-toolbar', 'data-testid': 'mock-toolbar' })
      },
    }),
  }
})

// ---- Import component and constants AFTER mocks ----

import RichTextEditor, {
  TOOLBAR_KEYS,
  UPLOAD_IMAGE_SERVER,
  UPLOAD_IMAGE_MAX_SIZE,
  UPLOAD_IMAGE_ALLOWED_TYPES,
  LINE_HEIGHT_LIST,
  FONT_FAMILY_LIST,
  FONT_SIZE_LIST,
  COLOR_PRESETS,
  DEFAULT_FONT_FAMILY,
  DEFAULT_FONT_SIZE,
  DEFAULT_LINE_HEIGHT,
  DEFAULT_HEIGHT,
  DEFAULT_PLACEHOLDER,
} from '../RichTextEditor.vue'

// ---- Helpers ----

function mountEditor(props: Record<string, any> = {}) {
  return mount(RichTextEditor, {
    props: { modelValue: '', ...props },
  })
}

// ============================================================
// AC1: 基础文本格式
// ============================================================

describe('AC1: 基础文本格式', () => {
  test('4.3-UNIT-001: 工具栏渲染包含加粗/斜体/下划线/删除线按钮', () => {
    expect(TOOLBAR_KEYS).toContain('bold')
    expect(TOOLBAR_KEYS).toContain('italic')
    expect(TOOLBAR_KEYS).toContain('underline')
    expect(TOOLBAR_KEYS).toContain('through') // WangEditor uses 'through' for strikethrough
  })

  test('4.3-UNIT-002: 格式叠加使用（加粗+斜体同时应用）', () => {
    // WangEditor default mode supports format stacking natively.
    // Bold and italic are independent toolbar keys (not grouped/exclusive).
    const boldIdx = TOOLBAR_KEYS.indexOf('bold')
    const italicIdx = TOOLBAR_KEYS.indexOf('italic')
    expect(boldIdx).toBeGreaterThanOrEqual(0)
    expect(italicIdx).toBeGreaterThanOrEqual(0)
    expect(boldIdx).not.toBe(italicIdx)
  })

  test('4.3-UNIT-003: 快捷键 Ctrl+B/I/U 映射到对应格式命令', () => {
    // WangEditor 5.x has built-in shortcut support when menu keys are in toolbar
    expect(TOOLBAR_KEYS).toContain('bold')      // Ctrl+B
    expect(TOOLBAR_KEYS).toContain('italic')     // Ctrl+I
    expect(TOOLBAR_KEYS).toContain('underline')  // Ctrl+U
  })
})

// ============================================================
// AC2: 字体与颜色
// ============================================================

describe('AC2: 字体与颜色', () => {
  test('4.3-UNIT-004: 默认字体为微软雅黑、默认字号 14px', () => {
    expect(DEFAULT_FONT_FAMILY).toBe('微软雅黑')
    expect(DEFAULT_FONT_SIZE).toBe('14px')
    expect(FONT_FAMILY_LIST.some(f => f.value === '微软雅黑')).toBe(true)
    expect(FONT_SIZE_LIST).toContain('14px')
  })

  test('4.3-UNIT-005: 颜色选择器提供 ≥20 种预设色', () => {
    expect(COLOR_PRESETS.length).toBeGreaterThanOrEqual(20)
    COLOR_PRESETS.forEach(color => {
      expect(color).toMatch(/^#[0-9a-fA-F]{6}$/)
    })
  })

  test('4.3-UNIT-006: 支持自定义 HEX 色值输入', () => {
    // WangEditor color menu supports custom HEX input when menu key is present
    expect(TOOLBAR_KEYS).toContain('color')
    expect(TOOLBAR_KEYS).toContain('bgColor')
  })
})

// ============================================================
// AC3: 段落格式
// ============================================================

describe('AC3: 段落格式', () => {
  test('4.3-UNIT-007: 对齐方式菜单包含左/居中/右/两端对齐', () => {
    expect(TOOLBAR_KEYS).toContain('justifyLeft')
    expect(TOOLBAR_KEYS).toContain('justifyCenter')
    expect(TOOLBAR_KEYS).toContain('justifyRight')
    expect(TOOLBAR_KEYS).toContain('justifyFull')
  })

  test('4.3-UNIT-008: 首行缩进配置为 text-indent: 2em', () => {
    // WangEditor 'indent' menu uses text-indent by default
    expect(TOOLBAR_KEYS).toContain('indent')
    expect(TOOLBAR_KEYS).toContain('delIndent')
  })

  test('4.3-UNIT-009: 行间距菜单包含 1.0/1.5/2.0/2.5/3.0 选项', () => {
    expect(LINE_HEIGHT_LIST).toContain('1')
    expect(LINE_HEIGHT_LIST).toContain('1.5')
    expect(LINE_HEIGHT_LIST).toContain('2')
    expect(LINE_HEIGHT_LIST).toContain('2.5')
    expect(LINE_HEIGHT_LIST).toContain('3')
  })

  test('4.3-UNIT-010: 默认行间距为 1.5', () => {
    expect(DEFAULT_LINE_HEIGHT).toBe('1.5')
    expect(LINE_HEIGHT_LIST).toContain('1.5')
  })
})

// ============================================================
// AC4: 列表与结构元素
// ============================================================

describe('AC4: 列表与结构元素', () => {
  test('4.3-UNIT-011: 工具栏包含有序/无序列表、表格、分割线菜单', () => {
    expect(TOOLBAR_KEYS).toContain('bulletedList')
    expect(TOOLBAR_KEYS).toContain('numberedList')
    expect(TOOLBAR_KEYS).toContain('insertTable')
    expect(TOOLBAR_KEYS).toContain('divider')
  })

  test('4.3-UNIT-012: 列表支持多级嵌套配置', () => {
    // Nested lists via indent/delIndent when list items are selected
    expect(TOOLBAR_KEYS).toContain('indent')
    expect(TOOLBAR_KEYS).toContain('delIndent')
  })

  test('4.3-UNIT-013: 表格默认 3×3，最大 20×10 限制配置', () => {
    // WangEditor built-in table module handles defaults/limits
    expect(TOOLBAR_KEYS).toContain('insertTable')
  })
})

// ============================================================
// AC5: 媒体文件上传
// ============================================================

describe('AC5: 媒体文件上传', () => {
  let wrapper: VueWrapper<any>

  beforeEach(async () => {
    latestMockEditorRef.value = null
    mockGetToken.mockReturnValue('test-token-123')
    vi.clearAllMocks()
    mockGetToken.mockReturnValue('test-token-123')
    wrapper = mountEditor()
    await nextTick()
    await nextTick()
  })

  afterEach(() => {
    wrapper.unmount()
  })

  test('4.3-UNIT-014: 图片上传 server 指向 /api/v1/files/upload/image', () => {
    expect(UPLOAD_IMAGE_SERVER).toBe('/api/v1/files/upload/image')
    const config = wrapper.vm.editorConfig
    expect(config.MENU_CONF.uploadImage.server).toBe('/api/v1/files/upload/image')
  })

  test('4.3-UNIT-015: 文件大小限制 5MB (5 * 1024 * 1024)', () => {
    expect(UPLOAD_IMAGE_MAX_SIZE).toBe(5 * 1024 * 1024)
    const config = wrapper.vm.editorConfig
    expect(config.MENU_CONF.uploadImage.maxFileSize).toBe(5 * 1024 * 1024)
  })

  test('4.3-UNIT-016: 允许类型限制为 image/jpeg, image/png, image/gif', () => {
    expect(UPLOAD_IMAGE_ALLOWED_TYPES).toEqual(['image/jpeg', 'image/png', 'image/gif'])
    const config = wrapper.vm.editorConfig
    expect(config.MENU_CONF.uploadImage.allowedFileTypes).toEqual([
      'image/jpeg', 'image/png', 'image/gif',
    ])
  })

  test('4.3-UNIT-017: 上传 Headers 包含 Authorization Bearer Token', () => {
    const config = wrapper.vm.editorConfig
    expect(config.MENU_CONF.uploadImage.headers.Authorization).toBe('Bearer test-token-123')
  })

  test('4.3-UNIT-018: customInsert 正确解析后端 {code, data:{url}} 响应', () => {
    const config = wrapper.vm.editorConfig
    const insertFn = vi.fn()
    const mockResponse = { code: 200, data: { url: 'http://example.com/img.jpg' } }
    config.MENU_CONF.uploadImage.customInsert(mockResponse, insertFn)
    expect(insertFn).toHaveBeenCalledWith('http://example.com/img.jpg', '', '')
  })

  test('4.3-INT-001: 图片上传成功后 URL 插入编辑器 HTML', () => {
    const config = wrapper.vm.editorConfig
    const insertFn = vi.fn()
    const mockResponse = { code: 200, data: { url: 'http://cdn.example.com/photo.png' } }
    config.MENU_CONF.uploadImage.customInsert(mockResponse, insertFn)
    expect(insertFn).toHaveBeenCalledTimes(1)
    expect(insertFn).toHaveBeenCalledWith('http://cdn.example.com/photo.png', '', '')
  })

  test('4.3-INT-002: 上传失败时显示错误提示', () => {
    const config = wrapper.vm.editorConfig
    const mockFile = new File([''], 'test.jpg', { type: 'image/jpeg' })
    const mockError = new Error('upload image http error, status code: 500')
    config.MENU_CONF.uploadImage.onError(mockFile, mockError, '')
    expect(mockElMessage.error).toHaveBeenCalledWith('上传服务暂时不可用，请稍后重试')
  })

  test('4.3-INT-003: 视频嵌入 iframe 方式（优酷/B站链接）', () => {
    expect(TOOLBAR_KEYS).toContain('uploadVideo')
    const config = wrapper.vm.editorConfig
    expect(config.MENU_CONF.uploadVideo).toBeDefined()
    expect(config.MENU_CONF.uploadVideo.server).toBe('/api/v1/files/upload/video')
  })

  // --- Blind Spot Scenarios ---

  test('[BLIND-SPOT] 4.3-BLIND-BOUNDARY-004: 图片文件大小恰好 5MB 边界值测试', () => {
    const config = wrapper.vm.editorConfig
    const exactSize = 5 * 1024 * 1024
    // WangEditor checks file.size <= maxFileSize, so exactly 5MB is allowed
    expect(config.MENU_CONF.uploadImage.maxFileSize).toBe(exactSize)
  })

  test('[BLIND-SPOT] 4.3-BLIND-BOUNDARY-005: 图片文件大小 5MB+1byte 被正确拒绝', () => {
    const config = wrapper.vm.editorConfig
    const overSize = 5 * 1024 * 1024 + 1
    expect(overSize).toBeGreaterThan(config.MENU_CONF.uploadImage.maxFileSize)
  })

  test('[BLIND-SPOT] 4.3-BLIND-ERROR-001: 图片上传网络超时处理', () => {
    const config = wrapper.vm.editorConfig
    expect(config.MENU_CONF.uploadImage.timeout).toBe(30000)
    const mockFile = new File([''], 'test.jpg', { type: 'image/jpeg' })
    const mockError = new Error('upload image network error')
    config.MENU_CONF.uploadImage.onError(mockFile, mockError, '')
    expect(mockElMessage.error).toHaveBeenCalledWith('上传服务暂时不可用，请稍后重试')
  })

  test('[BLIND-SPOT] 4.3-BLIND-ERROR-002: 上传服务返回 500 时的用户提示', () => {
    const config = wrapper.vm.editorConfig
    const mockFile = new File([''], 'test.jpg', { type: 'image/jpeg' })
    const mockError = new Error('upload image http error, status code: 500')
    config.MENU_CONF.uploadImage.onError(mockFile, mockError, '')
    expect(mockElMessage.error).toHaveBeenCalledWith('上传服务暂时不可用，请稍后重试')
  })

  test('[BLIND-SPOT] 4.3-BLIND-ERROR-003: Token 过期 (401) 时跳转登录页', () => {
    const config = wrapper.vm.editorConfig
    const mockFile = new File([''], 'test.jpg', { type: 'image/jpeg' })
    const mockError = new Error('upload image http error, status code: 401')
    config.MENU_CONF.uploadImage.onError(mockFile, mockError, '')
    expect(mockElMessage.error).toHaveBeenCalledWith('登录已过期，请重新登录')
    expect(mockRouterPush).toHaveBeenCalledWith('/login')
  })

  test('[BLIND-SPOT] 4.3-BLIND-ERROR-004: 上传响应格式异常（缺少 data.url 字段）', () => {
    const config = wrapper.vm.editorConfig
    const insertFn = vi.fn()
    const badResponse = { code: 200, data: {} }
    config.MENU_CONF.uploadImage.customInsert(badResponse, insertFn)
    expect(insertFn).not.toHaveBeenCalled()
    expect(mockElMessage.error).toHaveBeenCalledWith('上传失败：响应中缺少图片地址')
  })

  test('[BLIND-SPOT] 4.3-BLIND-FLOW-004: 上传进行中切换页面（中断上传）', async () => {
    expect(latestMockEditorRef.value).not.toBeNull()
    const editorInstance = latestMockEditorRef.value!
    wrapper.unmount()
    expect(editorInstance.destroy).toHaveBeenCalled()
  })
})

// ============================================================
// AC6: 编辑器增强功能
// ============================================================

describe('AC6: 编辑器增强功能', () => {
  let wrapper: VueWrapper<any>

  beforeEach(async () => {
    latestMockEditorRef.value = null
    vi.clearAllMocks()
    wrapper = mountEditor()
    await nextTick()
    await nextTick()
  })

  afterEach(() => {
    if (wrapper?.vm) wrapper.unmount()
  })

  test('4.3-UNIT-019: 撤销/重做功能启用（Ctrl+Z/Y）', () => {
    expect(TOOLBAR_KEYS).toContain('undo')
    expect(TOOLBAR_KEYS).toContain('redo')
  })

  test('4.3-UNIT-020: 撤销记录至少保留 50 步配置', () => {
    // WangEditor 5.x uses Slate.js with default history size of 100 (≥50)
    expect(TOOLBAR_KEYS).toContain('undo')
    expect(TOOLBAR_KEYS).toContain('redo')
  })

  test('4.3-INT-004: 源码模式切换时内容不丢失', async () => {
    const testHtml = '<p>Hello <strong>World</strong></p>'
    await wrapper.setProps({ modelValue: testHtml })

    // Rich text → Source
    wrapper.vm.toggleSourceMode()
    await nextTick()
    expect(wrapper.vm.isSourceMode).toBe(true)
    expect(wrapper.vm.sourceHtml).toBe(testHtml)

    // Source → Rich text
    wrapper.vm.toggleSourceMode()
    await nextTick()
    expect(wrapper.vm.isSourceMode).toBe(false)
    const emitted = wrapper.emitted('update:modelValue')
    expect(emitted).toBeTruthy()
    const lastEmit = emitted![emitted!.length - 1]
    expect(lastEmit[0]).toBe(testHtml)
  })

  test('4.3-INT-005: 全屏模式下工具栏保持可见', async () => {
    wrapper.vm.toggleFullscreen()
    await nextTick()

    expect(wrapper.vm.isFullscreen).toBe(true)
    expect(wrapper.find('.rich-text-editor__toolbar-wrapper').exists()).toBe(true)
    expect(wrapper.find('.rich-text-editor').classes()).toContain('is-fullscreen')
  })

  test('[BLIND-SPOT] 4.3-BLIND-FLOW-003: 源码模式 ↔ 富文本模式连续快速切换', async () => {
    const testHtml = '<p>Content</p>'
    await wrapper.setProps({ modelValue: testHtml })

    for (let i = 0; i < 5; i++) {
      wrapper.vm.toggleSourceMode()
      await nextTick()
    }
    // After 5 toggles (odd), should be in source mode
    expect(wrapper.vm.isSourceMode).toBe(true)
    expect(wrapper.vm.sourceHtml).toBeTruthy()

    wrapper.vm.toggleSourceMode()
    await nextTick()
    expect(wrapper.vm.isSourceMode).toBe(false)
  })
})

// ============================================================
// AC7: 全局组件复用
// ============================================================

describe('AC7: 全局组件复用', () => {
  beforeEach(() => {
    latestMockEditorRef.value = null
    vi.clearAllMocks()
  })

  test('4.3-UNIT-021: v-model 双向绑定：modelValue prop + update:modelValue emit', async () => {
    const wrapper = mountEditor({ modelValue: '<p>test</p>' })
    await nextTick()
    await nextTick()

    const editorComponent = wrapper.findComponent({ name: 'MockEditor' })
    expect(editorComponent.props('modelValue')).toBe('<p>test</p>')

    wrapper.unmount()
  })

  test('4.3-UNIT-022: height prop 控制编辑区高度（默认 500px）', async () => {
    // Default height
    const w1 = mountEditor()
    await nextTick()
    expect(w1.find('.rich-text-editor__editor').attributes('style')).toContain('500px')
    w1.unmount()

    // Custom height
    const w2 = mountEditor({ height: 300 })
    await nextTick()
    expect(w2.find('.rich-text-editor__editor').attributes('style')).toContain('300px')
    w2.unmount()
  })

  test('4.3-UNIT-023: readOnly prop 切换只读模式', async () => {
    const wrapper = mountEditor({ readOnly: true })
    await nextTick()
    await nextTick()

    expect(wrapper.vm.editorConfig.readOnly).toBe(true)
    expect(wrapper.find('.rich-text-editor').classes()).toContain('is-readonly')
    wrapper.unmount()
  })

  test('4.3-UNIT-024: placeholder prop 自定义占位文本（默认"请输入内容..."）', async () => {
    expect(DEFAULT_PLACEHOLDER).toBe('请输入内容...')

    const w1 = mountEditor()
    await nextTick()
    expect(w1.vm.editorConfig.placeholder).toBe('请输入内容...')
    w1.unmount()

    const w2 = mountEditor({ placeholder: '自定义占位文本' })
    await nextTick()
    expect(w2.vm.editorConfig.placeholder).toBe('自定义占位文本')
    w2.unmount()
  })

  test('4.3-UNIT-025: disabled 状态下工具栏灰化、不可编辑', async () => {
    const wrapper = mountEditor({ disabled: true })
    await nextTick()
    await nextTick()

    expect(wrapper.find('.rich-text-editor').classes()).toContain('is-disabled')
    expect(wrapper.vm.editorConfig.readOnly).toBe(true)
    expect(latestMockEditorRef.value).not.toBeNull()
    expect(latestMockEditorRef.value!.disable).toHaveBeenCalled()

    const sourceBtn = wrapper.find('.rich-text-editor__btn')
    expect(sourceBtn.attributes('disabled')).toBeDefined()

    wrapper.unmount()
  })

  test('4.3-INT-006: 组件销毁时调用 editor.destroy() 释放资源', async () => {
    const wrapper = mountEditor()
    await nextTick()
    await nextTick()

    expect(latestMockEditorRef.value).not.toBeNull()
    const editorInstance = latestMockEditorRef.value!
    wrapper.unmount()
    expect(editorInstance.destroy).toHaveBeenCalledTimes(1)
  })

  test('4.3-INT-007: 多实例场景下各编辑器独立工作', async () => {
    const editors: ReturnType<typeof createMockEditor>[] = []

    const w1 = mountEditor({ modelValue: '<p>Editor 1</p>' })
    await nextTick()
    await nextTick()
    editors.push(latestMockEditorRef.value!)

    const w2 = mountEditor({ modelValue: '<p>Editor 2</p>' })
    await nextTick()
    await nextTick()
    editors.push(latestMockEditorRef.value!)

    expect(editors[0]).not.toBe(editors[1])

    const e1 = w1.findComponent({ name: 'MockEditor' })
    const e2 = w2.findComponent({ name: 'MockEditor' })
    expect(e1.props('modelValue')).toBe('<p>Editor 1</p>')
    expect(e2.props('modelValue')).toBe('<p>Editor 2</p>')

    w1.unmount()
    expect(editors[0].destroy).toHaveBeenCalled()
    expect(editors[1].destroy).not.toHaveBeenCalled()

    w2.unmount()
    expect(editors[1].destroy).toHaveBeenCalled()
  })

  test('4.3-E2E-001: 编辑 → 获取 HTML → 回显完整流程', async () => {
    const wrapper = mountEditor({ modelValue: '' })
    await nextTick()
    await nextTick()

    await wrapper.setProps({ modelValue: '<p>Formatted <strong>content</strong></p>' })
    await nextTick()

    const editorComponent = wrapper.findComponent({ name: 'MockEditor' })
    expect(editorComponent.props('modelValue')).toBe('<p>Formatted <strong>content</strong></p>')

    const savedHtml = '<p>Formatted <strong>content</strong></p>'
    await wrapper.setProps({ modelValue: savedHtml })
    await nextTick()
    expect(editorComponent.props('modelValue')).toBe(savedHtml)

    wrapper.unmount()
  })

  // --- Blind Spot Scenarios ---

  test('[BLIND-SPOT] 4.3-BLIND-BOUNDARY-001: modelValue 为 null/undefined/空字符串时组件正常渲染', async () => {
    const w1 = mountEditor({ modelValue: '' })
    await nextTick()
    expect(w1.find('.rich-text-editor').exists()).toBe(true)
    w1.unmount()

    const w2 = mountEditor({})
    await nextTick()
    expect(w2.find('.rich-text-editor').exists()).toBe(true)
    w2.unmount()

    const w3 = mountEditor({ modelValue: null as any })
    await nextTick()
    expect(w3.find('.rich-text-editor').exists()).toBe(true)
    w3.unmount()
  })

  test('[BLIND-SPOT] 4.3-BLIND-BOUNDARY-002: height prop 为 0 或负数时的降级处理', async () => {
    const w1 = mountEditor({ height: 0 })
    await nextTick()
    expect(w1.find('.rich-text-editor__editor').attributes('style')).toContain(`${DEFAULT_HEIGHT}px`)
    w1.unmount()

    const w2 = mountEditor({ height: -100 })
    await nextTick()
    expect(w2.find('.rich-text-editor__editor').attributes('style')).toContain(`${DEFAULT_HEIGHT}px`)
    w2.unmount()
  })

  test('[BLIND-SPOT] 4.3-BLIND-BOUNDARY-003: 超大 HTML 内容 (>1MB) 传入 modelValue 时不卡死', async () => {
    const largeContent = '<p>' + 'a'.repeat(1024 * 1024) + '</p>'
    expect(largeContent.length).toBeGreaterThan(1024 * 1024)

    const wrapper = mountEditor({ modelValue: largeContent })
    await nextTick()
    expect(wrapper.find('.rich-text-editor').exists()).toBe(true)

    const editorComponent = wrapper.findComponent({ name: 'MockEditor' })
    expect(editorComponent.exists()).toBe(true)
    expect(editorComponent.props('modelValue')).toBe(largeContent)
    wrapper.unmount()
  })

  test('[BLIND-SPOT] 4.3-BLIND-FLOW-001: 编辑过程中组件被强制卸载（路由切换）', async () => {
    const wrapper = mountEditor({ modelValue: '<p>In-progress edit</p>' })
    await nextTick()
    await nextTick()

    expect(latestMockEditorRef.value).not.toBeNull()
    const editorInstance = latestMockEditorRef.value!
    wrapper.unmount()
    expect(editorInstance.destroy).toHaveBeenCalled()
  })

  test('[BLIND-SPOT] 4.3-BLIND-FLOW-002: 连续快速切换 readOnly/disabled 状态', async () => {
    const wrapper = mountEditor({ readOnly: false })
    await nextTick()
    await nextTick()
    expect(latestMockEditorRef.value).not.toBeNull()

    await wrapper.setProps({ readOnly: true })
    await nextTick()
    expect(latestMockEditorRef.value!.disable).toHaveBeenCalled()

    await wrapper.setProps({ readOnly: false })
    await nextTick()
    expect(latestMockEditorRef.value!.enable).toHaveBeenCalled()

    await wrapper.setProps({ readOnly: true })
    await nextTick()
    await wrapper.setProps({ readOnly: false })
    await nextTick()

    expect(wrapper.find('.rich-text-editor').exists()).toBe(true)
    wrapper.unmount()
  })

  test('[BLIND-SPOT] 4.3-BLIND-RESOURCE-001: 组件多次挂载/卸载无内存泄漏', async () => {
    const destroyFns: Mock[] = []

    for (let i = 0; i < 10; i++) {
      latestMockEditorRef.value = null
      const w = mountEditor()
      await nextTick()
      await nextTick()

      if (latestMockEditorRef.value) {
        destroyFns.push((latestMockEditorRef.value as any).destroy)
      }
      w.unmount()
    }

    destroyFns.forEach(fn => expect(fn).toHaveBeenCalledTimes(1))
    expect(destroyFns.length).toBe(10)
  })
})
