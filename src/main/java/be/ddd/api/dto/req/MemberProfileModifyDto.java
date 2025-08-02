package be.ddd.api.dto.req;

import be.ddd.domain.entity.member.ActivityRange;
import be.ddd.domain.entity.member.Gender;
import be.ddd.domain.entity.member.SugarIntakeLevel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MemberProfileModifyDto(
        String nickname,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate birthDay,
        Gender gender,
        Integer heightCm,
        BigDecimal weightKg,
        ActivityRange activityRange,
        SugarIntakeLevel sugarIntakeLevel) {}
