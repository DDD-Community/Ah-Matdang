package be.ddd.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Component;

/**
 * Provides the current time in Korean Standard Time (Asia/Seoul).
 *
 * <p>This service wraps {@link CustomClock} so tests that fix the clock keep working while giving
 * callers stable {@link ZoneId} aware timestamps.
 */
@Component
public class KoreanTimeService {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    public ZonedDateTime now() {
        return CustomClock.now().atZone(KST);
    }

    public LocalDateTime currentDateTime() {
        return now().toLocalDateTime();
    }

    public LocalDate currentDate() {
        return now().toLocalDate();
    }

    public LocalTime currentTime() {
        return now().toLocalTime();
    }
}
