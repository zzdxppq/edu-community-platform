# 前端开发规范

> 河南城乡学校共同体发展平台 - 前端开发规范 v1.0.0
>
> 最后更新：2026-02-06

---

## 一、技术栈概览

### 1.1 技术选型

| 应用 | 技术栈 | 版本 | 说明 |
|------|--------|------|------|
| **官网前端** | Vue 3 + Nuxt 3 | Vue 3.4.x / Nuxt 3.10.x | SSR 服务端渲染，SEO 友好 |
| **超级管理后台** | Vue 3 + Element Plus | Vue 3.4.x / Element Plus 2.5.x | 企业级后台 UI |
| **学校管理后台** | Vue 3 + Element Plus | Vue 3.4.x / Element Plus 2.5.x | 复用管理后台组件 |

### 1.2 核心依赖

| 分类 | 依赖 | 版本 | 说明 |
|------|------|------|------|
| **构建工具** | Vite | 5.x | 快速构建 |
| **状态管理** | Pinia | 2.x | Vue 3 官方状态管理 |
| **HTTP 客户端** | Axios | 1.6.x | API 请求 |
| **富文本编辑器** | WangEditor | 5.x | 内容编辑 |
| **图标库** | @element-plus/icons-vue | 2.x | Element Plus 图标 |
| **工具库** | VueUse | 10.x | Vue Composition API 工具集 |
| **日期处理** | dayjs | 1.x | 轻量日期库 |
| **代码规范** | ESLint + Prettier | - | 代码质量 |
| **类型检查** | TypeScript | 5.x | 类型安全 |

---

## 二、项目结构规范

### 2.1 官网前端 (Nuxt 3)

```
frontend/portal/
├── .nuxt/                      # Nuxt 构建目录
├── assets/                     # 静态资源
│   ├── css/                    # 全局样式
│   │   ├── variables.css       # CSS 变量 (配色/字体)
│   │   ├── reset.css           # 样式重置
│   │   └── main.css            # 主样式入口
│   ├── images/                 # 图片资源
│   └── fonts/                  # 字体文件
├── components/                 # 组件
│   ├── common/                 # 通用组件
│   │   ├── AppHeader.vue       # 导航栏
│   │   ├── AppFooter.vue       # 页脚
│   │   ├── AppSidebar.vue      # 侧边栏
│   │   ├── Pagination.vue      # 分页
│   │   └── SearchBox.vue       # 搜索框
│   ├── home/                   # 首页组件
│   │   ├── HeroBanner.vue      # 轮播图
│   │   ├── ModuleEntry.vue     # 模块入口
│   │   └── FriendLinks.vue     # 友情链接
│   ├── policy/                 # 政策文件组件
│   ├── school/                 # 示范校组件
│   └── resource/               # 资源共享组件
├── composables/                # 组合式函数
│   ├── useApi.ts               # API 封装
│   ├── useAuth.ts              # 认证状态
│   └── usePagination.ts        # 分页逻辑
├── layouts/                    # 布局
│   ├── default.vue             # 默认布局
│   └── blank.vue               # 空白布局 (登录页)
├── middleware/                 # 中间件
│   └── auth.ts                 # 认证中间件
├── pages/                      # 页面路由
│   ├── index.vue               # 首页
│   ├── intro.vue               # 项目介绍
│   ├── policy/                 # 政策文件
│   │   ├── index.vue           # 列表页
│   │   └── [id].vue            # 详情页
│   ├── schools/                # 示范校共体
│   │   ├── index.vue           # 学校列表
│   │   ├── [id]/               # 学校详情
│   │   │   ├── index.vue       # 学校主页 (Tab)
│   │   │   ├── activities/     # 活动
│   │   │   │   ├── index.vue   # 活动列表
│   │   │   │   └── [activityId].vue  # 活动详情
│   │   │   └── reports/        # 月报
│   │   │       ├── index.vue   # 月报列表
│   │   │       └── [reportId].vue    # 月报详情
│   ├── resources/              # 资源共享
│   │   ├── index.vue           # 列表页
│   │   └── [id].vue            # 详情页
│   ├── news/                   # 新闻资讯
│   │   ├── index.vue           # 列表页
│   │   └── [id].vue            # 详情页
│   ├── search.vue              # 搜索结果页
│   └── login.vue               # 登录页
├── plugins/                    # 插件
│   └── axios.ts                # Axios 配置
├── server/                     # 服务端
│   └── api/                    # API 代理 (可选)
├── stores/                     # Pinia 状态
│   ├── auth.ts                 # 认证状态
│   ├── site.ts                 # 站点配置
│   └── search.ts               # 搜索状态
├── types/                      # 类型定义
│   ├── api.d.ts                # API 响应类型
│   ├── model.d.ts              # 业务模型类型
│   └── env.d.ts                # 环境变量类型
├── utils/                      # 工具函数
│   ├── request.ts              # 请求封装
│   ├── storage.ts              # 本地存储
│   └── format.ts               # 格式化工具
├── nuxt.config.ts              # Nuxt 配置
├── app.vue                     # 根组件
├── error.vue                   # 错误页
├── tsconfig.json               # TS 配置
└── package.json
```

