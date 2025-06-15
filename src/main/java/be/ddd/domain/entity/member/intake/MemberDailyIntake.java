package be.ddd.domain.entity.member.intake;

import jakarta.persistence.Column;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "memberDailyIntake", timeToLive = 86400)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDailyIntake {

    @Id private String key;

    @Column(name = "intake_date", nullable = false)
    private LocalDateTime intakeDate;

    @Column(name = "total_kcal", nullable = false)
    private Integer totalKcal;

    @Column(name = "total_sugar_g", nullable = false)
    private Integer totalSugarG;

    @Column(name = "total_caffeine_mg", nullable = false)
    private Integer totalCaffeineMg;
}
