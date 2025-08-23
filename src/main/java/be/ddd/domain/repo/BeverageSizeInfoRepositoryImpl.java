package be.ddd.domain.repo;

import be.ddd.domain.entity.crawling.BeverageSize;
import be.ddd.domain.entity.crawling.BeverageSizeInfo;
import be.ddd.domain.entity.crawling.QBeverageSizeInfo;
import be.ddd.domain.entity.crawling.QCafeBeverage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BeverageSizeInfoRepositoryImpl implements BeverageSizeInfoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<BeverageSizeInfo> findWithBeverageByProductIdAndSizeType(
            UUID productId, BeverageSize sizeType) {
        QBeverageSizeInfo beverageSizeInfo = QBeverageSizeInfo.beverageSizeInfo;
        QCafeBeverage cafeBeverage = QCafeBeverage.cafeBeverage;

        BeverageSizeInfo result =
                queryFactory
                        .selectFrom(beverageSizeInfo)
                        .join(beverageSizeInfo.cafeBeverage, cafeBeverage)
                        .fetchJoin()
                        .where(
                                cafeBeverage.productId.eq(productId),
                                beverageSizeInfo.sizeType.eq(sizeType))
                        .fetchOne();

        return Optional.ofNullable(result);
    }
}
