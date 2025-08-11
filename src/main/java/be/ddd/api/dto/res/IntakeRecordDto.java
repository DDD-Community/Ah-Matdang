package be.ddd.api.dto.res;

import be.ddd.domain.entity.crawling.BeverageNutrition;
import be.ddd.domain.entity.crawling.BeverageSize;
import be.ddd.domain.entity.crawling.CafeBrand;
import be.ddd.domain.entity.crawling.SugarLevel;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.UUID;

public record IntakeRecordDto(
        Long intakeHistoryId,
        UUID productId,
        String beverageName,
        CafeBrand cafeBrand,
        LocalDateTime intakeTime,
        BeverageNutrition nutrition,
        String imgUrl,
        SugarLevel sugarLevel,
        BeverageSize beverageSize) {

    @QueryProjection
    public IntakeRecordDto {
        // Compact constructor
    }
}
