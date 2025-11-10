package be.ddd.application.beverage;

import be.ddd.api.dto.res.BeverageCountDto;
import be.ddd.api.dto.res.BeverageSearchResultDto;
import be.ddd.api.dto.res.CafeBeverageDetailsDto;
import be.ddd.application.beverage.dto.BrandBeverageListResponseDto;
import be.ddd.domain.entity.crawling.CafeBrand;
import be.ddd.domain.entity.crawling.SugarLevel;
import java.util.Optional;
import java.util.UUID;

public interface CafeBeverageQueryService {
    BrandBeverageListResponseDto getCafeBeverageCursorPage(
            Long cursor,
            int size,
            Optional<CafeBrand> brandFilter,
            Optional<SugarLevel> sugarLevel,
            Long memberId,
            Boolean onlyLiked,
            Optional<be.ddd.domain.entity.crawling.BeverageSize> preferredSize);

    CafeBeverageDetailsDto getCafeBeverageByProductId(UUID productId);

    BeverageCountDto getBeverageCountByBrandAndSugarLevel(
            Optional<CafeBrand> brandFilter, Long MemberId);

    BeverageSearchResultDto searchBeverages(
            String keyword, Long memberId, Optional<SugarLevel> sugarLevel, Boolean onlyLiked);
}
