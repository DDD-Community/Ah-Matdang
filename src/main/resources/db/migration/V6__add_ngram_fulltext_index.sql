ALTER TABLE cafe_beverages
    ADD FULLTEXT INDEX idx_cafe_beverages_name_ngram (name)
        WITH PARSER ngram;
