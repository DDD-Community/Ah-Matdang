package be.ddd.application.beverage.dto;

import java.util.List;

public record BrandBeverageDto(String koreanBrandName, List<BeverageInfoInBrandDto> items) {}
