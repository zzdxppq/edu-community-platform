# 十三、Epic 列表与 Story 定义

> 基于一期（P0）版本规划范围，将功能需求拆分为 **11 个 Epic**，共 **57 个 Story**。
> 每个 Epic 采用 YAML 格式定义，包含 Story 级别的验收标准、复杂度评估和优先级。

## 复杂度说明

| 级别 | 工作量估算 | 说明 |
|------|-----------|------|
| XS | 0.5-1天 | 极简改动，单一组件 |
| S | 1-2天 | 简单功能，少量接口/页面 |
| M | 3-5天 | 中等复杂度，多组件协同 |
| L | 5-8天 | 较高复杂度，跨模块集成 |
| XL | 8-13天 | 高复杂度，核心架构搭建 |

## 优先级说明

所有 Story 均属于一期 P0 范围。优先级（高/中/低）表示**同 Epic 内的实施顺序**。

## Epic 概览

| Epic ID | Epic 名称 | Story 数 | 关联功能模块 |
|---------|-----------|----------|-------------|
| 1 | 项目基础设施搭建 | 8 | 技术假设 TA-01~05 |
| 2 | 认证与用户管理系统 | 8 | F-LG, B-UM, BR-01~05, BR-AC |
| 3 | 前端官网框架与首页 | 5 | F-HP, F-PG |
| 4 | 内容管理基础 | 6 | B-CM, B-CT-00, BR-CM, BR-06 |
| 5 | 新闻资讯模块 | 3 | F-NW, B-CT-01 |
| 6 | 项目介绍模块 | 2 | F-PI, B-CT-06 |
| 7 | 政策文件模块 | 4 | F-PF, B-CT-02 |
| 8 | 示范校共体模块 | 10 | F-SC, B-SM, B-CT-07, S-DB~S-MB |
| 9 | 资源共享模块 | 3 | F-RS, B-CT-03 |
| 10 | 全站搜索 | 2 | F-SR |
| 11 | 系统设置与运维 | 6 | B-DB, B-CT-04, B-SS |

---

## Epic 1: 项目基础设施搭建

```yaml
epic_id: 1
title: 项目基础设施搭建
description: 完成 Monorepo 脚手架、微服务框架、前端工程、数据库、容器化等技术基础设施搭建
target_platform: fullstack
stories:

  - story_id: "1.1"
    title: 单仓库项目脚手架搭建
    description: >
      创建 Monorepo 目录结构（backend/、frontend/），配置根级构建脚本、
      .gitignore、.editorconfig 和项目说明文档
    acceptance_criteria:
      - 仓库包含 backend/ 和 frontend/ 顶级目录
      - 根目录包含 docker-compose.yml 骨架文件
      - .gitignore 覆盖 Java、Node.js、IDE 常见忽略项
      - 各子项目目录结构清晰，可独立构建
    estimated_complexity: S
    priority: 高

  - story_id: "1.2"
    title: 后端微服务基础框架搭建
    description: >
      搭建 Spring Boot 3.2 + Spring Cloud 2023 微服务框架，包含 API Gateway、
      core-service、content-service、file-service 四个模块及 Nacos 注册中心配置
    acceptance_criteria:
      - 四个微服务模块可独立启动且注册到 Nacos
      - API Gateway 路由转发正常（各服务前缀路由可达）
      - 健康检查端点（/actuator/health）返回正常
      - Maven 父工程管理统一依赖版本
      - JDK 21 编译运行正常
    estimated_complexity: L
    priority: 高

  - story_id: "1.3"
    title: 前端官网项目初始化
    description: >
      使用 Vue 3 + Nuxt 3 创建 SSR 官网项目，配置基础路由、SEO meta、
      全局样式变量和 CSS Reset
    acceptance_criteria:
      - Nuxt 3 项目正常启动，SSR 渲染正常
      - 基础路由配置完成（/、/about、/policy、/schools、/resources）
      - 全局 SCSS 变量和 reset 样式就绪
      - SEO meta 默认配置可用
    estimated_complexity: M
    priority: 高

  - story_id: "1.4"
    title: 管理后台项目初始化
    description: >
      使用 Vue 3 + Element Plus 创建管理后台项目，配置路由、后台布局框架、
      Axios 请求封装和权限路由基础
    acceptance_criteria:
      - Element Plus 项目正常启动
      - 后台布局框架完成（侧边栏 + 顶栏 + 内容区）
      - Axios 实例封装完成（拦截器、Token 注入、统一错误处理）
      - 路由配置和权限路由占位机制就绪
    estimated_complexity: M
    priority: 高

  - story_id: "1.5"
    title: 数据库与缓存基础环境
    description: >
      配置 MySQL 8.0 数据库（含全部核心表 DDL）、Redis 7 缓存、
      Flyway 数据库版本管理及初始数据导入脚本
    acceptance_criteria:
      - MySQL 8.0 容器正常运行，所有核心表创建完成（参见七、数据需求）
      - Redis 7 容器正常运行
      - Flyway 迁移脚本按版本号有序执行
      - 初始数据导入脚本就绪（6所示范校、预置栏目配置）
    estimated_complexity: M
    priority: 高

  - story_id: "1.6"
    title: Docker Compose 本地开发环境
    description: >
      编写完整的 docker-compose.yml，支持一键启动 MySQL、Redis、Nacos
      及全部微服务和前端项目
    acceptance_criteria:
      - docker-compose up 可一键启动所有服务
      - 各服务间网络互通、服务发现正常
      - 数据卷持久化配置正确
      - 包含 .env.example 环境变量模板
    estimated_complexity: S
    priority: 高

  - story_id: "1.7"
    title: 后端公共模块开发
    description: >
      开发后端公共模块，包含统一 API 响应封装、全局异常处理、
      分页请求/响应工具、参数校验增强和日志配置
    acceptance_criteria:
      - 统一 API 响应格式（code/message/data）封装完成
      - 全局异常处理器捕获业务异常、参数异常、系统异常并格式化返回
      - 分页工具支持 pageNum/pageSize 参数自动绑定
      - 参数校验注解（@Valid）与异常处理集成
      - Logback 日志配置完成（含请求链路追踪 ID）
    estimated_complexity: M
    priority: 高

  - story_id: "1.8"
    title: 文件上传下载服务（file-service）
    description: >
      实现 file-service 核心 API：文件上传（图片/文档/视频）、
      文件下载、图片缩略图生成；对接腾讯云 COS，预留存储抽象层支持后期切换 MinIO
    acceptance_criteria:
      - 文件上传 API 支持图片（jpg/png/gif，≤5MB）、文档（pdf/doc/docx/xls/xlsx，≤50MB）、视频（mp4/mov，≤200MB）
      - 上传成功返回文件 URL、文件大小、文件类型
      - 文件下载 API 支持断点续传
      - 图片上传自动生成缩略图
      - 存储层通过抽象接口封装，一期对接腾讯云 COS
      - 文件格式和大小校验（拒绝非法文件）
      - 上传接口需鉴权（仅管理员可上传）
    estimated_complexity: L
    priority: 高
```

