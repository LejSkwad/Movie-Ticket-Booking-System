SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS mtb_cinemas CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE mtb_cinemas;

CREATE TABLE rooms (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100)    NOT NULL,
    type        ENUM('LARGE','STANDARD','SWEETBOX','FOUR_DX','STARIUM') NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE seats (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    room_id     BIGINT          NOT NULL,
    row_label   CHAR(1)         NOT NULL,
    col_number  INT             NOT NULL,
    seat_type   ENUM('STANDARD','VIP','SWEETBOX','FOUR_DX') NOT NULL,
    pair_id     BIGINT          NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (room_id) REFERENCES rooms(id)
);

CREATE TABLE seat_price_config (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    seat_type   ENUM('STANDARD','VIP','SWEETBOX','FOUR_DX'),
    price       DECIMAL(10,0)   NOT NULL,
    PRIMARY KEY (id)
);pose

CREATE TABLE show_times (
    id          BIGINT                              NOT NULL AUTO_INCREMENT,
    movie_id    BIGINT                              NOT NULL,
    room_id     BIGINT                              NOT NULL,
    format      ENUM('TWO_D','THREE_D','FOUR_DX','IMAX')        NOT NULL,
    start_time  DATETIME                            NOT NULL,
    end_time    DATETIME                            NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (room_id) REFERENCES rooms(id)
);

CREATE TABLE showtime_seats (
    id          BIGINT  NOT NULL AUTO_INCREMENT,
    showtime_id BIGINT  NOT NULL,
    seat_id     BIGINT  NOT NULL,
    status      ENUM('AVAILABLE','BOOKED') NOT NULL DEFAULT 'AVAILABLE',
    PRIMARY KEY (id),
    UNIQUE KEY uk_showtime_seat (showtime_id, seat_id),
    FOREIGN KEY (showtime_id) REFERENCES show_times(id),
    FOREIGN KEY (seat_id) REFERENCES seats(id)
);

-- ============================================================
-- SEED: Rooms (10 phòng)
-- Phòng 1–3: layout 2D cũ (CGV style) — STANDARD type
-- Phòng 4–6: layout 2D mới (Standard style) — STANDARD type
-- Phòng 7–8: Sweetbox — SWEETBOX type
-- Phòng 9:   STARIUM — STARIUM type
-- Phòng 10:  4DX — 4DX type
-- ============================================================
INSERT INTO rooms (id, name, type) VALUES
( 1, 'Phòng 1',   'LARGE'),
( 2, 'Phòng 2',   'LARGE'),
( 3, 'Phòng 3',   'LARGE'),
( 4, 'Phòng 4',   'STANDARD'),
( 5, 'Phòng 5',   'STANDARD'),
( 6, 'Phòng 6',   'STANDARD'),
( 7, 'Phòng 7',   'SWEETBOX'),
( 8, 'Phòng 8',   'SWEETBOX'),
( 9, 'STARIUM',   'STARIUM'),
(10, 'Phòng 4DX', 'FOUR_DX');

-- ============================================================
-- SEED: Seat price config (reference)
-- ============================================================
INSERT INTO seat_price_config (seat_type, price) VALUES
('STANDARD',  75000),
('VIP',       95000),
('SWEETBOX', 200000),
('FOUR_DX',      130000);

-- ============================================================
-- SEED: Seats
--
-- Phòng 1–3 (layout CGV 2D):
--   A–C: STANDARD 75k, col 1–19
--   D–L: VIP 95k,      col 1–19
--   Row M: SWEETBOX 150k, col 1–22 (11 cặp/phòng)
--   pair_id: Room1→1-11, Room2→12-22, Room3→23-33
--
-- Phòng 4–6 (layout Standard 2D):
--   A–C: STANDARD 80k, col 1–18
--   D–H: VIP 100k,     col 1–18
--   Row J: SWEETBOX 180k, col 1–16 (8 cặp/phòng)
--   pair_id: Room4→34-41, Room5→42-49, Room6→50-57
--
-- Phòng 7–8 (layout Sweetbox):
--   A–C: L1(8,7) L2(6,5) R1(4,3) R2(2,1) — 4 cặp/hàng
--   D: L1(8,7) L2(6,5) — 2 cặp
--   price: 250k/ghế  (14 cặp/phòng)
--   pair_id: Room7→58-71, Room8→72-85
--
-- Phòng 9 (STARIUM):
--   A–E: STANDARD 90k, col 1–20
--   F–K: VIP 120k,     col 1–20
--   L–R: VIP 120k,     col 1–27 (thêm left wing)
--   Row S: SWEETBOX 160k, col 1–26 (13 cặp)
--   pair_id: 86–98
--
-- Phòng 10 (4DX):
--   A–F: 4DX 130k, col 1–20
-- ============================================================

