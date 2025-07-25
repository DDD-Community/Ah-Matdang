package be.ddd.application.beverage.dto;

import be.ddd.domain.entity.crawling.BeverageNutrition;
import be.ddd.domain.entity.crawling.BeverageType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import java.util.UUID;

public record BeverageSearchDto(
        @JsonIgnore Long id,
        UUID productId,
        String name,
        String imageUrl,
        BeverageType beverageType,
        CafeStoreDto cafeStoreDto,
        BeverageNutrition beverageNutrition,
        boolean isLiked) {
    /** QueryDSL용 생성자 */
    @QueryProjection
    public BeverageSearchDto(
            Long id,
            UUID productId,
            String name,
            String imageUrl,
            BeverageType beverageType,
            CafeStoreDto cafeStoreDto,
            BeverageNutrition beverageNutrition,
            boolean isLiked) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.beverageType = beverageType;
        this.cafeStoreDto = cafeStoreDto;
        this.beverageNutrition = beverageNutrition;
        this.isLiked = isLiked;
    }
}