---

## Epic 2: 认证与用户管理系统

```yaml
epic_id: 2
title: 认证与用户管理系统
description: >
  实现 JWT 认证体系、管理后台登录、RBAC 权限控制、
  用户管理及越权登录功能
target_platform: fullstack
stories:

  - story_id: "2.1"
    title: 管理员数据模型与基础 API
    description: >
      实现管理员账号表 CRUD API（参见 7.1.8），包含手机号唯一校验、
      BCrypt 密码加密、账号状态管理
    acceptance_criteria:
      - 管理员表 CRUD 接口完整（新增/查询/更新/删除）
      - 账号字段校验：手机号11位数字格式
      - 密码 BCrypt 加密存储
      - 新增时默认密码为手机号后6位
      - is_first_login 默认为 1（首次登录标记）
    estimated_complexity: S
    priority: 高

  - story_id: "2.2"
    title: JWT 认证体系实现
    description: >
      实现 Access Token（15min）+ Refresh Token（7天）双令牌机制，
      Redis Token 黑名单，网关层统一鉴权过滤器
    acceptance_criteria:
      - 登录成功返回 accessToken 和 refreshToken
      - Access Token 过期后可通过 Refresh Token 无感刷新
      - 登出时 Token 加入 Redis 黑名单
      - API Gateway 过滤器拦截无效/过期 Token
      - 白名单路径（登录接口、前端公开接口）可免鉴权访问
    estimated_complexity: L
    priority: 高

  - story_id: "2.3"
    title: 管理后台登录页面与登录流程
    description: >
      开发独立管理后台登录页面，提供身份选择（超级管理员/学校管理员），
      登录账号输入框提示"请输入手机号"，密码输入框提示"请输入密码"
    acceptance_criteria:
      - 登录页包含身份选择（超级管理员/学校管理员）
      - 输入框 placeholder 正确（"请输入手机号"/"请输入密码"）
      - 登录成功后根据角色跳转对应后台（超管/学校）
      - 登录失败显示具体错误信息（账号不存在/密码错误/账号已禁用）
      - 前端官网登录入口提供"管理员登录"链接跳转此页面
    estimated_complexity: M
    priority: 高

  - story_id: "2.4"
    title: 首次登录强制修改密码
    description: >
      首次登录后强制跳转修改密码页面，密码必须满足 8-20位，
      包含大写字母、小写字母和数字，修改后方可进入后台
    acceptance_criteria:
      - 首次登录（is_first_login=1）自动跳转修改密码页面
      - 密码校验规则：8-20位，必须包含大写字母+小写字母+数字
      - 新密码不能与原密码相同
      - 修改成功后 is_first_login 置为 0
      - 未完成改密无法访问后台其他页面（路由守卫拦截）
    estimated_complexity: S
    priority: 高

  - story_id: "2.5"
    title: 学校管理员账号管理
    description: >
      超级管理员可查看、新增、编辑学校管理员；支持按账号（手机号）搜索、
      按所属学校下拉筛选；新增时账号必须为手机号，无姓名字段
    acceptance_criteria:
      - 管理员列表分页展示，显示账号（手机号）、所属学校、状态、最后登录时间
      - 支持按手机号模糊搜索
      - 支持按所属学校下拉筛选
      - 新增管理员：手机号格式校验、必须关联示范校、无姓名字段
      - 默认密码为手机号后6位（不在页面展示）
    estimated_complexity: M
    priority: 中

  - story_id: "2.6"
    title: 账号管控功能
    description: >
      超级管理员可禁用/启用学校管理员账号，可重置密码
      （重置为手机号后6位并标记需首次改密）
    acceptance_criteria:
      - 禁用后该账号无法登录，已登录的 Token 立即失效
      - 启用后账号恢复正常登录能力
      - 重置密码后密码恢复为手机号后6位
      - 重置密码后 is_first_login 标记为 1
      - 操作需二次确认弹框
    estimated_complexity: S
    priority: 中

  - story_id: "2.7"
    title: RBAC 权限控制与路由守卫
    description: >
      实现基于角色的权限控制（super_admin / school_admin），
      前端路由守卫根据角色动态生成菜单和可访问路由
    acceptance_criteria:
      - super_admin 可访问超管后台所有功能
      - school_admin 仅可访问学校后台功能（数据隔离到本校）
      - 前端侧边栏菜单根据角色动态渲染
      - 后端接口层权限校验（注解方式）
      - 越权访问返回 403 并重定向
    estimated_complexity: M
    priority: 中

  - story_id: "2.8"
    title: 越权登录功能
    description: >
      超级管理员可在示范校列表点击"登录后台"按钮，
      一键模拟登录该校管理后台（用于运维排查），操作记录日志
    acceptance_criteria:
      - 示范校列表每行提供"登录后台"操作按钮
      - 点击后生成该校管理员临时 Token 并跳转学校后台
      - 越权登录操作写入操作日志（记录操作人、目标学校、IP、时间）
      - 学校后台页面显示"越权登录"标识（区别于正常登录）
      - 越权登录 Token 有效期缩短（建议30分钟）
    estimated_complexity: M
    priority: 低
```

