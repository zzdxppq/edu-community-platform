# 后端开发规范

> 河南城乡学校共同体发展平台 - 后端编码规范 v1.0.0
>
> 最后更新：2026-02-06

---

## 一、技术栈概览

| 分类 | 技术 | 版本 |
|------|------|------|
| **JDK** | OpenJDK / Eclipse Temurin | 21 LTS |
| **Spring Boot** | spring-boot-starter | 3.2.x |
| **Spring Cloud** | Spring Cloud | 2023.0.x (Leyton) |
| **服务注册/配置** | Nacos | 2.3.x |
| **ORM** | MyBatis-Plus | 3.5.x |
| **数据库** | MySQL | 8.0.x |
| **缓存** | Redis (Lettuce) | 7.x |
| **对象存储** | 腾讯云 COS / MinIO | - |
| **API 文档** | SpringDoc OpenAPI | 2.x |
| **构建工具** | Maven | 3.9.x |

---

## 二、项目结构规范

### 2.1 模块结构

```
edu-community-platform/
└── backend/
    ├── pom.xml                        # 父 POM
    ├── edu-gateway/                   # API 网关
    ├── edu-core/                      # 核心服务 (认证+用户+学校)
    ├── edu-content/                   # 内容服务 (内容+搜索)
    ├── edu-file/                      # 文件服务
    └── edu-common/                    # 公共模块
        ├── edu-common-core/           # 核心工具
        ├── edu-common-security/       # 安全模块
        ├── edu-common-redis/          # Redis 模块
        └── edu-common-mybatis/        # MyBatis 模块
```

### 2.2 服务内部结构

```
edu-{service}/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/henan/edu/{service}/
│   │   │       ├── {Service}Application.java    # 启动类
│   │   │       ├── config/                      # 配置类
│   │   │       ├── controller/                  # 控制器
│   │   │       ├── service/                     # 服务层
│   │   │       │   └── impl/                    # 服务实现
│   │   │       ├── mapper/                      # MyBatis Mapper
│   │   │       ├── entity/                      # 实体类
│   │   │       ├── dto/                         # 数据传输对象
│   │   │       │   ├── request/                 # 请求 DTO
│   │   │       │   └── response/                # 响应 DTO
│   │   │       ├── vo/                          # 视图对象 (可选)
│   │   │       ├── enums/                       # 枚举类
│   │   │       ├── exception/                   # 自定义异常
│   │   │       ├── handler/                     # 异常处理器
│   │   │       └── util/                        # 工具类
│   │   └── resources/
│   │       ├── bootstrap.yml                    # Nacos 配置
│   │       ├── application.yml                  # 本地配置
│   │       └── mapper/                          # MyBatis XML
│   └── test/
│       └── java/
│           └── com/henan/edu/{service}/
│               ├── controller/                  # 控制器测试
│               ├── service/                     # 服务层测试
│               └── mapper/                      # Mapper 测试
└── pom.xml
```

---

## 三、命名规范

### 3.1 包命名

```
com.henan.edu.{module}.{layer}

示例:
com.henan.edu.core.controller
com.henan.edu.core.service.impl
com.henan.edu.content.mapper
com.henan.edu.file.dto.request
```

### 3.2 类命名

| 分类 | 命名规则 | 示例 |
|------|----------|------|
| **启动类** | `{Module}Application` | `CoreApplication` |
| **Controller** | `{Entity}Controller` | `UserController` |
| **Service 接口** | `{Entity}Service` | `UserService` |
| **Service 实现** | `{Entity}ServiceImpl` | `UserServiceImpl` |
| **Mapper** | `{Entity}Mapper` | `UserMapper` |
| **Entity** | `{Entity}` | `User` |
| **请求 DTO** | `{Action}{Entity}Request` | `CreateUserRequest` |
| **响应 DTO** | `{Entity}{Type}Response` | `UserDetailResponse` |
| **枚举** | `{Entity}{Type}Enum` | `UserStatusEnum` |
| **异常** | `{Type}Exception` | `BusinessException` |
| **配置类** | `{Feature}Config` | `RedisConfig` |
| **工具类** | `{Feature}Utils` | `DateUtils` |
| **常量类** | `{Module}Constants` | `SecurityConstants` |

