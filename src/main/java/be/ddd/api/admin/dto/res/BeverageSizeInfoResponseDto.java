package be.ddd.api.admin.dto.res;

import be.ddd.domain.entity.crawling.BeverageSize;
import be.ddd.domain.entity.crawling.BeverageSizeInfo;
import lombok.Getter;

@Getter
public class BeverageSizeInfoResponseDto {
    private final BeverageSize size;
    private final BeverageNutritionResponseDto nutrition;

    public BeverageSizeInfoResponseDto(BeverageSizeInfo sizeInfo) {
        this.size = sizeInfo.getSizeType();
        this.nutrition = new BeverageNutritionResponseDto(sizeInfo.getBeverageNutrition());
    }
}
