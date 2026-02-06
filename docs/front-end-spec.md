# 前端开发规范文档

# 河南城乡学校共同体发展平台

---

## 文档信息

| 项目 | 内容 |
|------|------|
| **版本** | v1.0 |
| **基于** | ui-design-specification.md |
| **技术栈** | HTML5 + CSS3 + Vanilla JavaScript |
| **创建日期** | 2025-02-06 |

---

## 一、项目概述

### 1.1 技术选型

| 类型 | 技术方案 | 说明 |
|------|----------|------|
| **标记语言** | HTML5 | 语义化标签 |
| **样式方案** | CSS3 + CSS Variables | 原生CSS变量系统 |
| **脚本语言** | Vanilla JavaScript (ES6+) | 原生JS，无框架依赖 |
| **富文本编辑器** | WangEditor 5.x | 后台内容编辑 |
| **图标方案** | Emoji + 自定义SVG | 轻量化方案 |

### 1.2 浏览器兼容性

| 浏览器 | 最低版本 | 说明 |
|--------|----------|------|
| Chrome | 80+ | 主要支持 |
| Firefox | 75+ | 完全支持 |
| Safari | 13+ | 完全支持 |
| Edge | 80+ | 完全支持 |
| IE | 不支持 | 不做兼容 |

---

## 二、项目结构

### 2.1 目录结构

```
demo/
├── index.html                 # 首页
├── css/
│   ├── variables.css          # CSS变量定义（核心）
│   ├── styles.css             # 主样式文件
│   └── admin-modal.css        # 后台弹窗样式
├── js/
│   └── main.js                # 主脚本文件
├── pages/                     # 官网页面
│   ├── about.html             # 项目介绍
│   ├── news.html              # 新闻列表
│   ├── news-detail.html       # 新闻详情
│   ├── policy.html            # 政策文件列表
│   ├── policy-detail.html     # 政策详情
│   ├── resources.html         # 资源共享列表
│   ├── resource-detail.html   # 资源详情
│   ├── schools.html           # 示范校共体列表
│   ├── school-detail.html     # 示范校详情
│   ├── activity-list.html     # 活动列表
│   ├── activity-detail.html   # 活动详情
│   ├── report-list.html       # 月报列表
│   ├── report-detail.html     # 月报详情
│   ├── search.html            # 搜索结果页
│   └── admin-login.html       # 管理员登录
└── admin/                     # 超级管理后台
    ├── index.html             # 工作台
    ├── users.html             # 学校管理员
    ├── carousel.html          # 轮播图管理
    ├── about.html             # 项目介绍
    ├── news.html              # 新闻资讯
    ├── policy.html            # 政策文件
    ├── resources.html         # 资源共享
    ├── reports.html           # 月报管理
    ├── schools.html           # 示范校列表
    ├── navigation.html        # 导航管理
    └── settings.html          # 基础配置
```

### 2.2 命名规范

#### 文件命名

```
# 页面文件
{功能名}.html                    # 列表页
{功能名}-detail.html             # 详情页
{功能名}-list.html               # 列表页（备选）

# 样式文件
{模块名}.css                     # 模块样式
{模块名}-{子模块}.css            # 子模块样式

# 脚本文件
{功能名}.js                      # 功能脚本
```

#### CSS类命名（BEM变体）

```css
/* 块 (Block) */
.card { }
.sidebar { }
.nav { }

/* 元素 (Element) - 使用连字符 */
.card-header { }
.card-body { }
.card-footer { }

/* 修饰符 (Modifier) - 使用连字符 */
.btn-primary { }
.btn-outline { }
.nav-link.active { }

/* 状态类 */
.is-active { }
.is-disabled { }
.is-loading { }

/* 工具类 */
.text-center { }
.mt-4 { }
.hidden { }
```

---

## 三、CSS架构

### 3.1 CSS变量系统

所有设计Token统一在 `variables.css` 中定义：

