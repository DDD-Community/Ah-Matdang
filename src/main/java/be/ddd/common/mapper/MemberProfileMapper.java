package be.ddd.common.mapper;

import be.ddd.api.dto.req.MemberProfileModifyDto;
import be.ddd.domain.entity.member.Member;
import be.ddd.domain.entity.member.MemberHealthMetric;
import java.time.LocalDate;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MemberProfileMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void modifyFromDto(MemberProfileModifyDto dto, @MappingTarget Member member);

    @AfterMapping
    default void applyDomainUpdate(MemberProfileModifyDto dto, @MappingTarget Member member) {
        if (dto.nickname() != null
                || dto.birthDay() != null
                || dto.heightCm() != null
                || dto.weightKg() != null
                || dto.activityRange() != null
                || dto.sugarIntakeLevel() != null) {

            Integer age = member.getMemberHealthMetric().getAge();

            if (dto.birthDay() != null) {
                age = LocalDate.now().getYear() - dto.birthDay().getYear() + 1;
            }

            MemberHealthMetric metric =
                    new MemberHealthMetric(
                            age,
                            dto.heightCm() != null
                                    ? dto.heightCm()
                                    : member.getMemberHealthMetric().getHeightCm(),
                            dto.weightKg() != null
                                    ? dto.weightKg()
                                    : member.getMemberHealthMetric().getWeightKg(),
                            dto.gender() != null
                                    ? dto.gender()
                                    : member.getMemberHealthMetric().getGender(),
                            dto.activityRange() != null
                                    ? dto.activityRange()
                                    : member.getMemberHealthMetric().getActivityRange(),
                            dto.sugarIntakeLevel() != null
                                    ? dto.sugarIntakeLevel()
                                    : member.getMemberHealthMetric().getSugarIntakeLevel());

            member.ofProfile(
                    dto.nickname() != null ? dto.nickname() : member.getNickname(),
                    dto.birthDay() != null ? dto.birthDay() : member.getBirthDay(),
                    metric);
        }
    }
}