DELIMITER $$

CREATE PROCEDURE seed_seats()
BEGIN
    DECLARE i_row    INT DEFAULT 1;
    DECLARE i_col    INT DEFAULT 1;
    DECLARE row_ch   CHAR(1);
    DECLARE s_type   VARCHAR(10);
    DECLARE pair_ctr INT DEFAULT 1;
    DECLARE cur_room INT;

    -- ── Phòng 1–3: layout CGV 2D ────────────────────────────────
    SET cur_room = 1;
    WHILE cur_room <= 3 DO
        SET i_row = 1;
        WHILE i_row <= 12 DO
            SET row_ch = CHAR(64 + i_row);
            IF i_row <= 3 THEN SET s_type = 'STANDARD';
            ELSE               SET s_type = 'VIP';
            END IF;
            SET i_col = 1;
            WHILE i_col <= 19 DO
                INSERT INTO seats (room_id, row_label, col_number, seat_type, pair_id)
                VALUES (cur_room, row_ch, i_col, s_type, NULL);
                SET i_col = i_col + 1;
            END WHILE;
            SET i_row = i_row + 1;
        END WHILE;
        -- Row M: 11 cặp (col 22→1)
        SET i_col = 22;
        WHILE i_col >= 2 DO
            INSERT INTO seats (room_id, row_label, col_number, seat_type, pair_id)
            VALUES (cur_room, 'M', i_col,     'SWEETBOX', pair_ctr),
                   (cur_room, 'M', i_col - 1, 'SWEETBOX', pair_ctr);
            SET pair_ctr = pair_ctr + 1;
            SET i_col = i_col - 2;
        END WHILE;
        SET cur_room = cur_room + 1;
    END WHILE;

    -- ── Phòng 4–6: layout Standard 2D ───────────────────────────
    SET cur_room = 4;
    WHILE cur_room <= 6 DO
        SET i_row = 1;
        WHILE i_row <= 8 DO
            SET row_ch = CHAR(64 + i_row);
            IF i_row <= 3 THEN SET s_type = 'STANDARD';
            ELSE               SET s_type = 'VIP';
            END IF;
            -- Row A chỉ có 17 cột (A1–A17), các hàng còn lại đủ 18
            SET i_col = 1;
            WHILE i_col <= 18 DO
                IF NOT (row_ch = 'A' AND i_col = 18) THEN
                    INSERT INTO seats (room_id, row_label, col_number, seat_type, pair_id)
                    VALUES (cur_room, row_ch, i_col, s_type, NULL);
                END IF;
                SET i_col = i_col + 1;
            END WHILE;
            SET i_row = i_row + 1;
        END WHILE;
        -- Row J: 8 cặp (col 16→1)
        SET i_col = 16;
        WHILE i_col >= 2 DO
            INSERT INTO seats (room_id, row_label, col_number, seat_type, pair_id)
            VALUES (cur_room, 'J', i_col,     'SWEETBOX', pair_ctr),
                   (cur_room, 'J', i_col - 1, 'SWEETBOX', pair_ctr);
            SET pair_ctr = pair_ctr + 1;
            SET i_col = i_col - 2;
        END WHILE;
        SET cur_room = cur_room + 1;
    END WHILE;

    -- ── Phòng 7–8: layout Sweetbox ──────────────────────────────
    SET cur_room = 7;
    WHILE cur_room <= 8 DO
        SET i_row = 1;
        WHILE i_row <= 3 DO
            SET row_ch = CHAR(64 + i_row);
            INSERT INTO seats (room_id, row_label, col_number, seat_type, pair_id)
            VALUES (cur_room, row_ch, 8, 'SWEETBOX', pair_ctr),
                   (cur_room, row_ch, 7, 'SWEETBOX', pair_ctr);
            SET pair_ctr = pair_ctr + 1;
            INSERT INTO seats (room_id, row_label, col_number, seat_type, pair_id)
            VALUES (cur_room, row_ch, 6, 'SWEETBOX', pair_ctr),
                   (cur_room, row_ch, 5, 'SWEETBOX', pair_ctr);
            SET pair_ctr = pair_ctr + 1;
            INSERT INTO seats (room_id, row_label, col_number, seat_type, pair_id)
            VALUES (cur_room, row_ch, 4, 'SWEETBOX', pair_ctr),
                   (cur_room, row_ch, 3, 'SWEETBOX', pair_ctr);
            SET pair_ctr = pair_ctr + 1;
            INSERT INTO seats (room_id, row_label, col_number, seat_type, pair_id)
            VALUES (cur_room, row_ch, 2, 'SWEETBOX', pair_ctr),
                   (cur_room, row_ch, 1, 'SWEETBOX', pair_ctr);
            SET pair_ctr = pair_ctr + 1;
            SET i_row = i_row + 1;
        END WHILE;
        -- Row D: L1 + L2 only
        INSERT INTO seats (room_id, row_label, col_number, seat_type, pair_id)
        VALUES (cur_room, 'D', 8, 'SWEETBOX', pair_ctr),
               (cur_room, 'D', 7, 'SWEETBOX', pair_ctr);
        SET pair_ctr = pair_ctr + 1;
        INSERT INTO seats (room_id, row_label, col_number, seat_type, pair_id)
        VALUES (cur_room, 'D', 6, 'SWEETBOX', pair_ctr),
               (cur_room, 'D', 5, 'SWEETBOX', pair_ctr);
        SET pair_ctr = pair_ctr + 1;
        SET cur_room = cur_room + 1;
    END WHILE;

    -- ── Phòng 9: STARIUM ────────────────────────────────────────
    SET i_row = 1;
    WHILE i_row <= 18 DO
        SET row_ch = CHAR(64 + i_row);
        IF i_row <= 5 THEN SET s_type = 'STANDARD';
        ELSE               SET s_type = 'VIP';
        END IF;
        SET i_col = 1;
        WHILE i_col <= 20 DO
            INSERT INTO seats (room_id, row_label, col_number, seat_type, pair_id)
            VALUES (9, row_ch, i_col, s_type, NULL);
            SET i_col = i_col + 1;
        END WHILE;
        IF i_row >= 12 THEN
            SET i_col = 21;
            WHILE i_col <= 27 DO
                INSERT INTO seats (room_id, row_label, col_number, seat_type, pair_id)
                VALUES (9, row_ch, i_col, s_type, NULL);
                SET i_col = i_col + 1;
            END WHILE;
        END IF;
        SET i_row = i_row + 1;
    END WHILE;
    -- Row S: 13 cặp (col 26→1)
    SET i_col = 26;
    WHILE i_col >= 2 DO
        INSERT INTO seats (room_id, row_label, col_number, seat_type, pair_id)
        VALUES (9, 'S', i_col,     'SWEETBOX', pair_ctr),
               (9, 'S', i_col - 1, 'SWEETBOX', pair_ctr);
        SET pair_ctr = pair_ctr + 1;
        SET i_col = i_col - 2;
    END WHILE;

    -- ── Phòng 10: 4DX ───────────────────────────────────────────
    SET i_row = 1;
    WHILE i_row <= 6 DO
        SET row_ch = CHAR(64 + i_row);
        SET i_col = 1;
        WHILE i_col <= 20 DO
            INSERT INTO seats (room_id, row_label, col_number, seat_type, pair_id)
            VALUES (10, row_ch, i_col, 'FOUR_DX', NULL);
            SET i_col = i_col + 1;
        END WHILE;
        SET i_row = i_row + 1;
    END WHILE;

