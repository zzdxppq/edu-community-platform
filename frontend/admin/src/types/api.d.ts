interface ApiResponse<T = unknown> {
  code: number
  message: string
  data: T
  timestamp: string
  request_id: string
}

interface PageResult<T> {
  list: T[]
  total: number
  page: number
  size: number
}