### 2.2 管理后台 (Vue 3 + Element Plus)

```
frontend/admin/                  # 超级管理后台
frontend/school/                 # 学校管理后台 (结构相同)
├── public/                     # 静态资源
├── src/
│   ├── api/                    # API 模块
│   │   ├── index.ts            # API 入口
│   │   ├── auth.ts             # 认证 API
│   │   ├── user.ts             # 用户 API
│   │   ├── school.ts           # 学校 API
│   │   ├── content.ts          # 内容 API
│   │   └── system.ts           # 系统 API
│   ├── assets/                 # 静态资源
│   │   ├── styles/             # 样式
│   │   │   ├── variables.scss  # SCSS 变量
│   │   │   ├── element.scss    # Element Plus 覆写
│   │   │   └── index.scss      # 主样式
│   │   └── images/             # 图片
│   ├── components/             # 公共组件
│   │   ├── layout/             # 布局组件
│   │   │   ├── AppLayout.vue   # 主布局
│   │   │   ├── AppHeader.vue   # 顶栏
│   │   │   ├── AppSidebar.vue  # 侧边栏
│   │   │   └── AppBreadcrumb.vue # 面包屑
│   │   ├── common/             # 通用组件
│   │   │   ├── PageContainer.vue    # 页面容器
│   │   │   ├── SearchForm.vue       # 搜索表单
│   │   │   ├── ConfirmDialog.vue    # 确认弹窗
│   │   │   └── RichTextEditor.vue   # 富文本编辑器
│   │   └── business/           # 业务组件
│   │       ├── UserSelect.vue       # 用户选择器
│   │       └── SchoolSelect.vue     # 学校选择器
│   ├── composables/            # 组合式函数
│   │   ├── useTable.ts         # 表格逻辑
│   │   ├── useForm.ts          # 表单逻辑
│   │   └── usePermission.ts    # 权限控制
│   ├── directives/             # 自定义指令
│   │   └── permission.ts       # 权限指令
│   ├── hooks/                  # 业务 Hooks
│   ├── layouts/                # 布局
│   │   ├── MainLayout.vue      # 主布局
│   │   └── BlankLayout.vue     # 空白布局
│   ├── router/                 # 路由
│   │   ├── index.ts            # 路由入口
│   │   ├── routes.ts           # 路由配置
│   │   └── guards.ts           # 路由守卫
│   ├── stores/                 # Pinia 状态
│   │   ├── index.ts            # Store 入口
│   │   ├── auth.ts             # 认证状态
│   │   ├── app.ts              # 应用状态
│   │   └── permission.ts       # 权限状态
│   ├── types/                  # 类型定义
│   │   ├── api.d.ts            # API 类型
│   │   ├── model.d.ts          # 模型类型
│   │   └── router.d.ts         # 路由类型
│   ├── utils/                  # 工具函数
│   │   ├── request.ts          # 请求封装
│   │   ├── auth.ts             # Token 管理
│   │   ├── validate.ts         # 表单校验
│   │   └── download.ts         # 文件下载
│   ├── views/                  # 页面视图
│   │   ├── dashboard/          # 工作台
│   │   │   └── index.vue
│   │   ├── user/               # 用户管理
│   │   │   ├── list.vue
│   │   │   └── form.vue
│   │   ├── school/             # 示范校管理
│   │   ├── content/            # 内容管理
│   │   │   ├── news/           # 新闻资讯
│   │   │   ├── policy/         # 政策文件
│   │   │   ├── resource/       # 资源共享
│   │   │   └── banner/         # 轮播图
│   │   ├── system/             # 系统设置
│   │   │   ├── config.vue      # 基础配置
│   │   │   ├── sensitive-word.vue  # 敏感词
│   │   │   └── backup.vue      # 数据备份
│   │   └── login/              # 登录
│   │       └── index.vue
│   ├── App.vue                 # 根组件
│   └── main.ts                 # 入口文件
├── .env                        # 环境变量
├── .env.development            # 开发环境
├── .env.production             # 生产环境
├── vite.config.ts              # Vite 配置
├── tsconfig.json               # TS 配置
└── package.json
```

