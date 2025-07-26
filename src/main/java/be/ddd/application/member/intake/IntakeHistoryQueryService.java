package be.ddd.application.member.intake;

import be.ddd.api.dto.res.DailyIntakeDto;
import java.time.LocalDateTime;
import java.util.List;

public interface IntakeHistoryQueryService {
    DailyIntakeDto getDailyIntakeHistory(Long memberId, LocalDateTime date);

    List<DailyIntakeDto> getWeeklyIntakeHistory(Long memberId, LocalDateTime dateInWeek);

    List<DailyIntakeDto> getMonthlyIntakeHistory(Long memberId, LocalDateTime dateInMonth);
}
