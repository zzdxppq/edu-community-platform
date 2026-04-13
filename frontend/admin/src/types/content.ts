/** 发布方式枚举 */
export type PublishMode = 'richtext' | 'attachment' | 'link'

/** 附件信息 */
export interface AttachmentInfo {
  fileId: string
  fileName: string
  fileSize: number
  fileUrl: string
  mimeType: string
}

/** 内容发布数据（组件 v-model 输出） */
export interface ContentPublishData {
  publishType: PublishMode
  content?: string
  attachments?: AttachmentInfo[]
  externalLink?: string
  coverImage?: string
  summary?: string
}

/** ContentPublisher 组件 Props */
export interface ContentPublisherProps {
  modelValue: ContentPublishData
  modes?: PublishMode[]
  defaultMode?: PublishMode
  accept?: string
  maxSize?: number
  editorHeight?: number | string
  disabled?: boolean
}

/** ContentPublisher 暴露方法（defineExpose） */
export interface ContentPublisherExpose {
  validate: () => Promise<boolean>
  reset: () => void
}

/** 文件上传 API 响应 */
export interface FileUploadResponse {
  id: string
  url: string
  filename: string
  size: number
  mime_type: string
}

/** 发布模式标签映射 */
export const PUBLISH_MODE_LABELS: Record<PublishMode, string> = {
  richtext: '图文编辑',
  attachment: '附件上传',
  link: '外部链接',
}

/** 默认附件接受格式 */
export const DEFAULT_ACCEPT = '.pdf,.doc,.docx,.xls,.xlsx'

/** 默认附件大小限制 (MB) */
export const DEFAULT_MAX_SIZE = 50

/** 富文本内容最大大小 (bytes) — 2MB */
export const MAX_RICHTEXT_SIZE = 2 * 1024 * 1024
