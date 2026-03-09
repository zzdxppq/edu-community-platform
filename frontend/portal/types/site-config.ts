/** 站点基础配置 — 对应 site_settings 表 */
export interface SiteConfig {
  siteName: string
  copyright: string
  address: string
  email: string
}

/** 友情链接 */
export interface FriendLink {
  name: string
  url: string
}
