package be.ddd.domain.entity.crawling;

import be.ddd.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "beverage_size_info",
        uniqueConstraints = @UniqueConstraint(columnNames = {"cafe_beverage_id", "size_type"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BeverageSizeInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "beverage_size_info_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_beverage_id", nullable = false)
    private CafeBeverage cafeBeverage;

    @Enumerated(EnumType.STRING)
    @Column(name = "size_type", nullable = false)
    private BeverageSize sizeType;

    private BeverageNutrition beverageNutrition;

    public BeverageSizeInfo(
            CafeBeverage cafeBeverage, BeverageSize sizeType, BeverageNutrition beverageNutrition) {
        this.cafeBeverage = cafeBeverage;
        this.sizeType = sizeType;
        this.beverageNutrition = beverageNutrition;
    }

    public void updateBeverageNutrition(BeverageNutrition beverageNutrition) {
        this.beverageNutrition = beverageNutrition;
    }
}
