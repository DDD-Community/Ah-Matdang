package be.ddd.domain.repo;

import be.ddd.domain.entity.crawling.CafeBeverage;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CafeBeverageRepository extends JpaRepository<CafeBeverage, Long> {
    @Query("SELECT cb FROM CafeBeverage cb JOIN FETCH cb.cafeStore cs JOIN FETCH cb.sizes s")
    List<CafeBeverage> findAllWithDetails();

    List<CafeBeverage> findAllByName(String name);

    //    <S extends CafeBeverage> void saveAll(Iterable<S> entities);

    List<CafeBeverage> findByIdGreaterThanOrderByIdAsc(Long cursor, Pageable pageable);

    Optional<CafeBeverage> findByProductId(UUID productId);
}