---

## Epic 3: 前端官网框架与首页

```yaml
epic_id: 3
title: 前端官网框架与首页
description: >
  完成官网全局布局、导航栏、首页轮播图、模块入口、
  页脚及全局分页组件
target_platform: frontend
stories:

  - story_id: "3.1"
    title: 官网全局布局与导航栏
    description: >
      实现官网全局布局：固定导航栏（首页/项目介绍/政策文件/示范校共体/资源共享/登录），
      支持二级下拉菜单，跟随滚动固定顶部
    acceptance_criteria:
      - 导航栏显示六个一级栏目，滚动时固定在顶部
      - 政策文件、资源共享支持二级下拉菜单
      - 示范校共体栏目不显示二级下拉（通过 Tab 实现）
      - 当前页面对应栏目高亮
      - 新闻资讯不在导航栏显示
      - 适配 1280×720 及以上分辨率
    estimated_complexity: M
    priority: 高

  - story_id: "3.2"
    title: 首页轮播图组件
    description: >
      展示 3-5 张图片，支持自动切换（间隔5秒）和手动切换，
      点击可跳转对应详情页
    acceptance_criteria:
      - 轮播图自动切换（5秒间隔）
      - 支持手动左右切换和指示器点击
      - 点击轮播图跳转配置的链接
      - 轮播图数据从后台 API 获取
      - 无数据时显示默认占位图
    estimated_complexity: S
    priority: 高

  - story_id: "3.3"
    title: 首页模块入口区域
    description: >
      各模块显示最新5条（标题+发布时间），置顶内容优先显示，
      点击"查看更多"进入列表页；新闻资讯入口在首页展示
    acceptance_criteria:
      - 展示模块：新闻资讯、政策文件、示范校共体动态、资源共享
      - 每个模块显示最新5条记录（标题+发布时间）
      - 置顶内容优先显示
      - "查看更多"链接跳转对应模块列表页
      - 数据为空时模块区域正常显示（无报错）
    estimated_complexity: M
    priority: 中

  - story_id: "3.4"
    title: 页脚版权信息与友情链接
    description: >
      底部展示网站地址、邮箱、版权声明；友情链接区展示
      河南省教育厅、河南大学教育学部、河南省教育信息化发展研究中心链接
    acceptance_criteria:
      - 版权信息从基础配置 API 获取
      - 友情链接点击在新窗口打开
      - 不含底部快捷链接（项目介绍、政策文件等入口已删除）
      - 页脚高度和颜色与导航栏一致
    estimated_complexity: S
    priority: 中

  - story_id: "3.5"
    title: 全局分页组件
    description: >
      开发全局可复用分页组件：默认每页10条，支持10/20/50/100条切换，
      显示总条数，包含首页/上一页/页码/下一页/末页/页码跳转
    acceptance_criteria:
      - 默认每页 10 条
      - 支持选择每页条数：10/20/50/100
      - 显示总条数（如"共 128 条记录"）
      - 包含完整分页控件（首页/上一页/页码/下一页/末页/跳转）
      - 前端官网和管理后台可共用此组件
    estimated_complexity: S
    priority: 高
```

---

## Epic 4: 内容管理基础

