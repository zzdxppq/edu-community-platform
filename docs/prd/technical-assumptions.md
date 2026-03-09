# 三（补）、技术假设

## TA-01 仓库结构

| 项目 | 说明 |
|------|------|
| **仓库模式** | 单仓库（Monorepo） |
| **仓库名称** | edu-community-platform |
| **代码组织** | 前后端代码在同一仓库内，按目录分离（backend/、frontend/） |

## TA-02 技术约束

| 约束 | 说明 |
|------|------|
| **后端框架** | Spring Boot 3.2.x + Spring Cloud 2023.0.x |
| **前端框架** | 官网：Vue 3 + Nuxt 3（SSR）；后台：Vue 3 + Element Plus |
| **数据库** | MySQL 8.0（主从复制） |
| **缓存** | Redis 7（Sentinel 哨兵模式） |
| **文件存储** | 一期腾讯云 COS，后期迁移 MinIO（通过存储抽象层动态切换） |
| **服务注册/配置** | Nacos 2.3.x |
| **容器化** | Docker + Docker Compose（一期）/ K8s（后期） |
| **运行环境** | JDK 21 LTS |

## TA-03 架构模式

| 项目 | 说明 |
|------|------|
| **架构风格** | Spring Cloud 微服务 |
| **服务拆分** | 3 个业务服务（core-service、content-service、file-service）+ API Gateway |
| **认证方案** | JWT（Access Token 15min + Refresh Token 7天）+ Redis Token 黑名单 |
| **授权模型** | RBAC（super_admin / school_admin） |
| **安全等级** | 等保三级 |

## TA-04 开发环境要求

| 工具 | 版本要求 |
|------|----------|
| JDK | 21 LTS |
| Node.js | 20.x LTS |
| Maven | 3.9.x |
| Docker | 24.x |
| IDE | IntelliJ IDEA（推荐）/ VS Code |

## TA-05 一期平台约束

| 约束 | 说明 |
|------|------|
| 仅 Web 端 | 一期不支持移动端适配 |
| 仅管理员登录 | 一期不支持普通用户注册登录 |
| 分辨率 | 支持 1280×720 及以上 |
| 浏览器 | Chrome 80+、Firefox 75+、Edge 80+、Safari 13+ |

---
