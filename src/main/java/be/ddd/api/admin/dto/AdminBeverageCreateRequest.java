package be.ddd.api.admin.dto;

import be.ddd.domain.entity.crawling.CafeBrand;
import java.util.List;

public record AdminBeverageCreateRequest(
        String beverageName, CafeBrand cafeBrand, String imageUrl, List<SizeNutritionDto> sizes) {}
