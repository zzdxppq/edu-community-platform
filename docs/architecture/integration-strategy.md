# 六、集成策略

## 6.1 认证与授权

### 6.1.1 认证机制: JWT

| 配置项 | 值 | 说明 |
|--------|-----|------|
| **Token 类型** | Bearer | Authorization: Bearer {token} |
| **签名算法** | HS512 | HMAC-SHA512 |
| **Access Token 有效期** | 15 分钟 | 短期令牌 |
| **Refresh Token 有效期** | 7 天 | 长期令牌 |
| **Token 存储** | Redis | 支持主动失效 |

**JWT Payload 结构**:

```json
{
  "sub": "1",
  "username": "13800138000",
  "role": "super_admin",
  "school_id": null,
  "is_first_login": false,
  "permissions": ["user:read", "user:write", "content:*"],
  "iat": 1706947200,
  "exp": 1706948100,
  "jti": "550e8400-e29b-41d4-a716-446655440000"
}
```

### 6.1.2 授权模式: RBAC

| 角色 | 权限范围 | 说明 |
|------|----------|------|
| **super_admin** | 全平台 | 所有功能 |
| **school_admin** | 本校 | 仅本校内容管理 |

**权限码定义**:

```yaml
permissions:
  # 用户管理
  - user:read        # 查看用户
  - user:write       # 创建/编辑用户
  - user:delete      # 删除用户

  # 内容管理
  - content:read     # 查看内容
  - content:write    # 创建/编辑内容
  - content:delete   # 删除内容
  - content:publish  # 发布内容

  # 学校管理
  - school:read      # 查看学校
  - school:write     # 编辑学校
  - school:delete    # 删除学校
  - school:intro     # 编辑校共体介绍(本校)
  - school:progress  # 管理项目进展(本校)
  - school:report    # 管理月报(本校)
  - school:achievement # 管理资源成果(本校)
  - school:member    # 管理成员校(本校)

  # 系统设置
  - system:config    # 系统配置
  - system:backup    # 数据备份
  - system:log       # 日志查看
```

## 6.2 数据格式标准

| 标准项 | 规范 | 示例 |
|--------|------|------|
| **API 数据格式** | JSON | `Content-Type: application/json` |
| **日期时间格式** | ISO 8601 | `2026-02-04T10:30:00Z` |
| **时区处理** | UTC 存储，前端转换 | 数据库存 UTC，前端显示本地时间 |
| **分页样式** | 偏移分页 | `page=1&size=10` |
| **字段命名** | snake_case | `created_at`, `school_id` |
| **ID 格式** | Long (雪花算法) | `1754398276123456789` |
| **布尔值** | true/false | JSON 原生布尔 |

**统一响应格式**:

```json
{
  "code": 200,
  "message": "success",
  "data": { ... },
  "timestamp": "2026-02-04T10:30:00Z",
  "request_id": "550e8400-e29b-41d4-a716-446655440000"
}
```

**分页响应格式**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "items": [ ... ],
    "pagination": {
      "page": 1,
      "size": 10,
      "total": 156,
      "total_pages": 16
    }
  }
}
```

## 6.3 错误处理标准

**统一错误响应格式**:

```json
{
  "code": 400,
  "message": "请求参数错误",
  "error": {
    "type": "VALIDATION_ERROR",
    "details": [
      {
        "field": "email",
        "message": "邮箱格式不正确",
        "value": "invalid-email"
      }
    ]
  },
  "timestamp": "2026-02-04T10:30:00Z",
  "request_id": "550e8400-e29b-41d4-a716-446655440000",
  "path": "/api/v1/users"
}
```

**HTTP 状态码使用规范**:

| 状态码 | 场景 | 错误类型 |
|--------|------|----------|
| 200 | 成功 | - |
| 201 | 资源创建成功 | - |
| 204 | 删除成功 | - |
| 400 | 请求参数错误 | VALIDATION_ERROR |
| 401 | 未认证 | UNAUTHORIZED |
| 403 | 无权限 | FORBIDDEN |
| 404 | 资源不存在 | NOT_FOUND |
| 409 | 资源冲突 | CONFLICT |
| 422 | 业务逻辑错误 | BUSINESS_ERROR |
| 429 | 请求过于频繁 | RATE_LIMITED |
| 500 | 服务器内部错误 | INTERNAL_ERROR |

## 6.4 外部链接安全规则

根据 PRD 要求，平台内容支持外部链接发布，需遵循以下安全规则：

| 规则 | 说明 |
|------|------|
| **协议限制** | 仅支持 `http://` 和 `https://` 协议，禁止 `javascript:`、`data:` 等危险协议 |
| **跳转提示** | 点击外部链接时弹出提示："您即将离开本平台，访问外部网站，请注意信息安全" |
| **白名单机制** | 可配置信任域名列表，白名单内链接不弹提示直接跳转 |
| **新窗口打开** | 外部链接统一使用 `target="_blank"` + `rel="noopener noreferrer"` |
| **链接标识** | 详情页中外部链接以特殊样式展示（如图标标识），明确区分内外部链接 |
| **链接校验** | 发布时校验链接格式有效性，不校验链接可访问性 |

**白名单默认配置**:

```yaml
external_link:
  whitelist:
    - "*.edu.cn"           # 教育系统域名
    - "*.gov.cn"           # 政府域名
    - "henu.edu.cn"        # 河南大学
    - "haedu.gov.cn"       # 河南省教育厅
```

## 6.5 示范校前端主页 URL 规则

超级管理员可通过后台快速跳转至任意示范校前端主页：

| 配置项 | 规则 |
|--------|------|
| **URL 格式** | `/schools/{school_id}/intro` |
| **快速跳转 API** | `GET /api/v1/schools/{id}/frontend-url` 返回完整前端 URL |
| **后台功能** | 示范校列表每行显示"查看主页"按钮，点击在新窗口打开 |

---