### 3.3 方法命名

| 场景 | 命名规则 | 示例 |
|------|----------|------|
| **查询单个** | `get{Entity}ById` | `getUserById` |
| **查询列表** | `list{Entity}s` / `find{Entity}s` | `listUsers` |
| **分页查询** | `page{Entity}s` | `pageUsers` |
| **创建** | `create{Entity}` | `createUser` |
| **更新** | `update{Entity}` | `updateUser` |
| **删除** | `delete{Entity}` | `deleteUser` |
| **批量操作** | `batch{Action}{Entity}s` | `batchDeleteUsers` |
| **检查存在** | `exists{Entity}` / `check{Condition}` | `existsByUsername` |
| **统计** | `count{Entity}s` | `countUsers` |
| **私有方法** | `do{Action}` / `handle{Action}` | `doValidate` |

### 3.4 数据库命名

| 分类 | 命名规则 | 示例 |
|------|----------|------|
| **表名** | 小写 + 下划线，复数形式 | `users`, `school_introductions` |
| **字段名** | 小写 + 下划线 (snake_case) | `created_at`, `school_id` |
| **主键** | `id` (推荐) 或 `{table}_id` | `id` |
| **外键** | `{referenced_table}_id` | `user_id`, `school_id` |
| **索引** | `idx_{table}_{columns}` | `idx_users_username` |
| **唯一索引** | `uk_{table}_{columns}` | `uk_users_email` |
| **时间字段** | `{action}_at` | `created_at`, `updated_at` |
| **布尔字段** | `is_{adjective}` | `is_active`, `is_deleted` |
| **状态字段** | `status` 或 `{entity}_status` | `status` |

---

## 四、编码规范

### 4.1 Controller 规范

```java
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "用户管理", description = "用户相关接口")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 获取用户详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情")
    @PreAuthorize("hasAuthority('user:read')")
    public Result<UserDetailResponse> getById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    /**
     * 分页查询用户
     */
    @GetMapping
    @Operation(summary = "分页查询用户")
    @PreAuthorize("hasAuthority('user:read')")
    public Result<PageResult<UserListResponse>> page(
            @Valid UserQueryRequest request,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(userService.page(request, page, size));
    }

    /**
     * 创建用户
     */
    @PostMapping
    @Operation(summary = "创建用户")
    @PreAuthorize("hasAuthority('user:write')")
    public Result<Long> create(@Valid @RequestBody CreateUserRequest request) {
        return Result.success(userService.create(request));
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新用户")
    @PreAuthorize("hasAuthority('user:write')")
    public Result<Void> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        userService.update(id, request);
        return Result.success();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    @PreAuthorize("hasAuthority('user:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.success();
    }
}
```

**Controller 规范要点**:
- 使用 `@RequiredArgsConstructor` 配合 `final` 字段进行依赖注入
- 使用 `@Valid` 进行参数校验
- 使用 `@PreAuthorize` 进行权限控制
- 使用 `@Tag` 和 `@Operation` 添加 API 文档
- Controller 只负责参数接收、校验和响应封装，业务逻辑在 Service 层

### 4.2 Service 规范

