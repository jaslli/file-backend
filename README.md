```sql
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
    `id`            INT        		NOT NULL AUTO_INCREMENT	COMMENT '数据ID',
  	`file_name`     VARCHAR(255)    NOT NULL	            COMMENT '文件名称',
  	`path`      	VARCHAR(255)    NULL		            COMMENT '文件保存路径',
    `hash`      	VARCHAR(255)    NULL		            COMMENT '文件的hash值',
  	`size`        	BIGINT   		DEFAULT 0	            COMMENT '文件大小（字节）',
  	`status`        BIT DEFAULT 0   NOT NULL	            COMMENT '逻辑删除状态 0-正常 1-已删除',
    `create_time`   DATETIME        NOT NULL	            COMMENT '创建时间',
    `update_time`   DATETIME        NOT NULL 	            COMMENT '更新时间',
    PRIMARY KEY (`id`),
    CONSTRAINT path unique (path),
    INDEX `idx_hash` (`hash`)
  ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '文件信息实体类';
```