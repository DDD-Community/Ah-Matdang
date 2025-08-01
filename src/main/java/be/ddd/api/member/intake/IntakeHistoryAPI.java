package be.ddd.api.member.intake;

import be.ddd.api.dto.req.IntakeRegistrationRequestDto;
import be.ddd.api.dto.res.DailyIntakeDto;
import be.ddd.application.member.intake.IntakeHistoryCommandService;
import be.ddd.application.member.intake.IntakeHistoryQueryService;
import be.ddd.common.dto.ApiResponse;
import be.ddd.common.validation.NotFutureDate;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/intake-histories")
@RequiredArgsConstructor
@Validated
public class IntakeHistoryAPI {

    private final IntakeHistoryCommandService intakeHistoryCommand;
    private final IntakeHistoryQueryService intakeHistoryQuery;
    private final Long MEMBER_ID = 1L; // TODO: 추후 실제 회원 데이터로 변경

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<?> registerIntake(
            @RequestBody @Valid IntakeRegistrationRequestDto requestDto) {
        Long historyId = intakeHistoryCommand.registerIntake(MEMBER_ID, requestDto);
        return ApiResponse.success(historyId);
    }

    @GetMapping("/daily")
    public ApiResponse<DailyIntakeDto> getDailyIntake(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @NotFutureDate
                    LocalDateTime intakeTime) {
        DailyIntakeDto dailyIntake =
                intakeHistoryQuery.getDailyIntakeHistory(MEMBER_ID, intakeTime);
        return ApiResponse.success(dailyIntake);
    }

    @GetMapping("/weekly")
    public ApiResponse<List<DailyIntakeDto>> getWeeklyIntake(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @NotFutureDate
                    LocalDateTime dateInWeek) {
        List<DailyIntakeDto> weeklyIntake =
                intakeHistoryQuery.getWeeklyIntakeHistory(MEMBER_ID, dateInWeek);
        return ApiResponse.success(weeklyIntake);
    }

    @GetMapping("/monthly")
    public ApiResponse<List<DailyIntakeDto>> getMonthlyIntake(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotFutureDate
                    LocalDateTime dateInMonth) {
        List<DailyIntakeDto> monthlyIntake =
                intakeHistoryQuery.getMonthlyIntakeHistory(MEMBER_ID, dateInMonth);
        return ApiResponse.success(monthlyIntake);
    }

    @DeleteMapping("/{productId}")
    public ApiResponse<?> deleteMemberIntakeHistory(@PathVariable("productId") UUID productId) {
        intakeHistoryCommand.deleteIntakeHistory(MEMBER_ID, productId);

        return ApiResponse.success("deleted");
    }
}
