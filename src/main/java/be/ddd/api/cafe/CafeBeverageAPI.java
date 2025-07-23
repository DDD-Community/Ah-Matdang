package be.ddd.api.cafe;

import be.ddd.api.dto.res.BeverageCountDto;
import be.ddd.api.dto.res.BeverageLikeDto;
import be.ddd.api.dto.res.BeverageSearchDto;
import be.ddd.api.dto.res.CafeBeverageCursorPageDto;
import be.ddd.api.dto.res.CafeBeverageDetailsDto;
import be.ddd.application.beverage.BeverageLikeService;
import be.ddd.application.beverage.CafeBeverageQueryService;
import be.ddd.application.beverage.dto.CafeBeveragePageDto;
import be.ddd.common.dto.ApiResponse;
import be.ddd.common.util.StringBase64EncodingUtil;
import be.ddd.domain.entity.crawling.CafeBrand;
import be.ddd.domain.entity.crawling.SugarLevel;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cafe-beverages")
@RequiredArgsConstructor
public class CafeBeverageAPI {

    private final CafeBeverageQueryService cafeBeverageQueryService;
    private final BeverageLikeService beverageLikeService;
    private final StringBase64EncodingUtil encodingUtil;
    private final Long MEMBER_ID = 1L;

    @GetMapping
    public ApiResponse<CafeBeverageCursorPageDto<CafeBeveragePageDto>> getCafeBeverages(
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "15") @Positive int size,
            @RequestParam(required = false) String cafeBrand,
            @RequestParam(required = false) String sugarLevel) {
        Long decodedCursor =
                Optional.ofNullable(cursor).map(encodingUtil::decodeSignedCursor).orElse(0L);

        Optional<CafeBrand> brand =
                Optional.ofNullable(cafeBrand).flatMap(CafeBrand::findByDisplayName);

        Optional<SugarLevel> sugar = SugarLevel.fromParam(sugarLevel);

        CafeBeverageCursorPageDto<CafeBeveragePageDto> results =
                cafeBeverageQueryService.getCafeBeverageCursorPage(
                        decodedCursor, size, brand, sugar, MEMBER_ID);
        return ApiResponse.success(results);
    }

    @GetMapping("{productId}")
    public ApiResponse<CafeBeverageDetailsDto> getCafeBeverageByProductId(
            @PathVariable("productId") UUID productId) {
        CafeBeverageDetailsDto beverageDetails =
                cafeBeverageQueryService.getCafeBeverageByProductId(productId);
        return ApiResponse.success(beverageDetails);
    }

    @GetMapping("count")
    public ApiResponse<BeverageCountDto> getBeverageCountByBrandAndSugarLevel(
            @RequestParam(required = false) String cafeBrand) {
        Optional<CafeBrand> brand =
                Optional.ofNullable(cafeBrand).flatMap(CafeBrand::findByDisplayName);
        BeverageCountDto countDto =
                cafeBeverageQueryService.getBeverageCountByBrandAndSugarLevel(brand);
        return ApiResponse.success(countDto);
    }

    @PostMapping("{productId}/like")
    public ApiResponse<BeverageLikeDto> likeBeverage(@PathVariable UUID productId) {
        BeverageLikeDto likeDto = beverageLikeService.likeBeverage(MEMBER_ID, productId);
        return ApiResponse.success(likeDto);
    }

    @DeleteMapping("{productId}/unlike")
    public ApiResponse<BeverageLikeDto> unlikeBeverage(@PathVariable UUID productId) {
        BeverageLikeDto unlikeDto = beverageLikeService.unlikeBeverage(MEMBER_ID, productId);
        return ApiResponse.success(unlikeDto);
    }

    @GetMapping("/search")
    public ApiResponse<List<BeverageSearchDto>> searchBeverages(@RequestParam String keyword) {
        List<BeverageSearchDto> results =
                cafeBeverageQueryService.searchBeverages(keyword, MEMBER_ID);
        return ApiResponse.success(results);
    }
}
