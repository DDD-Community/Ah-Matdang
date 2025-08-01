package be.ddd.application.member.intake;

import be.ddd.api.dto.res.IntakeRecordDto;
import be.ddd.api.dto.res.QIntakeRecordDto;
import be.ddd.domain.entity.crawling.QBeverageSizeInfo;
import be.ddd.domain.entity.member.intake.QIntakeHistory;
import be.ddd.domain.repo.IntakeHistoryRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class IntakeHistoryRepositoryImpl implements IntakeHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QIntakeHistory intakeHistory = QIntakeHistory.intakeHistory;
    private final QBeverageSizeInfo beverageSizeInfo = QBeverageSizeInfo.beverageSizeInfo;

    @Override
    public List<IntakeRecordDto> findByMemberIdAndDate(Long memberId, LocalDateTime intakeTime) {
        LocalDateTime startOfDay = intakeTime.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = intakeTime.toLocalDate().atTime(LocalTime.MAX);

        return queryFactory
                .select(
                        new QIntakeRecordDto(
                                intakeHistory.id,
                                intakeHistory.cafeBeverage.id,
                                intakeHistory.cafeBeverage.name,
                                intakeHistory.cafeBeverage.cafeStore.cafeBrand,
                                intakeHistory.intakeTime,
                                beverageSizeInfo.beverageNutrition,
                                intakeHistory.cafeBeverage.imgUrl,
                                intakeHistory.cafeBeverage.sugarLevel))
                .from(intakeHistory)
                .leftJoin(intakeHistory.cafeBeverage.sizes, beverageSizeInfo)
                .where(
                        intakeHistory.member.id.eq(memberId),
                        intakeHistory.intakeTime.between(startOfDay, endOfDay))
                .orderBy(intakeHistory.intakeTime.asc())
                .fetch();
    }

    @Override
    public List<IntakeRecordDto> findByMemberIdAndDateBetween(
            Long memberId, LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime startDateTime = startDate.toLocalDate().atStartOfDay();
        LocalDateTime endDateTime = endDate.toLocalDate().atTime(LocalTime.MAX);

        return queryFactory
                .select(
                        new QIntakeRecordDto(
                                intakeHistory.id,
                                intakeHistory.cafeBeverage.id,
                                intakeHistory.cafeBeverage.name,
                                intakeHistory.cafeBeverage.cafeStore.cafeBrand,
                                intakeHistory.intakeTime,
                                beverageSizeInfo.beverageNutrition,
                                intakeHistory.cafeBeverage.imgUrl,
                                intakeHistory.cafeBeverage.sugarLevel))
                .from(intakeHistory)
                .leftJoin(intakeHistory.cafeBeverage.sizes, beverageSizeInfo)
                .where(
                        intakeHistory.member.id.eq(memberId),
                        intakeHistory.intakeTime.between(startDateTime, endDateTime))
                .orderBy(intakeHistory.intakeTime.asc())
                .fetch();
    }

    @Override
    public long deleteIntakeHistory(Long memberId, UUID productId, LocalDateTime intakeTime) {
        return queryFactory
                .delete(intakeHistory)
                .where(
                        intakeHistory.member.id.eq(memberId),
                        intakeHistory.cafeBeverage.productId.eq(productId),
                        intakeHistory.intakeTime.eq(intakeTime))
                .execute();
    }
}
