package be.ddd.application.beverage;

import be.ddd.api.dto.res.BeverageCountDto;
import be.ddd.api.dto.res.BeverageSearchDto;
import be.ddd.api.dto.res.QBeverageSearchDto;
import be.ddd.application.beverage.dto.CafeBeveragePageDto;
import be.ddd.application.beverage.dto.CafeStoreDto;
import be.ddd.domain.entity.crawling.CafeBrand;
import be.ddd.domain.entity.crawling.QCafeBeverage;
import be.ddd.domain.entity.crawling.SugarLevel;
import be.ddd.domain.entity.member.QMemberBeverageLike;
import be.ddd.domain.repo.CafeBeverageRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CafeBeverageRepositoryImpl implements CafeBeverageRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QCafeBeverage beverage = QCafeBeverage.cafeBeverage;
    private final QMemberBeverageLike memberBeverageLike = QMemberBeverageLike.memberBeverageLike;

    @Override
    public List<CafeBeveragePageDto> findWithCursor(
            Long cursor, int limit, CafeBrand brand, SugarLevel sugarLevel, Long memberId) {

        return queryFactory
                .select(
                        Projections.constructor(
                                CafeBeveragePageDto.class,
                                beverage.id,
                                beverage.productId,
                                beverage.name,
                                beverage.imgUrl,
                                beverage.beverageType,
                                Projections.constructor(
                                        CafeStoreDto.class, beverage.cafeStore.cafeBrand),
                                beverage.beverageNutrition,
                                memberBeverageLike.isNotNull()))
                .from(beverage)
                .leftJoin(memberBeverageLike)
                .on(
                        beverage.id
                                .eq(memberBeverageLike.beverage.id)
                                .and(memberBeverageLike.member.id.eq(memberId)))
                .where(beverage.id.gt(cursor), brandEq(brand), sugarLevelEq(sugarLevel))
                .orderBy(beverage.id.asc())
                .limit(limit)
                .fetch();
    }

    /*@Override
    public long countAll(CafeBrand brand) {
        Long cnt =
            queryFactory
                .select(beverage.count())
                .from(beverage)
                .where(brandEq(brand))
                .fetchOne();
        return cnt != null ? cnt : 0L;
    }

    @Override
    public long countBySugar(CafeBrand brand, SugarLevel sugarLevel) {
        Long cnt =
            queryFactory
                .select(beverage.count())
                .from(beverage)
                .where(brandEq(brand), sugarLevelEq(sugarLevel))
                .fetchOne();
        return cnt != null ? cnt : 0L;
    }*/

    @Override
    public BeverageCountDto countSugarLevelByBrand(@Nullable CafeBrand brandFilter) {
        BooleanExpression brandCond =
                brandFilter != null ? beverage.cafeStore.cafeBrand.eq(brandFilter) : null;

        return queryFactory
                .select(
                        Projections.constructor(
                                BeverageCountDto.class,
                                // 전체 개수
                                beverage.count(),
                                // 무당
                                new CaseBuilder()
                                        .when(beverage.sugarLevel.eq(SugarLevel.ZERO))
                                        .then(1L)
                                        .otherwise(0L)
                                        .sum(),
                                // 저당
                                new CaseBuilder()
                                        .when(beverage.sugarLevel.eq(SugarLevel.LOW))
                                        .then(1L)
                                        .otherwise(0L)
                                        .sum()))
                .from(beverage)
                .where(brandCond)
                .fetchOne();
    }

    private BooleanExpression brandEq(CafeBrand b) {
        return b != null ? beverage.cafeStore.cafeBrand.eq(b) : null;
    }

    private BooleanExpression sugarLevelEq(SugarLevel sugarLevel) {
        if (sugarLevel == null) {
            return null;
        }
        if (sugarLevel == SugarLevel.ZERO) {
            return beverage.beverageNutrition.sugarG.eq(0);
        }
        if (sugarLevel == SugarLevel.LOW) {
            return beverage.beverageNutrition.sugarG.between(1, 20);
        }
        return null;
    }

    @Override
    public List<BeverageSearchDto> searchByName(String keyword, Long memberId) {
        return queryFactory
                .select(
                        new QBeverageSearchDto(
                                beverage.id,
                                beverage.name,
                                beverage.cafeStore.cafeBrand.stringValue(),
                                beverage.imgUrl,
                                beverage.beverageType,
                                beverage.beverageNutrition,
                                memberBeverageLike.isNotNull()))
                .from(beverage)
                .leftJoin(memberBeverageLike)
                .on(
                        beverage.id
                                .eq(memberBeverageLike.beverage.id)
                                .and(memberBeverageLike.member.id.eq(memberId)))
                .where(beverage.name.containsIgnoreCase(keyword))
                .fetch();
    }
}
