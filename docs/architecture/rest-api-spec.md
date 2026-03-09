# 四、API 契约摘要 {#rest-api-spec}
<!-- Section ID: REST API Spec -->

## 4.1 API 版本策略

- **版本格式**: URL 路径版本，如 `/api/v1/users`
- **当前版本**: v1
- **向后兼容**: 新版本发布后，旧版本保留至少 6 个月

## 4.2 核心服务 API (core-service)

### 4.2.1 认证 API

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| POST | `/api/v1/auth/login` | 管理员登录 | 公开 |
| POST | `/api/v1/auth/logout` | 退出登录 | 需认证 |
| POST | `/api/v1/auth/refresh` | 刷新 Token | 需认证 |
| PUT | `/api/v1/auth/password` | 修改密码 | 需认证 |
| POST | `/api/v1/auth/password/reset` | 重置密码 | 超级管理员 |

**登录请求/响应示例**:

```json
// POST /api/v1/auth/login
// Request
{
  "username": "admin",
  "password": "encrypted_password",
  "role": "super_admin"  // super_admin | school_admin
}

// Response 200 OK
{
  "code": 200,
  "message": "success",
  "data": {
    "access_token": "eyJhbGciOiJIUzI1NiIs...",
    "refresh_token": "eyJhbGciOiJIUzI1NiIs...",
    "expires_in": 900,
    "token_type": "Bearer",
    "user": {
      "id": 1,
      "username": "admin",
      "role": "super_admin",
      "school_id": null,
      "is_first_login": false
    }
  }
}
```

### 4.2.2 用户管理 API

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | `/api/v1/users` | 获取用户列表 | 超级管理员 |
| GET | `/api/v1/users/{id}` | 获取用户详情 | 需认证 |
| POST | `/api/v1/users` | 创建用户 | 超级管理员 |
| PUT | `/api/v1/users/{id}` | 更新用户 | 超级管理员 |
| DELETE | `/api/v1/users/{id}` | 删除用户 | 超级管理员 |
| PUT | `/api/v1/users/{id}/status` | 启用/禁用用户 | 超级管理员 |
| GET | `/api/v1/users/profile` | 获取当前用户信息 | 需认证 |
| PUT | `/api/v1/users/profile` | 更新当前用户信息 | 需认证 |

### 4.2.3 学校管理 API

**示范校**

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | `/api/v1/schools` | 获取学校列表 | 公开 |
| GET | `/api/v1/schools/{id}` | 获取学校详情 | 公开 |
| POST | `/api/v1/schools` | 创建学校 | 超级管理员 |
| PUT | `/api/v1/schools/{id}` | 更新学校基础信息 | 超级管理员 |
| PUT | `/api/v1/schools/{id}/intro` | 更新校共体介绍 | 学校管理员 |
| DELETE | `/api/v1/schools/{id}` | 删除学校 | 超级管理员 |
| PUT | `/api/v1/schools/{id}/status` | 启用/禁用学校 | 超级管理员 |

**校共同体活动**

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | `/api/v1/schools/{schoolId}/activities` | 获取校共同体活动列表 | 公开 |
| GET | `/api/v1/schools/{schoolId}/activities/{id}` | 获取活动详情 | 公开 |
| POST | `/api/v1/schools/{schoolId}/activities` | 创建校共同体活动 | 学校管理员 |
| PUT | `/api/v1/schools/{schoolId}/activities/{id}` | 更新校共同体活动 | 学校管理员 |
| DELETE | `/api/v1/schools/{schoolId}/activities/{id}` | 删除校共同体活动 | 学校管理员 |

**月报管理**

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | `/api/v1/schools/{schoolId}/reports` | 获取月报列表 | 需认证 |
| GET | `/api/v1/schools/{schoolId}/reports/{id}` | 获取月报详情 | 需认证 |
| POST | `/api/v1/schools/{schoolId}/reports` | 创建月报 | 学校管理员 |
| PUT | `/api/v1/schools/{schoolId}/reports/{id}` | 更新月报 | 学校管理员 |
| DELETE | `/api/v1/schools/{schoolId}/reports/{id}` | 删除月报 | 学校管理员 |

