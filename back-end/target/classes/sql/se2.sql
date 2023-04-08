CREATE TABLE customer
(
    customer_id  INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(50),
    email        VARCHAR(50),
    id_card_info VARCHAR(20),
    credit_score INT,
    password     VARCHAR(50)
);
CREATE TABLE verification_code
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    email       VARCHAR(50) PRIMARY KEY,
    code        VARCHAR(10),
    expire_time DATETIME,
    create_time DATETIME
);
CREATE TABLE railway_info
(
    route_id       INT AUTO_INCREMENT PRIMARY KEY,
    origin         VARCHAR(50),
    destination    VARCHAR(50),
    departure_time DATETIME,
    arrival_time   DATETIME
);
CREATE TABLE ticket_info
(
    ticket_id  INT AUTO_INCREMENT PRIMARY KEY,
    route_id   INT,
    price      DECIMAL(10, 2),
    seat_class INT,
    seat_No    INT,
    selled     INT,
    FOREIGN KEY (route_id) REFERENCES railway_info (route_id)
);
-- 订单表
CREATE TABLE order_info
(
    order_id     INT AUTO_INCREMENT PRIMARY KEY,
    customer_id  INT,
    ticket_id    INT,
    order_status INT,
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id),
    FOREIGN KEY (ticket_id) REFERENCES ticket_info (ticket_id)
);


-- 工作人员表
CREATE TABLE staff_info
(
    staff_id    INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50),
    email       VARCHAR(50),
    password    VARCHAR(50),
    position_id INT,
    FOREIGN KEY (position_id) REFERENCES position_info (position_id)
);
-- 职位信息
CREATE TABLE position_info
(
    position_id   INT AUTO_INCREMENT PRIMARY KEY,
    staff_id      INT,
    position_name VARCHAR(50)

);
-- 订单过期计时器 30 分钟
DELIMITER //
CREATE EVENT update_order_status
    ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 30 MINUTE
    DO
    UPDATE order_info
    SET order_status = 2
    WHERE order_status = 0;
//
DELIMITER ;