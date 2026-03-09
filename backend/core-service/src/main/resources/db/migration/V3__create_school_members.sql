CREATE TABLE school_members (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    school_id BIGINT NOT NULL COMMENT '所属示范校ID',
    name VARCHAR(100) NOT NULL COMMENT '成员校名称',
    code VARCHAR(50) NULL COMMENT '成员校代码',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序(升序)',
    created_by BIGINT NOT NULL COMMENT '创建人ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX idx_school_id (school_id),
    INDEX idx_sort_order (sort_order),
    FOREIGN KEY (school_id) REFERENCES schools(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='成员校表';
