package be.ddd.application.member.intake;

import be.ddd.api.dto.req.IntakeRegistrationRequestDto;
import java.util.UUID;

public interface IntakeHistoryCommandService {
    Long registerIntake(Long memberId, IntakeRegistrationRequestDto requestDto);

    void deleteIntakeHistory(Long memberId, UUID productId);
}