```yaml
epic_id: 4
title: 内容管理基础
description: >
  完成栏目管理、富文本编辑器集成、轮播图管理、
  内容发布三模式框架及前端动态导航
target_platform: fullstack
stories:

  - story_id: "4.1"
    title: 一级栏目管理
    description: >
      基于栏目配置表（参见 7.1.9），超级管理员管理前端一级栏目：
      新增/编辑/删除/排序/隐藏；系统预置栏目不可删除仅可隐藏
    acceptance_criteria:
      - 栏目列表展示名称、代码、状态、排序、是否系统预置
      - 支持拖拽或数字调整排序
      - 系统预置栏目（首页/项目介绍/政策文件/示范校共体/资源共享/登录）仅可隐藏不可删除
      - 栏目下有关联内容时不可删除仅可隐藏
      - 编辑后前端导航实时更新
    estimated_complexity: M
    priority: 高

  - story_id: "4.2"
    title: 二级栏目管理
    description: >
      基于栏目配置表（参见 7.1.9），超级管理员设置一级栏目是否启用
      二级菜单；新增/编辑/删除/排序二级菜单项；二级菜单关联对应内容分类
    acceptance_criteria:
      - 开关控制一级栏目是否启用二级菜单
      - 二级菜单支持 CRUD 和排序
      - 预置二级菜单：政策文件（国家政策/省级政策）、资源共享（省外经验/省内经验/研究文献）
      - 示范校共体的二级内容通过 Tab 切换实现，不在此管理
      - 二级菜单关联内容分类用于前端筛选
    estimated_complexity: M
    priority: 高

  - story_id: "4.3"
    title: 富文本编辑器集成与统一配置
    description: >
      选型并集成全站统一富文本编辑器，配置完整功能（格式/字体/段落/
      列表/图片/视频/表格/链接/首行缩进/行间距等）
    acceptance_criteria:
      - 支持基础格式（加粗/斜体/下划线/删除线）
      - 支持字体选择（宋体/黑体/楷体/微软雅黑等）、字号（12-48px）、字体颜色、背景色
      - 支持段落格式（对齐方式/首行缩进2字符/行间距/段落间距）
      - 支持列表（有序/无序）、表格、分割线
      - 支持图片上传（≤5MB，jpg/png/gif）、视频上传/嵌入、链接
      - 支持撤销/重做、全屏编辑、源码模式
      - 编辑器作为全局组件，所有内容发布页面统一使用
    estimated_complexity: L
    priority: 高

  - story_id: "4.4"
    title: 轮播图管理
    description: >
      超级管理员管理首页轮播图：新增/编辑/删除/排序，
      设置图片、跳转链接、显示状态
    acceptance_criteria:
      - 轮播图列表展示缩略图、标题、跳转链接、状态、排序
      - 支持图片上传（建议尺寸提示）
      - 支持设置跳转链接和显示/隐藏状态
      - 支持拖拽排序
      - 前端首页轮播图据此数据渲染
    estimated_complexity: M
    priority: 中

  - story_id: "4.5"
    title: 内容发布三模式框架
    description: >
      开发可复用的内容发布组件，支持三种发布方式：①图文编辑（富文本）
      ②附件上传（PDF/Word，可选）③外部链接（仅标题+URL）
    acceptance_criteria:
      - 发布表单提供发布方式切换（图文编辑/附件上传/外部链接）
      - 图文编辑模式调用统一富文本编辑器
      - 附件上传支持 PDF/Word/Excel，单文件≤50MB
      - 外部链接模式仅需填写标题和URL（封面图/摘要可选）
      - 外部链接仅支持 http/https 协议
      - 组件可被新闻/政策/资源/活动等模块复用
    estimated_complexity: L
    priority: 高

  - story_id: "4.6"
    title: 前端动态导航渲染
    description: >
      前端根据后台栏目配置 API 动态生成导航栏，
      包括一级栏目和二级下拉菜单
    acceptance_criteria:
      - 导航栏数据从栏目配置 API 获取（非硬编码）
      - 隐藏状态的栏目不在导航栏显示
      - 二级菜单按配置的排序展示
      - 栏目配置变更后前端导航自动更新（无需重新部署）
      - API 响应缓存以保证加载性能
    estimated_complexity: M
    priority: 中
```

---

## Epic 5: 新闻资讯模块

```yaml
epic_id: 5
title: 新闻资讯模块
description: >
  实现新闻资讯全流程：后台发布管理、前端列表/详情展示、
  首页入口集成
target_platform: fullstack
stories:

  - story_id: "5.1"
    title: 新闻资讯数据模型与后端 API
    description: >
      实现新闻资讯表 CRUD API（参见 7.1.7），
      支持状态筛选、置顶、分页查询、三种发布方式
    acceptance_criteria:
      - CRUD 接口完整（新增/查询/更新/删除）
      - 支持按发布状态筛选（已发布/草稿/全部）
      - 支持置顶设置（置顶内容优先，多个置顶按时间倒序）
      - 支持三种发布方式字段（content/attachments/external_link）
      - 无摘要字段（summary）
      - 前端公开接口仅返回已发布状态数据
    estimated_complexity: S
    priority: 高

  - story_id: "5.2"
    title: 新闻资讯后台管理页面
    description: >
      超级管理员发布/编辑/删除新闻资讯；支持状态筛选、
      置顶设置、三种发布方式切换
    acceptance_criteria:
      - 新闻列表分页展示（标题/状态/置顶/发布时间）
      - 支持按发布状态下拉筛选
      - 发布/编辑表单使用统一内容发布组件（三种模式）
      - 置顶开关操作
      - 删除需二次确认
      - 外部链接类型在列表显示"外链"标识
    estimated_complexity: M
    priority: 高

  - story_id: "5.3"
    title: 新闻资讯前端展示
    description: >
      前端首页新闻入口（最新5条 + 查看更多）、
      新闻列表页（分页+置顶优先）、新闻详情页（富文本渲染）
    acceptance_criteria:
      - 首页新闻入口显示最新5条（标题+发布时间），置顶优先
      - "查看更多"跳转新闻列表页
      - 列表页按发布时间倒序、置顶优先、支持分页
      - 详情页正确渲染富文本内容
      - 外部链接类型点击后直接跳转外部页面（不进入详情页）
      - 外部链接跳转前弹出"您即将离开本平台"提示
      - 导航栏不显示新闻资讯入口
    estimated_complexity: M
    priority: 中
```

