CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) UNIQUE,
  `email` VARCHAR(100) UNIQUE,
  `phone` VARCHAR(20) UNIQUE,
  `password` VARCHAR(100) NOT NULL,
  `create_time` DATETIME,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; 