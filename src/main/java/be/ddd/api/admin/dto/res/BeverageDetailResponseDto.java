package be.ddd.api.admin.dto.res;

import be.ddd.domain.entity.crawling.CafeBeverage;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class BeverageDetailResponseDto {
    private final Long id;
    private final String name;
    private final be.ddd.domain.entity.crawling.CafeBrand brand;
    private final String imageUrl;
    private final List<BeverageSizeInfoResponseDto> sizes;

    public BeverageDetailResponseDto(CafeBeverage beverage) {
        this.id = beverage.getId();
        this.name = beverage.getName();
        this.brand = beverage.getCafeStore().getCafeBrand();
        this.imageUrl = beverage.getImgUrl();
        this.sizes =
                beverage.getSizes().stream()
                        .map(BeverageSizeInfoResponseDto::new)
                        .collect(Collectors.toList());
    }
}