```java
// Service 接口
public interface UserService {

    /**
     * 根据ID获取用户详情
     *
     * @param id 用户ID
     * @return 用户详情
     * @throws ResourceNotFoundException 用户不存在时抛出
     */
    UserDetailResponse getById(Long id);

    /**
     * 分页查询用户
     */
    PageResult<UserListResponse> page(UserQueryRequest request, Integer page, Integer size);

    /**
     * 创建用户
     *
     * @param request 创建请求
     * @return 新创建的用户ID
     * @throws BusinessException 用户名或邮箱已存在时抛出
     */
    Long create(CreateUserRequest request);

    /**
     * 更新用户
     */
    void update(Long id, UpdateUserRequest request);

    /**
     * 删除用户 (逻辑删除)
     */
    void delete(Long id);
}

// Service 实现
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public UserDetailResponse getById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new ResourceNotFoundException("用户不存在", id);
        }
        return UserConverter.INSTANCE.toDetailResponse(user);
    }

    @Override
    public PageResult<UserListResponse> page(UserQueryRequest request, Integer page, Integer size) {
        IPage<User> pageResult = userMapper.selectPage(
            new Page<>(page, size),
            buildQueryWrapper(request)
        );
        return PageResult.of(
            pageResult.getRecords().stream()
                .map(UserConverter.INSTANCE::toListResponse)
                .toList(),
            pageResult.getTotal(),
            page,
            size
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(CreateUserRequest request) {
        // 1. 业务校验
        checkUsernameUnique(request.getUsername());
        checkEmailUnique(request.getEmail());

        // 2. 构建实体
        User user = UserConverter.INSTANCE.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(UserStatusEnum.ACTIVE);

        // 3. 持久化
        userMapper.insert(user);

        // 4. 发布事件
        eventPublisher.publishEvent(new UserCreatedEvent(user.getId()));

        log.info("用户创建成功: id={}, username={}", user.getId(), user.getUsername());
        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, UpdateUserRequest request) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new ResourceNotFoundException("用户不存在", id);
        }

        // 更新字段
        UserConverter.INSTANCE.updateEntity(request, user);
        user.setUpdatedAt(LocalDateTime.now());

        userMapper.updateById(user);
        log.info("用户更新成功: id={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new ResourceNotFoundException("用户不存在", id);
        }

        // 逻辑删除
        user.setIsDeleted(true);
        user.setDeletedAt(LocalDateTime.now());
        userMapper.updateById(user);

        log.info("用户删除成功: id={}", id);
    }

    // ==================== 私有方法 ====================

    private void checkUsernameUnique(String username) {
        if (userMapper.existsByUsername(username)) {
            throw new BusinessException("用户名已存在");
        }
    }

    private void checkEmailUnique(String email) {
        if (email != null && userMapper.existsByEmail(email)) {
            throw new BusinessException("邮箱已被使用");
        }
    }

    private LambdaQueryWrapper<User> buildQueryWrapper(UserQueryRequest request) {
        return new LambdaQueryWrapper<User>()
            .like(StrUtil.isNotBlank(request.getKeyword()), User::getUsername, request.getKeyword())
            .eq(request.getStatus() != null, User::getStatus, request.getStatus())
            .eq(request.getRoleId() != null, User::getRoleId, request.getRoleId())
            .eq(User::getIsDeleted, false)
            .orderByDesc(User::getCreatedAt);
    }
}
```

**Service 规范要点**:
- 接口方法必须有 Javadoc，说明参数、返回值和可能抛出的异常
- 使用 `@Transactional(rollbackFor = Exception.class)` 进行事务管理
- 写操作必须有日志记录
- 业务校验放在方法开头
- 私有方法以 `check`、`build`、`do`、`handle` 等开头
- 使用 MapStruct 进行对象转换

### 4.3 Entity 规范

```java
@Data
@TableName("users")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 密码 (BCrypt 加密)
     */
    @TableField("password")
    private String password;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 用户状态
     */
    @TableField("status")
    private UserStatusEnum status;

    /**
     * 角色ID
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 所属学校ID (学校管理员)
     */
    @TableField("school_id")
    private Long schoolId;

    /**
     * 最后登录时间
     */
    @TableField("last_login_at")
    private LocalDateTime lastLoginAt;
}

// 基类
@Data
public abstract class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 是否删除
     */
    @TableField("is_deleted")
    @TableLogic
    private Boolean isDeleted;

    /**
     * 删除时间
     */
    @TableField("deleted_at")
    private LocalDateTime deletedAt;
}
```

### 4.4 DTO 规范

```java
// 请求 DTO
@Data
@Schema(description = "创建用户请求")
public class CreateUserRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20之间")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]*$", message = "用户名只能包含字母、数字和下划线，且以字母开头")
    @Schema(description = "用户名", example = "zhangsan")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 20, message = "密码长度必须在8-20之间")
    @Schema(description = "密码")
    private String password;

    @NotBlank(message = "真实姓名不能为空")
    @Size(max = 50, message = "真实姓名不能超过50个字符")
    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱", example = "zhangsan@example.com")
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    @NotNull(message = "角色ID不能为空")
    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "所属学校ID")
    private Long schoolId;
}

// 响应 DTO
@Data
@Schema(description = "用户详情响应")
public class UserDetailResponse {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "用户状态")
    private UserStatusEnum status;

    @Schema(description = "角色信息")
    private RoleResponse role;

    @Schema(description = "所属学校信息")
    private SchoolBriefResponse school;

    @Schema(description = "最后登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginAt;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
```

