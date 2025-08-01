package be.ddd.domain.entity.member;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
}
