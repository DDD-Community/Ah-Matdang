package be.ddd.domain.repo;

import be.ddd.api.dto.res.BeverageCountDto;
import be.ddd.application.beverage.dto.BeverageSearchDto;
import be.ddd.application.beverage.dto.CafeBeveragePageDto;
import be.ddd.domain.entity.crawling.CafeBrand;
import be.ddd.domain.entity.crawling.SugarLevel;
import jakarta.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public interface CafeBeverageRepositoryCustom {
    List<CafeBeveragePageDto> findWithCursor(
            Long cursor,
            int limit,
            @Nullable CafeBrand brand,
            @Nullable SugarLevel sugarLevel,
            Long memberId,
            @Nullable Boolean onlyLiked);

    BeverageCountDto countSugarLevelByBrand(@Nullable CafeBrand brandFilter);

    List<BeverageSearchDto> searchByName(
            String keyword, Long memberId, Optional<SugarLevel> sugarLevel, Boolean onlyLiked);

    long countAllLikedByMemberAndFilters(
            @Nullable CafeBrand brand, @Nullable SugarLevel sugarLevel, Long memberId);

    BeverageCountDto countSugarLevelBySearchFilters(
            String keyword, Long memberId, Optional<SugarLevel> sugarLevel, Boolean onlyLiked);
}