END$$

DELIMITER ;

CALL seed_seats();
DROP PROCEDURE IF EXISTS seed_seats;

-- ============================================================
-- SEED: Showtimes (2026-05-13 → 2026-05-15)
-- movie_id refs:
--   1=Avengers(150p)  2=Lilo&Stitch(108p)  3=MI8(145p)
--   4=Warfare(95p)    5=Materialists(112p)  6=Novocaine(110p)
--
-- Chiến lược phân bổ:
--   Day 13: Phim hot (1,2,3) độc chiếm P1/P2/P3 LARGE 4-5 suất/ngày
--   Day 14: Phim hot vẫn mạnh, P3 nhường 1 slot cuối cho phim thường
--   Day 15: Phim hot giảm còn 3 suất, nhường chỗ cho phim thường xen kẽ
--   P4-P6 STANDARD: phim thường luân phiên 2-3 suất, đan xen nhau
--   P7-P8 SWEETBOX, P9 STARIUM, P10 4DX: phim hot 1-3 suất mỗi ngày
-- ============================================================

-- ════════════════════════════════════ 2026-05-13 (cao điểm) ════════════

-- P1 LARGE — Avengers độc chiếm ×4
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(1, 1, 'TWO_D', '2026-05-13 09:00:00', '2026-05-13 11:30:00'),
(1, 1, 'TWO_D', '2026-05-13 12:00:00', '2026-05-13 14:30:00'),
(1, 1, 'TWO_D', '2026-05-13 15:00:00', '2026-05-13 17:30:00'),
(1, 1, 'TWO_D', '2026-05-13 18:00:00', '2026-05-13 20:30:00');

