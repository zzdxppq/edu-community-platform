export interface SubColumn {
  id: number
  columnId: number
  name: string
  code: string
  categoryCode?: string
  isSystem: boolean
  sortOrder: number
  createdAt: string
  updatedAt: string
}

export interface CreateSubColumnRequest {
  name: string
  code: string
  categoryCode?: string
  sortOrder?: number
}

export interface UpdateSubColumnRequest {
  name?: string
  code?: string
  categoryCode?: string
  sortOrder?: number
}

export interface SortSubColumnRequest {
  items: Array<{ id: number; sortOrder: number }>
}
