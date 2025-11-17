package be.ddd.application.batch.dto;

import be.ddd.application.batch.dto.deserializer.BeverageNutritionsDeserializer;
import be.ddd.domain.entity.crawling.BeverageType;
import be.ddd.domain.entity.crawling.CafeBeverage;
import be.ddd.domain.entity.crawling.CafeStore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public record LambdaBeverageDto(
        String brand,
        String name,
        String image,
        String beverageType,
        String beverageTemperature,
        @JsonDeserialize(using = BeverageNutritionsDeserializer.class)
                Map<String, BeverageNutritionDto> beverageNutritions) {

    public CafeBeverage toEntity(CafeStore cafeStore) {
        BeverageType type =
                Optional.ofNullable(beverageType)
                        .map(String::toUpperCase)
                        .map(BeverageType::valueOf)
                        .orElse(BeverageType.ANY);

        return CafeBeverage.of(
                name,
                UUID.randomUUID(),
                cafeStore,
                image,
                type,
                beverageTemperature,
                beverageNutritions == null ? Map.of() : beverageNutritions);
    }
}