```css
:root {
  /* ===== 主色 Primary ===== */
  /* 主色调: #1283E9 */
  --primary-900: #0A5CA8;      /* 深蓝 - hover状态 */
  --primary-800: #1283E9;      /* 主蓝 - 导航栏/标题/按钮 */
  --primary-700: #3A9BF0;      /* 中蓝 - 链接悬停 */
  --primary-600: #5FB0F5;      /* 浅蓝 - 选中状态 */

  /* ===== 强调色 Accent ===== */
  --accent-700: #0D6EBF;
  --accent-600: #1283E9;
  --accent-500: #4A9EEE;
  --accent-400: #7CB8F3;

  /* ===== 辅色 Secondary (暖色点缀) ===== */
  --secondary-700: #b85c1a;
  --secondary-600: #e07628;
  --secondary-500: #f08c3d;
  --secondary-400: #f5a862;

  /* ===== 中性色 Neutral ===== */
  --neutral-900: #1a1a1a;      /* 主标题 */
  --neutral-800: #333333;      /* 正文 */
  --neutral-700: #555555;      /* 副标题 */
  --neutral-600: #666666;      /* 辅助文本 */
  --neutral-500: #999999;      /* 占位符 */
  --neutral-400: #cccccc;      /* 分隔线 */
  --neutral-300: #e5e5e5;      /* 边框 */
  --neutral-200: #f2f2f2;      /* 背景灰 */
  --neutral-100: #f8f8f8;      /* 浅背景 */
  --neutral-50: #ffffff;       /* 白色 */

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

  /* ===== 动画 ===== */
  --duration-fast: 150ms;
  --duration-normal: 300ms;
  --duration-slow: 500ms;

  /* ===== 阴影 ===== */
  --shadow-sm: 0 1px 2px rgba(0, 0, 0, 0.05);
  --shadow-md: 0 4px 6px rgba(0, 0, 0, 0.1);
  --shadow-lg: 0 10px 15px rgba(0, 0, 0, 0.1);
  --shadow-xl: 0 20px 25px rgba(0, 0, 0, 0.15);

  /* ===== 圆角 ===== */
  --radius-sm: 2px;
  --radius-md: 4px;
  --radius-lg: 8px;
  --radius-xl: 12px;
}
```

### 3.2 样式文件结构

`styles.css` 按以下顺序组织：

```css
/* 1. 导入变量 */
@import url('variables.css');

/* 2. 重置与基础样式 */
/* Reset & Base */

/* 3. 布局组件 */
/* Layout */
.container { }
.page-wrapper { }
.main-content { }

/* 4. 导航组件 */
/* Header & Navigation */
.header { }
.nav { }
.nav-link { }
.nav-dropdown { }

/* 5. 内容组件 */
/* Content Components */
.section { }
.card { }
.list-item { }

/* 6. 表单组件 */
/* Forms */
.form-input { }
.btn { }

/* 7. 页脚 */
/* Footer */
.footer { }

/* 8. 后台布局 */
/* Admin Layout */
.admin-wrapper { }
.admin-sidebar { }
.admin-main { }

/* 9. 工具类 */
/* Utilities */
.text-center { }
.mt-4 { }

/* 10. 响应式 */
/* Responsive */
@media (max-width: 1024px) { }
@media (max-width: 768px) { }
```

---

## 四、核心组件实现

### 4.1 导航栏 (Header)

```html
<header class="header">
  <div class="container">
    <a href="index.html" class="logo">
      <span class="logo-icon">豫</span>
      <span>河南城乡学校共同体发展</span>
    </a>
    <nav class="nav">
      <a href="index.html" class="nav-link active">首页</a>
      <a href="pages/about.html" class="nav-link">项目介绍</a>
      <a href="pages/policy.html" class="nav-link">政策文件</a>
      <a href="pages/schools.html" class="nav-link">示范校共体</a>
      <!-- 带下拉菜单的导航项 -->
      <div class="nav-item">
        <a href="pages/resources.html" class="nav-link">资源共享</a>
        <div class="nav-dropdown">
          <a href="pages/resources.html?type=provincial" class="nav-dropdown-item">省内经验</a>
          <a href="pages/resources.html?type=external" class="nav-dropdown-item">省外经验</a>
        </div>
      </div>
    </nav>
    <div class="nav-search">
      <input type="text" class="search-input" placeholder="搜索...">
      <button class="search-btn">搜索</button>
    </div>
    <a href="#" class="nav-login">登录</a>
  </div>
</header>
```

**关键样式：**

