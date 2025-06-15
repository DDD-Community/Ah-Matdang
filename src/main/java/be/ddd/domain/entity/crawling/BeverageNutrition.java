package be.ddd.domain.entity.crawling;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BeverageNutrition {

    @Column(name = "SERVING_KCAL", nullable = false)
    private Integer servingKcal; // 1회 제공량(kcal)

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
}
