-- Add additional indices and constraints for better performance and data integrity

-- Add index on member_id in intake_history for faster lookups
-- This is already covered by the composite index (member_id, intake_time)

-- Add index on cafe_store_id in cafe_beverages for faster joins
CREATE INDEX idx_cafe_beverages_store_id ON cafe_beverages(cafe_store_id);

-- Add index on auth_provider and provider_id for faster OAuth lookups
CREATE INDEX idx_members_auth_provider ON members(auth_provider, provider_id);

-- Add index on beverage name for search functionality
CREATE INDEX idx_cafe_beverages_name ON cafe_beverages(name);

-- Note: Additional indices and constraints can be added here as the application evolves
-- For example, if you need to search beverages by nutritional content:
-- CREATE INDEX idx_cafe_beverages_sugar ON cafe_beverages(sugar_g);
-- CREATE INDEX idx_cafe_beverages_caffeine ON cafe_beverages(caffeine_mg);
