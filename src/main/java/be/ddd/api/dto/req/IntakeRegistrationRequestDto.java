package be.ddd.api.dto.req;

import be.ddd.common.validation.NotFutureDate;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record IntakeRegistrationRequestDto(
        @NotNull(message = "음료 ID는 필수입니다.") UUID productId,
        @NotNull(message = "섭취 날짜는 필수입니다.") @NotFutureDate LocalDateTime intakeTime) {}
