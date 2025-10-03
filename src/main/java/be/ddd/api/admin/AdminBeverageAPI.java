package be.ddd.api.admin;

import be.ddd.api.admin.dto.AdminBeverageCreateRequest;
import be.ddd.api.admin.dto.res.BeverageDetailResponseDto;
import be.ddd.application.admin.AdminBeverageQueryService;
import be.ddd.application.admin.AdminBeverageService;
import be.ddd.common.dto.ApiResponse;
import be.ddd.domain.entity.crawling.CafeBrand;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/beverages")
@RequiredArgsConstructor
public class AdminBeverageAPI {

    private final AdminBeverageService adminBeverageService;
    private final AdminBeverageQueryService adminBeverageQueryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<?> createBeverage(@RequestBody AdminBeverageCreateRequest request) {
        adminBeverageService.createBeverage(request);
        return ApiResponse.success("Beverage created successfully.");
    }

    @GetMapping
    public ApiResponse<List<BeverageDetailResponseDto>> getAllBeverages(
            @RequestParam(required = false) CafeBrand brand,
            @RequestParam(required = false) String keyword) {
        return ApiResponse.success(adminBeverageQueryService.getAllBeverages(brand, keyword));
    }
}
