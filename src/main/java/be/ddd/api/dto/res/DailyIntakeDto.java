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
                records.stream().mapToInt(r -> r.nutrition().getServingKcal()).sum(),
                records.stream().mapToInt(r -> r.nutrition().getSugarG()).sum(),
                records.stream().mapToInt(r -> r.nutrition().getCaffeineMg()).sum(),
                recommendedSugar);
    }
}