```css
.header {
  background: var(--primary-800);    /* #1283E9 */
  height: 70px;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.nav-link {
  color: var(--neutral-50);
  font-size: 16px;
  padding: 0 14px;
  line-height: 70px;
  transition: background var(--duration-normal);
}

.nav-link:hover,
.nav-link.active {
  background: var(--primary-900);
}
```

### 4.2 二级下拉菜单

```css
/* 导航项容器 */
.nav-item {
  position: relative;
  display: flex;
  align-items: center;
}

/* 下拉箭头 */
.nav-item > .nav-link::after {
  content: '';
  width: 0;
  height: 0;
  border-left: 4px solid transparent;
  border-right: 4px solid transparent;
  border-top: 5px solid rgba(255, 255, 255, 0.8);
  margin-left: 6px;
  transition: transform var(--duration-fast);
}

.nav-item:hover > .nav-link::after {
  transform: rotate(180deg);
}

/* 下拉菜单 */
.nav-dropdown {
  position: absolute;
  top: 100%;
  left: 50%;
  transform: translateX(-50%) translateY(10px);
  min-width: 140px;
  background: var(--neutral-50);
  border-top: 3px solid var(--primary-800);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
  opacity: 0;
  visibility: hidden;
  transition: all 0.25s ease;
  z-index: 1001;
}

/* 三角箭头 */
.nav-dropdown::before {
  content: '';
  position: absolute;
  top: -10px;
  left: 50%;
  transform: translateX(-50%);
  border-left: 8px solid transparent;
  border-right: 8px solid transparent;
  border-bottom: 7px solid var(--primary-800);
}

.nav-item:hover .nav-dropdown {
  opacity: 1;
  visibility: visible;
  transform: translateX(-50%) translateY(0);
}

/* 下拉菜单项 */
.nav-dropdown-item {
  display: block;
  padding: 14px 24px;
  color: var(--neutral-700);
  font-size: 14px;
  text-align: center;
  border-bottom: 1px solid var(--neutral-200);
  position: relative;
}

.nav-dropdown-item::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  width: 3px;
  height: 100%;
  background: var(--primary-800);
  transform: scaleY(0);
  transition: transform 0.2s ease;
}

.nav-dropdown-item:hover {
  background: var(--primary-50);
  color: var(--primary-800);
  padding-left: 28px;
}

.nav-dropdown-item:hover::before {
  transform: scaleY(1);
}
```

### 4.3 页脚 (Footer)

```html
<footer class="footer">
  <div class="container">
    <div class="footer-bottom">
      <p style="display: flex; justify-content: center; align-items: center; gap: 16px; flex-wrap: wrap; margin: 0; font-size: 14px; color: rgba(255,255,255,0.9);">
        <span>河南城乡学校共同体发展平台</span>
        <span style="color: rgba(255,255,255,0.5);">|</span>
        <span>地址：河南省郑州市金水区XX路XX号</span>
        <span style="color: rgba(255,255,255,0.5);">|</span>
        <span>邮箱：contact@example.edu.cn</span>
        <span style="color: rgba(255,255,255,0.5);">|</span>
        <span>&copy; 2025 河南城乡学校共同体发展平台 版权所有</span>
      </p>
    </div>
  </div>
</footer>
```

**关键样式：**

```css
.footer {
  background: var(--primary-800);    /* 与导航栏一致 */
  color: rgba(255,255,255,0.8);
  padding: 0;
  margin-top: auto;
}

.footer-bottom {
  height: 70px;                       /* 与导航栏高度一致 */
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: var(--text-sm);
}
```

### 4.4 按钮组件

```html
<button class="btn btn-primary">主要按钮</button>
<button class="btn btn-outline">轮廓按钮</button>
<button class="btn btn-danger">危险按钮</button>
```

```css
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 10px 24px;
  font-size: var(--text-base);
  font-weight: var(--font-medium);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all var(--duration-fast);
  border: none;
}

.btn-primary {
  background: var(--primary-800);
  color: var(--neutral-50);
}

.btn-primary:hover {
  background: var(--primary-900);
}

.btn-outline {
  background: transparent;
  color: var(--primary-800);
  border: 1px solid var(--primary-800);
}

.btn-outline:hover {
  background: var(--primary-800);
  color: var(--neutral-50);
}

.btn-danger {
  background: transparent;
  border: 1px solid var(--danger);
  color: var(--danger);
}

.btn-danger:hover {
  background: var(--danger);
  color: var(--neutral-50);
}
```

