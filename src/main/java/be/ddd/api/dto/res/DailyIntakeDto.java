package be.ddd.api.dto.res;

import java.time.LocalDateTime;
import java.util.List;

public record DailyIntakeDto(
        LocalDateTime date,
        List<IntakeRecordDto> records,
        int totalKcal,
        int totalSugarGrams,
        int totalCaffeine) {

    public DailyIntakeDto(LocalDateTime date, List<IntakeRecordDto> records) {
        this(
                date,
                records,
                records.stream().mapToInt(r -> r.nutrition().getServingKcal()).sum(),
                records.stream().mapToInt(r -> r.nutrition().getSugarG()).sum(),
                records.stream().mapToInt(r -> r.nutrition().getCaffeineMg()).sum());
    }
}
