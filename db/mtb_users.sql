CREATE DATABASE IF NOT EXISTS mtb_users;
USE mtb_users;

CREATE TABLE users (
    id            BIGINT          NOT NULL AUTO_INCREMENT,
    email         VARCHAR(255)    NOT NULL,
    password_hash VARCHAR(255)    NOT NULL,   -- BCrypt hash, luôn 60 ký tự
    full_name     VARCHAR(100)    NOT NULL,
    role          ENUM('CUSTOMER','ADMIN') NOT NULL DEFAULT 'CUSTOMER',
    is_active     BOOLEAN         NOT NULL DEFAULT TRUE,  -- FALSE = bị ban
    created_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY uk_email (email)
);
