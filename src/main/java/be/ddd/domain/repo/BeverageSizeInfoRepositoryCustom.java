package be.ddd.domain.repo;

import be.ddd.domain.entity.crawling.BeverageSize;
import be.ddd.domain.entity.crawling.BeverageSizeInfo;
import java.util.Optional;
import java.util.UUID;

public interface BeverageSizeInfoRepositoryCustom {
    Optional<BeverageSizeInfo> findWithBeverageByProductIdAndSizeType(
            UUID productId, BeverageSize sizeType);
}
