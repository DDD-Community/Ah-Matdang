package be.ddd.application.member.intake;

import be.ddd.api.dto.res.DailyIntakeDto;
import java.time.LocalDateTime;
import java.util.List;

public interface IntakeHistoryQueryService {
    DailyIntakeDto getDailyIntakeHistory(String providerId, LocalDateTime date);

    List<DailyIntakeDto> getWeeklyIntakeHistory(String providerId, LocalDateTime dateInWeek);

    List<DailyIntakeDto> getMonthlyIntakeHistory(String providerId, LocalDateTime dateInMonth);
}
