package be.ddd.api.admin;

import be.ddd.api.admin.dto.AdminBeverageCreateRequest;
import be.ddd.api.admin.dto.res.BeverageDetailResponseDto;
import be.ddd.application.admin.AdminBeverageQueryService;
import be.ddd.application.admin.AdminBeverageService;
import be.ddd.common.dto.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
        return ApiResponse.success(null, "Beverage created successfully.");
    }

    @GetMapping
    public ApiResponse<List<BeverageDetailResponseDto>> getAllBeverages() {
        return ApiResponse.success(adminBeverageQueryService.getAllBeverages());
    }
}
