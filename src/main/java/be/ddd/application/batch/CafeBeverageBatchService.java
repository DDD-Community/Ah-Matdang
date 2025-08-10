package be.ddd.application.batch;

import be.ddd.application.batch.dto.LambdaBeverageDto;
import be.ddd.domain.entity.crawling.CafeBeverage;
import be.ddd.domain.entity.crawling.CafeBrand;
import be.ddd.domain.entity.crawling.CafeStore;
import be.ddd.domain.repo.CafeBeverageRepository;
import be.ddd.domain.repo.CafeStoreRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class CafeBeverageBatchService {

    private final CafeBeverageRepository repository;
    private final WebClient.Builder webClientBuilder;

    private final String lambdaUrl =
            "https://u6wvrcscqwe7rdbblr3xebajf40avfxz.lambda-url.ap-northeast-2.on.aws/";
    private final CafeStoreRepository cafeStoreRepository;

    public List<LambdaBeverageDto> fetchAll() {
        return webClientBuilder
                .baseUrl(lambdaUrl)
                .build()
                .get()
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<LambdaBeverageDto>>() {})
                .block();
    }

    public CafeBeverage toEntity(LambdaBeverageDto dto) {
        Objects.requireNonNull(dto.name(), "Beverage name required");
        Optional<CafeBeverage> opt = repository.findByName(dto.name());
        if (opt.isPresent()) {
            CafeBeverage existing = opt.get();
            existing.updateFromDto(dto);
            return existing;
        } else {
            // TODO 카페 스토어 Batch 처리
            CafeStore cafeStore =
                    cafeStoreRepository
                            .findByCafeBrand(CafeBrand.STARBUCKS)
                            .orElseThrow(IllegalStateException::new);
            return dto.toEntity(cafeStore);
        }
    }

    /*    public List<CafeBeverage> saveAll(List<? extends CafeBeverage> cafeBeverages) {
        return repository.saveAll(cafeBeverages);
    }*/
}