**DTO 规范要点**:
- 请求 DTO 使用 JSR-380 校验注解
- 响应 DTO 不包含敏感字段 (如 password)
- 使用 `@Schema` 添加字段描述
- 时间字段使用 `@JsonFormat` 格式化

### 4.5 Mapper 规范

```java
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 检查用户名是否存在
     */
    @Select("SELECT COUNT(1) > 0 FROM users WHERE username = #{username} AND is_deleted = 0")
    boolean existsByUsername(@Param("username") String username);

    /**
     * 检查邮箱是否存在
     */
    @Select("SELECT COUNT(1) > 0 FROM users WHERE email = #{email} AND is_deleted = 0")
    boolean existsByEmail(@Param("email") String email);

    /**
     * 根据用户名查询 (包含角色信息)
     */
    @Select("""
        SELECT u.*, r.name as role_name, r.code as role_code
        FROM users u
        LEFT JOIN roles r ON u.role_id = r.id
        WHERE u.username = #{username} AND u.is_deleted = 0
        """)
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "username", column = "username"),
        @Result(property = "role.name", column = "role_name"),
        @Result(property = "role.code", column = "role_code")
    })
    UserWithRole findByUsername(@Param("username") String username);

    /**
     * 复杂查询使用 XML
     */
    List<UserStatisticsVO> selectUserStatistics(@Param("query") UserStatisticsQuery query);
}
```

**Mapper 规范要点**:
- 简单查询使用注解方式
- 复杂查询使用 XML 文件
- 方法名遵循 `select`、`insert`、`update`、`delete`、`count`、`exists` 前缀
- 参数使用 `@Param` 注解

### 4.6 枚举规范

```java
@Getter
@AllArgsConstructor
public enum UserStatusEnum implements IEnum<Integer> {

    INACTIVE(0, "未激活"),
    ACTIVE(1, "正常"),
    LOCKED(2, "锁定"),
    DISABLED(3, "禁用");

    private final Integer value;
    private final String desc;

    @Override
    public Integer getValue() {
        return this.value;
    }

    /**
     * 根据值获取枚举
     */
    public static UserStatusEnum of(Integer value) {
        for (UserStatusEnum status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的用户状态: " + value);
    }

    /**
     * JSON 序列化
     */
    @JsonValue
    public Integer jsonValue() {
        return this.value;
    }

    /**
     * JSON 反序列化
     */
    @JsonCreator
    public static UserStatusEnum fromJson(Integer value) {
        return of(value);
    }
}
```

---

## 五、异常处理规范

### 5.1 异常体系

```
RuntimeException
└── BaseException (基础异常)
    ├── BusinessException (业务异常 - 422)
    ├── ResourceNotFoundException (资源不存在 - 404)
    ├── UnauthorizedException (未认证 - 401)
    ├── ForbiddenException (无权限 - 403)
    ├── ValidationException (参数校验失败 - 400)
    └── RemoteServiceException (远程服务调用失败 - 502)
```

### 5.2 异常定义

```java
@Getter
public abstract class BaseException extends RuntimeException {

    private final String code;
    private final int httpStatus;
    private final Object[] args;

    protected BaseException(String code, int httpStatus, String message, Object... args) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
        this.args = args;
    }
}

public class BusinessException extends BaseException {

    public BusinessException(String message) {
        super("BUSINESS_ERROR", 422, message);
    }

    public BusinessException(String code, String message) {
        super(code, 422, message);
    }
}

public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(String resource, Object id) {
        super("NOT_FOUND", 404, resource + "不存在: " + id);
    }
}
```

### 5.3 全局异常处理

