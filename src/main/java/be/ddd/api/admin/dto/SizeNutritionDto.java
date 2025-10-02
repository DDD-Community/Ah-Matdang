package be.ddd.api.admin.dto;

import be.ddd.domain.entity.crawling.BeverageSize;

public record SizeNutritionDto(
        BeverageSize size,
        Integer servingKcal,
        Double saturatedFatG,
        Double proteinG,
        Integer sodiumMg,
        Integer sugarG,
        Integer caffeineMg) {}
