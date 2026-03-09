/** 轮播图数据项 — 与后端 BannerActiveResponse 对应 */
export interface BannerItem {
  id: number
  title: string
  imageUrl: string
  linkUrl: string | null
  linkType: 1 | 2 // 1=内部链接, 2=外部链接
}

/** 统一 API 响应包装 */
export interface ApiResult<T> {
  code: number
  message: string
  data: T
}
