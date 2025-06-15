package be.ddd.domain.entity.crawling;

import be.ddd.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cafe_beverages")
public class CafeBeverage extends BaseTimeEntity {

    @Id
    @Column(name = "cafe_beverage_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cafe_store_id", nullable = false)
    private CafeStore cafeStore;

    private BeverageNutrition beverageNutrition;

    @Enumerated(EnumType.STRING)
    private BeverageType beverageType;
}
