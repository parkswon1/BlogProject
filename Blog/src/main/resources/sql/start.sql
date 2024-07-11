drop database BlogDB;

create DATABASE BlogDB;

use BlogDB;

CREATE TABLE `User` (
                        `id` int PRIMARY KEY AUTO_INCREMENT,
                        `username` varchar(255) UNIQUE NOT NULL,
                        `password` varchar(255) NOT NULL,
                        `image_id` int,
                        `created_at` datetime DEFAULT (current_timestamp),
                        `updated_at` datetime DEFAULT (current_timestamp)
);

CREATE TABLE `Blog` (
                        `id` int PRIMARY KEY AUTO_INCREMENT,
                        `title` varchar(255) NOT NULL,
                        `description` text,
                        `user_id` int NOT NULL,
                        `image_id` int,
                        `views` int DEFAULT 0,
                        `created_at` datetime DEFAULT (current_timestamp),
                        `updated_at` datetime DEFAULT (current_timestamp)
);

CREATE TABLE `Post` (
                        `id` int PRIMARY KEY AUTO_INCREMENT,
                        `title` varchar(255) NOT NULL,
                        `content` text NOT NULL,
                        `blog_id` int NOT NULL,
                        `category_id` int NOT NULL,
                        `image_id` int,
                        `views` int DEFAULT 0,
                        `created_at` datetime DEFAULT (current_timestamp),
                        `updated_at` datetime DEFAULT (current_timestamp)
);

CREATE TABLE `Comment` (
                           `id` int PRIMARY KEY AUTO_INCREMENT,
                           `content` text NOT NULL,
                           `user_id` int NOT NULL,
                           `post_id` int NOT NULL,
                           `created_at` datetime DEFAULT (current_timestamp),
                           `updated_at` datetime DEFAULT (current_timestamp)
);

CREATE TABLE `Category` (
                            `id` int PRIMARY KEY AUTO_INCREMENT,
                            `name` varchar(255) UNIQUE NOT NULL,
                            `parent_id` int,
                            `created_at` datetime DEFAULT (current_timestamp),
                            `updated_at` datetime DEFAULT (current_timestamp)
);

CREATE TABLE `Tag` (
                       `id` int PRIMARY KEY AUTO_INCREMENT,
                       `name` varchar(255) UNIQUE NOT NULL,
                       `created_at` datetime DEFAULT (current_timestamp),
                       `updated_at` datetime DEFAULT (current_timestamp)
);

CREATE TABLE `PostTag` (
                           `id` int PRIMARY KEY AUTO_INCREMENT,
                           `post_id` int NOT NULL,
                           `tag_id` int NOT NULL,
                           `created_at` datetime DEFAULT (current_timestamp)
);

CREATE TABLE `Like` (
                        `id` int PRIMARY KEY AUTO_INCREMENT,
                        `user_id` int NOT NULL,
                        `blog_id` int,
                        `post_id` int,
                        `comment_id` int,
                        `created_at` datetime DEFAULT (current_timestamp)
);

CREATE TABLE `Image` (
                         `id` int PRIMARY KEY AUTO_INCREMENT,
                         `url` varchar(255) NOT NULL,
                         'uuid' varchar(255) NOT NULL,
                         'name' varchar(255) NOT NULL,
                         `created_at` datetime DEFAULT (current_timestamp)
);

ALTER TABLE `User` ADD FOREIGN KEY (`image_id`) REFERENCES `Image` (`id`);

ALTER TABLE `Blog` ADD FOREIGN KEY (`user_id`) REFERENCES `User` (`id`);

ALTER TABLE `Blog` ADD FOREIGN KEY (`image_id`) REFERENCES `Image` (`id`);

ALTER TABLE `Post` ADD FOREIGN KEY (`blog_id`) REFERENCES `Blog` (`id`);

ALTER TABLE `Post` ADD FOREIGN KEY (`category_id`) REFERENCES `Category` (`id`);

ALTER TABLE `Post` ADD FOREIGN KEY (`image_id`) REFERENCES `Image` (`id`);

ALTER TABLE `Comment` ADD FOREIGN KEY (`user_id`) REFERENCES `User` (`id`);

ALTER TABLE `Comment` ADD FOREIGN KEY (`post_id`) REFERENCES `Post` (`id`);

ALTER TABLE post_tag ADD FOREIGN KEY (`post_id`) REFERENCES `Post` (`id`);

ALTER TABLE post_tag ADD FOREIGN KEY (`tag_id`) REFERENCES `Tag` (`id`);

ALTER TABLE `Like` ADD FOREIGN KEY (`user_id`) REFERENCES `User` (`id`);

ALTER TABLE `Like` ADD FOREIGN KEY (`blog_id`) REFERENCES `Blog` (`id`);

ALTER TABLE `Like` ADD FOREIGN KEY (`post_id`) REFERENCES `Post` (`id`);

ALTER TABLE `Like` ADD FOREIGN KEY (`comment_id`) REFERENCES `Comment` (`id`);

ALTER TABLE `Category` ADD FOREIGN KEY (`parent_id`) REFERENCES `Category` (`id`);