---

## 三、设计规范对接

### 3.1 CSS 变量定义

与 `docs/ui-design-specification.md` 保持一致：

```css
/* assets/css/variables.css */

:root {
  /* ===== 主色 Primary (蓝色系) ===== */
  --primary-900: #1a3d6e;
  --primary-800: #2C5AA0;
  --primary-700: #3568b2;
  --primary-600: #4a7cc4;

  /* ===== 强调色 Accent ===== */
  --accent-700: #3558a8;
  --accent-600: #426CC6;
  --accent-500: #5580d4;
  --accent-400: #7a9fe0;

  /* ===== 辅色 Secondary (暖色点缀) ===== */
  --secondary-700: #b85c1a;
  --secondary-600: #e07628;
  --secondary-500: #f08c3d;
  --secondary-400: #f5a862;

  /* ===== 中性色 Neutral ===== */
  --neutral-900: #1a1a1a;
  --neutral-800: #333333;
  --neutral-700: #555555;
  --neutral-600: #666666;
  --neutral-500: #999999;
  --neutral-400: #cccccc;
  --neutral-300: #e5e5e5;
  --neutral-200: #f2f2f2;
  --neutral-100: #f8f8f8;
  --neutral-50: #ffffff;

  /* ===== 功能色 Functional ===== */
  --success: #28a745;
  --warning: #ffc107;
  --danger: #dc3545;
  --info: #17a2b8;

  /* ===== 字号 ===== */
  --text-xs: 12px;
  --text-sm: 13px;
  --text-base: 14px;
  --text-lg: 16px;
  --text-xl: 18px;
  --text-2xl: 20px;
  --text-3xl: 24px;
  --text-4xl: 28px;

  /* ===== 行高 ===== */
  --leading-tight: 1.4;
  --leading-normal: 1.5;
  --leading-relaxed: 1.75;

  /* ===== 字重 ===== */
  --font-normal: 400;
  --font-medium: 500;
  --font-semibold: 600;
  --font-bold: 700;

  /* ===== 间距 ===== */
  --spacing-1: 4px;
  --spacing-2: 8px;
  --spacing-3: 12px;
  --spacing-4: 16px;
  --spacing-5: 20px;
  --spacing-6: 24px;
  --spacing-8: 32px;
  --spacing-10: 40px;
  --spacing-12: 48px;
  --spacing-16: 64px;

  /* ===== 布局 ===== */
  --header-height: 80px;
  --sidebar-width: 220px;
  --footer-height: 120px;
  --container-max-width: 1200px;

  /* ===== 断点 ===== */
  --breakpoint-sm: 768px;
  --breakpoint-md: 1024px;
  --breakpoint-lg: 1200px;
  --breakpoint-xl: 1400px;

  /* ===== 字体 ===== */
  --font-primary: "Microsoft YaHei", "微软雅黑", "PingFang SC",
                  "Hiragino Sans GB", Arial, sans-serif;
  --font-heading: "Microsoft YaHei", "微软雅黑", "STHeiti",
                  "SimHei", Arial, sans-serif;
  --font-mono: "Consolas", "Monaco", "Microsoft YaHei", monospace;

  /* ===== 阴影 ===== */
  --shadow-sm: 0 1px 2px rgba(0, 0, 0, 0.05);
  --shadow-md: 0 4px 6px rgba(0, 0, 0, 0.1);
  --shadow-lg: 0 10px 15px rgba(0, 0, 0, 0.1);
  --shadow-header: 0 2px 8px rgba(0, 0, 0, 0.15);

  /* ===== 圆角 ===== */
  --radius-sm: 2px;
  --radius-md: 4px;
  --radius-lg: 8px;
  --radius-full: 9999px;

  /* ===== 过渡 ===== */
  --transition-fast: 0.15s ease;
  --transition-normal: 0.3s ease;
  --transition-slow: 0.5s ease;
}
```

