package be.ddd.application.notification;

import be.ddd.api.dto.req.notification.NotificationSettingsUpdateRequestDto;
import be.ddd.api.dto.res.notification.NotificationSettingsResponseDto;
import java.util.UUID;

public interface NotificationSettingsCommandService {
    NotificationSettingsResponseDto updateNotificationSettings(
            UUID memberFakeId, NotificationSettingsUpdateRequestDto dto);
}
