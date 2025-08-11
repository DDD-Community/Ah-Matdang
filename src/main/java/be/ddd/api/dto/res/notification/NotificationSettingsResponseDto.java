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
            return new NotificationSettingsResponseDto(true, true, LocalTime.of(21, 0), true, true);
        }

        boolean actualIsEnabled = settings.isEnabled();
        boolean actualRemindersEnabled = settings.isRemindersEnabled();
        boolean actualRiskWarningsEnabled = settings.isRiskWarningsEnabled();
        boolean actualNewsUpdatesEnabled = settings.isNewsUpdatesEnabled();

        if (!actualIsEnabled) {
            actualRemindersEnabled = false;
            actualRiskWarningsEnabled = false;
            actualNewsUpdatesEnabled = false;
        }

        return new NotificationSettingsResponseDto(
                actualIsEnabled,
                actualRemindersEnabled,
                settings.getReminderTime(),
                actualRiskWarningsEnabled,
                actualNewsUpdatesEnabled);
    }
}
