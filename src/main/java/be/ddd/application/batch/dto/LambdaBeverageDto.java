package be.ddd.application.batch.dto;

import be.ddd.domain.entity.crawling.*;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public record LambdaBeverageDto(
        String name,
        String image,
        String beverageType,
        String beverageTemperature,
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
                beverageNutritions);
    }
}