---

## Epic 6: 项目介绍模块

```yaml
epic_id: 6
title: 项目介绍模块
description: >
  实现项目介绍的后台编辑和前端展示（单页内容形式）
target_platform: fullstack
stories:

  - story_id: "6.1"
    title: 项目介绍后台编辑页面
    description: >
      基于单页内容表（参见 7.1.10），超级管理员编辑项目介绍：
      项目名称（可编辑）+ 项目内容（富文本编辑器），单页形式，非列表
    acceptance_criteria:
      - 页面包含项目名称输入框和富文本编辑器
      - 项目名称可编辑维护
      - 内容使用统一富文本编辑器
      - 保存后前端立即可见更新
      - 支持 SEO 字段设置（标题/关键词/描述）
    estimated_complexity: S
    priority: 高

  - story_id: "6.2"
    title: 项目介绍前端展示页
    description: >
      前端展示项目名称和项目内容（富文本渲染），
      页面路由 /about
    acceptance_criteria:
      - 页面正确显示项目名称
      - 富文本内容（图文结合）渲染正常
      - SEO meta 信息正确设置
      - 页面加载时间 ≤ 3秒
    estimated_complexity: S
    priority: 中
```

---

## Epic 7: 政策文件模块

```yaml
epic_id: 7
title: 政策文件模块
description: >
  实现政策文件全流程：后台发布管理（含分类、发文机构），
  前端分类浏览、详情查看、PDF 下载与打印
target_platform: fullstack
stories:

  - story_id: "7.1"
    title: 政策文件数据模型与后端 API
    description: >
      实现政策文件表 CRUD API（参见 7.1.5），支持分类（国家政策/省级政策）、
      状态筛选、置顶、发文机构字段、三种发布方式
    acceptance_criteria:
      - CRUD 接口完整
      - 支持按分类（国家政策/省级政策）筛选
      - 支持按发布状态筛选（已发布/草稿/全部）
      - 发文机构字段非必填
      - 详情页接口不返回文号字段（后台存储但前端不展示）
      - 置顶排序逻辑正确
    estimated_complexity: S
    priority: 高

  - story_id: "7.2"
    title: 政策文件后台管理页面
    description: >
      超级管理员发布/编辑/删除政策文件；支持分类选择、状态筛选、
      置顶设置、发文机构录入、三种发布方式
    acceptance_criteria:
      - 列表分页展示（标题/分类/发文机构/状态/置顶/发布时间）
      - 支持按分类和发布状态联合筛选
      - 发布表单包含发文机构输入框（非必填）
      - 使用统一内容发布组件（三种模式）
      - 支持定时发布
    estimated_complexity: M
    priority: 高

  - story_id: "7.3"
    title: 政策文件前端展示
    description: >
      前端政策文件页面：左侧分类导航（国家政策/省级政策），
      右侧列表（分页+置顶优先）、详情页（不展示文号）
    acceptance_criteria:
      - 左侧分类导航栏，点击可快速切换分类
      - 列表显示标题、发布时间、所属分类、发文机构
      - 按发布时间倒序，置顶优先
      - 详情页展示标题、发布时间、正文内容（不展示文号）
      - 外部链接类型直接跳转
      - 支持分页
    estimated_complexity: M
    priority: 中

  - story_id: "7.4"
    title: 政策文件 PDF 下载与在线打印
    description: >
      政策文件详情页支持 PDF 下载和在线打印，
      下载/打印按钮固定在页面顶部
    acceptance_criteria:
      - 详情页顶部固定显示"下载PDF"和"打印"按钮
      - PDF 下载功能正常（支持已上传的 PDF 和富文本转 PDF）
      - 在线打印功能正常（调用浏览器打印）
      - 打印样式优化（隐藏导航、页脚等无关元素）
      - 下载首字节响应时间 ≤ 3秒
    estimated_complexity: M
    priority: 低
```

---

## Epic 8: 示范校共体模块

