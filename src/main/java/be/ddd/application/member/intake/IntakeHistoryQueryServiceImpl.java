package be.ddd.application.member.intake;

import be.ddd.api.dto.res.DailyIntakeDto;
import be.ddd.api.dto.res.IntakeRecordDto;
import be.ddd.common.util.CustomClock;
import be.ddd.domain.exception.FutureDateNotAllowedException;
import be.ddd.domain.repo.CafeBeverageRepository;
import be.ddd.domain.repo.IntakeHistoryRepository;
import be.ddd.domain.repo.MemberRepository;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class IntakeHistoryQueryServiceImpl implements IntakeHistoryQueryService {

    private final IntakeHistoryRepository intakeHistoryRepository;
    private final MemberRepository memberRepository;
    private final CafeBeverageRepository cafeBeverageRepository;

    @Override
    public DailyIntakeDto getDailyIntakeHistory(Long memberId, LocalDateTime date) {
        validateFutureDate(date);
        List<IntakeRecordDto> records =
                intakeHistoryRepository.findByMemberIdAndDate(memberId, date);
        return new DailyIntakeDto(date, records);
    }

    @Override
    public List<DailyIntakeDto> getWeeklyIntakeHistory(Long memberId, LocalDateTime dateInWeek) {
        validateFutureDate(dateInWeek);
        LocalDateTime startOfWeek =
                dateInWeek.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDateTime endOfWeek = dateInWeek.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<IntakeRecordDto> records =
                intakeHistoryRepository.findByMemberIdAndDateBetween(
                        memberId, startOfWeek, endOfWeek);

        Map<LocalDateTime, List<IntakeRecordDto>> recordsByDate =
                records.stream()
                        .collect(
                                Collectors.groupingBy(
                                        record ->
                                                record.intakeTime().truncatedTo(ChronoUnit.DAYS)));

        List<DailyIntakeDto> dailyIntakeList = new ArrayList<>();
        for (LocalDateTime date = startOfWeek.toLocalDate().atStartOfDay();
                !date.isAfter(endOfWeek.toLocalDate().atStartOfDay());
                date = date.plusDays(1)) {
            dailyIntakeList.add(
                    new DailyIntakeDto(date, recordsByDate.getOrDefault(date, new ArrayList<>())));
        }
        return dailyIntakeList;
    }

    @Override
    public List<DailyIntakeDto> getMonthlyIntakeHistory(Long memberId, LocalDateTime dateInMonth) {
        validateFutureDate(dateInMonth);
        LocalDateTime firstDayOfMonth = dateInMonth.with(TemporalAdjusters.firstDayOfMonth());
        LocalDateTime lastDayOfMonth = dateInMonth.with(TemporalAdjusters.lastDayOfMonth());

        // Calculate start and end dates for the calendar view (considering previous/next month's
        // days)
        LocalDateTime calendarStartDate =
                firstDayOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDateTime calendarEndDate =
                lastDayOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<IntakeRecordDto> records =
                intakeHistoryRepository.findByMemberIdAndDateBetween(
                        memberId, calendarStartDate, calendarEndDate);

        Map<LocalDateTime, List<IntakeRecordDto>> recordsByDate =
                records.stream()
                        .collect(
                                Collectors.groupingBy(
                                        record ->
                                                record.intakeTime().truncatedTo(ChronoUnit.DAYS)));

        List<DailyIntakeDto> dailyIntakeList = new ArrayList<>();
        for (LocalDateTime date = calendarStartDate.toLocalDate().atStartOfDay();
                !date.isAfter(calendarEndDate.toLocalDate().atStartOfDay());
                date = date.plusDays(1)) {
            dailyIntakeList.add(
                    new DailyIntakeDto(date, recordsByDate.getOrDefault(date, new ArrayList<>())));
        }
        return dailyIntakeList;
    }

    private void validateFutureDate(LocalDateTime date) {
        if (date.isAfter(CustomClock.now())) {
            throw new FutureDateNotAllowedException();
        }
    }
}
