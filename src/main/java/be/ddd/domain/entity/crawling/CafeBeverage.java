package be.ddd.domain.entity.crawling;

import be.ddd.application.batch.dto.LambdaBeverageDto;
import be.ddd.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cafe_beverages")
public class CafeBeverage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cafe_beverage_id")
    private Long id;

    @Column(name = "product_id", nullable = false, unique = true)
    private UUID productId;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cafe_store_id", nullable = false)
    private CafeStore cafeStore;

    private String imgUrl;

    @Enumerated(EnumType.STRING)
    private BeverageType beverageType;

    @Enumerated(EnumType.STRING)
    private SugarLevel sugarLevel;

    @OneToMany(mappedBy = "cafeBeverage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BeverageSizeInfo> sizes = new ArrayList<>();

    public void updateFromDto(LambdaBeverageDto dto) {
        if (dto.image() != null) {
            this.imgUrl = dto.image();
        }

        if (dto.beverageType() != null) {
            this.beverageType =
                    BeverageType.valueOf(dto.beverageType().toUpperCase().replace(" ", "_"));
        }

        // 기존 beverageNutrition 로직은 BeverageSizeInfo로 이동
        // 여기서는 기본 사이즈의 영양 정보를 업데이트한다고 가정
        if (dto.beverageNutrition() != null) {
            // 기본 사이즈 (예: TALL)의 영양 정보를 업데이트하거나 추가
            BeverageSizeInfo defaultSizeInfo =
                    this.sizes.stream()
                            .filter(
                                    sizeInfo ->
                                            sizeInfo.getSizeType()
                                                    == BeverageSize.TALL) // 기본 사이즈를 TALL로 가정
                            .findFirst()
                            .orElseGet(
                                    () -> {
                                        BeverageSizeInfo newSizeInfo =
                                                new BeverageSizeInfo(this, BeverageSize.TALL, null);
                                        this.sizes.add(newSizeInfo);
                                        return newSizeInfo;
                                    });
            defaultSizeInfo.updateBeverageNutrition(
                    BeverageNutrition.from(dto.beverageNutrition()));
            this.sugarLevel =
                    SugarLevel.valueOfSugar(defaultSizeInfo.getBeverageNutrition().getSugarG());
        }
    }

    public static CafeBeverage of(
            String name,
            UUID productId,
            CafeStore cafeStore,
            String imgUrl,
            BeverageType beverageType,
            BeverageNutrition defaultNutrition) {
        CafeBeverage beverage = new CafeBeverage();
        beverage.name = name;
        beverage.productId = productId;
        beverage.cafeStore = cafeStore;
        beverage.imgUrl = imgUrl;
        beverage.beverageType = beverageType;
        beverage.sugarLevel = SugarLevel.valueOfSugar(defaultNutrition.getSugarG());
        beverage.sizes.add(
                new BeverageSizeInfo(beverage, BeverageSize.TALL, defaultNutrition)); // 기본 사이즈 추가
        return beverage;
    }

    public CafeBeverage(
            Long id,
            UUID productId,
            String name,
            CafeStore cafeStore,
            String imgUrl,
            BeverageType beverageType,
            SugarLevel sugarLevel) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.cafeStore = cafeStore;
        this.imgUrl = imgUrl;
        this.beverageType = beverageType;
        this.sugarLevel = sugarLevel;
    }
}
