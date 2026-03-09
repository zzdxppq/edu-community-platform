# 八、安全架构

## 8.1 等保三级要求

本系统按照 **等保三级** 标准设计，满足以下安全要求：

### 8.1.1 网络安全

| 要求 | 实现方案 |
|------|----------|
| **边界防护** | 防火墙 + WAF，仅开放 80/443 端口 |
| **网络隔离** | DMZ/应用/数据三层隔离，ACL 控制 |
| **入侵检测** | IDS/IPS 部署，实时告警 |
| **DDoS 防护** | 云防护 / 硬件清洗设备 |

### 8.1.2 应用安全

| 要求 | 实现方案 |
|------|----------|
| **身份鉴别** | 强密码策略 + 登录失败锁定 |
| **访问控制** | RBAC 权限模型 |
| **安全审计** | 操作日志完整记录 |
| **通信加密** | 全站 HTTPS (TLS 1.2+) |
| **数据加密** | 密码 BCrypt，敏感数据 AES-256 |

### 8.1.3 数据安全

| 要求 | 实现方案 |
|------|----------|
| **数据备份** | 每日全量 + 增量，异地备份 |
| **备份恢复** | RTO ≤ 4 小时，RPO ≤ 1 小时 |
| **数据脱敏** | 日志中敏感信息脱敏 |
| **数据销毁** | 逻辑删除 + 物理删除策略 |

## 8.2 密码安全

```yaml
password_policy:
  min_length: 8
  max_length: 20
  require_uppercase: true
  require_lowercase: true
  require_digit: true
  require_special: false
  max_age_days: 90
  history_count: 5

login_policy:
  max_fail_attempts: 5
  lockout_duration_minutes: 30
  session_timeout_minutes: 30
  concurrent_sessions: 1
```

## 8.3 安全 Headers

```yaml
security_headers:
  Strict-Transport-Security: "max-age=31536000; includeSubDomains"
  X-Content-Type-Options: "nosniff"
  X-Frame-Options: "DENY"
  X-XSS-Protection: "1; mode=block"
  Content-Security-Policy: "default-src 'self'; script-src 'self' 'unsafe-inline'"
  Referrer-Policy: "strict-origin-when-cross-origin"
```

---
