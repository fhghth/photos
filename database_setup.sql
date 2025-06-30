-- 确保users表有avatar字段
-- 如果avatar字段不存在，则添加它

USE vue20250610;

-- 检查avatar字段是否存在，如果不存在则添加
ALTER TABLE users ADD COLUMN IF NOT EXISTS avatar VARCHAR(255) DEFAULT NULL;

-- 查看表结构确认
DESCRIBE users; 