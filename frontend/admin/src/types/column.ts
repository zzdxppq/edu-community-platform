import type { SubColumn } from './subColumn'

export interface Column {
  id: number
  name: string
  code: string
  icon?: string
  routePath?: string
  isSystem: boolean
  isVisible: boolean
  subMenuEnabled: boolean
  sortOrder: number
  subColumns?: SubColumn[]
  createdAt: string
  updatedAt: string
}

export interface CreateColumnRequest {
  name: string
  code: string
  icon?: string
  routePath?: string
  sortOrder?: number
}

export interface UpdateColumnRequest {
  name?: string
  code?: string
  icon?: string
  routePath?: string
  isVisible?: boolean
  subMenuEnabled?: boolean
  sortOrder?: number
}

export interface SortColumnRequest {
  items: Array<{ id: number; sortOrder: number }>
}
