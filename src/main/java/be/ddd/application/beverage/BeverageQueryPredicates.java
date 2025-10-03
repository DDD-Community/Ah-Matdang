package be.ddd.application.beverage;

import be.ddd.domain.entity.crawling.BeverageSize;
import be.ddd.domain.entity.crawling.CafeBrand;
import be.ddd.domain.entity.crawling.QCafeBeverage;
import be.ddd.domain.entity.crawling.SugarLevel;
import be.ddd.domain.entity.member.QMemberBeverageLike;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class BeverageQueryPredicates {

    private final QCafeBeverage beverage = QCafeBeverage.cafeBeverage;
    private final QMemberBeverageLike memberBeverageLike = QMemberBeverageLike.memberBeverageLike;

    public BooleanExpression brandEq(@Nullable CafeBrand brand) {
        return brand != null ? beverage.cafeStore.cafeBrand.eq(brand) : null;
    }

    public BooleanExpression sugarLevelEq(@Nullable SugarLevel sugarLevel) {
        if (sugarLevel == null) {
            return null;
        }
        if (sugarLevel == SugarLevel.ZERO) {
            return beverage.sizes.any().beverageNutrition.sugarG.eq(0);
        }
        if (sugarLevel == SugarLevel.LOW) {
            NumberExpression<Integer> volume =
                    new CaseBuilder()
                            .when(beverage.sizes.any().sizeType.eq(BeverageSize.TALL))
                            .then(355)
                            .when(beverage.sizes.any().sizeType.eq(BeverageSize.GRANDE))
                            .then(473)
                            .when(beverage.sizes.any().sizeType.eq(BeverageSize.VENTI))
                            .then(591)
                            .when(beverage.sizes.any().sizeType.eq(BeverageSize.SHORT))
                            .then(237)
                            .otherwise(0);

            return beverage.sizes
                    .any()
                    .beverageNutrition
                    .sugarG
                    .gt(0)
                    .and(volume.gt(0))
                    .and(
                            beverage.sizes
                                    .any()
                                    .beverageNutrition
                                    .sugarG
                                    .doubleValue()
                                    .divide(volume)
                                    .multiply(100)
                                    .loe(2.5));
        }
        return null;
    }

    public BooleanExpression onlyLiked(@Nullable Boolean onlyLiked) {
        if (onlyLiked == null || !onlyLiked) {
            return null;
        }
        return memberBeverageLike.isNotNull();
    }

    public BooleanExpression keywordSearch(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }
        return beverage.name.containsIgnoreCase(keyword);
    }
}