### 4.5 表单组件

```html
<div class="form-group">
  <label class="form-label">字段标签 <span style="color: red;">*</span></label>
  <input type="text" class="form-input" placeholder="请输入内容">
</div>
```

```css
.form-group {
  margin-bottom: var(--spacing-5);
}

.form-label {
  display: block;
  font-size: var(--text-base);
  font-weight: var(--font-medium);
  color: var(--neutral-800);
  margin-bottom: var(--spacing-2);
}

.form-input {
  width: 100%;
  padding: 12px 16px;
  font-size: var(--text-base);
  border: 1px solid var(--neutral-400);
  border-radius: var(--radius-md);
  transition: border-color var(--duration-fast), box-shadow var(--duration-fast);
}

.form-input:focus {
  outline: none;
  border-color: var(--primary-800);
  box-shadow: 0 0 0 3px rgba(18, 131, 233, 0.15);
}
```

### 4.6 卡片组件

```html
<div class="content-card">
  <div class="section-header">
    <h2 class="section-title">模块标题</h2>
    <a href="#" class="section-more">更多 &gt;</a>
  </div>
  <!-- 内容区域 -->
</div>
```

```css
.content-card {
  background: var(--neutral-50);
  border-radius: var(--radius-md);
  padding: var(--spacing-6);
  box-shadow: var(--shadow-sm);
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: var(--spacing-6);
  padding-bottom: var(--spacing-4);
  border-bottom: 2px solid var(--primary-800);
}

.section-title {
  font-size: var(--text-3xl);
  font-weight: var(--font-bold);
  color: var(--neutral-900);
  display: flex;
  align-items: center;
}

.section-title::before {
  content: '';
  display: inline-block;
  width: 4px;
  height: 24px;
  background: var(--primary-800);
  margin-right: 12px;
}
```

### 4.7 分页组件

```html
<div class="pagination">
  <span class="page-item disabled">&lt;</span>
  <span class="page-item active">1</span>
  <span class="page-item">2</span>
  <span class="page-item">3</span>
  <span class="page-item">&gt;</span>
</div>
```

```css
.pagination {
  display: flex;
  justify-content: center;
  gap: var(--spacing-2);
  margin-top: var(--spacing-8);
}

.page-item {
  min-width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid var(--neutral-400);
  border-radius: var(--radius-md);
  font-size: var(--text-base);
  color: var(--neutral-800);
  cursor: pointer;
  transition: all var(--duration-fast);
  background: var(--neutral-50);
}

.page-item:hover {
  border-color: var(--primary-800);
  color: var(--primary-800);
}

.page-item.active {
  background: var(--primary-800);
  border-color: var(--primary-800);
  color: var(--neutral-50);
}

.page-item.disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
```

---

## 五、后台管理界面

### 5.1 后台布局结构

```html
<div class="admin-wrapper">
  <!-- 侧边栏 -->
  <aside class="admin-sidebar">
    <div class="admin-logo">
      <div class="admin-logo-icon">豫</div>
      <div class="admin-logo-text">城乡学校共同体<br>管理后台</div>
    </div>
    <nav class="admin-menu">
      <div class="admin-menu-group">
        <a href="index.html" class="admin-menu-item active">
          <span class="admin-menu-icon">📊</span>
          工作台
        </a>
      </div>
      <div class="admin-menu-group">
        <div class="admin-menu-title">内容管理</div>
        <a href="news.html" class="admin-menu-item">
          <span class="admin-menu-icon">📰</span>
          新闻资讯
        </a>
        <!-- 更多菜单项 -->
      </div>
    </nav>
  </aside>

  <!-- 主内容区 -->
  <main class="admin-main">
    <header class="admin-header">
      <div class="admin-breadcrumb">系统设置 / 基础配置</div>
      <div class="admin-user">
        <span>欢迎，超级管理员</span>
        <div class="admin-avatar">管</div>
        <a href="#">退出</a>
      </div>
    </header>
    <div class="admin-content">
      <!-- 页面内容 -->
    </div>
  </main>
</div>
```

