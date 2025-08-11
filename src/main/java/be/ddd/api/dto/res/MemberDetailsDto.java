package be.ddd.api.dto.res;

import be.ddd.domain.entity.member.ActivityRange;
import be.ddd.domain.entity.member.Gender;
import be.ddd.domain.entity.member.Member;
import be.ddd.domain.entity.member.SugarIntakeLevel;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDate;

public record MemberDetailsDto(
        String nickname,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate birthDay,
        Gender gender,
        Integer heightCm,
        BigDecimal weightKg,
        ActivityRange activityRange,
        SugarIntakeLevel sugarIntakeLevel) {

    public static MemberDetailsDto from(Member member) {
        return new MemberDetailsDto(
                member.getNickname(),
                member.getBirthDay(),
                member.getMemberHealthMetric().getGender(),
                member.getMemberHealthMetric().getHeightCm(),
                member.getMemberHealthMetric().getWeightKg(),
                member.getMemberHealthMetric().getActivityRange(),
                member.getMemberHealthMetric().getSugarIntakeLevel());
    }
}