-- P2 LARGE — Lilo & Stitch độc chiếm ×5
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(2, 2, 'TWO_D', '2026-05-13 09:00:00', '2026-05-13 10:48:00'),
(2, 2, 'TWO_D', '2026-05-13 11:30:00', '2026-05-13 13:18:00'),
(2, 2, 'TWO_D', '2026-05-13 14:00:00', '2026-05-13 15:48:00'),
(2, 2, 'TWO_D', '2026-05-13 16:30:00', '2026-05-13 18:18:00'),
(2, 2, 'TWO_D', '2026-05-13 19:00:00', '2026-05-13 20:48:00');

-- P3 LARGE — MI8 độc chiếm ×4
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(3, 3, 'TWO_D', '2026-05-13 09:00:00', '2026-05-13 11:25:00'),
(3, 3, 'TWO_D', '2026-05-13 12:00:00', '2026-05-13 14:25:00'),
(3, 3, 'TWO_D', '2026-05-13 15:00:00', '2026-05-13 17:25:00'),
(3, 3, 'TWO_D', '2026-05-13 18:00:00', '2026-05-13 20:25:00');

-- P4 STANDARD — Warfare ×3 + Novocaine ×2 (đan xen)
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(4, 4, 'TWO_D', '2026-05-13 09:30:00', '2026-05-13 11:05:00'),
(6, 4, 'TWO_D', '2026-05-13 11:30:00', '2026-05-13 13:20:00'),
(4, 4, 'TWO_D', '2026-05-13 14:00:00', '2026-05-13 15:35:00'),
(6, 4, 'TWO_D', '2026-05-13 16:00:00', '2026-05-13 17:50:00'),
(4, 4, 'TWO_D', '2026-05-13 18:30:00', '2026-05-13 20:05:00');

-- P5 STANDARD — Materialists ×3 + Warfare ×2 (đan xen)
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(5, 5, 'TWO_D', '2026-05-13 09:00:00', '2026-05-13 10:52:00'),
(4, 5, 'TWO_D', '2026-05-13 11:30:00', '2026-05-13 13:05:00'),
(5, 5, 'TWO_D', '2026-05-13 14:00:00', '2026-05-13 15:52:00'),
(4, 5, 'TWO_D', '2026-05-13 16:30:00', '2026-05-13 18:05:00'),
(5, 5, 'TWO_D', '2026-05-13 19:00:00', '2026-05-13 20:52:00');

