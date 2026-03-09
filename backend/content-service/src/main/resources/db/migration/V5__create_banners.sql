CREATE TABLE banners (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    title VARCHAR(100) NOT NULL COMMENT '标题',
    image_url VARCHAR(500) NOT NULL COMMENT '图片地址',
    link_url VARCHAR(500) NULL COMMENT '跳转链接',
    link_type TINYINT NOT NULL DEFAULT 1 COMMENT '链接类型: 1-内部链接, 2-外部链接',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序(升序)',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-隐藏, 1-显示',
    start_time DATETIME NULL COMMENT '开始展示时间',
    end_time DATETIME NULL COMMENT '结束展示时间',
    created_by BIGINT NOT NULL COMMENT '创建人ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at DATETIME NULL COMMENT '删除时间(软删除)',

    INDEX idx_status (status),
    INDEX idx_sort_order (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='轮播图表';