### 3.2 Element Plus 主题覆写

```scss
/* assets/styles/element.scss */

/* Element Plus 主题变量覆写 */
:root {
  --el-color-primary: #2C5AA0;
  --el-color-primary-light-3: #5580d4;
  --el-color-primary-light-5: #7a9fe0;
  --el-color-primary-light-7: #aec4eb;
  --el-color-primary-light-9: #e5ecf7;
  --el-color-primary-dark-2: #1a3d6e;

  --el-color-success: #28a745;
  --el-color-warning: #ffc107;
  --el-color-danger: #dc3545;
  --el-color-info: #17a2b8;

  --el-font-size-base: 14px;
  --el-border-radius-base: 4px;
}
```

---

## 四、路由规范

### 4.1 官网路由表

| 路由 | 页面 | 说明 |
|------|------|------|
| `/` | 首页 | 轮播图 + 模块入口 |
| `/intro` | 项目介绍 | 富文本内容页 |
| `/policy` | 政策文件列表 | 分类导航 + 列表 |
| `/policy/:id` | 政策文件详情 | 详情 + 下载 |
| `/schools` | 示范校列表 | 左侧导航 |
| `/schools/:id` | 学校主页 | Tab 切换 |
| `/schools/:id/activities` | 活动列表 | 分页列表 |
| `/schools/:id/activities/:activityId` | 活动详情 | 详情页 |
| `/schools/:id/reports` | 月报列表 | 分页列表 |
| `/schools/:id/reports/:reportId` | 月报详情 | 详情页 |
| `/resources` | 资源共享列表 | 分类 + 搜索 |
| `/resources/:id` | 资源详情 | 预览 + 下载 |
| `/news` | 新闻列表 | 分页列表 |
| `/news/:id` | 新闻详情 | 详情页 |
| `/search` | 搜索结果 | 全站搜索 |
| `/login` | 登录页 | 管理员登录 |

### 4.2 管理后台路由表 (超级管理员)

| 路由 | 页面 | 权限 |
|------|------|------|
| `/dashboard` | 工作台 | 登录即可 |
| `/user/list` | 管理员列表 | `user:read` |
| `/user/create` | 创建管理员 | `user:write` |
| `/school/list` | 示范校列表 | `school:read` |
| `/school/:id` | 示范校详情 | `school:read` |
| `/content/banner` | 轮播图管理 | `content:write` |
| `/content/news` | 新闻管理 | `content:write` |
| `/content/policy` | 政策管理 | `content:write` |
| `/content/resource` | 资源管理 | `content:write` |
| `/content/intro` | 项目介绍 | `content:write` |
| `/content/report` | 月报管理 | `content:read` |
| `/system/config` | 基础配置 | `system:config` |
| `/system/sensitive` | 敏感词管理 | `system:config` |
| `/system/backup` | 数据备份 | `system:backup` |

### 4.3 学校管理后台路由表

| 路由 | 页面 | 说明 |
|------|------|------|
| `/dashboard` | 工作台 | 活动 + 月报列表 |
| `/profile` | 账号设置 | 修改密码 |
| `/content/intro` | 校共同体介绍 | 富文本编辑 |
| `/content/activity` | 校共同体活动 | 列表 + CRUD |
| `/content/report` | 校共同体月报 | 列表 + CRUD |
| `/member` | 成员校管理 | 成员校列表 |

---

## 五、API 集成规范

### 5.1 请求封装

