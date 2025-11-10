package be.ddd.api.cafe;

import be.ddd.api.dto.res.*;
import be.ddd.api.security.custom.CurrentUser;
import be.ddd.application.beverage.BeverageLikeService;
import be.ddd.application.beverage.CafeBeverageQueryService;
import be.ddd.application.beverage.dto.BrandBeverageListResponseDto;
import be.ddd.application.member.MemberQueryService;
import be.ddd.common.dto.ApiResponse;
import be.ddd.common.util.StringBase64EncodingUtil;
import be.ddd.domain.entity.crawling.BeverageSize;
import be.ddd.domain.entity.crawling.CafeBrand;
import be.ddd.domain.entity.crawling.SugarLevel;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/cafe-beverages")
@RequiredArgsConstructor
public class CafeBeverageAPI {

    private final CafeBeverageQueryService cafeBeverageQueryService;
    private final BeverageLikeService beverageLikeService;
    private final StringBase64EncodingUtil encodingUtil;
    private final MemberQueryService memberQueryService;

    @GetMapping
    public ApiResponse<BrandBeverageListResponseDto> getCafeBeverages(
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "15") @Positive int size,
            @RequestParam(required = false) String cafeBrand,
            @RequestParam(required = false) String sugarLevel,
            @RequestParam(required = false) String sizeType,
            @RequestParam(required = false) Boolean onlyLiked,
            @CurrentUser String providerId) {
        Long decodedCursor =
                Optional.ofNullable(cursor).map(encodingUtil::decodeSignedCursor).orElse(0L);

        Optional<CafeBrand> brand =
                Optional.ofNullable(cafeBrand).flatMap(CafeBrand::findByDisplayName);

        Optional<SugarLevel> sugar = SugarLevel.fromParam(sugarLevel);

        Optional<BeverageSize> preferredSize =
                Optional.ofNullable(sizeType).flatMap(BeverageSize::fromStringOptional);

        BrandBeverageListResponseDto results =
                cafeBeverageQueryService.getCafeBeverageCursorPage(
                        decodedCursor,
                        size,
                        brand,
                        sugar,
                        memberQueryService.getMemberIdByProviderId(providerId),
                        onlyLiked,
                        preferredSize);
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
            @RequestParam(required = false) String cafeBrand, @CurrentUser String providerId) {
        Optional<CafeBrand> brand =
                Optional.ofNullable(cafeBrand).flatMap(CafeBrand::findByDisplayName);
        BeverageCountDto countDto =
                cafeBeverageQueryService.getBeverageCountByBrandAndSugarLevel(
                        brand, memberQueryService.getMemberIdByProviderId(providerId));
        return ApiResponse.success(countDto);
    }

    @PostMapping("{productId}/like")
    public ApiResponse<BeverageLikeDto> likeBeverage(
            @PathVariable UUID productId, @CurrentUser String providerId) {
        BeverageLikeDto likeDto =
                beverageLikeService.likeBeverage(
                        memberQueryService.getMemberIdByProviderId(providerId), productId);
        return ApiResponse.success(likeDto);
    }

    @DeleteMapping("{productId}/unlike")
    public ApiResponse<BeverageLikeDto> unlikeBeverage(
            @PathVariable UUID productId, @CurrentUser String providerId) {
        BeverageLikeDto unlikeDto =
                beverageLikeService.unlikeBeverage(
                        memberQueryService.getMemberIdByProviderId(providerId), productId);
        return ApiResponse.success(unlikeDto);
    }

    @GetMapping("/search")
    public ApiResponse<BeverageSearchResultDto> searchBeverages(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sugarLevel,
            @RequestParam(required = false) Boolean onlyLiked,
            @CurrentUser String providerId) {

        String targetKeyword = (keyword == null ? "" : keyword).trim();

        if (!StringUtils.hasText(targetKeyword)) {
            log.info("Empty keyword");
            return ApiResponse.success(new BeverageSearchResultDto(List.of(), 0, 0, 0, 0));
        }

        Optional<SugarLevel> sugar = SugarLevel.fromParam(sugarLevel);

        BeverageSearchResultDto beverageSearchResultDto =
                cafeBeverageQueryService.searchBeverages(
                        targetKeyword,
                        memberQueryService.getMemberIdByProviderId(providerId),
                        sugar,
                        onlyLiked);
        return ApiResponse.success(beverageSearchResultDto);
    }
}
