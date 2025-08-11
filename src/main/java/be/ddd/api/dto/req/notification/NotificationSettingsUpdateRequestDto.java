package be.ddd.api.dto.req.notification;

import java.time.LocalTime;

public record NotificationSettingsUpdateRequestDto(
        Boolean isEnabled,
        Boolean remindersEnabled,
        LocalTime reminderTime,
        Boolean riskWarningsEnabled,
        Boolean newsUpdatesEnabled) {}
