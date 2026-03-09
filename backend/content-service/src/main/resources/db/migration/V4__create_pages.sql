CREATE TABLE pages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    page_code VARCHAR(50) NOT NULL UNIQUE COMMENT '页面代码(唯一标识,如about)',
    project_name VARCHAR(200) NULL COMMENT '项目名称(项目介绍页专用)',
    title VARCHAR(100) NOT NULL COMMENT '页面标题',
    content LONGTEXT NOT NULL COMMENT '页面内容(富文本HTML)',
    seo_title VARCHAR(200) NULL COMMENT 'SEO标题',
    seo_keywords VARCHAR(500) NULL COMMENT 'SEO关键词',
    seo_description VARCHAR(500) NULL COMMENT 'SEO描述',
    updated_by BIGINT NOT NULL COMMENT '最后编辑人ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX idx_page_code (page_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='单页内容表';
