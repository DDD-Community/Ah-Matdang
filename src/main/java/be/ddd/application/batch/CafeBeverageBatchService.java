package be.ddd.application.batch;

import be.ddd.application.batch.dto.LambdaBeverageApiResponse;
import be.ddd.application.batch.dto.LambdaBeverageDto;
import be.ddd.domain.entity.crawling.CafeBeverage;
import be.ddd.domain.entity.crawling.CafeBrand;
import be.ddd.domain.entity.crawling.CafeStore;
import be.ddd.domain.repo.CafeBeverageRepository;
import be.ddd.domain.repo.CafeStoreRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CafeBeverageBatchService {

    private final CafeBeverageRepository repository;
    private final WebClient.Builder webClientBuilder;

    private final String lambdaUrl = "http://localhost:8000/api/v1/beverages";
    private final CafeStoreRepository cafeStoreRepository;

    public List<LambdaBeverageDto> fetchAll() {
        LambdaBeverageApiResponse apiResponse =
                webClientBuilder
                        .baseUrl(lambdaUrl)
                        .build()
                        .get()
                        .retrieve()
                        .bodyToMono(LambdaBeverageApiResponse.class)
                        .block();
        List<LambdaBeverageDto> beverages = apiResponse != null ? apiResponse.getData() : List.of();
        log.info("[DEBUG] Fetched {} beverages from Lambda.", beverages.size());
        return beverages;
    }

    public CafeBeverage toEntity(LambdaBeverageDto dto) {
        log.info(
                "[DEBUG] Processing DTO for beverage: '{}'. Sizes received: {}",
                dto.name(),
                dto.beverageNutritions().stream().map(n -> n.size()).collect(Collectors.toList()));
        Objects.requireNonNull(dto.name(), "Beverage name required");
        List<CafeBeverage> existingBeverages = repository.findAllByName(dto.name());
        if (!existingBeverages.isEmpty()) {
            // 중복된 음료가 있다면, 첫 번째 음료를 기준으로 업데이트
            CafeBeverage existing = existingBeverages.get(0);
            existing.updateFromDto(dto);
            return existing;
        } else {
            final CafeBrand brand = CafeBrand.fromDisplayName(dto.brand());
            if (brand == null) {
                log.warn(
                        "Unknown brand '{}' for new beverage '{}'. Skipping.",
                        dto.brand(),
                        dto.name());
                return null;
            }
            CafeStore cafeStore =
                    cafeStoreRepository
                            .findByCafeBrand(brand)
                            .orElseGet(() -> cafeStoreRepository.save(CafeStore.of(brand)));
            return dto.toEntity(cafeStore);
        }
    }

    /*    public List<CafeBeverage> saveAll(List<? extends CafeBeverage> cafeBeverages) {
        return repository.saveAll(cafeBeverages);
    }*/
}
