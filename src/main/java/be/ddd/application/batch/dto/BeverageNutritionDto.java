package be.ddd.application.batch.dto;

import java.util.Optional;

public record BeverageNutritionDto(
        String size,
        String servingMl,
        String servingKcal,
        String saturatedFatG,
        String proteinG,
        String sodiumMg,
        String sugarG,
        String caffeineMg) {

    public Optional<Double> getServingMlAsDouble() {
        return parseDouble(servingMl);
    }

    public Optional<Double> getServingKcalAsDouble() {
        return parseDouble(servingKcal);
    }

    public Optional<Double> getSaturatedFatGAsDouble() {
        return parseDouble(saturatedFatG);
    }

    public Optional<Double> getProteinGAsDouble() {
        return parseDouble(proteinG);
    }

    public Optional<Double> getSodiumMgAsDouble() {
        return parseDouble(sodiumMg);
    }

    public Optional<Double> getSugarGAsDouble() {
        return parseDouble(sugarG);
    }

    public Optional<Double> getCaffeineMgAsDouble() {
        return parseDouble(caffeineMg);
    }

    private Optional<Double> parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(Double.parseDouble(value.trim()));
        } catch (NumberFormatException e) {
            // Log the error or handle it as per application policy
            return Optional.empty();
        }
    }

    public BeverageNutritionDto withSize(String size) {
        return new BeverageNutritionDto(
                size,
                servingMl,
                servingKcal,
                saturatedFatG,
                proteinG,
                sodiumMg,
                sugarG,
                caffeineMg);
    }
}