```typescript
// utils/request.ts

import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import router from '@/router'

// 创建实例
const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    const authStore = useAuthStore()

    // 添加 Token
    if (authStore.token) {
      config.headers.Authorization = `Bearer ${authStore.token}`
    }

    // 添加请求 ID
    config.headers['X-Request-Id'] = crypto.randomUUID()

    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse<ApiResponse>) => {
    const { code, message, data } = response.data

    // 业务成功
    if (code === 200) {
      return data
    }

    // 业务错误
    ElMessage.error(message || '请求失败')
    return Promise.reject(new Error(message))
  },
  (error) => {
    const { status, data } = error.response || {}
    const authStore = useAuthStore()

    switch (status) {
      case 401:
        // 未认证，跳转登录
        authStore.logout()
        router.push('/login')
        break

      case 403:
        ElMessage.error('无权限访问')
        break

      case 404:
        ElMessage.error('资源不存在')
        break

      case 422:
        // 业务错误
        ElMessage.error(data?.message || '操作失败')
        break

      case 500:
        ElMessage.error('服务器异常，请稍后重试')
        break

      default:
        ElMessage.error(error.message || '网络异常')
    }

    return Promise.reject(error)
  }
)

// 统一响应类型
interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
  timestamp: string
  request_id: string
}

// 分页响应类型
interface PageResult<T> {
  items: T[]
  pagination: {
    page: number
    size: number
    total: number
    total_pages: number
  }
}

// 导出请求方法
export function get<T>(url: string, params?: object): Promise<T> {
  return service.get(url, { params })
}

export function post<T>(url: string, data?: object): Promise<T> {
  return service.post(url, data)
}

export function put<T>(url: string, data?: object): Promise<T> {
  return service.put(url, data)
}

export function del<T>(url: string, params?: object): Promise<T> {
  return service.delete(url, { params })
}

export default service
```

### 5.2 API 模块示例

```typescript
// api/school.ts

import { get, post, put, del } from '@/utils/request'
import type { School, SchoolListQuery, PageResult } from '@/types/model'

const BASE_URL = '/api/v1/schools'

/**
 * 获取示范校列表
 */
export function getSchoolList(params: SchoolListQuery): Promise<PageResult<School>> {
  return get(`${BASE_URL}`, params)
}

/**
 * 获取示范校详情
 */
export function getSchoolById(id: number): Promise<School> {
  return get(`${BASE_URL}/${id}`)
}

/**
 * 创建示范校
 */
export function createSchool(data: Partial<School>): Promise<number> {
  return post(`${BASE_URL}`, data)
}

/**
 * 更新示范校
 */
export function updateSchool(id: number, data: Partial<School>): Promise<void> {
  return put(`${BASE_URL}/${id}`, data)
}

/**
 * 删除示范校
 */
export function deleteSchool(id: number): Promise<void> {
  return del(`${BASE_URL}/${id}`)
}

/**
 * 获取学校活动列表
 */
export function getSchoolActivities(
  schoolId: number,
  params: { page: number; size: number }
): Promise<PageResult<Activity>> {
  return get(`${BASE_URL}/${schoolId}/activities`, params)
}

/**
 * 获取学校月报列表
 */
export function getSchoolReports(
  schoolId: number,
  params: { page: number; size: number; year?: number; month?: number }
): Promise<PageResult<Report>> {
  return get(`${BASE_URL}/${schoolId}/reports`, params)
}
```

### 5.3 与后端 API 对接

根据 `architecture.md` 中的 API 契约：

| 后端服务 | API 前缀 | 说明 |
|----------|----------|------|
| **core-service** | `/api/v1/auth/*` | 认证 API |
| **core-service** | `/api/v1/users/*` | 用户管理 |
| **core-service** | `/api/v1/schools/*` | 学校管理 |
| **content-service** | `/api/v1/news/*` | 新闻资讯 |
| **content-service** | `/api/v1/policies/*` | 政策文件 |
| **content-service** | `/api/v1/resources/*` | 资源共享 |
| **content-service** | `/api/v1/search` | 全站搜索 |
| **file-service** | `/api/v1/files/*` | 文件上传下载 |

---

## 六、状态管理规范

### 6.1 Store 结构

```typescript
// stores/auth.ts

import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login, logout, getUserInfo } from '@/api/auth'
import type { UserInfo, LoginRequest } from '@/types/model'

export const useAuthStore = defineStore('auth', () => {
  // State
  const token = ref<string | null>(localStorage.getItem('token'))
  const user = ref<UserInfo | null>(null)
  const permissions = ref<string[]>([])

  // Getters
  const isLoggedIn = computed(() => !!token.value)
  const isSuperAdmin = computed(() => user.value?.role?.code === 'super_admin')
  const isSchoolAdmin = computed(() => user.value?.role?.code === 'school_admin')

  // Actions
  async function doLogin(credentials: LoginRequest) {
    const res = await login(credentials)
    token.value = res.access_token
    localStorage.setItem('token', res.access_token)
    await fetchUserInfo()
  }

  async function fetchUserInfo() {
    const res = await getUserInfo()
    user.value = res
    permissions.value = res.permissions || []
  }

  async function doLogout() {
    await logout()
    token.value = null
    user.value = null
    permissions.value = []
    localStorage.removeItem('token')
  }

  function hasPermission(permission: string): boolean {
    return permissions.value.includes(permission)
  }

  return {
    token,
    user,
    permissions,
    isLoggedIn,
    isSuperAdmin,
    isSchoolAdmin,
    doLogin,
    fetchUserInfo,
    doLogout,
    hasPermission
  }
})
```

