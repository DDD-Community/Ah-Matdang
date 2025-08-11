package be.ddd.domain.entity.member;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberHealthMetric {

    private Integer age;

    @Column(name = "height_cm", nullable = false)
    private Integer heightCm;

    @Column(name = "weight_kg", nullable = false, precision = 5, scale = 2)
    private BigDecimal weightKg;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private ActivityRange activityRange;

    @Enumerated(EnumType.STRING)
    @Column(name = "sugar_intake_level")
    private SugarIntakeLevel sugarIntakeLevel;

    @Column(name = "sugar_max_g")
    private Double sugarMaxG = 0.0;

    @Column(name = "sugar_ideal_g")
    private Double sugarIdealG = 0.0;

    public MemberHealthMetric(
            Integer age,
            Integer heightCm,
            BigDecimal weightKg,
            Gender gender,
            ActivityRange activityRange,
            SugarIntakeLevel sugarIntakeLevel) {
        this.age = age;
        this.heightCm = heightCm;
        this.weightKg = weightKg;
        this.gender = gender;
        this.activityRange = activityRange;
        this.sugarIntakeLevel = sugarIntakeLevel;
    }

    public void calculatePersonalSugar(Double baseSugarMaxG, Double baseSugarIdealG) {
        if (this.sugarIntakeLevel == null) {
            this.sugarMaxG = baseSugarMaxG;
            this.sugarIdealG = baseSugarIdealG;
            return;
        }

        double multiplier = this.sugarIntakeLevel.getMultiplier();
        this.sugarMaxG = baseSugarMaxG * multiplier;
        this.sugarIdealG = baseSugarIdealG * multiplier;
    }
}
