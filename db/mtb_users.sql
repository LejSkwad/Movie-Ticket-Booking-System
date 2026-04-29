SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS mtb_users CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE mtb_users;

CREATE TABLE users (
    id            BIGINT          NOT NULL AUTO_INCREMENT,
    email         VARCHAR(255)    NOT NULL,
    password      VARCHAR(255)    NOT NULL,   -- BCrypt hash, luôn 60 ký tự
    full_name     VARCHAR(100)    NOT NULL,
    role          ENUM('CUSTOMER','ADMIN') NOT NULL DEFAULT 'CUSTOMER',
    is_active     BOOLEAN         NOT NULL DEFAULT TRUE,  -- FALSE = bị ban
    created_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY uk_email (email)
);

-- Seed data (password plaintext = "123456")
INSERT INTO users (email, password, full_name, role, is_active) VALUES
('admin@cinema.com',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin Cinema',   'ADMIN',    true),
('nguyen@gmail.com',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Nguyễn Văn A',   'CUSTOMER', true),
('tran@gmail.com',      '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Trần Thị B',     'CUSTOMER', true),
('le@gmail.com',        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Lê Văn C',       'CUSTOMER', true),
('pham@gmail.com',      '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Phạm Thị D',     'CUSTOMER', false),
('hoang@gmail.com',     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Hoàng Văn E',    'CUSTOMER', false);