### 5.2 后台样式规范

```css
.admin-wrapper {
  display: flex;
  min-height: 100vh;
}

.admin-sidebar {
  width: 240px;
  background: var(--primary-900);
  color: var(--neutral-50);
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  overflow-y: auto;
}

.admin-main {
  flex: 1;
  margin-left: 240px;
  background: var(--neutral-200);
  min-height: 100vh;
}

.admin-header {
  background: var(--neutral-50);
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.admin-content {
  padding: 24px;
}

.admin-menu-item {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  color: rgba(255,255,255,0.8);
  font-size: 14px;
  transition: all 0.2s;
  border-left: 3px solid transparent;
}

.admin-menu-item:hover {
  background: rgba(255,255,255,0.1);
  color: var(--neutral-50);
}

.admin-menu-item.active {
  background: rgba(255,255,255,0.15);
  border-left-color: var(--accent-400);
  color: var(--neutral-50);
}
```

### 5.3 后台表格

```css
.admin-table {
  width: 100%;
  border-collapse: collapse;
}

.admin-table th,
.admin-table td {
  padding: 12px 16px;
  text-align: left;
  border-bottom: 1px solid var(--neutral-300);
}

.admin-table th {
  background: var(--neutral-100);
  font-weight: 600;
  font-size: 13px;
  color: var(--neutral-700);
}

.admin-table td {
  font-size: 14px;
  color: var(--neutral-800);
}

.admin-table tr:hover td {
  background: var(--neutral-100);
}
```

### 5.4 后台状态标签

```css
.admin-badge {
  display: inline-block;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.admin-badge-success {
  background: rgba(40, 167, 69, 0.1);
  color: var(--success);
}

.admin-badge-warning {
  background: rgba(255, 193, 7, 0.1);
  color: #d39e00;
}

.admin-badge-info {
  background: rgba(23, 162, 184, 0.1);
  color: var(--info);
}

.admin-badge-error {
  background: rgba(220, 53, 69, 0.1);
  color: var(--danger);
}
```

---

## 六、响应式设计

### 6.1 断点定义

```css
/* 平板竖屏 */
@media (max-width: 768px) { }

/* 平板横屏 */
@media (max-width: 1024px) { }

/* 桌面端 - 主设计尺寸 */
@media (max-width: 1200px) { }

/* 大屏桌面 */
@media (max-width: 1400px) { }
```

### 6.2 关键响应式规则

```css
/* 1024px以下：导航栏自适应 */
@media (max-width: 1024px) {
  .header {
    height: auto;
    min-height: 60px;
  }

  .header .container {
    flex-wrap: wrap;
    padding: 10px 20px;
  }

  .logo {
    width: 100%;
    justify-content: center;
    margin-bottom: 10px;
  }

  .nav {
    width: 100%;
    justify-content: center;
    margin-left: 0;
  }

  .nav-search {
    display: none;
  }

  .main-content {
    padding-top: 120px;
  }

  /* 侧边栏变为全宽 */
  .page-layout {
    flex-direction: column;
  }

  .sidebar {
    width: 100%;
  }
}

/* 768px以下：移动端适配 */
@media (max-width: 768px) {
  .nav-link {
    padding: 0 8px;
    font-size: var(--text-xs);
  }

  .school-grid {
    grid-template-columns: 1fr;
  }

  .home-grid {
    grid-template-columns: 1fr;
  }
}
```

---

## 七、JavaScript规范

### 7.1 通用函数

```javascript
// Toast提示
function showToast(message) {
  const toast = document.createElement('div');
  toast.className = 'toast';
  toast.textContent = message;
  document.body.appendChild(toast);
  setTimeout(() => toast.classList.add('show'), 10);
  setTimeout(() => {
    toast.classList.remove('show');
    setTimeout(() => toast.remove(), 300);
  }, 2000);
}

// 模态框控制
function openModal(modalId) {
  document.getElementById(modalId).classList.add('active');
}

function closeModal(modalId) {
  document.getElementById(modalId).classList.remove('active');
}

// 确认对话框
function confirmAction(message, callback) {
  if (confirm(message)) {
    callback();
  }
}
```

### 7.2 Toast样式

