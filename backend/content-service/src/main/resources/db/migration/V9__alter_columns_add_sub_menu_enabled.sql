-- Story 4.2: 二级栏目管理 — ALTER columns 表添加 sub_menu_enabled 字段
ALTER TABLE columns ADD COLUMN sub_menu_enabled TINYINT NOT NULL DEFAULT 0
    COMMENT '是否启用二级菜单: 0-否, 1-是' AFTER is_visible;
