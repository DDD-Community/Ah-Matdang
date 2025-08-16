package be.ddd.application.beverage;

import be.ddd.domain.entity.crawling.CafeBrand;
import be.ddd.domain.entity.crawling.QCafeBeverage;
import be.ddd.domain.entity.crawling.SugarLevel;
import be.ddd.domain.entity.member.QMemberBeverageLike;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
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
            return beverage.sizes.any().beverageNutrition.sugarG.between(1, 20);
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
        String booleanWildcard = "+" + keyword.trim() + "*";
        return Expressions.numberTemplate(
                        Double.class,
                        "fulltext_match({0}, {1})",
                        beverage.name,
                        Expressions.constant(booleanWildcard))
                .gt(0);
    }
}
