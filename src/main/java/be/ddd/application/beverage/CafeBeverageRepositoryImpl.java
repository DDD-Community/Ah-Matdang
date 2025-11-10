package be.ddd.application.beverage;

import static com.querydsl.core.types.dsl.Expressions.constant;
import static com.querydsl.core.types.dsl.Expressions.numberTemplate;

import be.ddd.api.dto.res.BeverageCountDto;
import be.ddd.application.beverage.dto.BeverageInfoInBrandDto;
import be.ddd.application.beverage.dto.BeverageSearchDto;
import be.ddd.application.beverage.dto.CafeStoreDto;
import be.ddd.application.beverage.dto.QBeverageSearchDto;
import be.ddd.domain.entity.crawling.*;
import be.ddd.domain.entity.member.QMemberBeverageLike;
import be.ddd.domain.repo.CafeBeverageRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CafeBeverageRepositoryImpl implements CafeBeverageRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(CafeBeverageRepositoryImpl.class);

    private final JPAQueryFactory queryFactory;
    private final BeverageQueryPredicates beverageQueryPredicates;
    private final QCafeBeverage beverage = QCafeBeverage.cafeBeverage;
    private final QBeverageSizeInfo beverageSizeInfo = QBeverageSizeInfo.beverageSizeInfo;
    private final QMemberBeverageLike memberBeverageLike = QMemberBeverageLike.memberBeverageLike;

    @Override
    public List<BeverageInfoInBrandDto> findWithCursor(
            Long cursor,
            int limit,
            @Nullable CafeBrand brand,
            @Nullable SugarLevel sugarLevel,
            Long memberId,
            @Nullable Boolean onlyLiked,
            Optional<BeverageSize> preferredSize) {

        BooleanExpression sizeTypeCondition =
                preferredSize.map(beverageSizeInfo.sizeType::eq).orElse(null);

        return queryFactory
                .select(
                        Projections.constructor(
                                BeverageInfoInBrandDto.class,
                                beverage.id,
                                beverage.cafeStore.cafeBrand,
                                beverage.productId,
                                beverage.name,
                                beverage.imgUrl,
                                beverage.beverageType,
                                beverageSizeInfo.beverageNutrition,
                                memberBeverageLike.isNotNull()))
                .from(beverage)
                .leftJoin(beverage.sizes, beverageSizeInfo)
                .leftJoin(memberBeverageLike)
                .on(
                        beverage.id
                                .eq(memberBeverageLike.beverage.id)
                                .and(memberBeverageLike.member.id.eq(memberId)))
                .where(
                        beverage.id.gt(cursor),
                        beverageQueryPredicates.brandEq(brand),
                        beverageQueryPredicates.sugarLevelEq(sugarLevel),
                        beverageQueryPredicates.onlyLiked(onlyLiked),
                        sizeTypeCondition)
                .orderBy(beverage.id.asc())
                .limit(limit)
                .fetch();
    }

    @Override
    public BeverageCountDto countSugarLevelByBrand(@Nullable CafeBrand brandFilter, Long memberId) {
        return countSugarLevelBy(beverageQueryPredicates.brandEq(brandFilter), memberId);
    }

    @Override
    public List<BeverageSearchDto> searchByName(
            String keyword,
            Long memberId,
            Optional<SugarLevel> sugarLevelFilter,
            Boolean onlyLiked) {

        NumberExpression<Double> relevance = calculateRelevance(keyword);
        NumberExpression<Integer> likeOrder = calculateLikeOrder();

        SugarLevel sugar = sugarLevelFilter.orElse(null);

        return queryFactory
                .select(
                        new QBeverageSearchDto(
                                beverage.id,
                                beverage.productId,
                                beverage.name,
                                beverage.imgUrl,
                                beverage.beverageType,
                                Projections.constructor(
                                        CafeStoreDto.class, beverage.cafeStore.cafeBrand),
                                beverageSizeInfo.beverageNutrition,
                                memberBeverageLike.isNotNull()))
                .from(beverage)
                .leftJoin(beverage.sizes, beverageSizeInfo)
                .leftJoin(memberBeverageLike)
                .on(
                        beverage.id
                                .eq(memberBeverageLike.beverage.id)
                                .and(memberBeverageLike.member.id.eq(memberId)))
                .where(
                        beverageQueryPredicates.keywordSearch(keyword),
                        beverageQueryPredicates.sugarLevelEq(sugar),
                        beverageQueryPredicates.onlyLiked(onlyLiked),
                        beverageSizeInfo.sizeType.eq(
                                be.ddd.domain.entity.crawling.BeverageSize.TALL))
                .orderBy(likeOrder.desc(), relevance.desc())
                .fetch();
    }

    private NumberExpression<Double> calculateRelevance(String keyword) {
        String booleanWildcard = "+" + keyword.trim() + "*";
        return numberTemplate(
                Double.class, "fulltext_match({0}, {1})", beverage.name, constant(booleanWildcard));
    }

    private NumberExpression<Integer> calculateLikeOrder() {
        return numberTemplate(
                Integer.class,
                "case when {0} is not null then 1 else 0 end",
                memberBeverageLike.beverage.id);
    }

    @Override
    public BeverageCountDto countSugarLevelBySearchFilters(
            String keyword, Long memberId, Optional<SugarLevel> sugarLevel, Boolean onlyLiked) {

        SugarLevel sugar = sugarLevel.orElse(SugarLevel.HIGH);

        JPAQuery<BeverageCountDto> query =
                queryFactory
                        .select(
                                Projections.constructor(
                                        BeverageCountDto.class,
                                        beverage.id.countDistinct(),
                                        new CaseBuilder()
                                                .when(beverage.sugarLevel.eq(SugarLevel.ZERO))
                                                .then(1L)
                                                .otherwise(0L)
                                                .sum(),
                                        new CaseBuilder()
                                                .when(beverage.sugarLevel.eq(SugarLevel.LOW))
                                                .then(1L)
                                                .otherwise(0L)
                                                .sum(),
                                        new CaseBuilder()
                                                .when(memberBeverageLike.isNotNull())
                                                .then(1L)
                                                .otherwise(0L)
                                                .sum()))
                        .from(beverage);

        query.leftJoin(memberBeverageLike)
                .on(
                        beverage.id
                                .eq(memberBeverageLike.beverage.id)
                                .and(memberBeverageLike.member.id.eq(memberId)));

        query.where(beverageQueryPredicates.keywordSearch(keyword));

        BeverageCountDto result = query.fetchOne();
        log.info(
                "countSugarLevelBySearchFilters - Total Count: {}, Zero Count: {}, Low Count: {}, Like Count: {}",
                result.totalCount(),
                result.zeroCount(),
                result.lowCount(),
                result.likeCount());
        return result;
    }

    @Override
    public long countAllLikedByMemberAndFilters(
            CafeBrand brand, SugarLevel sugarLevel, Long memberId) {
        Long count =
                queryFactory
                        .select(beverage.count())
                        .from(beverage)
                        .innerJoin(memberBeverageLike)
                        .on(beverage.id.eq(memberBeverageLike.beverage.id))
                        .where(memberBeverageLike.member.id.eq(memberId))
                        .fetchOne();
        return count != null ? count : 0L;
    }

    private BeverageCountDto countSugarLevelBy(BooleanExpression predicate, Long memberId) {
        JPAQuery<BeverageCountDto> query =
                queryFactory
                        .select(
                                Projections.constructor(
                                        BeverageCountDto.class,
                                        beverage.count(),
                                        new CaseBuilder()
                                                .when(beverage.sugarLevel.eq(SugarLevel.ZERO))
                                                .then(1L)
                                                .otherwise(0L)
                                                .sum(),
                                        new CaseBuilder()
                                                .when(beverage.sugarLevel.eq(SugarLevel.LOW))
                                                .then(1L)
                                                .otherwise(0L)
                                                .sum(),
                                        new CaseBuilder()
                                                .when(memberBeverageLike.isNotNull())
                                                .then(1L)
                                                .otherwise(0L)
                                                .sum()))
                        .from(beverage)
                        .where(predicate);

        return query.leftJoin(memberBeverageLike)
                .on(
                        beverage.id
                                .eq(memberBeverageLike.beverage.id)
                                .and(memberBeverageLike.member.id.eq(memberId)))
                .fetchOne();
    }

    @Override
    public List<CafeBeverage> findBeverages(@Nullable CafeBrand brand, @Nullable String keyword) {
        return queryFactory
                .selectFrom(beverage)
                .leftJoin(beverage.sizes, beverageSizeInfo)
                .fetchJoin()
                .where(
                        beverageQueryPredicates.brandEq(brand),
                        beverageQueryPredicates.keywordSearch(keyword))
                .distinct()
                .fetch();
    }
}
