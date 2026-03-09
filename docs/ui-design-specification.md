# UI设计规范文档

# 河南城乡学校共同体发展平台

---

## 文档信息

| 项目 | 内容 |
|------|------|
| **版本** | v1.0 |
| **参考网站** | https://www.htu.edu.cn/jce/main.htm |
| **设计风格** | 学院风格、沉稳专业 |
| **创建日期** | 2025-XX-XX |

---

## 一、设计理念

### 1.1 设计原则

| 原则 | 说明 |
|------|------|
| **权威性** | 体现政府教育平台的官方性与公信力 |
| **易读性** | 清晰的信息层次，便于快速获取内容 |
| **稳重感** | 采用沉稳配色，展现教育行业专业形象 |
| **亲和力** | 暖色点缀，拉近与用户的心理距离 |

### 1.2 目标用户适配

- 35-50岁教育工作者为主要用户群体
- 优先考虑可读性和操作便捷性
- 避免过度设计，保持简洁明了

---

## 二、配色方案

### 2.1 主色系统

```css
:root {
  /* ===== 主色 Primary (蓝色系) ===== */
  --primary-900: #0A5DAD;      /* 深蓝 - 强调/hover状态 */
  --primary-800: #1283E9;      /* 主蓝 - 导航栏/标题/按钮 */
  --primary-700: #3594EC;      /* 中蓝 - 链接悬停 */
  --primary-600: #5AA8F0;      /* 浅蓝 - 选中状态 */

  /* ===== 强调色 Accent ===== */
  --accent-700: #0E6FCA;       /* 深强调 - 重要操作 */
  --accent-600: #2990E8;       /* 主强调 - 可点击元素/高亮 */
  --accent-500: #4FA6EE;       /* 中强调 - 悬停状态 */
  --accent-400: #7FC0F4;       /* 浅强调 - 辅助信息 */

  /* ===== 辅色 Secondary (暖色点缀) ===== */
  --secondary-700: #b85c1a;    /* 深橙 - 强调 */
  --secondary-600: #e07628;    /* 主橙 - 重要标签/提醒 */
  --secondary-500: #f08c3d;    /* 中橙 - 悬停 */
  --secondary-400: #f5a862;    /* 浅橙 - 装饰 */

  /* ===== 中性色 Neutral ===== */
  --neutral-900: #1a1a1a;      /* 纯黑 - 主标题 */
  --neutral-800: #333333;      /* 深灰 - 正文 */
  --neutral-700: #555555;      /* 中灰 - 副标题 */
  --neutral-600: #666666;      /* 灰色 - 辅助文本 */
  --neutral-500: #999999;      /* 浅灰 - 占位符 */
  --neutral-400: #cccccc;      /* 分隔线 */
  --neutral-300: #e5e5e5;      /* 边框 */
  --neutral-200: #f2f2f2;      /* 背景灰 */
  --neutral-100: #f8f8f8;      /* 浅背景 */
  --neutral-50: #ffffff;       /* 白色 */

  /* ===== 功能色 Functional ===== */
  --success: #28a745;          /* 成功/在线 */
  --warning: #ffc107;          /* 警告/待处理 */
  --danger: #dc3545;           /* 错误/删除 */
  --info: #17a2b8;             /* 信息/提示 */
}
```

### 2.2 配色应用场景

| 场景 | 颜色变量 | 色值 | 用途说明 |
|------|----------|------|----------|
| **导航栏背景** | `--primary-800` | #1283E9 | 顶部固定导航 |
| **导航栏文字** | `--neutral-50` | #FFFFFF | 白色文字 |
| **侧边栏标题背景** | `--primary-800` | #1283E9 | 分类栏目标题 |
| **主标题** | `--neutral-900` | #1A1A1A | 页面主标题 |
| **正文文字** | `--neutral-800` | #333333 | 段落文本 |
| **链接文字** | `--accent-600` | #2990E8 | 可点击链接 |
| **链接悬停** | `--primary-700` | #3594EC | hover状态 |
| **页面背景** | `--neutral-200` | #F2F2F2 | 整体页面背景 |
| **内容区背景** | `--neutral-50` | #FFFFFF | 白色内容卡片 |
| **分隔线** | `--neutral-400` | #CCCCCC | 虚线分隔符 |
| **日期/标签背景** | `--primary-800` | #1283E9 | 时间标签 |
| **按钮主色** | `--primary-800` | #1283E9 | 主要操作按钮 |
| **按钮强调** | `--accent-600` | #2990E8 | 强调操作按钮 |

### 2.3 配色对比示例

