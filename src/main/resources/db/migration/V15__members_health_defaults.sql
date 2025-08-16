-- 1) 기존 NULL 데이터를 기본값으로 보정
UPDATE members SET height_cm = 0    WHERE height_cm IS NULL;
UPDATE members SET weight_kg = 0.00 WHERE weight_kg IS NULL;

-- 2) 컬럼 기본값(DEFAULT) 설정 + NOT NULL 유지
ALTER TABLE members
    MODIFY COLUMN height_cm INT NOT NULL DEFAULT 0,
    MODIFY COLUMN weight_kg DECIMAL(5,2) NOT NULL DEFAULT 0.00;
