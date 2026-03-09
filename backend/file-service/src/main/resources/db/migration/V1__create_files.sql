CREATE TABLE files (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    uuid VARCHAR(36) NOT NULL UNIQUE COMMENT 'UUID',
    bucket VARCHAR(50) NOT NULL COMMENT 'MinIO Bucket',
    object_key VARCHAR(255) NOT NULL COMMENT 'MinIO Object Key',
    original_name VARCHAR(255) NOT NULL COMMENT '原始文件名',
    file_size BIGINT NOT NULL COMMENT '文件大小(字节)',
    mime_type VARCHAR(100) NOT NULL COMMENT 'MIME类型',
    file_ext VARCHAR(20) NOT NULL COMMENT '文件扩展名',
    file_type TINYINT NOT NULL COMMENT '文件类型: 1-图片, 2-文档, 3-视频',
    md5 VARCHAR(32) NULL COMMENT '文件MD5',
    upload_by BIGINT NOT NULL COMMENT '上传人ID',
    ref_count INT NOT NULL DEFAULT 1 COMMENT '引用计数',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted_at DATETIME NULL COMMENT '删除时间(软删除)',

    INDEX idx_uuid (uuid),
    INDEX idx_bucket_key (bucket, object_key),
    INDEX idx_file_type (file_type),
    INDEX idx_upload_by (upload_by)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件表';
