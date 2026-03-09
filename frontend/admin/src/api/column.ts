import { get, post, put, del } from '@/utils/request'
import type { Column, CreateColumnRequest, UpdateColumnRequest, SortColumnRequest } from '@/types/column'

export function getColumnList(): Promise<Column[]> {
  return get<Column[]>('/content/v1/columns')
}

export function createColumn(data: CreateColumnRequest): Promise<number> {
  return post<number>('/content/v1/columns', data)
}

export function updateColumn(id: number, data: UpdateColumnRequest): Promise<void> {
  return put<void>(`/content/v1/columns/${id}`, data)
}

export function deleteColumn(id: number): Promise<void> {
  return del<void>(`/content/v1/columns/${id}`)
}

export function sortColumns(data: SortColumnRequest): Promise<void> {
  return put<void>('/content/v1/columns/sort', data)
}
