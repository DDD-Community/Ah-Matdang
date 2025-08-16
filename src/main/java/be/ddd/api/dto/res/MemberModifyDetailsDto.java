package be.ddd.api.dto.res;

import be.ddd.application.member.dto.res.RecommendedSugar;
import be.ddd.domain.entity.member.ActivityRange;
import be.ddd.domain.entity.member.Gender;
import be.ddd.domain.entity.member.Member;
import be.ddd.domain.entity.member.SugarIntakeLevel;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record MemberModifyDetailsDto(
        String nickname,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate birthDay,
        Gender gender,
        Integer heightCm,
        Integer weightKg,
        ActivityRange activityRange,
        SugarIntakeLevel sugarIntakeLevel,
        RecommendedSugar recommendedSugar) {
    public static MemberModifyDetailsDto from(Member member, RecommendedSugar recommendedSugar) {
        return new MemberModifyDetailsDto(
                member.getNickname(),
                member.getBirthDay(),
                member.getMemberHealthMetric().getGender(),
                member.getMemberHealthMetric().getHeightCm(),
                member.getMemberHealthMetric().getWeightKg(),
                member.getMemberHealthMetric().getActivityRange(),
                member.getMemberHealthMetric().getSugarIntakeLevel(),
                recommendedSugar);
    }
}
