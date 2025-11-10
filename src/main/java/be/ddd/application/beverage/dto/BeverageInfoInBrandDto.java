package be.ddd.application.beverage.dto;

import be.ddd.domain.entity.crawling.BeverageNutrition;
import be.ddd.domain.entity.crawling.BeverageType;
import be.ddd.domain.entity.crawling.CafeBrand;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;

public record BeverageInfoInBrandDto(
        @JsonIgnore Long id,
        @JsonIgnore CafeBrand cafeBrand,
        UUID productId,
        String name,
        String imgUrl,
        BeverageType beverageType,
        BeverageNutrition beverageNutrition,
        boolean isLiked) {}