### 6.2 权限控制

```typescript
// composables/usePermission.ts

import { useAuthStore } from '@/stores/auth'

export function usePermission() {
  const authStore = useAuthStore()

  /**
   * 检查是否有权限
   */
  function hasPermission(permission: string | string[]): boolean {
    if (Array.isArray(permission)) {
      return permission.some(p => authStore.hasPermission(p))
    }
    return authStore.hasPermission(permission)
  }

  /**
   * 检查是否有所有权限
   */
  function hasAllPermissions(permissions: string[]): boolean {
    return permissions.every(p => authStore.hasPermission(p))
  }

  return {
    hasPermission,
    hasAllPermissions
  }
}

// 权限指令
// directives/permission.ts

import type { Directive } from 'vue'
import { useAuthStore } from '@/stores/auth'

export const vPermission: Directive<HTMLElement, string | string[]> = {
  mounted(el, binding) {
    const authStore = useAuthStore()
    const permission = binding.value

    const hasPermission = Array.isArray(permission)
      ? permission.some(p => authStore.hasPermission(p))
      : authStore.hasPermission(permission)

    if (!hasPermission) {
      el.parentNode?.removeChild(el)
    }
  }
}

// 使用示例
// <el-button v-permission="'user:write'">创建用户</el-button>
// <el-button v-permission="['user:delete', 'user:write']">操作</el-button>
```

---

## 七、组件规范

### 7.1 组件命名

| 类型 | 命名规则 | 示例 |
|------|----------|------|
| **页面组件** | PascalCase 或 kebab-case | `UserList.vue` / `user-list.vue` |
| **通用组件** | App/Base 前缀 | `AppHeader.vue`, `BaseButton.vue` |
| **业务组件** | 业务前缀 | `SchoolCard.vue`, `PolicyItem.vue` |
| **布局组件** | Layout 后缀 | `MainLayout.vue`, `BlankLayout.vue` |

### 7.2 组件结构

```vue
<!-- components/common/Pagination.vue -->

<template>
  <div class="pagination-wrapper">
    <span class="pagination-total">共 {{ total }} 条记录</span>
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      :page-sizes="pageSizes"
      :layout="layout"
      background
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

// Props
interface Props {
  page: number
  size: number
  total: number
  pageSizes?: number[]
  layout?: string
}

const props = withDefaults(defineProps<Props>(), {
  pageSizes: () => [10, 20, 50, 100],
  layout: 'total, sizes, prev, pager, next, jumper'
})

// Emits
const emit = defineEmits<{
  'update:page': [value: number]
  'update:size': [value: number]
  'change': [page: number, size: number]
}>()

// Computed
const currentPage = computed({
  get: () => props.page,
  set: (val) => emit('update:page', val)
})

const pageSize = computed({
  get: () => props.size,
  set: (val) => emit('update:size', val)
})

// Methods
function handleSizeChange(val: number) {
  emit('update:size', val)
  emit('update:page', 1)
  emit('change', 1, val)
}

function handleCurrentChange(val: number) {
  emit('update:page', val)
  emit('change', val, props.size)
}
</script>

<style scoped>
.pagination-wrapper {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--spacing-4) 0;
}

.pagination-total {
  color: var(--neutral-600);
  font-size: var(--text-sm);
}
</style>
```

### 7.3 表格页面模板

