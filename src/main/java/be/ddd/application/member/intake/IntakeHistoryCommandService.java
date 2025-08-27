package be.ddd.application.member.intake;

import be.ddd.api.dto.req.IntakeRegistrationRequestDto;
import java.time.LocalDateTime;
import java.util.UUID;

public interface IntakeHistoryCommandService {
    Long registerIntake(String providerId, IntakeRegistrationRequestDto requestDto);

    void deleteIntakeHistory(String providerId, UUID productId, LocalDateTime intakeTime);
}
