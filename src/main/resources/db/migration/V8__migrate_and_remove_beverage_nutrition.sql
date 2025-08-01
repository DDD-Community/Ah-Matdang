INSERT INTO beverage_size_info (
    cafe_beverage_id,
    size_type,
    serving_kcal,
    saturated_fat_g,
    protein_g,
    sodium_mg,
    sugar_g,
    caffeine_mg,
    created_date,
    update_date
)
SELECT
    cb.cafe_beverage_id,
    'TALL' AS size_type, -- 기본 사이즈를 TALL로 가정
    cb.serving_kcal,
    cb.saturated_fat_g,
    cb.protein_g,
    cb.sodium_mg,
    cb.sugar_g,
    cb.caffeine_mg,
    cb.created_date,
    cb.update_date
FROM cafe_beverages cb
WHERE cb.serving_kcal IS NOT NULL; -- 영양 정보가 있는 음료만 마이그레이션

ALTER TABLE cafe_beverages DROP COLUMN serving_kcal;
ALTER TABLE cafe_beverages DROP COLUMN saturated_fat_g;
ALTER TABLE cafe_beverages DROP COLUMN protein_g;
ALTER TABLE cafe_beverages DROP COLUMN sodium_mg;
ALTER TABLE cafe_beverages DROP COLUMN sugar_g;
ALTER TABLE cafe_beverages DROP COLUMN caffeine_mg;
