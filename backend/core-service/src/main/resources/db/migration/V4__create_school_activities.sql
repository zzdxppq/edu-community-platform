CREATE TABLE school_activities (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    school_id BIGINT NOT NULL COMMENT '学校ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    content LONGTEXT NOT NULL COMMENT '内容(富文本)',
    cover_image VARCHAR(255) NULL COMMENT '封面图',
    attachments JSON NULL COMMENT '附件列表',
    external_link VARCHAR(500) NULL COMMENT '外部链接',
    news_id BIGINT NULL COMMENT '同步到新闻的ID',
    view_count INT NOT NULL DEFAULT 0 COMMENT '浏览次数',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-草稿, 1-已发布',
    publish_time DATETIME NULL COMMENT '发布时间',
    created_by BIGINT NOT NULL COMMENT '创建人ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at DATETIME NULL COMMENT '删除时间(软删除)',

    INDEX idx_school_id (school_id),
    INDEX idx_status (status),
    INDEX idx_publish_time (publish_time),
    FOREIGN KEY (school_id) REFERENCES schools(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='校共同体活动表';