```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Void>> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return ResponseEntity
            .status(e.getHttpStatus())
            .body(Result.fail(e.getCode(), e.getMessage()));
    }

    /**
     * 资源不存在
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Result<Void>> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.warn("资源不存在: {}", e.getMessage());
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(Result.fail("NOT_FOUND", e.getMessage()));
    }

    /**
     * 参数校验失败
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Void>> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining("; "));
        log.warn("参数校验失败: {}", message);
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Result.fail("VALIDATION_ERROR", message));
    }

    /**
     * 权限不足
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Result<Void>> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("权限不足: {}", e.getMessage());
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(Result.fail("FORBIDDEN", "无权限访问该资源"));
    }

    /**
     * 未知异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleException(Exception e, HttpServletRequest request) {
        String requestId = request.getHeader("X-Request-Id");
        log.error("系统异常 [requestId={}]: {}", requestId, e.getMessage(), e);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Result.fail("INTERNAL_ERROR", "系统繁忙，请稍后重试"));
    }
}
```

---

## 六、日志规范

### 6.1 日志级别使用

| 级别 | 使用场景 |
|------|----------|
| **ERROR** | 系统错误，需要立即处理，如数据库连接失败、第三方服务不可用 |
| **WARN** | 预期内的业务异常，如用户名已存在、权限不足 |
| **INFO** | 关键业务操作，如用户登录、订单创建、数据变更 |
| **DEBUG** | 开发调试信息，生产环境不开启 |
| **TRACE** | 详细追踪信息，仅在排查问题时临时开启 |

### 6.2 日志格式

```java
// 正确示例
log.info("用户登录成功: userId={}, ip={}", userId, ip);
log.warn("登录失败，密码错误: username={}, attempt={}", username, attemptCount);
log.error("数据库查询异常: sql={}, params={}", sql, params, exception);

// 错误示例
log.info("用户登录成功: " + userId);  // 不要使用字符串拼接
log.info("用户信息: {}", user.toString());  // 避免大对象 toString
log.error("异常");  // 缺少异常对象和上下文
```

### 6.3 敏感信息脱敏

```java
@Slf4j
public class UserServiceImpl {

    public void createUser(CreateUserRequest request) {
        // 日志中不要记录密码
        log.info("创建用户: username={}, email={}",
            request.getUsername(),
            DesensitizeUtils.email(request.getEmail()));
    }
}

// 脱敏工具类
public class DesensitizeUtils {

    public static String phone(String phone) {
        if (phone == null || phone.length() != 11) return phone;
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    public static String email(String email) {
        if (email == null || !email.contains("@")) return email;
        int atIndex = email.indexOf("@");
        return email.substring(0, Math.min(3, atIndex)) + "***" + email.substring(atIndex);
    }

    public static String idCard(String idCard) {
        if (idCard == null || idCard.length() < 10) return idCard;
        return idCard.substring(0, 6) + "********" + idCard.substring(idCard.length() - 4);
    }
}
```

---

## 七、安全规范

### 7.1 SQL 注入防护

```java
// 正确: 使用参数化查询
@Select("SELECT * FROM users WHERE username = #{username}")
User findByUsername(@Param("username") String username);

// 正确: MyBatis-Plus Wrapper
new LambdaQueryWrapper<User>()
    .eq(User::getUsername, username);

// 错误: 字符串拼接
@Select("SELECT * FROM users WHERE username = '" + username + "'")  // 严禁!
```

### 7.2 XSS 防护

```java
// 使用 HTML 转义
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new XssStringJsonSerializer());
        mapper.registerModule(module);
        return mapper;
    }
}

public class XssStringJsonSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        if (value != null) {
            gen.writeString(HtmlUtils.htmlEscape(value));
        }
    }
}
```

### 7.3 敏感数据加密

```java
// 密码加密 (BCrypt)
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

// 使用
String encoded = passwordEncoder.encode(rawPassword);
boolean matches = passwordEncoder.matches(rawPassword, encoded);

// 敏感字段 AES 加密 (如手机号、身份证)
@TableField(typeHandler = EncryptTypeHandler.class)
private String idCard;
```

### 7.4 接口安全

```java
// 1. 必须进行权限校验
@PreAuthorize("hasAuthority('user:delete')")
public void deleteUser(Long id) { ... }

// 2. 操作自己的资源需验证归属
public void updateProfile(Long userId, UpdateProfileRequest request) {
    Long currentUserId = SecurityContextHolder.getCurrentUserId();
    if (!currentUserId.equals(userId)) {
        throw new ForbiddenException("无权修改他人资料");
    }
    // ...
}

// 3. 批量操作限制数量
@Size(max = 100, message = "单次最多操作100条")
private List<Long> ids;
```

