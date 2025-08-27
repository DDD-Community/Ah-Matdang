package be.ddd.application.member.intake;

import be.ddd.api.dto.res.IntakeRecordDto;
import be.ddd.api.dto.res.QIntakeRecordDto;
import be.ddd.domain.entity.crawling.QBeverageSizeInfo;
import be.ddd.domain.entity.member.intake.QIntakeHistory;
import be.ddd.domain.exception.MemberNotFoundException;
import be.ddd.domain.repo.IntakeHistoryRepositoryCustom;
import be.ddd.domain.repo.MemberRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class IntakeHistoryRepositoryImpl implements IntakeHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final MemberRepository memberRepository;
    private final QIntakeHistory intakeHistory = QIntakeHistory.intakeHistory;
    private final QBeverageSizeInfo beverageSizeInfo = QBeverageSizeInfo.beverageSizeInfo;

    @Override
    public List<IntakeRecordDto> findByMemberIdAndDate(
            String providerId, LocalDateTime intakeTime) {
        LocalDateTime startOfDay = intakeTime.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = intakeTime.toLocalDate().atTime(LocalTime.MAX);

        Long memberId =
                memberRepository
                        .findByProviderId(providerId)
                        .orElseThrow(MemberNotFoundException::new)
                        .getId();

        return queryFactory
                .select(
                        new QIntakeRecordDto(
                                intakeHistory.id,
                                intakeHistory.cafeBeverage.productId,
                                intakeHistory.cafeBeverage.name,
                                intakeHistory.cafeBeverage.cafeStore.cafeBrand,
                                intakeHistory.intakeTime,
                                beverageSizeInfo.beverageNutrition,
                                intakeHistory.cafeBeverage.imgUrl,
                                intakeHistory.cafeBeverage.sugarLevel,
                                intakeHistory.sizeType))
                .from(intakeHistory)
                .leftJoin(beverageSizeInfo)
                .on(
                        beverageSizeInfo
                                .cafeBeverage
                                .id
                                .eq(intakeHistory.cafeBeverage.id)
                                .and(beverageSizeInfo.sizeType.eq(intakeHistory.sizeType)))
                .where(
                        intakeHistory.member.id.eq(memberId),
                        intakeHistory.intakeTime.between(startOfDay, endOfDay))
                .orderBy(intakeHistory.intakeTime.asc())
                .fetch();
    }

    @Override
    public List<IntakeRecordDto> findByMemberIdAndDateBetween(
            String providerId, LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime startDateTime = startDate.toLocalDate().atStartOfDay();
        LocalDateTime endDateTime = endDate.toLocalDate().atTime(LocalTime.MAX);

        Long memberId =
                memberRepository
                        .findByProviderId(providerId)
                        .orElseThrow(MemberNotFoundException::new)
                        .getId();

        return queryFactory
                .select(
                        new QIntakeRecordDto(
                                intakeHistory.id,
                                intakeHistory.cafeBeverage.productId,
                                intakeHistory.cafeBeverage.name,
                                intakeHistory.cafeBeverage.cafeStore.cafeBrand,
                                intakeHistory.intakeTime,
                                beverageSizeInfo.beverageNutrition,
                                intakeHistory.cafeBeverage.imgUrl,
                                intakeHistory.cafeBeverage.sugarLevel,
                                intakeHistory.sizeType))
                .from(intakeHistory)
                .leftJoin(beverageSizeInfo)
                .on(
                        beverageSizeInfo
                                .cafeBeverage
                                .id
                                .eq(intakeHistory.cafeBeverage.id)
                                .and(beverageSizeInfo.sizeType.eq(intakeHistory.sizeType)))
                .where(
                        intakeHistory.member.id.eq(memberId),
                        intakeHistory.intakeTime.between(startDateTime, endDateTime))
                .orderBy(intakeHistory.intakeTime.asc())
                .fetch();
    }

    @Override
    public long deleteIntakeHistory(String providerId, UUID productId, LocalDateTime intakeTime) {
        Long memberId =
                memberRepository
                        .findByProviderId(providerId)
                        .orElseThrow(MemberNotFoundException::new)
                        .getId();

        return queryFactory
                .delete(intakeHistory)
                .where(
                        intakeHistory.member.id.eq(memberId),
                        intakeHistory.cafeBeverage.productId.eq(productId),
                        intakeHistory.intakeTime.eq(intakeTime))
                .execute();
    }

    @Override
    public Map<Long, Double> sumSugarByMemberIdsAndDate(
            List<String> providerIds, LocalDateTime date) {
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = date.toLocalDate().atTime(LocalTime.MAX);

        List<Long> memberIds =
                memberRepository.findByProviderIdIn(providerIds).stream()
                        .map(member -> member.getId())
                        .collect(Collectors.toList());

        return queryFactory
                .select(
                        intakeHistory.member.id,
                        beverageSizeInfo.beverageNutrition.sugarG.doubleValue().sum())
                .from(intakeHistory)
                .leftJoin(intakeHistory.cafeBeverage.sizes, beverageSizeInfo)
                .where(
                        intakeHistory.member.id.in(memberIds),
                        intakeHistory.intakeTime.between(startOfDay, endOfDay))
                .groupBy(intakeHistory.member.id)
                .transform(
                        com.querydsl.core.group.GroupBy.groupBy(intakeHistory.member.id)
                                .as(beverageSizeInfo.beverageNutrition.sugarG.doubleValue().sum()));
    }
}
