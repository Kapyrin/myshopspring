CREATE TABLE IF NOT EXISTS role
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name   VARCHAR(50)        NOT NULL,
    last_name    VARCHAR(50)        NOT NULL,
    email        VARCHAR(50) UNIQUE NOT NULL,
    password     VARCHAR(60)        NOT NULL,
    phone_number VARCHAR(20) UNIQUE NOT NULL,
    address      VARCHAR(100)       NOT NULL,
    role_id      BIGINT,
    FOREIGN KEY (role_id) REFERENCES role (id)
);

CREATE TABLE IF NOT EXISTS order_status
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    status_name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS shop_order
(
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id         BIGINT NOT NULL,
    order_creation_date DATE   NOT NULL,
    order_close_date    DATE,
    status_id           BIGINT,
    FOREIGN KEY (customer_id) REFERENCES users (id),
    FOREIGN KEY (status_id) REFERENCES order_status (id)
);

CREATE TABLE IF NOT EXISTS product
(
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_name        VARCHAR(50)  NOT NULL,
    product_description VARCHAR(200) NOT NULL,
    price               FLOAT        NOT NULL,
    product_remain      INT          NOT NULL
);


CREATE TABLE IF NOT EXISTS product_order
(
    PRIMARY KEY (order_id, product_id),
    order_id   BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT    NOT NULL,
    FOREIGN KEY (order_id) REFERENCES shop_order (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

INSERT INTO role (user_role)
VALUES ('admin'),
       ('manager'),
       ('customer');

INSERT INTO users (first_name, last_name, email, password, phone_number, address, role_id)
VALUES ('Main', 'Admin', 'admin@admin.ru', '$2a$10$LRjsrby8q3DBWiEFmiCn1.aEiUXozePemEFcIhkdw.G5kYbP9R/oW', '+79997057796', 'Bryansk', 1),

       ('Bill', 'Gates', 'bill@microsot.com', '$2a$10$1cPy8AO4u1issvhp11uU1OEiIAcsLirMnw5nqJ0domeu5lwFWHVya', '0987654321', 'Ostin', 2),

       ('Vladimir', 'Kapyrin', 'vladimir.kapyrin@gmail.com', '$2a$10$w9vK3ZUJT3h0Y.EkybKMJecytid6QYFzZaEJEnamoCYimdIIQ6t6K', '1234567890', 'Russia', 3),

       ('Elon', 'Musk', 'elon@tesla.com', '$2a$10$uzcMruZSdZPwv62wz2i2Beb/FJxQSjHkBD0XMuYFFKn8i4KfB8g.e', '1234509876', 'Redmond', 2);


INSERT INTO order_status (status_name)
VALUES ('Created'),
       ('In build'),
       ('Delivering'),
       ('Delivered-closed'),
       ('Canceled');

INSERT INTO product (product_name, product_description, price, product_remain)
VALUES ('Monitor Acer', 'TFT 27 inch', 26000.0, 20),
       ('Unit AMD', 'Powered by AMD', 35000.0, 50),
       ('Unit Intel', 'Powered by Intel', 30000.0, 50);
