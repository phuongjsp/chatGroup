-- chat_room_members: table
CREATE TABLE `chat_room_members` (
  `id`           int(11)                                NOT NULL AUTO_INCREMENT,
  `room_chat_id` int(11)                                NOT NULL,
  `username`     varchar(50) COLLATE utf8_vietnamese_ci NOT NULL,
  `time_add`     datetime                                        DEFAULT CURRENT_TIMESTAMP,
  `is_create`    tinyint(1)                                      DEFAULT '0',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 379
  DEFAULT CHARSET = utf8
  COLLATE = utf8_vietnamese_ci;

-- chat_room_messages: table
CREATE TABLE `chat_room_messages` (
  `id`        int(11) NOT NULL                       AUTO_INCREMENT,
  `from_user` varchar(50) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `to_room`   int(11) NOT NULL,
  `content`   text COLLATE utf8_vietnamese_ci,
  `time`      datetime                               DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `chat_room_messages_chatroom_id_fk` (`to_room`),
  CONSTRAINT `chat_room_messages_chatroom_id_fk` FOREIGN KEY (`to_room`) REFERENCES `chatroom` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 395
  DEFAULT CHARSET = utf8
  COLLATE = utf8_vietnamese_ci;

-- No native definition for element:  chat_room_messages_chatroom_id_fk: index
;

-- chatroom: table
CREATE TABLE `chatroom` (
  `id`          int(11) NOT NULL                       AUTO_INCREMENT,
  `name`        varchar(50) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `time_create` datetime                               DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8
  COLLATE = utf8_vietnamese_ci;

-- messages: table
CREATE TABLE `messages` (
  `id`        int(11)                                NOT NULL AUTO_INCREMENT,
  `from_user` varchar(50) COLLATE utf8_vietnamese_ci NOT NULL,
  `to_user`   varchar(50) COLLATE utf8_vietnamese_ci NOT NULL,
  `date_time` datetime                                        DEFAULT CURRENT_TIMESTAMP,
  `content`   text COLLATE utf8_vietnamese_ci,
  `readed`    tinyint(1)                                      DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `messages_from_user_index` (`from_user`),
  KEY `messages_to_user_index` (`to_user`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 395
  DEFAULT CHARSET = utf8
  COLLATE = utf8_vietnamese_ci;

-- No native definition for element:  messages_from_user_index: index
;

-- No native definition for element:  messages_to_user_index: index
;

-- persistent_logins: table
CREATE TABLE `persistent_logins` (
  `username`  varchar(64) NOT NULL,
  `series`    varchar(64)          DEFAULT NULL,
  `token`     varchar(64) NOT NULL,
  `last_used` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP
  ON UPDATE CURRENT_TIMESTAMP
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- post_comment: table
CREATE TABLE `post_comment` (
  `id`        int(11)                                NOT NULL AUTO_INCREMENT,
  `username`  varchar(50) COLLATE utf8_vietnamese_ci NOT NULL,
  `post_id`   int(11)                                NOT NULL,
  `content`   text COLLATE utf8_vietnamese_ci        NOT NULL,
  `date_time` datetime                                        DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `post_comment_post_messages_id_fk` (`post_id`),
  CONSTRAINT `post_comment_post_messages_id_fk` FOREIGN KEY (`post_id`) REFERENCES `post_messages` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 52
  DEFAULT CHARSET = utf8
  COLLATE = utf8_vietnamese_ci;

-- No native definition for element:  post_comment_post_messages_id_fk: index
;

-- post_files: table
CREATE TABLE `post_files` (
  `id`       int(11) NOT NULL                       AUTO_INCREMENT,
  `post_id`  int(11)                                DEFAULT NULL,
  `url_file` text COLLATE utf8_vietnamese_ci,
  `alt`      varchar(50) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_fxk6wwnccjkkdt2s06ryowvs8` (`id`),
  KEY `post_files_post_messages_id_fk` (`post_id`),
  CONSTRAINT `FKajw5025f5q1nroeg7q6iwtg4l` FOREIGN KEY (`id`) REFERENCES `post_images` (`id`),
  CONSTRAINT `post_files_post_messages_id_fk` FOREIGN KEY (`post_id`) REFERENCES `post_messages` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_vietnamese_ci;

-- No native definition for element:  post_files_post_messages_id_fk: index
;

-- post_histories: table
CREATE TABLE `post_histories` (
  `id`        int(11)                                 NOT NULL AUTO_INCREMENT,
  `post_id`   int(11)                                 NOT NULL,
  `content`   text COLLATE utf8_vietnamese_ci         NOT NULL,
  `date_time` datetime                                         DEFAULT NULL,
  `title`     varchar(120) COLLATE utf8_vietnamese_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `post_historys_post_messages_id_fk` (`post_id`),
  CONSTRAINT `post_historys_post_messages_id_fk` FOREIGN KEY (`post_id`) REFERENCES `post_messages` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8
  COLLATE = utf8_vietnamese_ci;

-- No native definition for element:  post_historys_post_messages_id_fk: index
;

-- post_images: table
CREATE TABLE `post_images` (
  `id`       int(11)                                 NOT NULL AUTO_INCREMENT,
  `post_id`  int(11)                                 NOT NULL,
  `url`      varchar(120) COLLATE utf8_vietnamese_ci NOT NULL,
  `name`     varchar(50) COLLATE utf8_vietnamese_ci           DEFAULT NULL,
  `alt`      varchar(255) COLLATE utf8_vietnamese_ci          DEFAULT NULL,
  `url_file` varchar(255) COLLATE utf8_vietnamese_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `Post_Images_post_messages_id_fk` (`post_id`),
  CONSTRAINT `Post_Images_post_messages_id_fk` FOREIGN KEY (`post_id`) REFERENCES `post_messages` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_vietnamese_ci;

-- No native definition for element:  Post_Images_post_messages_id_fk: index
;

-- post_messages: table
CREATE TABLE `post_messages` (
  `id`        int(11)                                 NOT NULL AUTO_INCREMENT,
  `username`  varchar(50) COLLATE utf8_vietnamese_ci  NOT NULL,
  `title`     varchar(120) COLLATE utf8_vietnamese_ci NOT NULL,
  `content`   text COLLATE utf8_vietnamese_ci         NOT NULL,
  `date_time` datetime                                         DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 21
  DEFAULT CHARSET = utf8
  COLLATE = utf8_vietnamese_ci;

-- post_tag: table
CREATE TABLE `post_tag` (
  `id`       int(11)                                NOT NULL AUTO_INCREMENT,
  `post_id`  int(11)                                NOT NULL,
  `username` varchar(32) COLLATE utf8_vietnamese_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `post_tag_post_messages_id_fk` (`post_id`),
  CONSTRAINT `post_tag_post_messages_id_fk` FOREIGN KEY (`post_id`) REFERENCES `post_messages` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 13
  DEFAULT CHARSET = utf8
  COLLATE = utf8_vietnamese_ci;

-- No native definition for element:  post_tag_post_messages_id_fk: index
;

-- post_vote: table
CREATE TABLE `post_vote` (
  `id`        int(11) NOT NULL                       AUTO_INCREMENT,
  `post_id`   int(11) NOT NULL,
  `username`  varchar(50) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `type`      varchar(20) COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `date_time` datetime                               DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `post_vote_username_post_id_uindex` (`username`, `post_id`),
  KEY `post_vote_post_messages_id_fk` (`post_id`),
  CONSTRAINT `post_vote_post_messages_id_fk` FOREIGN KEY (`post_id`) REFERENCES `post_messages` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 436
  DEFAULT CHARSET = utf8
  COLLATE = utf8_vietnamese_ci;

-- No native definition for element:  post_vote_post_messages_id_fk: index
;

-- role: table
CREATE TABLE `role` (
  `id`          int(11) NOT NULL                  AUTO_INCREMENT,
  `name`        varchar(10) CHARACTER SET latin1  DEFAULT NULL,
  `description` varchar(100) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_name_uindex` (`name`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_vietnamese_ci;

-- user: table
CREATE TABLE `user` (
  `id`            int(11) NOT NULL AUTO_INCREMENT,
  `username`      varchar(64)      DEFAULT NULL,
  `password`      varchar(64)      DEFAULT NULL,
  `enable`        tinyint(1)       DEFAULT '1',
  `time_register` datetime         DEFAULT CURRENT_TIMESTAMP,
  `images`        varchar(120)     DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_username_uindex` (`username`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 10
  DEFAULT CHARSET = latin1;

-- users_roles: table
CREATE TABLE `users_roles` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`),
  KEY `users_roles_role_id_fk` (`role_id`),
  CONSTRAINT `users_roles_role_id_fk` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `users_roles_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- No native definition for element:  users_roles_role_id_fk: index
;

