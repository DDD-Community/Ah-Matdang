package be.ddd.api.admin.dto.res;

import be.ddd.domain.entity.crawling.BeverageNutrition;
import lombok.Getter;

@Getter
public class BeverageNutritionResponseDto {
    private final Integer servingKcal;
    private final Double saturatedFatG;
    private final Double proteinG;
    private final Integer sodiumMg;
    private final Integer sugarG;
    private final Integer caffeineMg;

    public BeverageNutritionResponseDto(BeverageNutrition nutrition) {
        this.servingKcal = nutrition.getServingKcal();
        this.saturatedFatG = nutrition.getSaturatedFatG();
        this.proteinG = nutrition.getProteinG();
        this.sodiumMg = nutrition.getSodiumMg();
        this.sugarG = nutrition.getSugarG();
        this.caffeineMg = nutrition.getCaffeineMg();
    }
}
