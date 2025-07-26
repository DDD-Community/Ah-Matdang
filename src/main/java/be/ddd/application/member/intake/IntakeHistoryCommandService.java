package be.ddd.application.member.intake;

import be.ddd.api.dto.req.IntakeRegistrationRequestDto;

public interface IntakeHistoryCommandService {
    Long registerIntake(Long memberId, IntakeRegistrationRequestDto requestDto);
}
