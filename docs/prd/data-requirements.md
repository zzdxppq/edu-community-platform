# 七、数据需求

## 7.1 核心数据模型

### 7.1.1 示范校信息

| 字段名 | 字段类型 | 必填 | 说明 | 管理权限 |
|--------|----------|------|------|----------|
| id | int | 是 | 主键 | 系统 |
| name | varchar(100) | 是 | 学校名称 | 超级管理员 |
| code | varchar(50) | 否 | 学校代码 | 超级管理员 |
| level | varchar(20) | 是 | 学段（小学/中学/高中/大学） | 超级管理员 |
| province | varchar(50) | 是 | 省 | 超级管理员 |
| city | varchar(50) | 是 | 市 | 超级管理员 |
| county | varchar(50) | 是 | 县/区（必填） | 超级管理员 |
| phone | varchar(20) | 否 | 联系电话 | 超级管理员 |
| intro | text | 否 | 校共体详细介绍 | 学校管理员 |
| model | text | 否 | 共同体组建模式 | 学校管理员 |
| advantage | text | 否 | 核心优势 | 学校管理员 |
| images | json | 否 | 学校环境图片 | 学校管理员 |
| background_image | varchar(500) | 否 | 页面背景图片URL | 学校管理员 |
| is_visible | tinyint | 是 | 是否在官网显示（0-否/1-是） | 超级管理员 |
| status | int | 是 | 状态（启用/禁用） | 超级管理员 |
| sort_order | int | 是 | 排序（数值越小越靠前） | 超级管理员 |
| created_at | datetime | 是 | 创建时间 | 系统 |
| updated_at | datetime | 是 | 更新时间 | 系统 |

### 7.1.2 成员校信息（新增）

| 字段名 | 字段类型 | 必填 | 说明 |
|--------|----------|------|------|
| id | int | 是 | 主键 |
| school_id | int | 是 | 所属示范校ID |
| name | varchar(100) | 是 | 成员校名称 |
| code | varchar(50) | 否 | 成员校代码 |
| sort_order | int | 是 | 排序（数值越小越靠前） |
| created_by | int | 是 | 创建人ID |
| created_at | datetime | 是 | 创建时间 |
| updated_at | datetime | 是 | 更新时间 |

### 7.1.3 校共同体活动

| 字段名 | 字段类型 | 必填 | 说明 |
|--------|----------|------|------|
| id | int | 是 | 主键 |
| school_id | int | 是 | 所属学校ID |
| title | varchar(200) | 是 | 标题 |
| content | text | 是 | 内容（富文本） |
| attachments | json | 否 | 附件列表 |
| external_link | varchar(500) | 否 | 外部链接 |
| view_count | int | 是 | 浏览次数 |
| status | int | 是 | 状态（草稿/已发布/已删除） |
| publish_time | datetime | 否 | 发布时间 |
| created_by | int | 是 | 创建人ID |
| created_at | datetime | 是 | 创建时间 |
| updated_at | datetime | 是 | 更新时间 |

### 7.1.4 校共同体月报

| 字段名 | 字段类型 | 必填 | 说明 |
|--------|----------|------|------|
| id | int | 是 | 主键 |
| school_id | int | 是 | 所属学校ID |
| title | varchar(200) | 是 | 标题 |
| year | int | 是 | 年份 |
| month | int | 是 | 月份（1-12） |
| content | text | 否 | 内容（富文本） |
| attachments | json | 否 | 附件列表（PDF/Word） |
| status | int | 是 | 状态（草稿/已发布/已删除） |
| created_by | int | 是 | 创建人ID |
| created_at | datetime | 是 | 创建时间 |
| updated_at | datetime | 是 | 更新时间 |

### 7.1.5 政策文件

| 字段名 | 字段类型 | 必填 | 说明 |
|--------|----------|------|------|
| id | int | 是 | 主键 |
| title | varchar(200) | 是 | 标题 |
| doc_number | varchar(100) | 否 | 文号（后台存储，前端详情页不展示） |
| issuing_authority | varchar(200) | 否 | **发文机构**（非必填） |
| category | int | 是 | 分类（1-国家政策/2-省级政策） |
| content | text | 是 | 内容（富文本） |
| pdf_url | varchar(500) | 否 | PDF文件地址 |
| external_link | varchar(500) | 否 | 外部链接 |
| interpretation | text | 否 | 解读材料 |
| is_top | tinyint | 是 | 是否置顶（0-否/1-是） |
| publish_date | date | 是 | 发布日期 |
| status | int | 是 | 状态（1-已发布/2-草稿） |
| sort_order | int | 是 | 排序 |
| created_at | datetime | 是 | 创建时间 |
| updated_at | datetime | 是 | 更新时间 |