-- P6 STANDARD — Novocaine ×3 + Materialists ×2 (đan xen)
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(6, 6, 'TWO_D', '2026-05-13 09:30:00', '2026-05-13 11:20:00'),
(5, 6, 'TWO_D', '2026-05-13 12:00:00', '2026-05-13 13:52:00'),
(6, 6, 'TWO_D', '2026-05-13 14:30:00', '2026-05-13 16:20:00'),
(5, 6, 'TWO_D', '2026-05-13 17:00:00', '2026-05-13 18:52:00'),
(6, 6, 'TWO_D', '2026-05-13 19:30:00', '2026-05-13 21:20:00');

-- P7 SWEETBOX — 1 suất mỗi phim hot
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(2, 7, 'TWO_D', '2026-05-13 10:30:00', '2026-05-13 12:18:00'),
(1, 7, 'TWO_D', '2026-05-13 14:00:00', '2026-05-13 16:30:00'),
(3, 7, 'TWO_D', '2026-05-13 17:30:00', '2026-05-13 19:55:00');

-- P8 SWEETBOX — 1 suất mỗi phim hot (thứ tự khác)
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(3, 8, 'TWO_D', '2026-05-13 11:00:00', '2026-05-13 13:25:00'),
(2, 8, 'TWO_D', '2026-05-13 15:00:00', '2026-05-13 16:48:00'),
(1, 8, 'TWO_D', '2026-05-13 19:00:00', '2026-05-13 21:30:00');

-- P9 STARIUM — phim hot xoay vòng
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(3, 9, 'IMAX', '2026-05-13 09:30:00', '2026-05-13 11:55:00'),
(1, 9, 'IMAX', '2026-05-13 12:30:00', '2026-05-13 15:00:00'),
(2, 9, 'IMAX', '2026-05-13 16:00:00', '2026-05-13 17:48:00'),
(3, 9, 'IMAX', '2026-05-13 18:30:00', '2026-05-13 20:55:00');

-- P10 4DX — phim hot
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(1, 10, 'FOUR_DX', '2026-05-13 10:00:00', '2026-05-13 12:30:00'),
(3, 10, 'FOUR_DX', '2026-05-13 13:30:00', '2026-05-13 15:55:00'),
(1, 10, 'FOUR_DX', '2026-05-13 17:00:00', '2026-05-13 19:30:00');

-- ════════════════════════════════════ 2026-05-14 ════════════

-- P1 LARGE — Avengers ×4
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(1, 1, 'TWO_D', '2026-05-14 09:00:00', '2026-05-14 11:30:00'),
(1, 1, 'TWO_D', '2026-05-14 12:00:00', '2026-05-14 14:30:00'),
(1, 1, 'TWO_D', '2026-05-14 15:00:00', '2026-05-14 17:30:00'),
(1, 1, 'TWO_D', '2026-05-14 18:00:00', '2026-05-14 20:30:00');

-- P2 LARGE — Lilo ×5
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(2, 2, 'TWO_D', '2026-05-14 09:00:00', '2026-05-14 10:48:00'),
(2, 2, 'TWO_D', '2026-05-14 11:30:00', '2026-05-14 13:18:00'),
(2, 2, 'TWO_D', '2026-05-14 14:00:00', '2026-05-14 15:48:00'),
(2, 2, 'TWO_D', '2026-05-14 16:30:00', '2026-05-14 18:18:00'),
(2, 2, 'TWO_D', '2026-05-14 19:00:00', '2026-05-14 20:48:00');

-- P3 LARGE — MI8 ×3 + Warfare nhường 1 slot cuối
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(3, 3, 'TWO_D', '2026-05-14 09:00:00', '2026-05-14 11:25:00'),
(3, 3, 'TWO_D', '2026-05-14 12:00:00', '2026-05-14 14:25:00'),
(3, 3, 'TWO_D', '2026-05-14 15:00:00', '2026-05-14 17:25:00'),
(4, 3, 'TWO_D', '2026-05-14 18:30:00', '2026-05-14 20:05:00');

