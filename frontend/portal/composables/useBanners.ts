import type { BannerItem, ApiResult } from '~/types/banner'

export function useBanners() {
  const { data, pending, error } = useApiFetch<ApiResult<BannerItem[]>>(
    '/content/v1/banners/active',
  )

  const banners = computed(() => {
    const result = data.value as ApiResult<BannerItem[]> | null
    return result?.data ?? []
  })

  return { banners, pending, error }
}
