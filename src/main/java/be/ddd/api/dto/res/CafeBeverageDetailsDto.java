package be.ddd.api.dto.res;

import be.ddd.application.beverage.dto.CafeStoreDto;
import be.ddd.domain.entity.crawling.BeverageNutrition;
import be.ddd.domain.entity.crawling.BeverageType;
import java.util.List;
import java.util.UUID;

public record CafeBeverageDetailsDto(
        String name,
        UUID productId,
        String imgUrl,
        BeverageType beverageType,
        BeverageNutrition defaultNutrition,
        List<BeverageSizeDetailDto> sizes,
        CafeStoreDto cafeStoreDto) {

    public static CafeBeverageDetailsDto from(
            String name,
            UUID productId,
            String imgUrl,
            BeverageType beverageType,
            BeverageNutrition defaultNutrition,
            List<BeverageSizeDetailDto> sizes,
            CafeStoreDto cafeStoreDto) {
        return new CafeBeverageDetailsDto(
                name, productId, imgUrl, beverageType, defaultNutrition, sizes, cafeStoreDto);
    }
}
