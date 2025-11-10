package be.ddd.application.beverage.dto;

import java.util.List;

public record BrandBeverageListResponseDto(
        List<BrandBeverageDto> brands, String nextCursor, boolean hasNext, long totalLikedCount) {}
