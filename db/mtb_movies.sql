SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS mtb_movies CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE mtb_movies;

CREATE TABLE genres (
    id    BIGINT       NOT NULL AUTO_INCREMENT,
    name  VARCHAR(100) NOT NULL,

    PRIMARY KEY (id),
    UNIQUE KEY uk_name (name)
);

CREATE TABLE movies (
    id           BIGINT        NOT NULL AUTO_INCREMENT,
    title        VARCHAR(255)  NOT NULL,
    description  TEXT,
    duration_min INT           NOT NULL,
    director     VARCHAR(255),
    cast_list    TEXT,
    poster_url   MEDIUMTEXT,
    rated        ENUM('P','K','T13','T16','T18','C'),
    status       ENUM('NOW_SHOWING','COMING_SOON','ENDED') NOT NULL,
    release_date DATE,

    PRIMARY KEY (id)
);

CREATE TABLE movie_genres (
    movie_id  BIGINT NOT NULL,
    genre_id  BIGINT NOT NULL,

    PRIMARY KEY (movie_id, genre_id),
    FOREIGN KEY (movie_id) REFERENCES movies(id),
    FOREIGN KEY (genre_id) REFERENCES genres(id)
);

-- ============================================================
-- SEED: Genres
-- ============================================================
INSERT INTO genres (id, name) VALUES
(1, 'Hành động'),
(2, 'Khoa học viễn tưởng'),
(3, 'Hoạt hình'),
(4, 'Gia đình'),
(5, 'Phiêu lưu'),
(6, 'Hồi hộp'),
(7, 'Chiến tranh'),
(8, 'Lãng mạn'),
(9, 'Hài hước');

-- ============================================================
-- SEED: Movies
-- ============================================================

-- NOW_SHOWING
INSERT INTO movies (id, title, description, duration_min, director, cast_list, poster_url, rated, status, release_date) VALUES
(1, 'Avengers: Doomsday',
 'Các siêu anh hùng Marvel đối mặt với mối đe dọa lớn nhất từ trước đến nay — một thế lực có thể xóa sổ toàn bộ vũ trụ.',
 150, 'Russo Brothers',
 'Robert Downey Jr., Chris Evans, Scarlett Johansson, Benedict Cumberbatch',
 '/posters/avengers-doomsday.jpg',
 'T13', 'NOW_SHOWING', '2025-04-25'),

(2, 'Lilo & Stitch',
 'Phiên bản live-action của bộ phim hoạt hình kinh điển về tình bạn giữa cô bé Lilo và sinh vật ngoài hành tinh Stitch.',
 108, 'Dean Fleischer Camp',
 'Maia Kealoha, Sydney Agudong, Zach Galifianakis',
 '/posters/lilo-stitch.jpg',
 'P', 'NOW_SHOWING', '2025-05-23'),

(3, 'Mission: Impossible 8',
 'Ethan Hunt thực hiện nhiệm vụ nguy hiểm nhất sự nghiệp, đối mặt với kẻ thù từ quá khứ đe dọa cả thế giới.',
 145, 'Christopher McQuarrie',
 'Tom Cruise, Hayley Atwell, Ving Rhames, Simon Pegg',
 '/posters/mi8.jpg',
 'T13', 'NOW_SHOWING', '2025-05-19'),

(4, 'Warfare',
 'Một đội biệt kích hải quân Mỹ bị phục kích tại Iraq — tái hiện trận chiến sinh tử trong thời gian thực.',
 95, 'Alex Garland, Ray Mendoza',
 'D''Pharaoh Woon-A-Tai, Kit Connor, Cosmo Jarvis, Joseph Quinn',
 '/posters/warfare.jpg',
 'T18', 'NOW_SHOWING', '2025-04-11'),

(5, 'Materialists',
 'Một chuyên gia mai mối ở New York bị kẹt giữa người yêu cũ giàu có và người bạn trai hiện tại bình thường.',
 112, 'Celine Song',
 'Dakota Johnson, Chris Evans, Pedro Pascal',
 '/posters/materialists.jpg',
 'T13', 'NOW_SHOWING', '2025-06-06'),

(6, 'Novocaine',
 'Một nhân viên ngân hàng bẩm sinh không cảm thấy đau biến khả năng kỳ lạ thành vũ khí khi bạn gái bị bắt cóc.',
 110, 'Dan Berk, Robert Olsen',
 'Jack Quaid, Amber Midthunder, Ray Nicholson',
 '/posters/novocaine.jpg',
 'T16', 'NOW_SHOWING', '2025-03-14');

