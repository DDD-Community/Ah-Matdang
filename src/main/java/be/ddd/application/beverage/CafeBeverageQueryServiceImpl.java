package be.ddd.application.beverage;

import be.ddd.api.dto.res.BeverageCountDto;
import be.ddd.api.dto.res.BeverageSearchResultDto;
import be.ddd.api.dto.res.BeverageSizeDetailDto;
import be.ddd.api.dto.res.CafeBeverageCursorPageDto;
import be.ddd.api.dto.res.CafeBeverageDetailsDto;
import be.ddd.application.beverage.dto.BeverageSearchDto;
import be.ddd.application.beverage.dto.CafeBeveragePageDto;
import be.ddd.application.beverage.dto.CafeStoreDto;
import be.ddd.common.util.StringBase64EncodingUtil;
import be.ddd.domain.entity.crawling.BeverageNutrition;
import be.ddd.domain.entity.crawling.CafeBeverage;
import be.ddd.domain.entity.crawling.CafeBrand;
import be.ddd.domain.entity.crawling.SugarLevel;
import be.ddd.domain.exception.CafeBeverageNotFoundException;
import be.ddd.domain.repo.CafeBeverageRepository;
import be.ddd.domain.repo.MemberBeverageLikeRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Log4j2
public class CafeBeverageQueryServiceImpl implements CafeBeverageQueryService {
    private final CafeBeverageRepository beverageRepository;
    private final StringBase64EncodingUtil encodingUtil;
    private final MemberBeverageLikeRepository likeRepository;

    @Override
    public CafeBeverageCursorPageDto<CafeBeveragePageDto> getCafeBeverageCursorPage(
            Long cursor,
            int size,
            Optional<CafeBrand> brandFilter,
            Optional<SugarLevel> sugarLevel,
            Long memberId) {

        CafeBrand brand = brandFilter.orElse(null);
        SugarLevel sugar = sugarLevel.orElse(null);

        List<CafeBeveragePageDto> fetched =
                beverageRepository.findWithCursor(cursor, size + 1, brand, sugar, memberId);

        boolean hasNext = fetched.size() > size;

        String nextCursor = null;
        if (hasNext) {
            nextCursor = encodingUtil.encodeSignedCursor(fetched.get(size - 1).id());
        }

        List<CafeBeveragePageDto> pageResults = fetched.stream().limit(size).toList();

        long totalLikedCount =
                beverageRepository.countAllLikedByMemberAndFilters(brand, sugar, memberId);

        return new CafeBeverageCursorPageDto<>(pageResults, nextCursor, hasNext, totalLikedCount);
    }

    @Override
    public CafeBeverageDetailsDto getCafeBeverageByProductId(UUID productId) {

        log.info("product detail query:{}", productId);
        CafeBeverage fetch =
                beverageRepository
                        .findByProductId(productId)
                        .orElseThrow(CafeBeverageNotFoundException::new);

        BeverageNutrition defaultNutrition;
        List<BeverageSizeDetailDto> sizes;

        if (fetch.getSizes().isEmpty()) {
            defaultNutrition = BeverageNutrition.empty();
            sizes = List.of();
        } else {
            defaultNutrition = fetch.getSizes().get(0).getBeverageNutrition();
            sizes =
                    fetch.getSizes().stream()
                            .map(
                                    sizeInfo ->
                                            new BeverageSizeDetailDto(
                                                    sizeInfo.getSizeType(),
                                                    sizeInfo.getBeverageNutrition()))
                            .toList();
        }

        return CafeBeverageDetailsDto.from(
                fetch.getName(),
                fetch.getProductId(),
                fetch.getImgUrl(),
                fetch.getBeverageType(),
                defaultNutrition,
                sizes,
                new CafeStoreDto(fetch.getCafeStore().getCafeBrand()));
    }

    @Override
    public BeverageCountDto getBeverageCountByBrandAndSugarLevel(Optional<CafeBrand> brandFilter) {

        CafeBrand brand = brandFilter.orElse(null);

        return beverageRepository.countSugarLevelByBrand(brand);
    }

    @Override
    public BeverageSearchResultDto searchBeverages(String keyword, Long memberId) {
        List<BeverageSearchDto> beverageSearchResults =
                beverageRepository.searchByName(keyword, memberId);
        long likeCount = beverageSearchResults.stream().filter(BeverageSearchDto::isLiked).count();
        return new BeverageSearchResultDto(beverageSearchResults, likeCount);
    }
}
