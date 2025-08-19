package be.ddd.domain.entity.member;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberHealthMetric {

    private Integer age;

    @Column(name = "height_cm", nullable = false)
    @ColumnDefault("0")
    private Integer heightCm;

    @Column(name = "weight_kg", nullable = false)
    @ColumnDefault("0")
    private Integer weightKg;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private ActivityRange activityRange;

    @Enumerated(EnumType.STRING)
    @Column(name = "sugar_intake_level")
    private SugarIntakeLevel sugarIntakeLevel;

    @Column(name = "sugar_max_g")
    private Integer sugarMaxG = 0;

    @Column(name = "sugar_ideal_g")
    private Integer sugarIdealG = 0;

    public MemberHealthMetric(
            Integer age,
            Integer heightCm,
            Integer weightKg,
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

    public void calculatePersonalSugar(Integer baseSugarMaxG, Integer baseSugarIdealG) {
        if (this.sugarIntakeLevel == null) {
            this.sugarMaxG = baseSugarMaxG;
            this.sugarIdealG = baseSugarIdealG;
            return;
        }

        double multiplier = this.sugarIntakeLevel.getMultiplier();
        this.sugarMaxG = roundToInteger(baseSugarMaxG * multiplier);
        this.sugarIdealG = roundToInteger(baseSugarIdealG * multiplier);
    }

    private Integer roundToInteger(Double value) {
        if (value == null) {
            return 0;
        }
        return (int) Math.round(value);
    }
}
