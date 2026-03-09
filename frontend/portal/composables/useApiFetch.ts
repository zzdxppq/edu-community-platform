export function useApiFetch<T>(url: string | (() => string), opts?: Record<string, unknown>) {
  const config = useRuntimeConfig()
  return useFetch<T>(url, {
    baseURL: config.public.apiBase as string,
    ...(opts as object),
  })
}
