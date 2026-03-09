CREATE TABLE site_settings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    setting_key VARCHAR(50) NOT NULL UNIQUE COMMENT '配置键',
    setting_value TEXT NULL COMMENT '配置值',
    setting_type VARCHAR(20) NOT NULL DEFAULT 'text' COMMENT '配置类型: text/image/json',
    description VARCHAR(200) NULL COMMENT '配置说明',
    updated_by BIGINT NULL COMMENT '最后修改人ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX idx_setting_key (setting_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='站点配置表';