```
┌─────────────────────────────────────────────────────────────┐
│  [导航栏] 背景: #1283E9  文字: #FFFFFF                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌──────────────┐  ┌────────────────────────────────────┐  │
│  │ [侧边分类导航]│  │          [内容区域]                 │  │
│  │ 背景: #f2f2f2│  │          背景: #ffffff              │  │
│  │              │  │                                    │  │
│  │ ┌──────────┐ │  │  标题文字: #1a1a1a                 │  │
│  │ │栏目标题  │ │  │  正文文字: #333333                 │  │
│  │ │#1283E9   │ │  │  链接文字: #2990E8                 │  │
│  │ └──────────┘ │  │  日期文字: #666666                 │  │
│  │              │  │                                    │  │
│  │  • 子栏目    │  │  ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ (分隔线#ccc)   │  │
│  │  • 子栏目    │  │                                    │  │
│  └──────────────┘  └────────────────────────────────────┘  │
│                                                             │
├─────────────────────────────────────────────────────────────┤
│  [页脚] 背景: #1283E9  文字: #FFFFFF                         │
└─────────────────────────────────────────────────────────────┘
```

---

## 三、字体规范

### 3.1 字体家族

```css
:root {
  /* 主字体栈 */
  --font-primary: "Microsoft YaHei", "微软雅黑", "PingFang SC",
                  "Hiragino Sans GB", Arial, sans-serif;

  /* 标题字体栈 */
  --font-heading: "Microsoft YaHei", "微软雅黑", "STHeiti",
                  "SimHei", Arial, sans-serif;

  /* 等宽字体（代码/数据） */
  --font-mono: "Consolas", "Monaco", "Microsoft YaHei", monospace;
}
```

### 3.2 字号系统

| 层级 | 字号 | 行高 | 字重 | 应用场景 |
|------|------|------|------|----------|
| **H1** | 28px | 1.4 | 700 | 页面主标题 |
| **H2** | 24px | 1.4 | 700 | 模块标题 |
| **H3** | 20px | 1.5 | 600 | 栏目标题 |
| **H4** | 18px | 1.5 | 600 | 卡片标题 |
| **H5** | 16px | 1.5 | 600 | 列表标题 |
| **Body L** | 16px | 1.75 | 400 | 正文大字 |
| **Body** | 14px | 1.75 | 400 | 正文默认 |
| **Body S** | 13px | 1.6 | 400 | 辅助信息 |
| **Caption** | 12px | 1.5 | 400 | 标签/日期 |

### 3.3 CSS 变量定义

```css
:root {
  /* 字号 */
  --text-xs: 12px;
  --text-sm: 13px;
  --text-base: 14px;
  --text-lg: 16px;
  --text-xl: 18px;
  --text-2xl: 20px;
  --text-3xl: 24px;
  --text-4xl: 28px;

  /* 行高 */
  --leading-tight: 1.4;
  --leading-normal: 1.5;
  --leading-relaxed: 1.75;

  /* 字重 */
  --font-normal: 400;
  --font-medium: 500;
  --font-semibold: 600;
  --font-bold: 700;
}
```

---

## 四、布局规范

### 4.1 响应式断点

```css
:root {
  --breakpoint-sm: 768px;      /* 平板竖屏 */
  --breakpoint-md: 1024px;     /* 平板横屏 */
  --breakpoint-lg: 1200px;     /* 桌面端（主设计尺寸） */
  --breakpoint-xl: 1400px;     /* 大屏桌面 */
}
```

### 4.2 页面容器

```css
.container {
  max-width: 1200px;           /* 内容最大宽度 */
  margin: 0 auto;
  padding: 0 20px;
}

@media (max-width: 1024px) {
  .container {
    max-width: 1000px;
  }
}
```

### 4.3 栅格系统

```
┌─────────────────────────────────────────────────────────────┐
│                     Header (固定高度: 80px)                   │
├───────────────┬─────────────────────────────────────────────┤
│               │                                             │
│   Sidebar     │              Main Content                   │
│   (220px)     │              (flex: 1)                      │
│               │                                             │
│   固定定位     │              最小高度: calc(100vh - 200px)   │
│               │                                             │
├───────────────┴─────────────────────────────────────────────┤
│                     Footer (高度: 120px)                     │
└─────────────────────────────────────────────────────────────┘
```

### 4.4 间距系统

```css
:root {
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
}
```

---

## 五、组件规范

### 5.1 导航栏 (Header)

```
┌─────────────────────────────────────────────────────────────────┐
│  [LOGO] 河南城乡学校共同体发展平台                                │
│                                                                 │
│  首页  项目介绍  政策文件  示范校共体  资源共享  [🔍] [登录]          │
└─────────────────────────────────────────────────────────────────┘
```

**样式规范：**