**资源成果（二期规划，一期不实现）**

> **注意**：资源成果功能为二期规划内容，一期暂不实现。以下API定义保留供二期参考。

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | `/api/v1/schools/{schoolId}/achievements` | 获取资源成果列表 | 公开 |
| GET | `/api/v1/schools/{schoolId}/achievements/{id}` | 获取成果详情 | 公开 |
| POST | `/api/v1/schools/{schoolId}/achievements` | 创建资源成果 | 学校管理员 |
| PUT | `/api/v1/schools/{schoolId}/achievements/{id}` | 更新资源成果 | 学校管理员 |
| DELETE | `/api/v1/schools/{schoolId}/achievements/{id}` | 删除资源成果 | 学校管理员 |

**成员校管理**

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | `/api/v1/schools/{schoolId}/members` | 获取成员校列表 | 公开 |
| POST | `/api/v1/schools/{schoolId}/members` | 添加成员校 | 学校管理员 |
| PUT | `/api/v1/schools/{schoolId}/members/{id}` | 更新成员校 | 学校管理员 |
| DELETE | `/api/v1/schools/{schoolId}/members/{id}` | 删除成员校 | 学校管理员 |
| PUT | `/api/v1/schools/{schoolId}/members/sort` | 成员校排序 | 学校管理员 |

## 4.3 内容服务 API (content-service)

### 4.3.1 政策文件

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | `/api/v1/policies` | 获取政策列表 | 公开 |
| GET | `/api/v1/policies/{id}` | 获取政策详情 | 公开 |
| POST | `/api/v1/policies` | 创建政策 | 超级管理员 |
| PUT | `/api/v1/policies/{id}` | 更新政策 | 超级管理员 |
| DELETE | `/api/v1/policies/{id}` | 删除政策 | 超级管理员 |

### 4.3.2 新闻资讯

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | `/api/v1/news` | 获取新闻列表 | 公开 |
| GET | `/api/v1/news/{id}` | 获取新闻详情 | 公开 |
| POST | `/api/v1/news` | 创建新闻 | 需认证 |
| PUT | `/api/v1/news/{id}` | 更新新闻 | 需认证 |
| DELETE | `/api/v1/news/{id}` | 删除新闻 | 超级管理员 |
| GET | `/api/v1/news/{id}/views` | 获取浏览统计 | 超级管理员 |

### 4.3.3 资源共享

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | `/api/v1/resources` | 获取资源列表 | 公开 |
| GET | `/api/v1/resources/{id}` | 获取资源详情 | 公开 |
| POST | `/api/v1/resources` | 创建资源 | 超级管理员 |
| PUT | `/api/v1/resources/{id}` | 更新资源 | 超级管理员 |
| DELETE | `/api/v1/resources/{id}` | 删除资源 | 超级管理员 |
| GET | `/api/v1/resources/{id}/download` | 下载资源 | 公开 |

### 4.3.4 栏目管理

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | `/api/v1/columns` | 获取栏目列表 | 公开 |
| GET | `/api/v1/columns/{id}` | 获取栏目详情 | 公开 |
| POST | `/api/v1/columns` | 创建栏目 | 超级管理员 |
| PUT | `/api/v1/columns/{id}` | 更新栏目 | 超级管理员 |
| DELETE | `/api/v1/columns/{id}` | 删除栏目 | 超级管理员 |
| PUT | `/api/v1/columns/{id}/sort` | 栏目排序 | 超级管理员 |

### 4.3.5 轮播图管理

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | `/api/v1/banners` | 获取轮播图列表 | 公开 |
| POST | `/api/v1/banners` | 创建轮播图 | 超级管理员 |
| PUT | `/api/v1/banners/{id}` | 更新轮播图 | 超级管理员 |
| DELETE | `/api/v1/banners/{id}` | 删除轮播图 | 超级管理员 |

