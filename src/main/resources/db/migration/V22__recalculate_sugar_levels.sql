UPDATE cafe_beverages cb
SET cb.sugar_level =
    CASE
        WHEN EXISTS (SELECT 1 FROM beverage_size_info bsi WHERE bsi.cafe_beverage_id = cb.cafe_beverage_id AND bsi.sugar_g = 0) THEN 'ZERO'
        WHEN EXISTS (
            SELECT 1
            FROM beverage_size_info bsi
            WHERE bsi.cafe_beverage_id = cb.cafe_beverage_id
              AND bsi.sugar_g > 0
              AND CASE bsi.size_type
                    WHEN 'TALL' THEN 355
                    WHEN 'GRANDE' THEN 473
                    WHEN 'VENTI' THEN 591
                    WHEN 'SHORT' THEN 237
                    ELSE 0
                  END > 0
              AND (bsi.sugar_g * 100.0) /
                  CASE bsi.size_type
                    WHEN 'TALL' THEN 355
                    WHEN 'GRANDE' THEN 473
                    WHEN 'VENTI' THEN 591
                    WHEN 'SHORT' THEN 237
                    ELSE 1 -- Avoid division by zero
                  END <= 2.5
        ) THEN 'LOW'
        ELSE 'HIGH'
    END;