```vue
<!-- views/user/list.vue -->

<template>
  <PageContainer title="管理员列表">
    <!-- 搜索表单 -->
    <SearchForm>
      <el-form :model="queryParams" inline>
        <el-form-item label="关键词">
          <el-input
            v-model="queryParams.keyword"
            placeholder="账号/手机号"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="所属学校">
          <SchoolSelect v-model="queryParams.schoolId" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </SearchForm>

    <!-- 操作栏 -->
    <div class="table-toolbar">
      <el-button
        v-permission="'user:write'"
        type="primary"
        :icon="Plus"
        @click="handleCreate"
      >
        添加管理员
      </el-button>
    </div>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column prop="username" label="账号" min-width="120" />
      <el-table-column prop="phone" label="手机号" width="120" />
      <el-table-column prop="school.name" label="所属学校" min-width="150" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" width="180">
        <template #default="{ row }">
          {{ formatDate(row.createdAt) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button
            v-permission="'user:write'"
            type="primary"
            link
            @click="handleEdit(row)"
          >
            编辑
          </el-button>
          <el-button
            v-permission="'user:delete'"
            type="danger"
            link
            @click="handleDelete(row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <Pagination
      v-model:page="queryParams.page"
      v-model:size="queryParams.size"
      :total="total"
      @change="fetchData"
    />
  </PageContainer>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import { getUserList, deleteUser } from '@/api/user'
import { formatDate } from '@/utils/format'
import type { User } from '@/types/model'

// 查询参数
const queryParams = reactive({
  keyword: '',
  schoolId: null as number | null,
  page: 1,
  size: 10
})

// 状态
const loading = ref(false)
const tableData = ref<User[]>([])
const total = ref(0)

// 获取数据
async function fetchData() {
  loading.value = true
  try {
    const res = await getUserList(queryParams)
    tableData.value = res.items
    total.value = res.pagination.total
  } finally {
    loading.value = false
  }
}

// 搜索
function handleSearch() {
  queryParams.page = 1
  fetchData()
}

// 重置
function handleReset() {
  queryParams.keyword = ''
  queryParams.schoolId = null
  queryParams.page = 1
  fetchData()
}

// 创建
function handleCreate() {
  // router.push('/user/create')
}

// 编辑
function handleEdit(row: User) {
  // router.push(`/user/${row.id}/edit`)
}

// 删除
async function handleDelete(row: User) {
  await ElMessageBox.confirm(`确定删除管理员「${row.username}」吗？`, '提示', {
    type: 'warning'
  })
  await deleteUser(row.id)
  ElMessage.success('删除成功')
  fetchData()
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.table-toolbar {
  margin-bottom: var(--spacing-4);
}
</style>
```

---

## 八、代码规范

### 8.1 ESLint 配置

```javascript
// .eslintrc.cjs

module.exports = {
  root: true,
  env: {
    browser: true,
    node: true,
    es2022: true
  },
  extends: [
    'eslint:recommended',
    'plugin:vue/vue3-recommended',
    'plugin:@typescript-eslint/recommended',
    'prettier'
  ],
  parser: 'vue-eslint-parser',
  parserOptions: {
    parser: '@typescript-eslint/parser',
    ecmaVersion: 2022,
    sourceType: 'module'
  },
  rules: {
    // Vue
    'vue/multi-word-component-names': 'off',
    'vue/no-v-html': 'warn',
    'vue/require-default-prop': 'off',
    'vue/component-tags-order': ['error', {
      order: ['template', 'script', 'style']
    }],

    // TypeScript
    '@typescript-eslint/no-explicit-any': 'warn',
    '@typescript-eslint/no-unused-vars': ['error', {
      argsIgnorePattern: '^_'
    }],

    // General
    'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off'
  }
}
```

### 8.2 Prettier 配置

```json
// .prettierrc

{
  "semi": false,
  "singleQuote": true,
  "tabWidth": 2,
  "trailingComma": "none",
  "printWidth": 100,
  "bracketSpacing": true,
  "arrowParens": "avoid",
  "endOfLine": "lf",
  "vueIndentScriptAndStyle": false
}
```

### 8.3 命名规范

| 分类 | 命名规则 | 示例 |
|------|----------|------|
| **文件夹** | kebab-case | `user-management/` |
| **组件文件** | PascalCase | `UserList.vue` |
| **TS/JS 文件** | camelCase 或 kebab-case | `useAuth.ts` / `request.ts` |
| **CSS 类名** | kebab-case / BEM | `page-container`, `card__title` |
| **变量** | camelCase | `userName`, `isLoading` |
| **常量** | UPPER_SNAKE_CASE | `API_BASE_URL` |
| **组件 Props** | camelCase | `pageSize`, `showHeader` |
| **事件** | kebab-case | `@update:page`, `@item-click` |
| **Store** | use + 名称 + Store | `useAuthStore` |
| **Composable** | use + 功能 | `useTable`, `usePagination` |
| **类型** | PascalCase | `UserInfo`, `ApiResponse` |