---

## 八、性能规范

### 8.1 数据库优化

```java
// 1. 禁止 SELECT *
@Select("SELECT id, username, real_name, status FROM users WHERE id = #{id}")
UserBrief selectBriefById(@Param("id") Long id);

// 2. 分页必须限制大小
@Max(value = 100, message = "每页最多100条")
private Integer size;

// 3. 批量插入使用 MyBatis-Plus 批量方法
@Transactional(rollbackFor = Exception.class)
public void batchCreate(List<User> users) {
    if (CollUtil.isEmpty(users)) return;
    // 分批插入，每批 500 条
    List<List<User>> batches = ListUtil.split(users, 500);
    for (List<User> batch : batches) {
        userMapper.insertBatchSomeColumn(batch);
    }
}

// 4. 使用索引覆盖
// 确保查询条件字段和返回字段都在索引中
@Select("SELECT id, username FROM users WHERE status = #{status}")
List<UserIdAndName> selectIdAndNameByStatus(@Param("status") Integer status);
```

### 8.2 缓存使用

```java
// 1. 使用 Spring Cache
@Cacheable(value = "user", key = "#id", unless = "#result == null")
public UserDetailResponse getById(Long id) { ... }

@CacheEvict(value = "user", key = "#id")
public void update(Long id, UpdateUserRequest request) { ... }

@CacheEvict(value = "user", allEntries = true)
public void batchUpdate(List<Long> ids) { ... }

// 2. 设置合理的过期时间
@Bean
public RedisCacheConfiguration cacheConfiguration() {
    return RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofMinutes(30))
        .serializeKeysWith(...)
        .serializeValuesWith(...);
}

// 3. 防止缓存穿透
@Cacheable(value = "user", key = "#id", unless = "#result == null")
public UserDetailResponse getById(Long id) {
    User user = userMapper.selectById(id);
    if (user == null) {
        // 缓存空值，短时间过期
        return null;  // Spring Cache 会缓存 null
    }
    return UserConverter.INSTANCE.toDetailResponse(user);
}
```

### 8.3 N+1 问题

```java
// 错误: N+1 查询
public List<UserWithRoleResponse> listUsers() {
    List<User> users = userMapper.selectList(null);
    return users.stream()
        .map(user -> {
            Role role = roleMapper.selectById(user.getRoleId());  // N 次查询!
            return new UserWithRoleResponse(user, role);
        })
        .toList();
}

// 正确: JOIN 查询或批量查询
public List<UserWithRoleResponse> listUsers() {
    // 方式1: JOIN 查询
    return userMapper.selectUsersWithRole();

    // 方式2: 批量查询后关联
    List<User> users = userMapper.selectList(null);
    Set<Long> roleIds = users.stream()
        .map(User::getRoleId)
        .collect(Collectors.toSet());
    Map<Long, Role> roleMap = roleMapper.selectBatchIds(roleIds).stream()
        .collect(Collectors.toMap(Role::getId, Function.identity()));

    return users.stream()
        .map(user -> new UserWithRoleResponse(user, roleMap.get(user.getRoleId())))
        .toList();
}
```

---

## 九、测试规范

### 9.1 测试分层

| 层级 | 覆盖率要求 | 说明 |
|------|-----------|------|
| **Service 单元测试** | ≥ 80% | 核心业务逻辑 |
| **Controller 测试** | ≥ 60% | API 接口测试 |
| **Mapper 测试** | 关键查询 | 复杂 SQL 验证 |
| **集成测试** | 核心流程 | 端到端测试 |

### 9.2 单元测试示例

```java
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("创建用户成功")
    void createUser_Success() {
        // Given
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testuser");
        request.setPassword("password123");
        request.setRealName("测试用户");
        request.setRoleId(1L);

        when(userMapper.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
        when(userMapper.insert(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return 1;
        });

        // When
        Long userId = userService.create(request);

        // Then
        assertThat(userId).isEqualTo(1L);
        verify(userMapper).insert(argThat(user ->
            user.getUsername().equals("testuser") &&
            user.getPassword().equals("encoded_password")
        ));
        verify(eventPublisher).publishEvent(any(UserCreatedEvent.class));
    }

    @Test
    @DisplayName("创建用户失败 - 用户名已存在")
    void createUser_UsernameExists() {
        // Given
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("existinguser");

        when(userMapper.existsByUsername("existinguser")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.create(request))
            .isInstanceOf(BusinessException.class)
            .hasMessage("用户名已存在");

        verify(userMapper, never()).insert(any());
    }

    @Test
    @DisplayName("获取用户详情 - 用户不存在")
    void getById_NotFound() {
        // Given
        when(userMapper.selectById(999L)).thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> userService.getById(999L))
            .isInstanceOf(ResourceNotFoundException.class);
    }
}
```