### 7.1.6 资源共享

| 字段名 | 字段类型 | 必填 | 说明 |
|--------|----------|------|------|
| id | int | 是 | 主键 |
| title | varchar(200) | 是 | 标题 |
| category | int | 是 | 分类（1-省外经验/2-省内经验/3-研究文献） |
| description | text | 否 | **资源描述**（全站统一富文本编辑器） |
| source | varchar(200) | 否 | 来源（手动输入） |
| file_url | varchar(500) | 否 | 文件地址 |
| external_link | varchar(500) | 否 | 外部链接 |
| file_size | int | 否 | 文件大小（字节） |
| file_type | varchar(20) | 否 | 文件类型 |
| download_count | int | 是 | 下载次数 |
| is_top | tinyint | 是 | 是否置顶（0-否/1-是） |
| status | int | 是 | 状态 |
| created_at | datetime | 是 | 创建时间 |
| updated_at | datetime | 是 | 更新时间 |

> **注意**：资源共享表已去掉 region（地区）、organization（单位）字段，新增 source（来源）、description（资源描述，富文本）字段。

### 7.1.7 新闻资讯

| 字段名 | 字段类型 | 必填 | 说明 |
|--------|----------|------|------|
| id | int | 是 | 主键 |
| title | varchar(200) | 是 | 标题 |
| content | text | 是 | 内容（富文本） |
| cover_image | varchar(500) | 否 | 封面图片 |
| attachments | json | 否 | 附件列表 |
| external_link | varchar(500) | 否 | 外部链接 |
| view_count | int | 是 | 浏览次数 |
| is_top | tinyint | 是 | 是否置顶（0-否/1-是） |
| status | int | 是 | 状态（1-已发布/2-草稿） |
| publish_time | datetime | 否 | 发布时间 |
| created_by | int | 是 | 创建人ID |
| created_at | datetime | 是 | 创建时间 |
| updated_at | datetime | 是 | 更新时间 |

> **注意**：新闻资讯表**无摘要字段**（summary），导航栏不显示，仅通过首页入口访问。

### 7.1.8 管理员账号

| 字段名 | 字段类型 | 必填 | 说明 |
|--------|----------|------|------|
| id | int | 是 | 主键 |
| username | varchar(20) | 是 | 账号（必须为11位手机号） |
| password | varchar(255) | 是 | 密码（加密存储） |
| role | int | 是 | 角色（1-超级管理员/2-学校管理员） |
| school_id | int | 否 | 关联学校ID（学校管理员必填） |
| phone | varchar(20) | 否 | 联系电话 |
| email | varchar(100) | 否 | 邮箱 |
| is_first_login | tinyint | 是 | 是否首次登录（1-是/0-否），首次登录需强制改密 |
| status | int | 是 | 状态（启用/禁用） |
| last_login_at | datetime | 否 | 最后登录时间 |
| created_at | datetime | 是 | 创建时间 |
| updated_at | datetime | 是 | 更新时间 |

> **注意**：管理员账号表已去掉 name（姓名）字段。

### 7.1.9 栏目配置

| 字段名 | 字段类型 | 必填 | 说明 |
|--------|----------|------|------|
| id | int | 是 | 主键 |
| name | varchar(50) | 是 | 栏目名称 |
| code | varchar(50) | 是 | 栏目代码（唯一标识） |
| parent_id | int | 否 | 父级栏目ID（一级栏目为空） |
| level | int | 是 | 栏目层级（1-一级/2-二级） |
| url | varchar(200) | 否 | 栏目链接地址 |
| icon | varchar(200) | 否 | 栏目图标 |
| has_submenu | tinyint | 是 | 是否启用二级菜单（0-否/1-是） |
| is_system | tinyint | 是 | 是否系统预置栏目（0-否/1-是），系统预置栏目不可删除 |
| status | int | 是 | 状态（1-显示/0-隐藏） |
| sort_order | int | 是 | 排序（数值越小越靠前） |
| created_at | datetime | 是 | 创建时间 |
| updated_at | datetime | 是 | 更新时间 |

