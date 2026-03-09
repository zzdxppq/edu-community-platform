<template>
  <header class="header">
    <div class="header-container">
      <div class="header-logo">
        <NuxtLink to="/">河南城乡学校共同体发展平台</NuxtLink>
      </div>
      <nav class="header-nav">
        <template v-for="item in navItems" :key="item.path">
          <div
            v-if="item.children"
            class="nav-item-dropdown"
            :class="{ active: isActive(item) }"
          >
            <NuxtLink :to="item.path" class="nav-link nav-link-arrow">
              {{ item.label }}
            </NuxtLink>
            <div class="dropdown-menu">
              <NuxtLink
                v-for="child in item.children"
                :key="child.path"
                :to="child.path"
                class="dropdown-item"
              >
                {{ child.label }}
              </NuxtLink>
            </div>
          </div>
          <NuxtLink
            v-else
            :to="item.path"
            class="nav-link"
            :class="{ active: isActive(item) }"
          >
            {{ item.label }}
          </NuxtLink>
        </template>
        <a :href="`${adminUrl}/login`" target="_blank" class="nav-link">管理员登录</a>
      </nav>
    </div>
  </header>
</template>

<script setup lang="ts">
interface NavItem {
  label: string
  path: string
  external?: boolean
  target?: string
  children?: {
    label: string
    path: string
  }[]
}

const route = useRoute()
const runtimeConfig = useRuntimeConfig()
const adminUrl = runtimeConfig.public.adminUrl as string

const navItems: NavItem[] = [
  { label: '首页', path: '/' },
  { label: '项目介绍', path: '/intro' },
  {
    label: '政策文件',
    path: '/policy',
    children: [
      { label: '国家政策', path: '/policy?category=national' },
      { label: '省级政策', path: '/policy?category=provincial' },
    ],
  },
  { label: '示范校共体', path: '/schools' },
  {
    label: '资源共享',
    path: '/resources',
    children: [
      { label: '省外经验', path: '/resources?category=outside' },
      { label: '省内经验', path: '/resources?category=inside' },
      { label: '研究文献', path: '/resources?category=research' },
    ],
  },
]

function isActive(item: NavItem): boolean {
  if (item.path === '/') {
    return route.path === '/'
  }
  return route.path.startsWith(item.path)
}
</script>

<style scoped>
.header {
  background: var(--primary-800);
  height: var(--header-height);
  position: fixed;
  top: 0;
  width: 100%;
  z-index: 1000;
  box-shadow: var(--shadow-header);
}

.header-container {
  max-width: var(--container-max-width);
  margin: 0 auto;
  padding: 0 var(--spacing-5);
  display: flex;
  align-items: center;
  height: 100%;
  justify-content: space-between;
}

.header-logo a {
  color: var(--neutral-50);
  font-size: var(--text-xl);
  font-weight: var(--font-bold);
  text-decoration: none;
}

.header-nav {
  display: flex;
  align-items: center;
}

.nav-link {
  color: var(--neutral-50);
  font-size: var(--text-lg);
  padding: 0 var(--spacing-5);
  line-height: var(--header-height);
  text-decoration: none;
  transition: background var(--transition-normal);
  display: block;
}

.nav-link:hover,
.nav-link.active {
  background: var(--primary-900);
}

/* Dropdown wrapper */
.nav-item-dropdown {
  position: relative;
}

.nav-item-dropdown.active > .nav-link {
  background: var(--primary-900);
}

.nav-item-dropdown:hover > .nav-link {
  background: var(--primary-900);
}

/* Arrow indicator */
.nav-link-arrow::after {
  content: '▼';
  font-size: 10px;
  margin-left: var(--spacing-1);
  display: inline-block;
  transition: transform var(--transition-normal);
}

.nav-item-dropdown:hover .nav-link-arrow::after {
  transform: rotate(180deg);
}

/* Dropdown menu */
.dropdown-menu {
  display: none;
  position: absolute;
  top: 100%;
  left: 0;
  min-width: 160px;
  background: var(--neutral-50);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  z-index: 1001;
}

.nav-item-dropdown:hover .dropdown-menu {
  display: block;
}

.dropdown-item {
  display: block;
  padding: var(--spacing-3) var(--spacing-5);
  color: var(--neutral-800);
  text-decoration: none;
  font-size: var(--text-base);
  transition: background var(--transition-normal), color var(--transition-normal);
  border-bottom: 1px solid var(--neutral-300);
}

.dropdown-item:last-child {
  border-bottom: none;
}

.dropdown-item:hover {
  background: var(--neutral-100);
  color: var(--primary-800);
}

/* Responsive - narrow screens */
@media (max-width: 1280px) {
  .nav-link {
    padding: 0 var(--spacing-3);
  }
}
</style>
