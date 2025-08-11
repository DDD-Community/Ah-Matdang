package be.ddd.application.batch.dto;

public record BeverageNutritionDto(
        String servingKcal,
        String saturatedFatG,
        String proteinG,
        String sodiumMg,
        String sugarG,
        String caffeineMg) {}
