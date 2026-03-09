import type { SiteConfig, FriendLink } from '~/types/site-config'
import type { ApiResult } from '~/types/banner'

const DEFAULT_SITE_CONFIG: SiteConfig = {
  siteName: '河南城乡学校共同体发展平台',
  copyright: `© ${new Date().getFullYear()} 河南城乡学校共同体发展平台 版权所有`,
  address: '河南省新乡市建设路东段46号',
  email: 'hnctgt@htu.edu.cn',
}

const FRIEND_LINKS: FriendLink[] = [
  { name: '河南省教育厅', url: 'https://jyt.henan.gov.cn/' },
  { name: '河南大学教育学部', url: 'https://jyxy.henu.edu.cn/' },
  { name: '河南省教育信息化发展研究中心', url: 'https://www.htu.edu.cn/jce/' },
]

export function useSiteConfig() {
  const { data } = useApiFetch<ApiResult<SiteConfig>>(
    '/core/v1/site-settings/public',
  )

  const siteConfig = computed<SiteConfig>(() => {
    const result = data.value as ApiResult<SiteConfig> | null
    return result?.data ?? DEFAULT_SITE_CONFIG
  })

  const friendLinks = FRIEND_LINKS

  return { siteConfig, friendLinks }
}
