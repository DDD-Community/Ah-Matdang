package be.ddd.domain.entity.crawling;

import be.ddd.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cafe_stores")
public class CafeStore extends BaseTimeEntity {

    @Id
    @Column(name = "cafe_store_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cafe_brand")
    @Enumerated(EnumType.STRING)
    private CafeBrand cafeBrand;

    @OneToMany(mappedBy = "cafeStore", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CafeBeverage> beverages = new ArrayList<>();

    // TODO : 카페 위치는 도입 논의

    /*만약 카페 위치를 도입하게 된다면, 별도의 테이블로 CafeStore와 매핑하거나
    CafeBrand 클래스를가지고 있는 클래스로 분리*/

}
