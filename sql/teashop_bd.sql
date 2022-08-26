CREATE DATABASE `teashop_db`;

USE `teashop_db`;

CREATE TABLE user_role(
	role_id BIGINT PRIMARY KEY AUTO_INCREMENT,
	role_name VARCHAR(50) NOT NULL
);

CREATE TABLE `users` (
user_id BIGINT NOT NULL AUTO_INCREMENT,
login VARCHAR(20) NOT NULL UNIQUE,
is_active BOOL NOT NULL,
`password` VARCHAR(32) NOT NULL,
role_id BIGINT NOT NULL,
CONSTRAINT PK_user PRIMARY KEY  (user_id),
CONSTRAINT FK_users_role
FOREIGN KEY (role_id) REFERENCES user_role (role_id)
ON DELETE RESTRICT
);

CREATE TABLE user_info(
user_id BIGINT NOT NULL,
email VARCHAR(255),
first_name VARCHAR(25),
last_name VARCHAR(25),
phone INT UNSIGNED,
address VARCHAR(100),
CONSTRAINT UN_clients_email UNIQUE(email),
CONSTRAINT PK_user_info PRIMARY KEY (user_id),
CONSTRAINT FK_user_info FOREIGN KEY (user_id)
REFERENCES `users` (user_id)
ON DELETE RESTRICT
);

CREATE TABLE category (
category_id BIGINT NOT NULL AUTO_INCREMENT,
name_category VARCHAR(100) NOT NULL,
CONSTRAINT PK_category PRIMARY KEY (category_id)
);

ALTER TABLE category
ADD COLUMN is_active BOOL NOT NULL;

CREATE TABLE products(
	product_id BIGINT AUTO_INCREMENT,
    category_id BIGINT NOT NULL,
	name_product VARCHAR(50),
	picture_path VARCHAR(200),
	composition VARCHAR(200),
	weight DECIMAL(8,2),
	brewing_time TIME,
	water_temperature INT,
	price DECIMAL(8,2),
    is_active BOOL NOT NULL,
    CONSTRAINT PK_products PRIMARY KEY (product_id),
    CONSTRAINT FK_products_category FOREIGN KEY (`category_id`)
	REFERENCES `category` (`category_id`)
	ON DELETE RESTRICT
);

CREATE TABLE orders(
order_id BIGINT AUTO_INCREMENT,
order_date TIMESTAMP,
payment_method VARCHAR(10) CHECK (payment_method IN ('CACHE','CREDIT_CARD')),
user_id BIGINT NOT NULL,
address VARCHAR(100),
total_cost DECIMAL(8,2),
CONSTRAINT PK_orders PRIMARY KEY (order_id),
CONSTRAINT FK_orders_user_id
FOREIGN KEY (user_id) REFERENCES users (user_id)
ON DELETE RESTRICT
);
ALTER TABLE orders
    ADD COLUMN order_status VARCHAR(20) CHECK (order_status IN ('waiting','accepted','rejected'));

ALTER TABLE orders MODIFY payment_method VARCHAR(12);

CREATE TABLE orders_products(
order_id BIGINT NOT NULL,
product_id BIGINT NOT NULL,
quantity INT,
CONSTRAINT FK_orders_products_order_id
FOREIGN KEY (order_id) REFERENCES orders(order_id)
ON DELETE RESTRICT,
CONSTRAINT FK_orders_products_product_id
FOREIGN KEY (product_id) REFERENCES products(product_id)
ON DELETE RESTRICT
);

CREATE TABLE favorite_products(
user_id BIGINT NOT NULL,
product_id BIGINT NOT NULL,
CONSTRAINT FK_favorite_products_product_id
FOREIGN KEY (product_id) REFERENCES products(product_id)
ON DELETE RESTRICT,
CONSTRAINT FK_favorite_products_user_id
FOREIGN KEY (user_id) REFERENCES users(user_id)
ON DELETE RESTRICT
);

