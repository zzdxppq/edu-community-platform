import 'vue-router'

declare module 'vue-router' {
  interface RouteMeta {
    title?: string
    requiresAuth?: boolean
    permissions?: string[]
    roles?: number[]
    layout?: 'main' | 'blank'
    icon?: string
    hidden?: boolean
    order?: number
  }
}
