import { get } from '@/utils/request'

export function getSchoolOptions(): Promise<SchoolOption[]> {
  return get<SchoolOption[]>('/core/v1/schools/options')
}
