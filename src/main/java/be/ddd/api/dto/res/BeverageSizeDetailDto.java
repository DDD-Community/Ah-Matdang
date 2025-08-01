package be.ddd.api.dto.res;

import be.ddd.domain.entity.crawling.BeverageNutrition;
import be.ddd.domain.entity.crawling.BeverageSize;

public record BeverageSizeDetailDto(BeverageSize sizeType, BeverageNutrition nutrition) {}
