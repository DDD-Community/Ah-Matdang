package be.ddd.application.beverage.dto;

import be.ddd.domain.entity.crawling.BeverageNutrition;
import be.ddd.domain.entity.crawling.BeverageType;
import be.ddd.domain.entity.crawling.CafeBeverage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;

public record CafeBeveragePageDto(
        @JsonIgnore Long id,
        UUID productId,
        String name,
        String imgUrl,
        BeverageType beverageType,
        CafeStoreDto cafeStoreDto,
        BeverageNutrition beverageNutrition,
        boolean isLiked) {

    public static CafeBeveragePageDto of(CafeBeverage e, boolean isLiked) {
        return new CafeBeveragePageDto(
                e.getId(),
                e.getProductId(),
                e.getName(),
                e.getImgUrl(),
                e.getBeverageType(),
                new CafeStoreDto(e.getCafeStore().getCafeBrand()),
                e.getBeverageNutrition(),
                isLiked);
    }
}
