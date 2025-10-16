package be.ddd.application.batch.dto;

import be.ddd.domain.entity.crawling.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public record LambdaBeverageDto(
        String brand,
        String name,
        String image,
        String beverageType,
        String beverageTemperature,
        List<BeverageNutritionDto> beverageNutritions) {

    public CafeBeverage toEntity(CafeStore cafeStore) {
        BeverageType type =
                Optional.ofNullable(beverageType)
                        .map(String::toUpperCase)
                        .map(BeverageType::valueOf)
                        .orElse(BeverageType.ANY);

        Map<String, BeverageNutritionDto> nutritionsMap =
                beverageNutritions.stream()
                        .collect(
                                Collectors.toMap(
                                        BeverageNutritionDto::size,
                                        Function.identity(),
                                        (existing, replacement) -> existing));

        return CafeBeverage.of(
                name,
                UUID.randomUUID(),
                cafeStore,
                image,
                type,
                beverageTemperature,
                nutritionsMap);
    }
}
