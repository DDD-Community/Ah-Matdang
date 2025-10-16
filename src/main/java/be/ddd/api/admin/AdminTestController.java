package be.ddd.api.admin;

import be.ddd.api.admin.dto.req.TimeRequest;
import be.ddd.application.notification.NotificationSchedulingService;
import be.ddd.common.dto.ApiResponse;
import be.ddd.common.util.CustomClock;
import be.ddd.common.util.FixedClock;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/test")
@RequiredArgsConstructor
public class AdminTestController {

    private final NotificationSchedulingService notificationSchedulingService;

    @PostMapping("/time")
    public ApiResponse<?> setFixedTime(@RequestBody TimeRequest request) {
        CustomClock.setInstance(new FixedClock(request.fixedTime()));
        return ApiResponse.success("Time fixed to: " + request.fixedTime());
    }

    @PostMapping("/time/reset")
    public ApiResponse<?> resetTime() {
        CustomClock.reset();
        return ApiResponse.success("Time reset to current time.");
    }

    @PostMapping("/notifications")
    public ApiResponse<?> triggerNotifications() {
        notificationSchedulingService.sendSugarIntakeNotifications();
        return ApiResponse.success("Notification sending triggered.");
    }
}