```css
.toast {
  position: fixed;
  bottom: 30px;
  left: 50%;
  transform: translateX(-50%) translateY(20px);
  background: rgba(0, 0, 0, 0.8);
  color: #fff;
  padding: 12px 24px;
  border-radius: 4px;
  font-size: 14px;
  z-index: 9999;
  opacity: 0;
  transition: all 0.3s;
}

.toast.show {
  opacity: 1;
  transform: translateX(-50%) translateY(0);
}
```

---

## 八、WangEditor集成

### 8.1 引入方式

```html
<!-- 在需要富文本编辑的页面引入 -->
<link href="https://unpkg.com/@wangeditor/editor@latest/dist/css/style.css" rel="stylesheet">
<script src="https://unpkg.com/@wangeditor/editor@latest/dist/index.js"></script>
```

### 8.2 初始化配置

```javascript
// 创建编辑器
const editor = window.wangEditor.createEditor({
  selector: '#editor-container',
  config: {
    placeholder: '请输入内容...',
    MENU_CONF: {
      uploadImage: {
        server: '/api/upload',
        fieldName: 'file'
      }
    }
  }
});

// 创建工具栏
const toolbar = window.wangEditor.createToolbar({
  editor,
  selector: '#toolbar-container',
  config: {
    excludeKeys: ['group-video']
  }
});
```

### 8.3 编辑器容器样式

```css
#toolbar-container {
  border: 1px solid var(--neutral-300);
  border-bottom: none;
}

#editor-container {
  border: 1px solid var(--neutral-300);
  min-height: 400px;
}
```

---

## 九、性能优化

### 9.1 CSS优化

- 使用CSS变量减少重复定义
- 避免深层选择器嵌套（最多3层）
- 合理使用 `will-change` 优化动画性能
- 使用 `transform` 代替 `top/left` 做位移动画

### 9.2 图片优化

- 使用适当的图片格式（PNG透明/JPG照片）
- 设置图片的 width 和 height 属性避免布局偏移
- 考虑使用 loading="lazy" 实现懒加载

### 9.3 JavaScript优化

- 事件委托减少事件监听器数量
- 使用 `requestAnimationFrame` 优化动画
- 避免同步的 DOM 操作

---

## 十、开发规范

### 10.1 代码格式

- 缩进：2空格
- 属性引号：双引号
- CSS属性顺序：定位 > 盒模型 > 排版 > 视觉 > 其他

### 10.2 注释规范

```css
/* ===== 模块标题 ===== */

/* 组件说明 */
.component { }

/* TODO: 待优化项 */
```

```html
<!-- 模块开始: 导航栏 -->
<header class="header">
  ...
</header>
<!-- 模块结束: 导航栏 -->
```

### 10.3 Git提交规范

```
feat: 新功能
fix: 修复bug
style: 样式调整
refactor: 重构
docs: 文档更新
chore: 构建/工具相关
```

---

## 附录：快速参考

### A. 颜色速查

| 用途 | 变量 | 色值 |
|------|------|------|
| 主蓝色 | `--primary-800` | #1283E9 |
| 深蓝色 | `--primary-900` | #0A5CA8 |
| 强调色 | `--accent-600` | #1283E9 |
| 主文字 | `--neutral-800` | #333333 |
| 辅助文字 | `--neutral-600` | #666666 |
| 边框色 | `--neutral-300` | #e5e5e5 |
| 背景色 | `--neutral-200` | #f2f2f2 |
| 白色 | `--neutral-50` | #ffffff |

### B. 间距速查

| 级别 | 变量 | 数值 |
|------|------|------|
| 紧凑 | `--spacing-2` | 8px |
| 标准 | `--spacing-4` | 16px |
| 宽松 | `--spacing-6` | 24px |
| 区块 | `--spacing-8` | 32px |
| 大区块 | `--spacing-12` | 48px |

### C. 字号速查

| 用途 | 变量 | 数值 |
|------|------|------|
| 页面标题 | `--text-4xl` | 28px |
| 模块标题 | `--text-3xl` | 24px |
| 卡片标题 | `--text-xl` | 18px |
| 正文 | `--text-base` | 14px |
| 辅助信息 | `--text-sm` | 13px |
| 标签 | `--text-xs` | 12px |

---

**文档结束**