```css
.header {
  background: var(--primary-800);    /* #1283E9 */
  height: 80px;
  position: fixed;
  top: 0;
  width: 100%;
  z-index: 1000;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.nav-link {
  color: var(--neutral-50);
  font-size: var(--text-lg);
  padding: 0 20px;
  line-height: 80px;
  transition: background 0.3s;
}

.nav-link:hover,
.nav-link.active {
  background: var(--primary-900);    /* #0A5DAD */
}
```

### 5.1.1 二级菜单下拉（导航栏）

> **说明**：政策文件、资源共享栏目支持二级菜单下拉展示。

```
┌─────────────────────────────────────────────────────────────────┐
│  [LOGO] 河南城乡学校共同体发展平台                                │
│                                                                 │
│  首页  项目介绍  [政策文件 ▼]  示范校共体  资源共享  [🔍] [登录]   │
└────────────────────┬────────────────────────────────────────────┘
                     │
                     ▼
               ┌──────────────┐
               │  国家政策    │  ← hover: 背景#f5f5f5
               ├──────────────┤
               │  省级政策    │
               └──────────────┘
```

**二级菜单栏目配置：**

| 一级栏目 | 二级菜单项 |
|----------|------------|
| 政策文件 | 国家政策、省级政策 |
| 资源共享 | 省外经验、省内经验、研究文献 |

**下拉菜单样式规范：**

```css
/* 导航栏带下拉的菜单项 */
.nav-item-dropdown {
  position: relative;
}

.nav-item-dropdown:hover .dropdown-menu {
  display: block;
}

/* 下拉箭头 */
.nav-link-arrow::after {
  content: '▼';
  font-size: 10px;
  margin-left: 4px;
  transition: transform 0.3s;
}

.nav-item-dropdown:hover .nav-link-arrow::after {
  transform: rotate(180deg);
}

/* 下拉菜单容器 */
.dropdown-menu {
  display: none;
  position: absolute;
  top: 100%;
  left: 0;
  min-width: 140px;
  background: var(--neutral-50);           /* #FFFFFF */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  border-radius: 0 0 4px 4px;
  z-index: 1001;
}

/* 下拉菜单项 */
.dropdown-item {
  display: block;
  padding: 12px 20px;
  color: var(--neutral-800);               /* #333333 */
  font-size: var(--text-base);             /* 14px */
  border-bottom: 1px solid var(--neutral-300);  /* #e5e5e5 */
  transition: all 0.2s;
}

.dropdown-item:last-child {
  border-bottom: none;
}

.dropdown-item:hover {
  background: var(--neutral-100);          /* #f8f8f8 */
  color: var(--primary-800);               /* #1283E9 */
}

.dropdown-item.active {
  color: var(--primary-800);
  font-weight: var(--font-medium);
}
```

### 5.2 侧边栏导航

```
┌────────────────────┐
│ ┌────────────────┐ │
│ │   栏目标题     │ │  ← 背景: #1283E9, 文字: #fff
│ └────────────────┘ │
│                    │
│  • 子栏目一        │  ← 文字: #333, 左侧边框: #2990E8
│  ─────────────────│  ← 虚线分隔: #ccc
│  • 子栏目二        │
│  ─────────────────│
│  • 子栏目三        │
│                    │
└────────────────────┘
```

**样式规范：**

```css
.sidebar {
  width: 220px;
  background: var(--neutral-200);
  position: sticky;
  top: 100px;
}

.sidebar-title {
  background: var(--primary-800);     /* #1283E9 */
  color: var(--neutral-50);
  padding: 15px 20px;
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
}

.sidebar-item {
  padding: 12px 20px;
  border-bottom: 1px dashed var(--neutral-400);
  color: var(--neutral-800);
  transition: all 0.3s;
}

.sidebar-item:hover,
.sidebar-item.active {
  color: var(--primary-800);
  border-left: 3px solid var(--accent-600);  /* #2990E8 */
  background: var(--neutral-100);
}
```

### 5.3 内容列表

```
┌─────────────────────────────────────────────────────────────────┐
│                                                                 │
│  ┌──────┐                                                      │
│  │ 日期 │  新闻标题在这里显示，最多显示一行，超出省略...          │
│  │03-15 │                                                       │
│  └──────┘  来源：平台发布                                       │
│  ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─  │
│                                                                 │
│  ┌──────┐                                                      │
│  │ 日期 │  另一条新闻标题...                                    │
│  │03-14 │                                                       │
│  └──────┘  来源：XX学校                                         │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

**样式规范：**

```css
.list-item {
  display: flex;
  align-items: flex-start;
  padding: 16px 0;
  border-bottom: 1px dashed var(--neutral-400);
}

.list-date {
  background: var(--primary-800);    /* #1283E9 */
  color: var(--neutral-50);
  padding: 8px 12px;
  text-align: center;
  min-width: 60px;
  margin-right: 16px;
}

.list-date-day {
  font-size: var(--text-xl);
  font-weight: var(--font-bold);
}