```yaml
epic_id: 8
title: 示范校共体模块
description: >
  实现示范校全流程：超管学校管理、前端学校展示（左侧导航+Tab+独立列表/详情页）、
  学校管理后台（工作台/个人中心/内容管理/成员校管理）、超管月报管理
target_platform: fullstack
stories:

  - story_id: "8.1"
    title: 示范校与成员校数据模型与 API
    description: >
      实现示范校信息表（7.1.1）、成员校信息表（7.1.2）、
      校共同体活动表（7.1.3）、校共同体月报表（7.1.4）的 CRUD API
    acceptance_criteria:
      - 四张表 CRUD 接口完整
      - 示范校 API 支持按 is_visible 和 sort_order 排序查询
      - 活动/月报 API 支持按 school_id 隔离查询
      - 成员校 API 支持按 school_id 查询和排序
      - 月报 API 支持按年月筛选
      - 前端公开接口仅返回已发布且可见的数据
    estimated_complexity: M
    priority: 高

  - story_id: "8.2"
    title: 超管示范校管理
    description: >
      超级管理员管理示范校列表：新增/编辑/删除示范校，
      设置是否在官网显示、排序；列表不显示统计信息、不提供批量操作
    acceptance_criteria:
      - 列表展示学校名称、学段、区域、状态、排序、是否显示
      - 不显示统计信息（活动数、月报数等）
      - 不提供批量操作按钮
      - 新增/编辑：学校名称、学校代码、学段、省市县、联系电话、显示控制、排序
      - 提供"登录后台"按钮（关联 Epic 2 越权登录）
      - 提供"查看前端"快速跳转链接
    estimated_complexity: M
    priority: 高

  - story_id: "8.3"
    title: 前端示范校导航与 Tab 框架
    description: >
      前端示范校共体页面：左侧导航栏（默认6个+查看全部+固定定位），
      右侧 Tab 切换（校共同体介绍/活动/月报），支持 URL 定位到指定 Tab
    acceptance_criteria:
      - 左侧导航默认显示前6个示范校（按 sort_order 和 is_visible）
      - "查看全部"展开显示所有学校
      - 左侧导航栏固定定位，不随页面滚动
      - 右侧 Tab 切换正常（介绍/活动/月报）
      - URL 支持 Tab 定位（如 /schools/:id?tab=activity）
      - 点击左侧学校切换右侧内容
    estimated_complexity: L
    priority: 高

  - story_id: "8.4"
    title: 校共同体介绍（编辑与展示）
    description: >
      学校管理员使用富文本编辑器维护校共同体介绍（组建模式/核心优势/详细介绍），
      前端展示介绍内容及成员校列表
    acceptance_criteria:
      - 学校管理员点击菜单进入编辑页面（非列表）
      - 使用富文本编辑器编辑介绍内容
      - 不可修改基础信息（学校名称、学段、区域等）
      - 前端介绍 Tab 展示富文本内容
      - 前端展示成员校列表（数量统计 + 名称列表）
    estimated_complexity: M
    priority: 中

  - story_id: "8.5"
    title: 校共同体活动（管理与展示）
    description: >
      学校管理员发布/编辑/删除活动（列表视图+分页+富文本+三种发布方式），
      前端活动 Tab 内展示最新5条、独立列表页和详情页
    acceptance_criteria:
      - 学校后台活动列表视图，支持分页、编辑、删除、发布按钮
      - 发布使用富文本编辑器 + 三种发布方式
      - 发布后自动同步至首页动态区块，标注学校名称
      - 前端活动 Tab 显示最新5条，"查看更多"跳转独立列表页
      - 独立列表页 URL：/schools/:id/activities，支持分页
      - 独立详情页 URL：/schools/:id/activities/:activityId
    estimated_complexity: L
    priority: 中

  - story_id: "8.6"
    title: 校共同体月报（管理与展示）
    description: >
      学校管理员提交/编辑/删除月报（列表视图+分页+富文本+按月筛选），
      前端月报 Tab 内展示最新5条、独立列表页和详情页
    acceptance_criteria:
      - 学校后台月报列表视图，支持分页、编辑、删除、提交按钮
      - 使用富文本编辑器，含年份和月份选择
      - 前端月报 Tab 显示最新5条，"查看更多"跳转独立列表页
      - 独立列表页 URL：/schools/:id/reports，支持按年月筛选、分页
      - 独立详情页 URL：/schools/:id/reports/:reportId
      - 月报详情支持附件下载
    estimated_complexity: L
    priority: 中

  - story_id: "8.7"
    title: 成员校管理
    description: >
      学校管理员添加/编辑/删除/排序成员校，
      成员校信息展示到前端校共同体介绍页面
    acceptance_criteria:
      - 成员校列表展示名称、学校代码、排序
      - 支持添加（学校名称必填+学校代码可选）、编辑、删除
      - 支持拖拽排序（前端按此顺序展示）
      - 前端校共同体介绍 Tab 显示成员校数量统计和名称列表
      - 添加/删除成员校后前端实时更新
    estimated_complexity: M
    priority: 中

  - story_id: "8.8"
    title: 示范校页面背景图
    description: >
      学校管理员上传学校背景图（替代纯色背景），
      前端展示背景图并添加遮罩层保证文字可读性
    acceptance_criteria:
      - 学校管理员可上传背景图（建议 1920×400px，jpg/png，≤2MB）
      - 前端使用学校背景图作为页面头部背景
      - 未上传背景图的学校使用平台默认图片
      - 背景图添加适度遮罩层保证文字可读性
      - 每所学校可配置专属背景图
    estimated_complexity: S
    priority: 低

  - story_id: "8.9"
    title: 学校管理后台工作台与个人中心
    description: >
      学校管理后台工作台（最近活动列表+月报记录+分页+快捷操作按钮），
      个人中心（账号设置+修改密码），栏目查看（只读）
    acceptance_criteria:
      - 工作台显示最近发布的活动列表，支持分页、编辑、删除，提供"发布活动"按钮
      - 工作台显示月报提交记录列表，支持分页、编辑、删除，提供"提交月报"按钮
      - 个人中心可修改密码和联系方式
      - 栏目管理页面为只读查看（不可修改）
      - 密码修改需满足复杂度要求（8-20位，大写+小写+数字）
    estimated_complexity: M
    priority: 中

  - story_id: "8.10"
    title: 超管月报管理
    description: >
      超级管理员查看所有学校月报，支持按学校和时间筛选，
      可删除不合规月报并保留删除记录
    acceptance_criteria:
      - 月报列表展示标题、所属学校、年月、状态
      - 支持按学校下拉筛选
      - 支持按时间范围筛选
      - 删除需二次确认，保留删除记录（删除人、时间、原内容摘要）
      - 删除后前端立即不可见
    estimated_complexity: M
    priority: 低
```

