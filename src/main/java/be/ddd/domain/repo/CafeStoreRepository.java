package be.ddd.domain.repo;

import be.ddd.domain.entity.crawling.CafeBrand;
import be.ddd.domain.entity.crawling.CafeStore;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface CafeStoreRepository extends Repository<CafeStore, Long> {
    Optional<CafeStore> findById(Long id);

    Optional<CafeStore> findByCafeBrand(CafeBrand cafeBrand);

    List<CafeStore> findAllByCafeBrandIn(Collection<CafeBrand> cafeBrands);

    <S extends CafeStore> List<S> saveAll(Iterable<S> entities);

    CafeStore save(CafeStore cafeStore);
}
