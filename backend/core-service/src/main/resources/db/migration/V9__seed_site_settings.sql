-- 预置站点配置（4条）
-- 数据来源: docs/architecture/database-schema.md §5.2.11

INSERT INTO site_settings (setting_key, setting_value, setting_type, description) VALUES
('site_logo', NULL, 'image', '网站Logo'),
('site_name', '河南城乡学校共同体发展平台', 'text', '网站名称'),
('copyright', NULL, 'text', '版权信息'),
('friend_links', '[]', 'json', '友情链接列表');