### 8.4 注释规范

```typescript
/**
 * 获取用户列表
 * @param params - 查询参数
 * @returns 分页用户列表
 */
export function getUserList(params: UserQuery): Promise<PageResult<User>> {
  return get('/api/v1/users', params)
}

// 单行注释用于解释复杂逻辑
const filteredUsers = users.filter(user => {
  // 排除已删除和禁用的用户
  return !user.isDeleted && user.status === 1
})
```

---

## 九、性能优化

### 9.1 代码分割

```typescript
// router/routes.ts

// 路由懒加载
const routes = [
  {
    path: '/dashboard',
    component: () => import('@/views/dashboard/index.vue')
  },
  {
    path: '/user',
    component: () => import('@/views/user/list.vue')
  }
]
```

### 9.2 图片优化

```vue
<!-- 使用 loading="lazy" -->
<img
  :src="imageUrl"
  :alt="title"
  loading="lazy"
  @error="handleImageError"
/>

<!-- Nuxt 使用 nuxt-img -->
<NuxtImg
  :src="imageUrl"
  :alt="title"
  format="webp"
  quality="80"
  loading="lazy"
/>
```

### 9.3 列表虚拟滚动

```vue
<!-- 大列表使用虚拟滚动 -->
<el-table-v2
  :columns="columns"
  :data="tableData"
  :width="tableWidth"
  :height="tableHeight"
  :row-height="50"
/>
```

### 9.4 性能目标

| 指标 | 目标值 | 说明 |
|------|--------|------|
| **首屏加载** | < 3s | 100Mbps 网络 |
| **交互响应** | < 100ms | 用户操作反馈 |
| **LCP** | < 2.5s | 最大内容绘制 |
| **FID** | < 100ms | 首次输入延迟 |
| **CLS** | < 0.1 | 累积布局偏移 |

---

## 十、环境配置

### 10.1 环境变量

```bash
# .env.development

VITE_APP_TITLE=河南城乡学校共同体发展平台
VITE_API_BASE_URL=http://localhost:8080
VITE_UPLOAD_URL=http://localhost:8080/api/v1/files/upload

# .env.production

VITE_APP_TITLE=河南城乡学校共同体发展平台
VITE_API_BASE_URL=https://api.example.com
VITE_UPLOAD_URL=https://api.example.com/api/v1/files/upload
```

### 10.2 Vite 配置

```typescript
// vite.config.ts

import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd())

  return {
    plugins: [vue()],
    resolve: {
      alias: {
        '@': resolve(__dirname, 'src')
      }
    },
    server: {
      port: 3000,
      proxy: {
        '/api': {
          target: env.VITE_API_BASE_URL,
          changeOrigin: true
        }
      }
    },
    build: {
      target: 'es2020',
      sourcemap: mode !== 'production',
      rollupOptions: {
        output: {
          manualChunks: {
            'element-plus': ['element-plus'],
            'vue-vendor': ['vue', 'vue-router', 'pinia']
          }
        }
      }
    }
  }
})
```

---

## 附录

### 附录 A: 参考文档

| 文档 | 链接 |
|------|------|
| Vue 3 官方文档 | https://vuejs.org |
| Nuxt 3 官方文档 | https://nuxt.com |
| Element Plus 官方文档 | https://element-plus.org |
| Pinia 官方文档 | https://pinia.vuejs.org |
| VueUse 官方文档 | https://vueuse.org |
| WangEditor 官方文档 | https://www.wangeditor.com |
| UI 设计规范 | docs/ui-design-specification.md |
| 系统架构文档 | docs/architecture.md |

### 附录 B: 开发工具

**VS Code 推荐插件**:
- Vue - Official (Volar)
- TypeScript Vue Plugin
- ESLint
- Prettier
- Auto Close Tag
- Path Intellisense

**浏览器插件**:
- Vue.js Devtools

---

| 版本 | 日期 | 修订人 | 修订内容 |
|------|------|--------|----------|
| 1.0.0 | 2026-02-06 | Architect Agent | 初始版本，与 architecture.md v1.2.0 对齐 |