---

## Epic 9: 资源共享模块

```yaml
epic_id: 9
title: 资源共享模块
description: >
  实现资源共享全流程：后台发布管理（含分类、置顶），
  前端分类浏览、搜索筛选、在线预览与下载
target_platform: fullstack
stories:

  - story_id: "9.1"
    title: 资源共享数据模型与后端 API
    description: >
      实现资源共享表 CRUD API（参见 7.1.6），支持分类（省外经验/省内经验/研究文献）、
      状态筛选、置顶、文件上传与管理
    acceptance_criteria:
      - CRUD 接口完整
      - 支持按分类（省外经验/省内经验/研究文献）筛选
      - 资源描述字段使用富文本
      - 支持文件上传（PDF/Word/Excel，≤50MB）
      - 超级管理员可删除全站所有资源
      - 删除操作保留删除记录（删除人、时间、原内容摘要）
      - 置顶排序逻辑正确
    estimated_complexity: S
    priority: 高

  - story_id: "9.2"
    title: 资源共享后台管理页面
    description: >
      超级管理员发布/编辑/删除资源；支持分类选择、状态筛选、
      置顶设置、三种发布方式；可删除全站资源
    acceptance_criteria:
      - 列表分页展示（标题/分类/来源/文件大小/状态/置顶/发布时间）
      - 支持按分类和发布状态联合筛选
      - 资源描述使用统一富文本编辑器
      - 使用统一内容发布组件（三种模式）
      - 删除操作保留删除记录
    estimated_complexity: M
    priority: 高

  - story_id: "9.3"
    title: 资源共享前端展示
    description: >
      前端资源共享页面：左侧分类导航、右侧资源列表（含分类标签/来源/文件大小），
      模块内搜索、二级分类筛选、在线预览与下载
    acceptance_criteria:
      - 左侧分类导航栏（省外经验/省内经验/研究文献）
      - 列表显示标题、发布时间、文件大小、所属二级分类、来源
      - 按时间倒序、置顶优先、支持分页
      - 支持关键词搜索（模块内）
      - 支持按二级分类下拉筛选
      - 在线预览功能正常（PDF/文档）
      - 文件下载功能正常
      - 搜索无结果时显示友好提示
    estimated_complexity: L
    priority: 中
```

---

## Epic 10: 全站搜索

```yaml
epic_id: 10
title: 全站搜索
description: >
  实现全站跨模块搜索功能，包括搜索后端服务和前端搜索结果页面
target_platform: fullstack
stories:

  - story_id: "10.1"
    title: 搜索后端服务
    description: >
      实现跨模块搜索 API：搜索范围包括政策文件标题、示范校名称、
      校共同体活动标题、资源标题；支持按模块筛选和分页
    acceptance_criteria:
      - 搜索接口支持关键词查询
      - 搜索范围：政策文件标题、示范校名称、活动标题、资源标题
      - 搜索结果按相关度排序
      - 支持按模块筛选（政策文件/示范校/活动/资源）
      - 支持分页
      - 搜索性能 ≤ 500ms
    estimated_complexity: M
    priority: 高

  - story_id: "10.2"
    title: 搜索前端页面
    description: >
      首页导航栏搜索框集成，搜索结果页分类展示
      （标题/所属模块/发布时间），支持模块筛选和空结果处理
    acceptance_criteria:
      - 导航栏右侧搜索框输入关键词后跳转搜索结果页
      - 搜索结果分类展示（标题、所属模块、发布时间）
      - 点击结果跳转对应详情页
      - 支持按模块下拉筛选
      - 支持分页
      - 无结果时显示"未找到相关内容，请尝试其他关键词"
    estimated_complexity: M
    priority: 中
```

---

## Epic 11: 系统设置与运维

