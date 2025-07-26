package be.ddd.api.dto.res;

import be.ddd.domain.entity.crawling.BeverageNutrition;
import be.ddd.domain.entity.crawling.CafeBrand;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record IntakeRecordDto(
        Long intakeHistoryId,
        Long beverageId,
        String beverageName,
        CafeBrand cafeBrand,
        LocalDateTime intakeTime,
        BeverageNutrition nutrition) {

    @QueryProjection
    public IntakeRecordDto {
        // Compact constructor
    }
}
