package be.ddd.application.admin;

import be.ddd.api.admin.dto.AdminBeverageCreateRequest;
import be.ddd.api.admin.dto.SizeNutritionDto;
import be.ddd.domain.entity.crawling.BeverageNutrition;
import be.ddd.domain.entity.crawling.BeverageSizeInfo;
import be.ddd.domain.entity.crawling.CafeBeverage;
import be.ddd.domain.entity.crawling.CafeStore;
import be.ddd.domain.repo.CafeBeverageRepository;
import be.ddd.domain.repo.CafeStoreRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminBeverageService {

    private final CafeBeverageRepository cafeBeverageRepository;
    private final CafeStoreRepository cafeStoreRepository;

    @Transactional
    public void createBeverage(AdminBeverageCreateRequest request) {
        CafeStore cafeStore =
                cafeStoreRepository
                        .findByCafeBrand(request.cafeBrand())
                        .orElseGet(
                                () -> cafeStoreRepository.save(CafeStore.of(request.cafeBrand())));

        CafeBeverage newBeverage =
                new CafeBeverage(request.beverageName(), request.imageUrl(), cafeStore);
        newBeverage.setProductId(UUID.randomUUID());

        if (request.sizes() != null) {
            for (SizeNutritionDto sizeDto : request.sizes()) {
                if (sizeDto.servingKcal() != null) { // 칼로리 값이 있는 경우만 사이즈 정보로 추가
                    BeverageNutrition nutrition =
                            new BeverageNutrition(
                                    sizeDto.servingKcal(),
                                    sizeDto.saturatedFatG(),
                                    sizeDto.proteinG(),
                                    sizeDto.sodiumMg(),
                                    sizeDto.sugarG(),
                                    sizeDto.caffeineMg());
                    BeverageSizeInfo sizeInfo =
                            new BeverageSizeInfo(newBeverage, sizeDto.size(), nutrition);
                    newBeverage.addSizeInfo(sizeInfo);
                }
            }
        }

        cafeBeverageRepository.save(newBeverage);
    }
}