### 9.3 Controller 测试示例

```java
@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(authorities = "user:read")
    @DisplayName("获取用户详情")
    void getById_Success() throws Exception {
        // Given
        UserDetailResponse response = new UserDetailResponse();
        response.setId(1L);
        response.setUsername("testuser");

        when(userService.getById(1L)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/v1/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data.id").value(1))
            .andExpect(jsonPath("$.data.username").value("testuser"));
    }

    @Test
    @WithMockUser(authorities = "user:write")
    @DisplayName("创建用户 - 参数校验失败")
    void create_ValidationFailed() throws Exception {
        // Given
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("ab");  // 太短

        // When & Then
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }
}
```

---

## 十、Git 提交规范

### 10.1 分支命名

| 分支类型 | 命名规则 | 示例 |
|----------|----------|------|
| **主分支** | `main` | main |
| **开发分支** | `develop` | develop |
| **功能分支** | `feature/{story-id}-{description}` | feature/US-001-user-login |
| **修复分支** | `bugfix/{issue-id}-{description}` | bugfix/BUG-123-login-error |
| **热修复** | `hotfix/{issue-id}-{description}` | hotfix/BUG-456-critical-fix |
| **发布分支** | `release/v{version}` | release/v1.0.0 |

### 10.2 提交消息格式

```
<type>(<scope>): <subject>

<body>

<footer>
```

**Type 类型**:

| Type | 说明 |
|------|------|
| `feat` | 新功能 |
| `fix` | Bug 修复 |
| `docs` | 文档更新 |
| `style` | 代码格式调整 (不影响逻辑) |
| `refactor` | 重构 (非新功能、非Bug修复) |
| `perf` | 性能优化 |
| `test` | 测试相关 |
| `chore` | 构建、依赖等杂项 |

**示例**:

```
feat(user): 添加用户登录功能

- 实现基于 JWT 的认证
- 添加登录失败次数限制
- 支持记住我功能

Closes #123
```

---

## 附录

### 附录 A: 常用依赖版本

```xml
<properties>
    <java.version>21</java.version>
    <spring-boot.version>3.2.2</spring-boot.version>
    <spring-cloud.version>2023.0.0</spring-cloud.version>
    <spring-cloud-alibaba.version>2023.0.0.0</spring-cloud-alibaba.version>
    <mybatis-plus.version>3.5.5</mybatis-plus.version>
    <hutool.version>5.8.25</hutool.version>
    <mapstruct.version>1.5.5.Final</mapstruct.version>
    <springdoc.version>2.3.0</springdoc.version>
</properties>
```

### 附录 B: IDE 配置

**IntelliJ IDEA 推荐插件**:
- Lombok
- MapStruct Support
- MyBatisX
- SonarLint
- CheckStyle-IDEA

**代码格式化**:
- 使用项目根目录下的 `.editorconfig`
- Import 顺序：`java.` → `javax.` → `org.` → `com.` → `static`
- 缩进：4 空格

### 附录 C: Checkstyle 规则摘要

```xml
<!-- 主要规则 -->
<module name="LineLength"><property name="max" value="120"/></module>
<module name="MethodLength"><property name="max" value="50"/></module>
<module name="ParameterNumber"><property name="max" value="7"/></module>
<module name="NeedBraces"/>
<module name="LeftCurly"><property name="option" value="eol"/></module>
<module name="EmptyBlock"/>
<module name="EqualsHashCode"/>
<module name="IllegalImport"/>
<module name="UnusedImports"/>
```

---

| 版本 | 日期 | 修订人 | 修订内容 |
|------|------|--------|----------|
| 1.0.0 | 2026-02-06 | Architect Agent | 初始版本 |