-- P4 STANDARD — Warfare ×3 + Novocaine ×2 (đan xen)
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(4, 4, 'TWO_D', '2026-05-14 09:30:00', '2026-05-14 11:05:00'),
(6, 4, 'TWO_D', '2026-05-14 11:30:00', '2026-05-14 13:20:00'),
(4, 4, 'TWO_D', '2026-05-14 14:00:00', '2026-05-14 15:35:00'),
(6, 4, 'TWO_D', '2026-05-14 16:00:00', '2026-05-14 17:50:00'),
(4, 4, 'TWO_D', '2026-05-14 18:30:00', '2026-05-14 20:05:00');

-- P5 STANDARD — Materialists ×3 + Warfare ×2 (đan xen)
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(5, 5, 'TWO_D', '2026-05-14 09:00:00', '2026-05-14 10:52:00'),
(4, 5, 'TWO_D', '2026-05-14 11:30:00', '2026-05-14 13:05:00'),
(5, 5, 'TWO_D', '2026-05-14 14:00:00', '2026-05-14 15:52:00'),
(4, 5, 'TWO_D', '2026-05-14 16:30:00', '2026-05-14 18:05:00'),
(5, 5, 'TWO_D', '2026-05-14 19:00:00', '2026-05-14 20:52:00');

-- P6 STANDARD — Novocaine ×3 + Materialists ×2 (đan xen)
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(6, 6, 'TWO_D', '2026-05-14 09:30:00', '2026-05-14 11:20:00'),
(5, 6, 'TWO_D', '2026-05-14 12:00:00', '2026-05-14 13:52:00'),
(6, 6, 'TWO_D', '2026-05-14 14:30:00', '2026-05-14 16:20:00'),
(5, 6, 'TWO_D', '2026-05-14 17:00:00', '2026-05-14 18:52:00'),
(6, 6, 'TWO_D', '2026-05-14 19:30:00', '2026-05-14 21:20:00');

-- P7 SWEETBOX
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(2, 7, 'TWO_D', '2026-05-14 10:30:00', '2026-05-14 12:18:00'),
(1, 7, 'TWO_D', '2026-05-14 14:00:00', '2026-05-14 16:30:00'),
(3, 7, 'TWO_D', '2026-05-14 17:30:00', '2026-05-14 19:55:00');

-- P8 SWEETBOX
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(3, 8, 'TWO_D', '2026-05-14 11:00:00', '2026-05-14 13:25:00'),
(2, 8, 'TWO_D', '2026-05-14 15:00:00', '2026-05-14 16:48:00'),
(1, 8, 'TWO_D', '2026-05-14 19:00:00', '2026-05-14 21:30:00');

-- P9 STARIUM
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(3, 9, 'IMAX', '2026-05-14 09:30:00', '2026-05-14 11:55:00'),
(1, 9, 'IMAX', '2026-05-14 12:30:00', '2026-05-14 15:00:00'),
(2, 9, 'IMAX', '2026-05-14 16:00:00', '2026-05-14 17:48:00'),
(3, 9, 'IMAX', '2026-05-14 18:30:00', '2026-05-14 20:55:00');

-- P10 4DX
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(1, 10, 'FOUR_DX', '2026-05-14 10:00:00', '2026-05-14 12:30:00'),
(3, 10, 'FOUR_DX', '2026-05-14 13:30:00', '2026-05-14 15:55:00'),
(1, 10, 'FOUR_DX', '2026-05-14 17:00:00', '2026-05-14 19:30:00');

-- ════════════════════════════════════ 2026-05-15 (phim hot giảm dần) ════════════

-- P1 LARGE — Avengers ×3 + Novocaine lấp slot
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(1, 1, 'TWO_D', '2026-05-15 09:00:00', '2026-05-15 11:30:00'),
(6, 1, 'TWO_D', '2026-05-15 12:00:00', '2026-05-15 13:50:00'),
(1, 1, 'TWO_D', '2026-05-15 14:30:00', '2026-05-15 17:00:00'),
(1, 1, 'TWO_D', '2026-05-15 19:00:00', '2026-05-15 21:30:00');

