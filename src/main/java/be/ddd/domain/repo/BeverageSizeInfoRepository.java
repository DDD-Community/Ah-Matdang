package be.ddd.domain.repo;

import be.ddd.domain.entity.crawling.BeverageSize;
import be.ddd.domain.entity.crawling.BeverageSizeInfo;
import be.ddd.domain.entity.crawling.CafeBeverage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeverageSizeInfoRepository
        extends JpaRepository<BeverageSizeInfo, Long>, BeverageSizeInfoRepositoryCustom {
    Optional<BeverageSizeInfo> findByCafeBeverageAndSizeType(
            CafeBeverage cafeBeverage, BeverageSize sizeType);
}
