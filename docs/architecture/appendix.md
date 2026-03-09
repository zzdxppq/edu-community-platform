# 附录

## 附录 A: 项目目录结构 {#source-tree}
<!-- Section ID: Source Tree -->

```
edu-community-platform/
├── backend/                        # 后端代码根目录
│   ├── edu-gateway/               # API 网关
│   ├── edu-core/                  # 核心服务 (认证+用户+学校)
│   ├── edu-content/               # 内容服务 (内容+搜索)
│   ├── edu-file/                  # 文件服务
│   └── edu-common/                # 公共模块
│       ├── edu-common-core/       # 核心工具
│       ├── edu-common-security/   # 安全模块
│       ├── edu-common-redis/      # Redis 模块
│       └── edu-common-mybatis/    # MyBatis 模块
│
├── frontend/                       # 前端代码根目录
│   ├── portal/                    # 官网前端 (Nuxt.js)
│   ├── admin/                     # 超级管理后台 (Vue + Element Plus)
│   └── school/                    # 学校管理后台 (Vue + Element Plus)
│
├── docs/                          # 文档目录
│   ├── architecture.md            # 本架构文档
│   ├── api/                       # API 文档
│   └── db/                        # 数据库文档
│
├── deploy/                        # 部署配置
│   ├── docker/                    # Docker 配置
│   ├── k8s/                       # Kubernetes 配置 (可选)
│   └── nginx/                     # Nginx 配置
│
└── scripts/                       # 脚本目录
    ├── init/                      # 初始化脚本
    ├── backup/                    # 备份脚本
    └── deploy/                    # 部署脚本
```

## 附录 B: 配置中心命名规范

```yaml
# Nacos 配置命名规范
# 格式: {service-name}-{profile}.yaml

edu-gateway-dev.yaml          # 网关开发环境配置
edu-gateway-prod.yaml         # 网关生产环境配置
edu-core-dev.yaml             # 核心服务开发配置
edu-core-prod.yaml            # 核心服务生产配置
edu-content-dev.yaml          # 内容服务开发配置
edu-content-prod.yaml         # 内容服务生产配置
edu-file-dev.yaml             # 文件服务开发配置
edu-file-prod.yaml            # 文件服务生产配置
edu-common.yaml               # 公共配置
```

## 附录 C: 参考文档

| 文档 | 链接 |
|------|------|
| Spring Cloud 官方文档 | https://spring.io/projects/spring-cloud |
| Nacos 官方文档 | https://nacos.io/docs |
| 腾讯云 COS 官方文档 | https://cloud.tencent.com/document/product/436 |
| MinIO 官方文档 | https://min.io/docs/minio |
| Vue 3 官方文档 | https://vuejs.org |
| Element Plus 官方文档 | https://element-plus.org |

---

**文档结束**

| 版本 | 日期 | 修订人 | 修订内容 |
|------|------|--------|----------|
| 1.0.0 | 2026-02-04 | Architect Agent | 初始版本 |
| 1.1.0 | 2026-02-04 | Architect Agent | 与 PRD 对齐更新：1) 新增 school_achievements 表(资源成果)；2) 新增外部链接安全规则(6.4)；3) 新增示范校前端主页 URL 规则(6.5)；4) 更新 ER 关系图 |
| 1.2.0 | 2026-02-06 | Architect Agent | 存储策略调整：1) 新增存储抽象层设计 (4.4.1)，支持 COS/MinIO 动态切换；2) 更新基础设施选型，一期使用腾讯云 COS，二期迁移至 MinIO；3) 更新 ADR-004 文件存储决策；4) 更新部署架构图及服务器配置规划 |
| 1.3.0 | 2026-02-06 | Architect Agent | 与 PRD v1.5 全面对齐：1) admin_users 表删除 name 字段，新增 is_first_login 字段(首次登录强制改密)；2) news 表删除 summary 字段(新闻无摘要)；3) resources 表删除 region/organization 字段，新增 source/description 字段；4) policies 表新增 issuing_authority 字段(发文机构)、is_top 字段(置顶)、external_link 字段；5) schools 表新增 background_image 字段、is_visible 字段；6) 新增 school_members 表(成员校管理)；7) 新增 pages 表(单页内容/项目介绍)；8) 新增 site_settings 表(站点配置/Logo)；9) 新增 banners 表(轮播图管理)；10) 更新登录 API 响应移除 name 字段；11) 新增成员校管理 API；12) 新增单页内容 API；13) 更新服务职责描述；14) 更新 ER 关系图 |