### 4.3.6 搜索 API

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | `/api/v1/search` | 全站搜索 | 公开 |
| GET | `/api/v1/search/policies` | 搜索政策文件 | 公开 |
| GET | `/api/v1/search/news` | 搜索新闻资讯 | 公开 |
| GET | `/api/v1/search/schools` | 搜索示范校 | 公开 |
| GET | `/api/v1/search/resources` | 搜索资源共享 | 公开 |
| POST | `/api/v1/search/index/rebuild` | 重建索引 | 超级管理员 |

**搜索请求/响应示例**:

```json
// GET /api/v1/search?keyword=教育&module=all&page=1&size=10

// Response 200 OK
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 56,
    "page": 1,
    "size": 10,
    "items": [
      {
        "id": 1,
        "title": "河南省义务教育一体化实施方案",
        "module": "policy",
        "module_name": "政策文件",
        "summary": "...关于推进城乡教育一体化发展...",
        "publish_time": "2026-01-15T08:00:00Z",
        "url": "/policy/1"
      }
    ]
  }
}
```

## 4.4 文件服务 API (file-service)

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| POST | `/api/v1/files/upload` | 上传文件 | 需认证 |
| POST | `/api/v1/files/upload/image` | 上传图片 | 需认证 |
| GET | `/api/v1/files/{id}` | 获取文件信息 | 公开 |
| GET | `/api/v1/files/{id}/download` | 下载文件 | 公开 |
| GET | `/api/v1/files/{id}/preview` | 预览文件 | 公开 |
| DELETE | `/api/v1/files/{id}` | 删除文件 | 需认证 |

**文件上传请求/响应示例**:

```json
// POST /api/v1/files/upload
// Request: multipart/form-data
// - file: 文件二进制
// - type: image | document | video

// Response 200 OK
{
  "code": 200,
  "message": "success",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "filename": "policy.pdf",
    "original_name": "教育政策文件.pdf",
    "size": 1048576,
    "mime_type": "application/pdf",
    "url": "/api/v1/files/550e8400-e29b-41d4-a716-446655440000/download",
    "preview_url": "/api/v1/files/550e8400-e29b-41d4-a716-446655440000/preview",
    "created_at": "2026-02-04T10:30:00Z"
  }
}
```

### 4.4.1 存储抽象层设计

文件服务采用 **策略模式** 实现存储抽象，支持腾讯云 COS 和 MinIO 动态切换。

**存储接口定义**:

```java
public interface StorageService {

    /**
     * 上传文件
     * @param inputStream 文件流
     * @param objectKey 对象键 (路径)
     * @param contentType MIME类型
     * @return 文件访问信息
     */
    FileInfo upload(InputStream inputStream, String objectKey, String contentType);

    /**
     * 下载文件
     * @param objectKey 对象键
     * @return 文件流
     */
    InputStream download(String objectKey);

    /**
     * 删除文件
     * @param objectKey 对象键
     */
    void delete(String objectKey);

    /**
     * 获取预签名URL (用于前端直传或临时访问)
     * @param objectKey 对象键
     * @param expireSeconds 过期时间(秒)
     * @return 预签名URL
     */
    String getPresignedUrl(String objectKey, int expireSeconds);

    /**
     * 获取公开访问URL
     * @param objectKey 对象键
     * @return 访问URL
     */
    String getPublicUrl(String objectKey);

    /**
     * 检查文件是否存在
     * @param objectKey 对象键
     * @return 是否存在
     */
    boolean exists(String objectKey);
}
```

**存储配置 (Nacos)**:

