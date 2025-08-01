CREATE TABLE beverage_size_info
(
    beverage_size_info_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cafe_beverage_id      BIGINT       NOT NULL,
    size_type             VARCHAR(20)  NOT NULL,
    serving_kcal          INT          NOT NULL DEFAULT 0,
    saturated_fat_g       DOUBLE       NOT NULL DEFAULT 0,
    protein_g             DOUBLE       NOT NULL DEFAULT 0,
    sodium_mg             INT          NOT NULL DEFAULT 0,
    sugar_g               INT          NOT NULL DEFAULT 0,
    caffeine_mg           INT          NOT NULL DEFAULT 0,
    created_date          TIMESTAMP    NOT NULL,
    update_date           TIMESTAMP,
    FOREIGN KEY (cafe_beverage_id) REFERENCES cafe_beverages (cafe_beverage_id),
    UNIQUE (cafe_beverage_id, size_type)
);