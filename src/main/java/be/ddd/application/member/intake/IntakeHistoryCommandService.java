package be.ddd.application.member.intake;

import be.ddd.api.dto.req.IntakeRegistrationRequestDto;
import be.ddd.domain.entity.crawling.BeverageSize;
import java.time.LocalDateTime;
import java.util.UUID;

public interface IntakeHistoryCommandService {
    Long registerIntake(Long memberId, IntakeRegistrationRequestDto requestDto, BeverageSize size);

    void deleteIntakeHistory(Long memberId, UUID productId, LocalDateTime intakeTime);
}
