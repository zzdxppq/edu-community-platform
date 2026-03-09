CREATE TABLE columns (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(50) NOT NULL COMMENT '栏目名称',
    code VARCHAR(50) NOT NULL COMMENT '栏目代码(唯一标识,如 home/intro/policy)',
    icon VARCHAR(100) NULL COMMENT '栏目图标(预留)',
    route_path VARCHAR(200) NULL COMMENT '前端路由路径',
    is_system TINYINT NOT NULL DEFAULT 0 COMMENT '是否系统预置: 0-否, 1-是',
    is_visible TINYINT NOT NULL DEFAULT 1 COMMENT '是否在导航栏显示: 0-隐藏, 1-显示',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序(升序,值越小越靠前)',
    created_by BIGINT NULL COMMENT '创建人ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at DATETIME NULL COMMENT '删除时间(软删除)',

    INDEX idx_columns_code (code),
    INDEX idx_columns_sort_order (sort_order),
    INDEX idx_columns_visible (is_visible)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='一级栏目表';

-- 预置系统栏目
INSERT INTO columns (name, code, route_path, is_system, is_visible, sort_order) VALUES
('首页', 'home', '/', 1, 1, 1),
('项目介绍', 'intro', '/intro', 1, 1, 2),
('政策文件', 'policy', '/policy', 1, 1, 3),
('示范校共体', 'schools', '/schools', 1, 1, 4),
('资源共享', 'resources', '/resources', 1, 1, 5),
('登录', 'login', '/login', 1, 1, 6);
