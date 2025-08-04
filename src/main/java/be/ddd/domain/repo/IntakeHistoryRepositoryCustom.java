package be.ddd.domain.repo;

import be.ddd.api.dto.res.IntakeRecordDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IntakeHistoryRepositoryCustom {
    List<IntakeRecordDto> findByMemberIdAndDate(Long memberId, LocalDateTime intakeTime);

    List<IntakeRecordDto> findByMemberIdAndDateBetween(
            Long memberId, LocalDateTime startDate, LocalDateTime endDate);

    long deleteIntakeHistory(Long memberId, UUID productId, LocalDateTime intakeTime);

    Double sumSugarByMemberIdAndDate(Long memberId, LocalDateTime date);
}
