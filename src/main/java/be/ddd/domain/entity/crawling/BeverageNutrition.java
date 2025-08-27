package be.ddd.domain.entity.crawling;

import be.ddd.application.batch.dto.BeverageNutritionDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BeverageNutrition {

    @Column(name = "SERVING_KCAL", nullable = false)
    private Integer servingKcal; // 1회 제공량(kcal)

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Column(name = "SATURATED_FAT_G", nullable = false)
    private Double saturatedFatG; // 포화지방(g)

    @Column(name = "PROTEIN_G", nullable = false)
    private Double proteinG; // 단백질(g)

    @Column(name = "SODIUM_MG", nullable = false)
    private Integer sodiumMg; // 나트륨(mg)

    @Column(name = "SUGAR_G", nullable = false)
    private Integer sugarG; // 당류(g)

    @Column(name = "CAFFEINE_MG", nullable = false)
    private Integer caffeineMg; // 카페인(mg)

    public static BeverageNutrition from(BeverageNutritionDto dto) {
        return new BeverageNutrition(
                parseInt(dto.servingKcal()),
                parseDouble(dto.saturatedFatG()),
                parseDouble(dto.proteinG()),
                parseInt(dto.sodiumMg()),
                parseInt(dto.sugarG()),
                parseInt(dto.caffeineMg()));
    }

    private static Integer parseInt(String value) {
        if (value == null || value.isBlank()) {
            return 0;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static Double parseDouble(String value) {
        if (value == null || value.isBlank()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public BeverageNutrition(
            Integer servingKcal,
            Double saturatedFatG,
            Double proteinG,
            Integer sodiumMg,
            Integer sugarG,
            Integer caffeineMg) {
        this.servingKcal = servingKcal;
        this.saturatedFatG = saturatedFatG;
        this.proteinG = proteinG;
        this.sodiumMg = sodiumMg;
        this.sugarG = sugarG;
        this.caffeineMg = caffeineMg;
    }

    public static BeverageNutrition empty() {
        return new BeverageNutrition(0, 0.0, 0.0, 0, 0, 0);
    }
}