-- P2 LARGE — Lilo ×4 + Materialists lấp slot
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(2, 2, 'TWO_D', '2026-05-15 09:00:00', '2026-05-15 10:48:00'),
(2, 2, 'TWO_D', '2026-05-15 11:30:00', '2026-05-15 13:18:00'),
(5, 2, 'TWO_D', '2026-05-15 14:00:00', '2026-05-15 15:52:00'),
(2, 2, 'TWO_D', '2026-05-15 16:30:00', '2026-05-15 18:18:00'),
(2, 2, 'TWO_D', '2026-05-15 19:00:00', '2026-05-15 20:48:00');

-- P3 LARGE — MI8 ×2 + phim thường lấp slot còn lại
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(3, 3, 'TWO_D', '2026-05-15 09:00:00', '2026-05-15 11:25:00'),
(4, 3, 'TWO_D', '2026-05-15 12:00:00', '2026-05-15 13:35:00'),
(5, 3, 'TWO_D', '2026-05-15 14:30:00', '2026-05-15 16:22:00'),
(3, 3, 'TWO_D', '2026-05-15 17:30:00', '2026-05-15 19:55:00');

-- P4 STANDARD — Warfare ×2 + Novocaine ×2
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(4, 4, 'TWO_D', '2026-05-15 09:30:00', '2026-05-15 11:05:00'),
(6, 4, 'TWO_D', '2026-05-15 11:30:00', '2026-05-15 13:20:00'),
(4, 4, 'TWO_D', '2026-05-15 15:00:00', '2026-05-15 16:35:00'),
(6, 4, 'TWO_D', '2026-05-15 17:30:00', '2026-05-15 19:20:00');

-- P5 STANDARD — Materialists ×2 + Warfare ×1
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(5, 5, 'TWO_D', '2026-05-15 10:00:00', '2026-05-15 11:52:00'),
(4, 5, 'TWO_D', '2026-05-15 13:00:00', '2026-05-15 14:35:00'),
(5, 5, 'TWO_D', '2026-05-15 16:00:00', '2026-05-15 17:52:00');

-- P6 STANDARD — Novocaine ×2 + Materialists ×2
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(6, 6, 'TWO_D', '2026-05-15 09:30:00', '2026-05-15 11:20:00'),
(5, 6, 'TWO_D', '2026-05-15 12:00:00', '2026-05-15 13:52:00'),
(6, 6, 'TWO_D', '2026-05-15 16:00:00', '2026-05-15 17:50:00'),
(5, 6, 'TWO_D', '2026-05-15 19:00:00', '2026-05-15 20:52:00');

-- P7 SWEETBOX — 2 suất
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(2, 7, 'TWO_D', '2026-05-15 11:00:00', '2026-05-15 12:48:00'),
(1, 7, 'TWO_D', '2026-05-15 17:00:00', '2026-05-15 19:30:00');

-- P8 SWEETBOX — 2 suất
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(3, 8, 'TWO_D', '2026-05-15 12:00:00', '2026-05-15 14:25:00'),
(2, 8, 'TWO_D', '2026-05-15 16:00:00', '2026-05-15 17:48:00');

-- P9 STARIUM — 3 suất
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(1, 9, 'IMAX', '2026-05-15 10:00:00', '2026-05-15 12:30:00'),
(3, 9, 'IMAX', '2026-05-15 13:30:00', '2026-05-15 15:55:00'),
(2, 9, 'IMAX', '2026-05-15 17:00:00', '2026-05-15 18:48:00');

-- P10 4DX — 2 suất
INSERT INTO show_times (movie_id, room_id, format, start_time, end_time) VALUES
(3, 10, 'FOUR_DX', '2026-05-15 11:00:00', '2026-05-15 13:25:00'),
(1, 10, 'FOUR_DX', '2026-05-15 15:00:00', '2026-05-15 17:30:00');

-- ============================================================
-- SEED: showtime_seats — AVAILABLE cho tất cả suất chiếu
-- ============================================================
INSERT INTO showtime_seats (showtime_id, seat_id, status)
SELECT st.id, s.id, 'AVAILABLE'
FROM show_times st
JOIN seats s ON s.room_id = st.room_id
ORDER BY st.id, s.id;