.list-date-month {
  font-size: var(--text-xs);
}

.list-title {
  color: var(--neutral-900);
  font-size: var(--text-lg);
  line-height: 1.5;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.list-title:hover {
  color: var(--primary-800);
}

.list-source {
  color: var(--neutral-600);
  font-size: var(--text-sm);
  margin-top: 4px;
}
```

### 5.4 按钮样式

```
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│   主要按钮   │  │   强调按钮   │  │   文字按钮   │
│   #1283E9    │  │   #2990E8    │  │   #2990E8    │
└──────────────┘  └──────────────┘  └──────────────┘
```

**样式规范：**

```css
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 10px 24px;
  font-size: var(--text-base);
  font-weight: var(--font-medium);
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

/* 主要按钮 */
.btn-primary {
  background: var(--primary-800);    /* #1283E9 */
  color: var(--neutral-50);
  border: none;
}

.btn-primary:hover {
  background: var(--primary-900);    /* #0A5DAD */
}

/* 强调按钮 */
.btn-accent {
  background: var(--accent-600);     /* #2990E8 */
  color: var(--neutral-50);
  border: none;
}

.btn-accent:hover {
  background: var(--accent-700);     /* #0E6FCA */
}

/* 轮廓按钮 */
.btn-outline {
  background: transparent;
  color: var(--primary-800);
  border: 1px solid var(--primary-800);
}

.btn-outline:hover {
  background: var(--primary-800);
  color: var(--neutral-50);
}

/* 文字按钮 */
.btn-text {
  background: transparent;
  color: var(--accent-600);          /* #2990E8 */
  border: none;
  padding: 10px 16px;
}

.btn-text:hover {
  color: var(--primary-800);
  text-decoration: underline;
}
```

### 5.5 卡片组件

```
┌─────────────────────────────────────┐
│  ┌───────────────────────────────┐  │
│  │                               │  │
│  │         [封面图片]            │  │  ← 高度: 220px
│  │                               │  │
│  └───────────────────────────────┘  │
│                                     │
│  卡片标题                           │  ← 字号: 18px, 颜色: #1a1a1a
│                                     │
│  卡片描述文本，最多两行显示，        │  ← 字号: 14px, 颜色: #666
│  超出部分省略...                    │
│                                     │
│  2025-03-15  [查看详情 →]           │  ← 日期: #999, 链接: #1283E9
└─────────────────────────────────────┘
```

**样式规范：**

```css
.card {
  background: var(--neutral-50);
  border-radius: 4px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: box-shadow 0.3s, transform 0.3s;
}

.card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.card-image {
  width: 100%;
  height: 220px;
  object-fit: cover;
}

.card-body {
  padding: 16px 20px;
}

.card-title {
  font-size: var(--text-xl);
  font-weight: var(--font-semibold);
  color: var(--neutral-900);
  margin-bottom: 8px;
  line-height: 1.4;
}

.card-title:hover {
  color: var(--primary-800);
}

.card-desc {
  font-size: var(--text-base);
  color: var(--neutral-600);
  line-height: var(--leading-relaxed);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  margin-top: 12px;
  border-top: 1px dashed var(--neutral-300);
}

.card-date {
  font-size: var(--text-sm);
  color: var(--neutral-500);
}

.card-link {
  font-size: var(--text-sm);
  color: var(--accent-600);
}
```

### 5.6 轮播图

```
┌─────────────────────────────────────────────────────────────────┐
│                                                                 │
│                                                                 │
│                      [轮播图片 1200×467]                        │
│                                                                 │
│                                                                 │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                     轮播标题文字                         │   │  ← 半透明黑底
│  └─────────────────────────────────────────────────────────┘   │
│                                                                 │
│                    ● ○ ○ ○ ○                                   │  ← 指示器
└─────────────────────────────────────────────────────────────────┘
```

**样式规范：**

```css
.carousel {
  position: relative;
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  overflow: hidden;
}

.carousel-item {
  width: 100%;
  aspect-ratio: 1200 / 467;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.carousel-caption {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.6);
  color: var(--neutral-50);
  padding: 16px 24px;
  font-size: var(--text-lg);
}

.carousel-indicators {
  position: absolute;
  bottom: 60px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 8px;
}

.carousel-indicator {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.5);
  cursor: pointer;
  transition: background 0.3s;
}

.carousel-indicator.active {
  background: var(--neutral-50);
}
```

### 5.7 表单组件

```css
/* 输入框 */
.form-input {
  width: 100%;
  padding: 10px 14px;
  font-size: var(--text-base);
  border: 1px solid var(--neutral-400);
  border-radius: 4px;
  transition: border-color 0.3s, box-shadow 0.3s;
}

.form-input:focus {
  outline: none;
  border-color: var(--primary-800);
  box-shadow: 0 0 0 3px rgba(18, 131, 233, 0.15);  /* #1283E9 with opacity */
}

