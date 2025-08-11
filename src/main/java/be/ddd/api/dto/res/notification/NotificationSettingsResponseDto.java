package be.ddd.api.dto.res.notification;

import be.ddd.domain.entity.member.NotificationSettings;
import java.time.LocalTime;

public record NotificationSettingsResponseDto(
        boolean isEnabled,
        boolean remindersEnabled,
        LocalTime reminderTime,
        boolean riskWarningsEnabled,
        boolean newsUpdatesEnabled) {
    public static NotificationSettingsResponseDto from(NotificationSettings settings) {
        if (settings == null) {
            // 설정이 없는 경우 기본값으로 응답
            return new NotificationSettingsResponseDto(true, true, LocalTime.of(21, 0), true, true);
        }
        return new NotificationSettingsResponseDto(
                settings.isEnabled(),
                settings.isRemindersEnabled(),
                settings.getReminderTime(),
                settings.isRiskWarningsEnabled(),
                settings.isNewsUpdatesEnabled());
    }
}
