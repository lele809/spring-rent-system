-- 修复数据库表结构，确保字段名与实体类映射一致
USE rent_system;

-- 检查并修复contacts_old表结构
-- 如果字段是room_id，改名为roomId
SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'rent_system' 
AND TABLE_NAME = 'contacts_old' 
AND COLUMN_NAME IN ('room_id', 'roomId');

-- 修复contacts_old表
ALTER TABLE contacts_old CHANGE COLUMN room_id roomId VARCHAR(20) NOT NULL;

-- 修复contacts_new表  
ALTER TABLE contacts_new CHANGE COLUMN room_id roomId VARCHAR(20) NOT NULL;

-- 显示修复后的表结构
DESCRIBE contacts_old;
DESCRIBE contacts_new; 