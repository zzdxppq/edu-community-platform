import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', requiresAuth: false, layout: 'blank', hidden: true }
  },
  {
    path: '/change-password',
    name: 'ChangePassword',
    component: () => import('@/views/change-password/index.vue'),
    meta: { title: '修改密码', requiresAuth: true, layout: 'blank', hidden: true }
  },
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/dashboard/index.vue'),
    meta: { title: '工作台', requiresAuth: true, layout: 'main', roles: [1, 2], icon: 'Odometer', order: 1 }
  },
  {
    path: '/admin-users',
    name: 'AdminUsers',
    component: () => import('@/views/admin-users/index.vue'),
    meta: { title: '管理员管理', requiresAuth: true, layout: 'main', roles: [1], icon: 'User', order: 10 }
  },
  {
    path: '/schools',
    name: 'Schools',
    component: () => import('@/views/schools/index.vue'),
    meta: { title: '示范校管理', requiresAuth: true, layout: 'main', roles: [1], icon: 'School', order: 20 }
  },
  {
    path: '/content/column',
    name: 'ColumnManagement',
    component: () => import('@/views/content/column/index.vue'),
    meta: { title: '栏目管理', requiresAuth: true, layout: 'main', roles: [1], icon: 'Menu', order: 30 }
  }
]

export default routes
