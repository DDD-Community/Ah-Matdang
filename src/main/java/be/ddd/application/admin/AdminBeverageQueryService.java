package be.ddd.application.admin;

import be.ddd.api.admin.dto.res.BeverageDetailResponseDto;
import be.ddd.domain.entity.crawling.CafeBrand;
import be.ddd.domain.repo.CafeBeverageRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminBeverageQueryService {

    private final CafeBeverageRepository cafeBeverageRepository;

    public List<BeverageDetailResponseDto> getAllBeverages(CafeBrand brand, String keyword) {
        List<be.ddd.domain.entity.crawling.CafeBeverage> beverages =
                cafeBeverageRepository.findBeverages(brand, keyword);
        log.info(
                "AdminBeverageQueryService fetched {} beverages from repository.",
                beverages.size());
        return beverages.stream().map(BeverageDetailResponseDto::new).collect(Collectors.toList());
    }
}