```yaml
epic_id: 11
title: 系统设置与运维
description: >
  实现超管工作台、基础配置、权限设置、敏感词管理、
  数据备份及内容管控功能
target_platform: fullstack
stories:

  - story_id: "11.1"
    title: 超管工作台
    description: >
      超级管理后台工作台：统计卡片（示范校数量/管理员数量/本月活动数/本月月报数）
      和快捷操作入口（添加管理员/发布政策/发布资源/发布新闻）
    acceptance_criteria:
      - 统计卡片实时显示四项数据
      - 快捷操作按钮跳转到对应功能页面
      - 发布新闻/政策的快捷入口直接打开发布表单
      - 数据统计接口响应 ≤ 500ms
      - 页面为登录后默认首页
    estimated_complexity: M
    priority: 中

  - story_id: "11.2"
    title: 基础配置管理
    description: >
      超级管理员管理网站基础配置：网站Logo（支持上传维护）、
      网站名称、版权信息、友情链接
    acceptance_criteria:
      - Logo 支持图片上传和预览
      - 网站名称、版权信息为文本编辑
      - 友情链接支持 CRUD（名称+URL+排序）
      - 保存后前端页脚/导航栏实时更新
      - 配置数据有 Redis 缓存
    estimated_complexity: M
    priority: 高

  - story_id: "11.3"
    title: 权限设置
    description: >
      超级管理员配置管理员权限，支持新增子账号
      （超管角色下的子账号，非学校管理员）
    acceptance_criteria:
      - 支持为超管角色创建子账号
      - 子账号可配置可访问的功能模块
      - 权限变更后立即生效
      - 子账号列表管理（查看/编辑/禁用）
    estimated_complexity: M
    priority: 低

  - story_id: "11.4"
    title: 敏感词管理
    description: >
      超级管理员维护敏感词库：支持批量导入、手动添加、删除；
      内容发布时自动检测敏感词并提示修改
    acceptance_criteria:
      - 敏感词列表分页展示，支持搜索
      - 支持手动逐条添加
      - 支持批量导入（TXT/CSV 文件）
      - 支持单条删除和批量删除
      - 发布内容时自动检测敏感词
      - 命中敏感词时前端提示具体词汇，阻止发布直到修改
    estimated_complexity: M
    priority: 中

  - story_id: "11.5"
    title: 数据备份功能
    description: >
      实现自动每日备份（凌晨2:00-4:00）和手动备份；
      备份内容含数据库和上传文件；保留30天，超期自动清理
    acceptance_criteria:
      - 定时任务每日凌晨 2:00-4:00 执行全量备份
      - 备份包含数据库数据和上传文件（图片/附件）
      - 备份文件存储于独立服务器/云存储
      - 保留最近30天备份，超期自动清理
      - 支持手动触发备份（每日限1次）
      - 后台可查看备份历史和状态
      - 备份失败时告警通知
    estimated_complexity: M
    priority: 低

  - story_id: "11.6"
    title: 内容管控
    description: >
      超级管理员查看全平台内容（含学校发布的活动），
      可直接删除/编辑，保留删除记录
    acceptance_criteria:
      - 按模块分类展示全平台内容（新闻/政策/资源/活动/月报）
      - 支持按模块、学校、时间筛选
      - 可直接编辑或删除任意内容
      - 删除需二次确认
      - 保留删除记录（删除人、时间、原内容摘要）
      - 删除后前端立即不可见
    estimated_complexity: M
    priority: 中
```

---

## 功能覆盖验证

### 前端功能 → Epic/Story 映射

| 功能ID 范围 | 模块 | 对应 Epic | 对应 Story |
|-------------|------|-----------|-----------|
| F-HP-01,03~06 | 首页（导航/轮播/入口/页脚） | Epic 3 | 3.1~3.4 |
| F-HP-02 | 首页（全站搜索框） | Epic 10 | 10.1~10.2 |
| F-SR-01~04 | 搜索 | Epic 10 | 10.1~10.2 |
| F-NW-01~03 | 新闻资讯 | Epic 5 | 5.1~5.3 |
| F-PI-01 | 项目介绍 | Epic 6 | 6.2 |
| F-PF-01~04 | 政策文件 | Epic 7 | 7.3~7.4 |
| F-SC-01~10 | 示范校共体 | Epic 8 | 8.3~8.8 |
| F-RS-01~04 | 资源共享 | Epic 9 | 9.3 |
| F-LG-02~04 | 登录 | Epic 2 | 2.3~2.4 |
| F-PG-01~04 | 分页 | Epic 3 | 3.5 |

### 超管后台功能 → Epic/Story 映射

| 功能ID 范围 | 模块 | 对应 Epic | 对应 Story |
|-------------|------|-----------|-----------|
| B-DB-01~02 | 工作台 | Epic 11 | 11.1 |
| B-UM-01~02 | 用户管理 | Epic 2 | 2.5~2.6 |
| B-CM-01~02 | 栏目管理 | Epic 4 | 4.1~4.2 |
| B-CT-00 | 轮播图管理 | Epic 4 | 4.4 |
| B-CT-01 | 新闻资讯管理 | Epic 5 | 5.2 |
| B-CT-02 | 政策文件管理 | Epic 7 | 7.2 |
| B-CT-03 | 资源共享管理 | Epic 9 | 9.2 |
| B-CT-04 | 内容管控 | Epic 11 | 11.6 |
| B-CT-06 | 项目介绍编辑 | Epic 6 | 6.1 |
| B-CT-07 | 月报管理 | Epic 8 | 8.10 |
| B-SM-01~03 | 示范校管理 | Epic 8 | 8.2 + Epic 2: 2.8 |
| B-SS-01~04 | 系统设置 | Epic 11 | 11.2~11.5 |

### 学校后台功能 → Epic/Story 映射

| 功能ID 范围 | 模块 | 对应 Epic | 对应 Story |
|-------------|------|-----------|-----------|
| S-DB-01~02 | 工作台 | Epic 8 | 8.9 |
| S-PC-01 | 个人中心 | Epic 8 | 8.9 |
| S-CM-01 | 栏目查看 | Epic 8 | 8.9 |
| S-CT-01 | 校共同体介绍 | Epic 8 | 8.4 |
| S-CT-02 | 校共同体活动 | Epic 8 | 8.5 |
| S-CT-03 | 校共同体月报 | Epic 8 | 8.6 |
| S-MB-01~04 | 成员校管理 | Epic 8 | 8.7 |

### 排除项（P1 二期）

| 功能ID | 说明 |
|--------|------|
| F-LG-01 | 普通用户登录（一期隐藏） |
| B-CT-05 | 内容统计 |
| B-SS-05 | 日志管理 |
| S-CT-04 | 内容查询 |
| S-RS-01 | 本地资源管理 |

---
