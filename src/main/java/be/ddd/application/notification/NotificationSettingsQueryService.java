package be.ddd.application.notification;

import be.ddd.api.dto.res.notification.NotificationSettingsResponseDto;
import java.util.UUID;

public interface NotificationSettingsQueryService {
    NotificationSettingsResponseDto getNotificationSettings(UUID memberFakeId);
}
