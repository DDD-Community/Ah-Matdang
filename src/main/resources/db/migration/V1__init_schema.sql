-- 사용자 정보 테이블
CREATE TABLE members
(
    member_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    fake_id        BINARY(16)    NOT NULL UNIQUE,
    auth_provider  VARCHAR(10)   NOT NULL,
    provider_id    VARCHAR(255) UNIQUE,
    nickname       VARCHAR(255),
    profile_url    VARCHAR(255),
    age            INT,
    height_cm      INT           NOT NULL,
    weight_kg      DECIMAL(5, 2) NOT NULL,
    gender         VARCHAR(10),
    activity_range VARCHAR(10),
    created_date   DATETIME      NOT NULL,
    update_date    DATETIME
);

-- 카페 매장 정보 테이블
CREATE TABLE cafe_stores
(
    cafe_store_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cafe_brand    VARCHAR(50) NOT NULL,
    created_date  DATETIME    NOT NULL,
    update_date   DATETIME
);

-- 카페 음료 정보 테이블
CREATE TABLE cafe_beverages
(
    cafe_beverage_id BIGINT PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    cafe_store_id    BIGINT       NOT NULL,
    serving_kcal     INT          NOT NULL,
    saturated_fat_g  DOUBLE       NOT NULL,
    protein_g        DOUBLE       NOT NULL,
    sodium_mg        INT          NOT NULL,
    sugar_g          INT          NOT NULL,
    caffeine_mg      INT          NOT NULL,
    beverage_type    VARCHAR(100) NOT NULL,
    created_date     DATETIME     NOT NULL,
    update_date      DATETIME,
    FOREIGN KEY (cafe_store_id) REFERENCES cafe_stores (cafe_store_id)
);

-- 사용자 음료 섭취 기록 테이블
CREATE TABLE intake_history
(
    intake_history_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id         BIGINT   NOT NULL,
    intake_time       DATETIME NOT NULL,
    beverage_id       BIGINT   NOT NULL,
    created_date      DATETIME NOT NULL,
    update_date       DATETIME,
    FOREIGN KEY (member_id) REFERENCES members (member_id),
    FOREIGN KEY (beverage_id) REFERENCES cafe_beverages (cafe_beverage_id),
    INDEX idx_member_intake_time (member_id, intake_time)
);