-- COMING_SOON
INSERT INTO movies (id, title, description, duration_min, director, cast_list, poster_url, rated, status, release_date) VALUES
(7, 'Superman',
 'Clark Kent khám phá lại bản thân giữa di sản Krypton và cuộc sống con người, đối mặt với Lex Luthor.',
 130, 'James Gunn',
 'David Corenswet, Rachel Brosnahan, Nicholas Hoult',
 '/posters/superman.jpg',
 'T13', 'COMING_SOON', '2025-07-11'),

(8, 'Jurassic World Rebirth',
 'Năm năm sau thảm họa, một nhóm thám hiểm mạo hiểm đến vùng đất hoang sơ nơi khủng long vẫn còn sống sót.',
 128, 'Gareth Edwards',
 'Scarlett Johansson, Jonathan Bailey, Mahershala Ali',
 '/posters/jurassic-rebirth.jpg',
 'T13', 'COMING_SOON', '2025-07-02'),

(9, 'The Fantastic Four: First Steps',
 'Nhóm bốn siêu anh hùng đầu tiên của MCU ra mắt trong bối cảnh thập niên 60 đầy màu sắc và hiểm họa vũ trụ.',
 135, 'Matt Shakman',
 'Pedro Pascal, Vanessa Kirby, Joseph Quinn, Ebon Moss-Bachrach',
 '/posters/fantastic-four.jpg',
 'P', 'COMING_SOON', '2025-07-25'),

(10, 'How to Train Your Dragon',
 'Phiên bản live-action về cậu bé Viking Hiccup và chú rồng Toothless — hành trình vượt định kiến để trở thành anh hùng.',
 115, 'Dean DeBlois',
 'Mason Thames, Nico Parker, Gerard Butler',
 '/posters/httyd.jpg',
 'P', 'COMING_SOON', '2025-06-13');

-- ENDED
INSERT INTO movies (id, title, description, duration_min, director, cast_list, poster_url, rated, status, release_date) VALUES
(11, 'Deadpool & Wolverine',
 'Deadpool bị kéo vào Multiverse, tình cờ gặp một Wolverine chán đời — bộ đôi khó ưa nhất MCU.',
 128, 'Shawn Levy',
 'Ryan Reynolds, Hugh Jackman, Emma Corrin',
 '/posters/deadpool-wolverine.jpg',
 'T18', 'ENDED', '2024-07-26'),

(12, 'Inside Out 2',
 'Riley bước vào tuổi teen, cảm xúc mới Anxiety xuất hiện và chiếm quyền kiểm soát tâm trí.',
 100, 'Kelsey Mann',
 'Amy Poehler, Maya Hawke, Kensington Tallman',
 '/posters/inside-out-2.jpg',
 'P', 'ENDED', '2024-06-14'),

(13, 'Dune: Part Two',
 'Paul Atreides liên minh với người Fremen để trả thù những kẻ đã hủy diệt gia đình anh.',
 166, 'Denis Villeneuve',
 'Timothée Chalamet, Zendaya, Austin Butler, Florence Pugh',
 '/posters/dune-2.jpg',
 'T16', 'ENDED', '2024-03-01');

-- ============================================================
-- SEED: Movie Genres
-- 1=Hành động  2=Khoa học viễn tưởng  3=Hoạt hình  4=Gia đình
-- 5=Phiêu lưu  6=Hồi hộp  7=Chiến tranh  8=Lãng mạn  9=Hài hước
-- ============================================================
INSERT INTO movie_genres (movie_id, genre_id) VALUES
(1, 1),  (1, 2),             -- Avengers: Hành động, Sci-Fi
(2, 3),  (2, 4),  (2, 9),   -- Lilo & Stitch: Hoạt hình, Gia đình, Hài hước
(3, 1),  (3, 5),  (3, 6),   -- MI8: Hành động, Phiêu lưu, Hồi hộp
(4, 7),  (4, 1),             -- Warfare: Chiến tranh, Hành động
(5, 8),  (5, 9),             -- Materialists: Lãng mạn, Hài hước
(6, 1),  (6, 6),             -- Novocaine: Hành động, Hồi hộp
(7, 1),  (7, 2),             -- Superman: Hành động, Sci-Fi
(8, 1),  (8, 5),             -- Jurassic: Hành động, Phiêu lưu
(9, 1),  (9, 2),  (9, 4),   -- Fantastic Four: Hành động, Sci-Fi, Gia đình
(10, 3), (10, 5), (10, 4),  -- HTTYD: Hoạt hình, Phiêu lưu, Gia đình
(11, 1), (11, 9),            -- Deadpool: Hành động, Hài hước
(12, 3), (12, 4), (12, 9),  -- Inside Out 2: Hoạt hình, Gia đình, Hài hước
(13, 1), (13, 2), (13, 5);  -- Dune 2: Hành động, Sci-Fi, Phiêu lưu