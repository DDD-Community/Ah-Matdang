package be.ddd.domain.repo;

import be.ddd.api.dto.res.IntakeRecordDto;
import java.time.LocalDateTime;
import java.util.List;

public interface IntakeHistoryRepositoryCustom {
    List<IntakeRecordDto> findByMemberIdAndDate(Long memberId, LocalDateTime intakeTime);

    List<IntakeRecordDto> findByMemberIdAndDateBetween(
            Long memberId, LocalDateTime startDate, LocalDateTime endDate);
}
