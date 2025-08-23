package be.ddd.api.dto.res;

import be.ddd.application.member.dto.res.RecommendedSugar;
import java.time.LocalDateTime;
import java.util.List;

public record DailyIntakeDto(
        LocalDateTime date,
        List<IntakeRecordDto> records,
        int totalKcal,
        int totalSugarGrams,
        int totalCaffeine,
        RecommendedSugar recommendedSugar) {

    public DailyIntakeDto(
            LocalDateTime date, List<IntakeRecordDto> records, RecommendedSugar recommendedSugar) {
        this(
                date,
                records,
                records.stream()
                        .mapToInt(r -> r.nutrition() != null ? r.nutrition().getServingKcal() : 0)
                        .sum(),
                records.stream()
                        .mapToInt(r -> r.nutrition() != null ? r.nutrition().getSugarG() : 0)
                        .sum(),
                records.stream()
                        .mapToInt(r -> r.nutrition() != null ? r.nutrition().getCaffeineMg() : 0)
                        .sum(),
                recommendedSugar);
    }
}
