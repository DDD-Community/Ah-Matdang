package be.ddd.domain.repo;

import be.ddd.api.dto.res.IntakeRecordDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IntakeHistoryRepositoryCustom {
    List<IntakeRecordDto> findByMemberIdAndDate(String providerId, LocalDateTime intakeTime);

    List<IntakeRecordDto> findByMemberIdAndDateBetween(
            String providerId, LocalDateTime startDate, LocalDateTime endDate);

    long deleteIntakeHistory(String providerId, UUID productId, LocalDateTime intakeTime);

    Map<Long, Double> sumSugarByMemberIdsAndDate(List<String> providerIds, LocalDateTime date);
}
