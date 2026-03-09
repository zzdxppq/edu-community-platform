import { get, post, put, del } from '@/utils/request'
import type {
  SubColumn,
  CreateSubColumnRequest,
  UpdateSubColumnRequest,
  SortSubColumnRequest
} from '@/types/subColumn'

export function getSubColumnList(columnId: number): Promise<SubColumn[]> {
  return get<SubColumn[]>(`/content/v1/columns/${columnId}/sub-columns`)
}

export function createSubColumn(columnId: number, data: CreateSubColumnRequest): Promise<number> {
  return post<number>(`/content/v1/columns/${columnId}/sub-columns`, data)
}

export function updateSubColumn(columnId: number, id: number, data: UpdateSubColumnRequest): Promise<void> {
  return put<void>(`/content/v1/columns/${columnId}/sub-columns/${id}`, data)
}

export function deleteSubColumn(columnId: number, id: number): Promise<void> {
  return del<void>(`/content/v1/columns/${columnId}/sub-columns/${id}`)
}

export function sortSubColumns(columnId: number, data: SortSubColumnRequest): Promise<void> {
  return put<void>(`/content/v1/columns/${columnId}/sub-columns/sort`, data)
}
