-- Story 4.2: 二级栏目管理 — 创建二级栏目表 + 预置数据

CREATE TABLE sub_columns (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    column_id BIGINT NOT NULL COMMENT '父栏目ID',
    name VARCHAR(50) NOT NULL COMMENT '二级菜单名称',
    code VARCHAR(50) NOT NULL COMMENT '二级菜单代码',
    category_code VARCHAR(50) NULL COMMENT '关联内容分类代码(用于前端筛选)',
    is_system TINYINT NOT NULL DEFAULT 0 COMMENT '是否系统预置: 0-否, 1-是',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序(升序,值越小越靠前)',
    created_by BIGINT NULL COMMENT '创建人ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at DATETIME NULL COMMENT '删除时间(软删除)',

    INDEX idx_sub_columns_column_id (column_id),
    INDEX idx_sub_columns_sort_order (sort_order),
    FOREIGN KEY (column_id) REFERENCES columns(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='二级栏目表';

-- 启用政策文件和资源共享的二级菜单
UPDATE columns SET sub_menu_enabled = 1 WHERE code IN ('policy', 'resources') AND deleted_at IS NULL;

-- 政策文件二级菜单
INSERT INTO sub_columns (column_id, name, code, category_code, is_system, sort_order)
SELECT id, '国家政策', 'national-policy', 'national', 1, 1 FROM columns WHERE code = 'policy' AND deleted_at IS NULL;

INSERT INTO sub_columns (column_id, name, code, category_code, is_system, sort_order)
SELECT id, '省级政策', 'provincial-policy', 'provincial', 1, 2 FROM columns WHERE code = 'policy' AND deleted_at IS NULL;

-- 资源共享二级菜单
INSERT INTO sub_columns (column_id, name, code, category_code, is_system, sort_order)
SELECT id, '省外经验', 'external-experience', 'external', 1, 1 FROM columns WHERE code = 'resources' AND deleted_at IS NULL;

INSERT INTO sub_columns (column_id, name, code, category_code, is_system, sort_order)
SELECT id, '省内经验', 'internal-experience', 'internal', 1, 2 FROM columns WHERE code = 'resources' AND deleted_at IS NULL;

INSERT INTO sub_columns (column_id, name, code, category_code, is_system, sort_order)
SELECT id, '研究文献', 'research-papers', 'research', 1, 3 FROM columns WHERE code = 'resources' AND deleted_at IS NULL;
