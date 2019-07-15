CREATE TABLE `fans`.`User`(
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `userName` VARCHAR(100) NOT NULL,
  `pwd` VARCHAR(100) NOT NULL,
  `task_id` INT UNSIGNED NOT NULL,
  `status` VARCHAR(100) NOT NULL,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `task_id_key` (`task_id`),
  INDEX `update_time` (`update_time`),
  INDEX `create_time` (`create_time`)
) ENGINE=INNODB CHARSET=utf8mb4;

