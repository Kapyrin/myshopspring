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
    password     VARCHAR(50)        NOT NULL,
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
    product_name        VARCHAR(50)    NOT NULL,
    product_description VARCHAR(200)   NOT NULL,
    price               FLOAT(15, 2)   NOT NULL,
    product_remain      INT            NOT NULL
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
