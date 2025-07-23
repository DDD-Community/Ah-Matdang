package be.ddd.domain.repo;

import be.ddd.api.dto.res.BeverageCountDto;
import be.ddd.api.dto.res.BeverageSearchDto;
import be.ddd.application.beverage.dto.CafeBeveragePageDto;
import be.ddd.domain.entity.crawling.CafeBrand;
import be.ddd.domain.entity.crawling.SugarLevel;
import jakarta.annotation.Nullable;
import java.util.List;

public interface CafeBeverageRepositoryCustom {
    List<CafeBeveragePageDto> findWithCursor(
            Long cursor,
            int limit,
            @Nullable CafeBrand brand,
            @Nullable SugarLevel sugarLevel,
            Long memberId);

    BeverageCountDto countSugarLevelByBrand(@Nullable CafeBrand brandFilter);

    List<BeverageSearchDto> searchByName(String keyword, Long memberId);
}