### 7.1.10 单页内容（项目介绍）

| 字段名 | 字段类型 | 必填 | 说明 |
|--------|----------|------|------|
| id | int | 是 | 主键 |
| page_code | varchar(50) | 是 | 页面代码（唯一标识，如about） |
| project_name | varchar(200) | 是 | **项目名称**（可编辑维护） |
| title | varchar(100) | 是 | 页面标题 |
| content | longtext | 是 | 页面内容（**富文本编辑器**HTML） |
| seo_title | varchar(200) | 否 | SEO标题 |
| seo_keywords | varchar(500) | 否 | SEO关键词 |
| seo_description | varchar(500) | 否 | SEO描述 |
| updated_by | int | 是 | 最后编辑人ID |
| created_at | datetime | 是 | 创建时间 |
| updated_at | datetime | 是 | 更新时间 |

### 7.1.11 操作日志

| 字段名 | 字段类型 | 必填 | 说明 |
|--------|----------|------|------|
| id | int | 是 | 主键 |
| user_id | int | 是 | 操作人ID |
| user_name | varchar(50) | 是 | 操作人姓名 |
| action | varchar(50) | 是 | 操作类型（如：login、delete、impersonate等） |
| target_type | varchar(50) | 否 | 操作对象类型（如：school、report等） |
| target_id | int | 否 | 操作对象ID |
| target_name | varchar(200) | 否 | 操作对象名称 |
| detail | text | 否 | 操作详情 |
| ip | varchar(50) | 否 | 操作IP |
| created_at | datetime | 是 | 操作时间 |

## 7.2 初始数据

### 初始示范校名单

| 序号 | 学校名称 | 学段 | 所属区域 |
|------|----------|------|----------|
| 1 | 信阳董家河小学中心校 | 小学 | 河南省-信阳市-浉河区 |
| 2 | 濮阳市开德小学 | 小学 | 河南省-濮阳市-华龙区 |
| 3 | 扶沟县实验小学 | 小学 | 河南省-周口市-扶沟县 |
| 4 | 修武县方庄中心小学 | 小学 | 河南省-焦作市-修武县 |
| 5 | 栾川县第四实验小学 | 小学 | 河南省-洛阳市-栾川县 |
| 6 | 航空港区科技一街小学 | 小学 | 河南省-郑州市-航空港区 |

### 预置栏目配置

**一级栏目：**

| 序号 | 栏目名称 | 栏目代码 | 是否启用二级菜单 | 是否系统预置 |
|------|----------|----------|------------------|--------------|
| 1 | 首页 | home | 否 | 是 |
| 2 | 项目介绍 | about | 否 | 是 |
| 3 | 政策文件 | policy | 是 | 是 |
| 4 | 示范校共体 | schools | 是（Tab形式） | 是 |
| 5 | 资源共享 | resources | 是 | 是 |
| 6 | 登录 | login | 否 | 是 |

**二级菜单（预置）：**

| 所属一级栏目 | 二级菜单名称 | 菜单代码 |
|--------------|--------------|----------|
| 政策文件 | 国家政策 | policy-national |
| 政策文件 | 省级政策 | policy-provincial |
| 资源共享 | 省外经验 | resources-external |
| 资源共享 | 省内经验 | resources-internal |
| 资源共享 | 研究文献 | resources-research |

> 注：示范校共体的二级内容（校共同体介绍、校共同体活动、校共同体月报）通过右侧Tab切换实现，不作为左侧二级菜单配置。

### 用户身份标签（二期）

| 身份标签 | 说明 |
|----------|------|
| 教师 | 一线教学人员 |
| 教研员 | 教学研究人员 |
| 学校管理者 | 学校行政管理人员 |
| 教育研究者 | 高校/研究机构人员 |
| 家长 | 学生家长 |
| 媒体 | 新闻媒体从业人员 |
| 县级教育行政部门工作人员 | 教育局等部门人员 |

---
