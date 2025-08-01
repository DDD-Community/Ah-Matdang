package be.ddd.api.dto.req;

import be.ddd.domain.entity.member.ActivityRange;
import be.ddd.domain.entity.member.Gender;
import be.ddd.domain.entity.member.SugarIntakeLevel;
import io.micrometer.common.lang.NonNullApi;
import io.micrometer.common.lang.NonNullFields;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@NonNullApi
@NonNullFields
public record MemberProfileRegistrationDto(
        UUID fakeId,
        String nickname,
        LocalDate birthDay,
        Gender gender,
        Integer heightCm,
        BigDecimal weightKg,
        ActivityRange activityRange,
        SugarIntakeLevel sugarIntakeLevel) {}
