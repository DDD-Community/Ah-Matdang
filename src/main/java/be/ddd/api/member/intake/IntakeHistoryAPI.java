package be.ddd.api.member.intake;

import be.ddd.api.dto.req.IntakeHistoryDeleteReqDto;
import be.ddd.api.dto.req.IntakeRegistrationRequestDto;
import be.ddd.api.dto.res.DailyIntakeDto;
import be.ddd.api.security.custom.CurrentUser;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<?> registerIntake(
            @CurrentUser String providerId,
            @RequestBody @Valid IntakeRegistrationRequestDto requestDto) {
        Long historyId = intakeHistoryCommand.registerIntake(providerId, requestDto);
        return ApiResponse.success(historyId);
    }

    @GetMapping("/daily")
    public ApiResponse<DailyIntakeDto> getDailyIntake(
            @CurrentUser String providerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @NotFutureDate
                    LocalDateTime intakeTime) {
        DailyIntakeDto dailyIntake =
                intakeHistoryQuery.getDailyIntakeHistory(providerId, intakeTime);
        return ApiResponse.success(dailyIntake);
    }

    @GetMapping("/weekly")
    public ApiResponse<List<DailyIntakeDto>> getWeeklyIntake(
            @CurrentUser String providerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @NotFutureDate
                    LocalDateTime dateInWeek) {
        List<DailyIntakeDto> weeklyIntake =
                intakeHistoryQuery.getWeeklyIntakeHistory(providerId, dateInWeek);
        return ApiResponse.success(weeklyIntake);
    }

    @GetMapping("/monthly")
    public ApiResponse<List<DailyIntakeDto>> getMonthlyIntake(
            @CurrentUser String providerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotFutureDate
                    LocalDateTime dateInMonth) {
        List<DailyIntakeDto> monthlyIntake =
                intakeHistoryQuery.getMonthlyIntakeHistory(providerId, dateInMonth);
        return ApiResponse.success(monthlyIntake);
    }

    @DeleteMapping("/{productId}")
    public ApiResponse<?> deleteMemberIntakeHistory(
            @CurrentUser String providerId,
            @PathVariable("productId") UUID productId,
            @RequestBody IntakeHistoryDeleteReqDto req) {
        intakeHistoryCommand.deleteIntakeHistory(providerId, productId, req.intakeTime());

        return ApiResponse.success("deleted");
    }
}