```yaml
# edu-file-prod.yaml

storage:
  # 存储类型: cos | minio
  type: cos

  # 腾讯云 COS 配置 (一期使用)
  cos:
    secret-id: ${COS_SECRET_ID}
    secret-key: ${COS_SECRET_KEY}
    region: ap-guangzhou
    bucket: edu-community-1234567890
    # CDN 加速域名 (可选)
    cdn-domain: https://cdn.example.com
    # 访问域名
    endpoint: https://edu-community-1234567890.cos.ap-guangzhou.myqcloud.com
    # 临时凭证有效期 (秒)
    presigned-expire: 3600

  # MinIO 配置 (后期迁移)
  minio:
    endpoint: http://minio.internal:9000
    access-key: ${MINIO_ACCESS_KEY}
    secret-key: ${MINIO_SECRET_KEY}
    bucket: edu-community
    # 外部访问域名
    external-endpoint: https://storage.example.com
    presigned-expire: 3600

  # 通用配置
  common:
    # 文件大小限制 (MB)
    max-file-size: 200
    # 允许的图片类型
    allowed-image-types: jpg,jpeg,png,gif,webp
    # 允许的文档类型
    allowed-document-types: pdf,doc,docx,xls,xlsx,ppt,pptx
    # 允许的视频类型
    allowed-video-types: mp4,mov,avi
    # 文件路径格式: {type}/{year}/{month}/{day}/{uuid}.{ext}
    path-pattern: "{type}/{year}/{month}/{day}/{uuid}.{ext}"
```

**动态切换实现**:

```java
@Component
@RefreshScope  // 支持 Nacos 配置热更新
public class StorageServiceFactory {

    @Value("${storage.type:cos}")
    private String storageType;

    @Autowired
    private TencentCosStorage cosStorage;

    @Autowired
    private MinioStorage minioStorage;

    public StorageService getStorageService() {
        return switch (storageType) {
            case "cos" -> cosStorage;
            case "minio" -> minioStorage;
            default -> throw new IllegalArgumentException("Unknown storage type: " + storageType);
        };
    }
}
```

**切换步骤** (从 COS 迁移到 MinIO):

| 步骤 | 操作 | 说明 |
|------|------|------|
| 1 | 部署 MinIO 集群 | 准备好 MinIO 环境并测试连通性 |
| 2 | 数据迁移 | 使用 `rclone` 或脚本将 COS 数据同步到 MinIO |
| 3 | 并行运行 | 配置双写模式，新文件同时写入 COS 和 MinIO |
| 4 | 切换读取 | 修改 Nacos 配置 `storage.type=minio`，服务自动切换 |
| 5 | 验证 | 验证文件访问正常后，停止 COS 双写 |
| 6 | 清理 | 确认无误后，清理 COS 上的数据 |

## 4.5 系统设置 API (分布在 core-service 和 content-service)

### 4.5.1 站点配置

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | `/api/v1/settings/site` | 获取站点配置(名称、Logo、版权) | 公开 |
| PUT | `/api/v1/settings/site` | 更新站点配置 | 超级管理员 |
| POST | `/api/v1/settings/site/logo` | 上传网站Logo | 超级管理员 |
| GET | `/api/v1/settings/links` | 获取友情链接 | 公开 |
| PUT | `/api/v1/settings/links` | 更新友情链接 | 超级管理员 |

### 4.5.2 单页内容

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | `/api/v1/pages/{code}` | 获取单页内容(如about) | 公开 |
| PUT | `/api/v1/pages/{code}` | 更新单页内容 | 超级管理员 |

**项目介绍页请求/响应示例**:

```json
// GET /api/v1/pages/about

// Response 200 OK
{
  "code": 200,
  "message": "success",
  "data": {
    "page_code": "about",
    "project_name": "河南省县域城乡义务教育学校共同体建设",
    "title": "项目介绍",
    "content": "<p>项目内容HTML...</p>",
    "updated_at": "2026-02-06T10:30:00Z"
  }
}
```

### 4.5.3 敏感词管理

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | `/api/v1/settings/sensitive-words` | 获取敏感词列表 | 超级管理员 |
| POST | `/api/v1/settings/sensitive-words` | 添加敏感词 | 超级管理员 |
| POST | `/api/v1/settings/sensitive-words/import` | 批量导入敏感词 | 超级管理员 |
| DELETE | `/api/v1/settings/sensitive-words/{id}` | 删除敏感词 | 超级管理员 |

### 4.5.4 数据备份

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | `/api/v1/settings/backups` | 获取备份列表 | 超级管理员 |
| POST | `/api/v1/settings/backups` | 手动触发备份 | 超级管理员 |

---
