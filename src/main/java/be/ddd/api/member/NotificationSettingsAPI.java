package be.ddd.api.member;

import be.ddd.api.dto.req.notification.NotificationSettingsUpdateRequestDto;
import be.ddd.api.dto.res.notification.NotificationSettingsResponseDto;
import be.ddd.application.notification.NotificationSettingsCommandService;
import be.ddd.application.notification.NotificationSettingsQueryService;
import be.ddd.common.dto.ApiResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members/{fakeId}/notification-settings")
@RequiredArgsConstructor
public class NotificationSettingsAPI {

    private final NotificationSettingsQueryService notificationSettingsQueryService;
    private final NotificationSettingsCommandService notificationSettingsCommandService;

    @GetMapping
    public ApiResponse<NotificationSettingsResponseDto> getNotificationSettings(
            @PathVariable("fakeId") UUID fakeId) {
        NotificationSettingsResponseDto response =
                notificationSettingsQueryService.getNotificationSettings(fakeId);
        return ApiResponse.success(response);
    }

    @PutMapping
    public ApiResponse<NotificationSettingsResponseDto> updateNotificationSettings(
            @PathVariable("fakeId") UUID fakeId,
            @Valid @RequestBody NotificationSettingsUpdateRequestDto request) {
        NotificationSettingsResponseDto response =
                notificationSettingsCommandService.updateNotificationSettings(fakeId, request);
        return ApiResponse.success(response);
    }
}
