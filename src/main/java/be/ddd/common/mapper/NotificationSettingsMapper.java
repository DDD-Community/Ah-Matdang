package be.ddd.common.mapper;

import be.ddd.api.dto.req.notification.NotificationSettingsUpdateRequestDto;
import be.ddd.domain.entity.member.NotificationSettings;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NotificationSettingsMapper {
    void updateFromDto(
            NotificationSettingsUpdateRequestDto dto, @MappingTarget NotificationSettings entity);
}