.form-input::placeholder {
  color: var(--neutral-500);
}

/* 标签 */
.form-label {
  display: block;
  font-size: var(--text-base);
  font-weight: var(--font-medium);
  color: var(--neutral-800);
  margin-bottom: 8px;
}

/* 必填标记 */
.form-required::after {
  content: "*";
  color: var(--danger);
  margin-left: 4px;
}

/* 搜索框 */
.search-box {
  display: flex;
  background: var(--neutral-50);
  border-radius: 4px;
  overflow: hidden;
}

.search-input {
  flex: 1;
  border: none;
  padding: 10px 14px;
  font-size: var(--text-base);
}

.search-btn {
  background: var(--primary-800);    /* #1283E9 */
  color: var(--neutral-50);
  border: none;
  padding: 10px 20px;
  cursor: pointer;
}

.search-btn:hover {
  background: var(--primary-900);    /* #0A5DAD */
}
```

### 5.8 分页组件

```
┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐
│ < │ │ 1 │ │ 2 │ │ 3 │ │...│ │10 │ │ > │
└───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘
        ↑
      当前页 (背景: #1283E9)
```

**样式规范：**

```css
.pagination {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin: 32px 0;
}

.page-item {
  min-width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid var(--neutral-400);
  border-radius: 4px;
  font-size: var(--text-base);
  color: var(--neutral-800);
  cursor: pointer;
  transition: all 0.3s;
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

### 5.9 面包屑导航

```
首页 > 政策文件 > 国家政策 > 文章标题
```

**样式规范：**

```css
.breadcrumb {
  display: flex;
  align-items: center;
  font-size: var(--text-sm);
  color: var(--neutral-600);
  padding: 12px 0;
  margin-bottom: 16px;
  border-bottom: 1px solid var(--neutral-300);
}

.breadcrumb-item {
  color: var(--neutral-600);
}

.breadcrumb-item:not(:last-child)::after {
  content: ">";
  margin: 0 8px;
  color: var(--neutral-500);
}

.breadcrumb-item a {
  color: var(--accent-600);
}

.breadcrumb-item a:hover {
  color: var(--primary-800);
  text-decoration: underline;
}

.breadcrumb-item:last-child {
  color: var(--neutral-900);
  font-weight: var(--font-medium);
}
```

---

## 六、页面模板

### 6.1 首页布局

```
┌─────────────────────────────────────────────────────────────────┐
│                         [导航栏]                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                      [轮播图]                              │  │
│  │                    1200 × 467                             │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ┌─────────────────────────┐  ┌─────────────────────────────┐  │
│  │                         │  │                             │  │
│  │     [新闻资讯区块]       │  │     [政策文件区块]          │  │
│  │      最新5条            │  │       最新5条               │  │
│  │                         │  │                             │  │
│  └─────────────────────────┘  └─────────────────────────────┘  │
│                                                                 │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                                                           │  │
│  │                    [示范校展示区块]                         │  │
│  │                                                           │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                                                           │  │
│  │                  [资源共享区块] 最新5条                     │  │
│  │                                                           │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                      [友情链接]                            │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                 │
├─────────────────────────────────────────────────────────────────┤
│                         [页脚]                                   │
└─────────────────────────────────────────────────────────────────┘
```

### 6.2 列表页布局

```
┌─────────────────────────────────────────────────────────────────┐
│                         [导航栏]                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  首页 > 政策文件 > 国家政策                                      │
│                                                                 │
│  ┌──────────────┐  ┌────────────────────────────────────────┐  │
│  │              │  │                                        │  │
│  │  [侧边导航]   │  │            [内容列表]                  │  │
│  │              │  │                                        │  │
│  │  ┌─────────┐ │  │  • 列表项 1        2025-03-15          │  │
│  │  │国家政策 │ │  │  ─────────────────────────────────     │  │
│  │  └─────────┘ │  │  • 列表项 2        2025-03-14          │  │
│  │              │  │  ─────────────────────────────────     │  │
│  │  ┌─────────┐ │  │  • 列表项 3        2025-03-13          │  │
│  │  │省级政策 │ │  │                                        │  │
│  │  └─────────┘ │  │                                        │  │
│  │              │  │         [分页 1 2 3 ... 10]            │  │
│  │              │  │                                        │  │
│  └──────────────┘  └────────────────────────────────────────┘  │
│                                                                 │
├─────────────────────────────────────────────────────────────────┤
│                         [页脚]                                   │
└─────────────────────────────────────────────────────────────────┘
```

### 6.3 详情页布局

```
┌─────────────────────────────────────────────────────────────────┐
│                         [导航栏]                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  首页 > 政策文件 > 国家政策 > 文章标题                           │
│                                                                 │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                                                           │  │
│  │  文章标题 (H1)                                            │  │
│  │                                                           │  │
│  │  发布时间: 2025-03-15  |  来源: 平台发布  |  浏览: 1234次   │  │
│  │  ─────────────────────────────────────────────────────── │  │
│  │                                                           │  │
│  │  [正文内容区域]                                            │  │
│  │                                                           │  │
│  │  段落文字...                                              │  │
│  │                                                           │  │
│  │  [图片]                                                   │  │
│  │                                                           │  │
│  │  段落文字...                                              │  │
│  │                                                           │  │
│  │  ─────────────────────────────────────────────────────── │  │
│  │                                                           │  │
│  │  附件下载:                                                │  │
│  │  📎 文件名.pdf (2.5MB)  [下载]                            │  │
│  │                                                           │  │
│  │  ─────────────────────────────────────────────────────── │  │
│  │                                                           │  │
│  │  [← 上一篇: xxx]                    [下一篇: xxx →]       │  │
│  │                                                           │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                 │
├─────────────────────────────────────────────────────────────────┤
│                         [页脚]                                   │
└─────────────────────────────────────────────────────────────────┘
```

---

## 七、图标规范

### 7.1 推荐图标库

- **主选**: [Remix Icon](https://remixicon.com/) - 简洁现代
- **备选**: [Font Awesome](https://fontawesome.com/) - 覆盖全面

### 7.2 常用图标

| 场景 | 图标名称 | Unicode |
|------|----------|---------|
| 搜索 | ri-search-line | \eb7f |
| 首页 | ri-home-line | \eaf5 |
| 文件 | ri-file-text-line | \ead4 |
| 下载 | ri-download-line | \ea18 |
| 用户 | ri-user-line | \f264 |
| 日历 | ri-calendar-line | \e9dd |
| 链接 | ri-links-line | \ef27 |
| 查看更多 | ri-arrow-right-s-line | \e8e1 |
| 上一篇 | ri-arrow-left-line | \e8ce |
| 下一篇 | ri-arrow-right-line | \e8de |
| 电话 | ri-phone-line | \f0fa |
| 邮箱 | ri-mail-line | \ef64 |

### 7.3 图标样式

```css
.icon {
  font-size: 16px;
  vertical-align: middle;
  margin-right: 4px;
}

.icon-sm { font-size: 14px; }
.icon-lg { font-size: 20px; }
.icon-xl { font-size: 24px; }
```

---

## 八、动效规范

### 8.1 过渡时间

```css
:root {
  --duration-fast: 150ms;      /* 快速反馈 */
  --duration-normal: 300ms;    /* 常规过渡 */
  --duration-slow: 500ms;      /* 复杂动画 */

  --easing-default: ease;
  --easing-in-out: ease-in-out;
}
```

### 8.2 常用过渡效果

```css
/* 按钮悬停 */
.btn {
  transition: background var(--duration-normal) var(--easing-default),
              transform var(--duration-fast) var(--easing-default);
}

.btn:hover {
  transform: translateY(-1px);
}

/* 卡片悬停 */
.card {
  transition: box-shadow var(--duration-normal) var(--easing-default),
              transform var(--duration-normal) var(--easing-default);
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

/* 链接悬停 */
a {
  transition: color var(--duration-fast) var(--easing-default);
}

/* 展开/收起 */
.collapse {
  transition: height var(--duration-normal) var(--easing-in-out);
  overflow: hidden;
}
```

---

## 九、后台界面规范

### 9.1 后台配色

后台采用与前台统一的蓝色主题，保持品牌一致性：

```css
:root {
  /* 后台主色 - 与前台统一 */
  --admin-primary: #1283E9;    /* 主蓝色 */
  --admin-accent: #2990E8;     /* 强调色 */
  --admin-sidebar-bg: #0A5DAD; /* 深蓝侧边栏 */
  --admin-sidebar-text: #a0aec0;
  --admin-content-bg: #f5f7fa;
}
```

### 9.2 后台布局

```
┌─────────────────────────────────────────────────────────────────┐
│  LOGO  河南城乡学校共同体发展平台 - 管理后台    [王编辑] [退出]   │
├──────────────┬──────────────────────────────────────────────────┤
│              │                                                  │
│  [仪表盘]    │   面包屑导航                                     │
│              │                                                  │
│  用户管理    │  ┌────────────────────────────────────────────┐ │
│    • 用户列表│  │                                            │ │
│    • 账号管控│  │            [工作区内容]                    │ │
│              │  │                                            │ │
│  内容管理    │  │                                            │ │
│    • 政策文件│  │                                            │ │
│    • 新闻资讯│  │                                            │ │
│    • 资源共享│  │                                            │ │
│              │  │                                            │ │
│  示范校管理  │  │                                            │ │
│              │  └────────────────────────────────────────────┘ │
│  系统设置    │                                                  │
│              │                                                  │
└──────────────┴──────────────────────────────────────────────────┘
```

### 9.3 学校后台列表视图规范

> **说明**：学校管理员后台的内容管理采用列表视图，用于展示校共同体活动、月报等内容。

```
┌─────────────────────────────────────────────────────────────────────────┐
│  内容管理 > 校共同体活动                                                  │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  ┌───────────────────────────────────────────────┐  [+ 发布活动]        │
│  │ 🔍 搜索活动标题...                            │                      │
│  └───────────────────────────────────────────────┘                      │
│                                                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐│
│  │ 序号 │        标题        │ 发布时间  │ 状态  │      操作          ││
│  ├──────┼───────────────────┼──────────┼───────┼───────────────────┤│
│  │  1   │ 2025年春季教研活动 │ 03-15    │ 已发布 │ [编辑] [删除]     ││
│  ├──────┼───────────────────┼──────────┼───────┼───────────────────┤│
│  │  2   │ 名师送教下乡活动   │ 03-10    │ 草稿  │ [编辑] [删除] [发布]││
│  ├──────┼───────────────────┼──────────┼───────┼───────────────────┤│
│  │  3   │ 课堂教学观摩活动   │ 03-05    │ 已发布 │ [编辑] [删除]     ││
│  └──────┴───────────────────┴──────────┴───────┴───────────────────┘│
│                                                                         │
│                         < 1 2 3 ... 10 >                                │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

**样式规范：**

```css
/* 操作按钮组 */
.table-actions {
  display: flex;
  gap: 8px;
}

.table-actions .btn {
  padding: 4px 12px;
  font-size: var(--text-sm);
}

/* 编辑按钮 */
.btn-edit {
  color: var(--accent-600);
  background: transparent;
  border: 1px solid var(--accent-600);
}

.btn-edit:hover {
  background: var(--accent-600);
  color: var(--neutral-50);
}

/* 删除按钮 */
.btn-delete {
  color: var(--danger);
  background: transparent;
  border: 1px solid var(--danger);
}

.btn-delete:hover {
  background: var(--danger);
  color: var(--neutral-50);
}

/* 发布按钮 */
.btn-publish {
  color: var(--success);
  background: transparent;
  border: 1px solid var(--success);
}

.btn-publish:hover {
  background: var(--success);
  color: var(--neutral-50);
}

/* 状态标签 */
.status-tag {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: var(--text-xs);
}

.status-published {
  background: rgba(40, 167, 69, 0.1);
  color: var(--success);
}

.status-draft {
  background: rgba(255, 193, 7, 0.1);
  color: #b8860b;
}
```

### 9.4 示范校导航规范（官网）

> **说明**：示范校共体页面左侧导航默认显示前6个学校，支持"查看全部"展开。

```
┌────────────────────┐
│ ┌────────────────┐ │
│ │  示范校共体    │ │  ← 背景: #1283E9, 文字: #fff
│ └────────────────┘ │
│                    │
│  ● 信阳董家河小学  │  ← 当前选中: 左侧蓝色边框
│  ─────────────────│
│  ○ 濮阳市开德小学  │
│  ─────────────────│
│  ○ 扶沟县实验小学  │
│  ─────────────────│
│  ○ 修武县方庄小学  │
│  ─────────────────│
│  ○ 栾川县第四实验  │
│  ─────────────────│
│  ○ 航空港区科技一街│
│  ─────────────────│
│  ┌────────────────┐│
│  │   查看全部 ▼   ││  ← 点击展开更多学校
│  └────────────────┘│
└────────────────────┘
```

**样式规范：**

```css
.school-nav {
  width: 220px;
  background: var(--neutral-200);
  position: sticky;
  top: 100px;
}

.school-nav-title {
  background: var(--primary-800);
  color: var(--neutral-50);
  padding: 15px 20px;
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
}

.school-nav-item {
  padding: 12px 20px;
  border-bottom: 1px dashed var(--neutral-400);
  color: var(--neutral-800);
  font-size: var(--text-base);
  cursor: pointer;
  transition: all 0.3s;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.school-nav-item:hover,
.school-nav-item.active {
  color: var(--primary-800);
  border-left: 3px solid var(--accent-600);
  background: var(--neutral-100);
}

/* 查看全部按钮 */
.school-nav-expand {
  padding: 12px 20px;
  text-align: center;
  color: var(--accent-600);
  font-size: var(--text-sm);
  cursor: pointer;
  border-top: 1px solid var(--neutral-300);
}

.school-nav-expand:hover {
  background: var(--neutral-100);
  color: var(--primary-800);
}

/* 展开状态 */
.school-nav-expand.expanded::after {
  content: ' ▲';
}

.school-nav-expand:not(.expanded)::after {
  content: ' ▼';
}
```

### 9.5 文件上传组件规范

> **说明**：统一的文件上传组件，适用于图片、文档、附件等场景。

```
┌─────────────────────────────────────────────────────────────┐
│                                                             │
│     ┌───────────────────────────────────────────────┐      │
│     │                                               │      │
│     │          📁 点击或拖拽文件到此处上传            │      │
│     │                                               │      │
│     │      支持格式: jpg, png, pdf, doc, docx       │      │
│     │           单个文件不超过 10MB                 │      │
│     │                                               │      │
│     └───────────────────────────────────────────────┘      │
│                                                             │
│  已上传文件:                                                │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  📄 教学设计方案.pdf (2.5MB)      [预览] [删除]     │   │
│  └─────────────────────────────────────────────────────┘   │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  🖼️ 活动照片.jpg (1.2MB)         [预览] [删除]     │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

**样式规范：**

```css
/* 上传区域 */
.upload-area {
  border: 2px dashed var(--neutral-400);
  border-radius: 8px;
  padding: 40px 20px;
  text-align: center;
  background: var(--neutral-100);
  cursor: pointer;
  transition: all 0.3s;
}

.upload-area:hover {
  border-color: var(--primary-800);
  background: rgba(18, 131, 233, 0.05);
}

.upload-area.dragging {
  border-color: var(--primary-800);
  background: rgba(18, 131, 233, 0.1);
}

.upload-icon {
  font-size: 48px;
  color: var(--neutral-500);
  margin-bottom: 12px;
}

.upload-text {
  font-size: var(--text-base);
  color: var(--neutral-700);
  margin-bottom: 8px;
}

.upload-hint {
  font-size: var(--text-sm);
  color: var(--neutral-500);
}

/* 文件列表 */
.file-list {
  margin-top: 16px;
}

.file-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background: var(--neutral-100);
  border-radius: 4px;
  margin-bottom: 8px;
}

.file-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.file-icon {
  font-size: 20px;
}

.file-name {
  font-size: var(--text-base);
  color: var(--neutral-800);
}

.file-size {
  font-size: var(--text-sm);
  color: var(--neutral-500);
  margin-left: 8px;
}

.file-actions {
  display: flex;
  gap: 8px;
}

/* 上传进度条 */
.upload-progress {
  height: 4px;
  background: var(--neutral-300);
  border-radius: 2px;
  overflow: hidden;
  margin-top: 8px;
}

.upload-progress-bar {
  height: 100%;
  background: var(--primary-800);
  transition: width 0.3s;
}

/* 上传错误提示 */
.upload-error {
  color: var(--danger);
  font-size: var(--text-sm);
  margin-top: 8px;
}
```

---

## 十、设计资源

### 10.1 设计交付清单

| 资源 | 格式 | 说明 |
|------|------|------|
| 设计稿 | Figma/Sketch | 包含所有页面设计 |
| 标注文档 | PDF | 开发交付用 |
| 切图资源 | PNG/SVG | @1x @2x 两套 |
| 配色文件 | ASE/CSS | 导入设计软件用 |
| 图标库 | SVG/Icon Font | 完整图标集 |
| 组件库 | Figma/Sketch | 可复用组件 |

### 10.2 开发交付

```
/design
  ├── /assets
  │   ├── /images          # 图片资源
  │   ├── /icons           # 图标资源
  │   └── /fonts           # 字体文件
  ├── /styles
  │   ├── variables.css    # CSS变量定义
  │   ├── reset.css        # 样式重置
  │   ├── components.css   # 组件样式
  │   └── pages.css        # 页面样式
  └── /docs
      ├── design-spec.md   # 本规范文档
      └── changelog.md     # 设计变更记录
```

---

## 附录：快速参考

### A. 颜色速查

| 用途 | 颜色值 |
|------|--------|
| 主蓝色 | `#1283E9` |
| 强调色 | `#2990E8` |
| 深蓝色 | `#0A5DAD` |
| 主文字 | `#333333` |
| 辅助文字 | `#666666` |
| 边框色 | `#cccccc` |
| 背景色 | `#f2f2f2` |
| 白色 | `#ffffff` |

### B. 字号速查

| 用途 | 字号 |
|------|------|
| 页面标题 | 28px |
| 模块标题 | 24px |
| 栏目标题 | 20px |
| 卡片标题 | 18px |
| 正文 | 14px |
| 辅助信息 | 13px |
| 标签/日期 | 12px |

### C. 间距速查

| 级别 | 数值 |
|------|------|
| 紧凑 | 8px |
| 标准 | 16px |
| 宽松 | 24px |
| 区块 | 32px |
| 大区块 | 48px |

---

**文档结束**
