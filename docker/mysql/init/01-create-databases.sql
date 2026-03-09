-- 创建教育社区平台逻辑数据库
-- 字符集: utf8mb4, 排序规则: utf8mb4_unicode_ci

CREATE DATABASE IF NOT EXISTS `edu_core`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS `edu_content`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS `edu_file`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

-- 授权 root 用户访问所有数据库（开发环境）
GRANT ALL PRIVILEGES ON `edu_core`.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON `edu_content`.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON `edu_file`.* TO 'root'@'%';
FLUSH PRIVILEGES;
