package be.ddd.domain.entity.crawling;

import be.ddd.application.batch.dto.BeverageNutritionDto;
import be.ddd.application.batch.dto.LambdaBeverageDto;
import be.ddd.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
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
        /* log.info(
                        "[DEBUG] Updating beverage: '{}'. DTO contains sizes: {}. DB entity has sizes: {}",
                        this.name,
                        dto.beverageNutritions().keySet(),
                        this.sizes.stream().map(BeverageSizeInfo::getSizeType).collect(Collectors.toList()));
        */
        if (dto.image() != null) {
            this.imgUrl = dto.image();
        }

        if (dto.beverageType() != null) {
            this.beverageType =
                    BeverageType.valueOf(dto.beverageType().toUpperCase().replace(" ", "_"));
        }

        if (dto.beverageNutritions() != null && !dto.beverageNutritions().isEmpty()) {
            Map<BeverageSize, BeverageSizeInfo> existingSizes =
                    this.sizes.stream()
                            .collect(
                                    Collectors.toMap(
                                            BeverageSizeInfo::getSizeType, Function.identity()));

            dto.beverageNutritions()
                    .forEach(
                            (sizeStr, nutritionDto) -> {
                                BeverageSize size = BeverageSize.fromString(sizeStr);
                                BeverageNutrition nutrition = BeverageNutrition.from(nutritionDto);
                                if (existingSizes.containsKey(size)) {
                                    existingSizes.get(size).updateBeverageNutrition(nutrition);
                                } else {
                                    addSizeInfo(new BeverageSizeInfo(this, size, nutrition));
                                }
                            });

            // 대표 당 레벨 업데이트 (예: TALL 사이즈 기준)
            this.sizes.stream()
                    .filter(s -> s.getSizeType() == BeverageSize.TALL)
                    .findFirst()
                    .ifPresent(
                            sizeInfo -> {
                                BeverageNutrition nutrition = sizeInfo.getBeverageNutrition();
                                if (nutrition != null) {
                                    this.sugarLevel =
                                            SugarLevel.valueOfSugar(
                                                    nutrition.getSugarG(),
                                                    sizeInfo.getSizeType().getVolume());
                                }
                            });
        }
    }

    // == 연관관계 편의 메소드 == //
    public void addSizeInfo(BeverageSizeInfo sizeInfo) {
        this.sizes.add(sizeInfo);
        sizeInfo.setCafeBeverage(this);
    }

    public static CafeBeverage of(
            String name,
            UUID productId,
            CafeStore cafeStore,
            String imgUrl,
            BeverageType beverageType,
            String beverageTemperature,
            Map<String, BeverageNutritionDto> beverageNutritions) {
        CafeBeverage beverage = new CafeBeverage();
        beverage.name = name;
        beverage.productId = productId;
        beverage.cafeStore = cafeStore;
        beverage.imgUrl = imgUrl;
        beverage.beverageType = beverageType;

        beverageNutritions.forEach(
                (sizeStr, nutritionDto) -> {
                    BeverageSize size = BeverageSize.fromString(sizeStr);
                    BeverageNutrition nutrition = BeverageNutrition.from(nutritionDto);
                    BeverageSizeInfo sizeInfo = new BeverageSizeInfo(beverage, size, nutrition);
                    beverage.sizes.add(sizeInfo);
                });

        beverage.sizes.stream()
                .filter(s -> s.getSizeType() == BeverageSize.TALL)
                .findFirst()
                .or(() -> beverage.sizes.stream().findFirst())
                .ifPresent(
                        sizeInfo -> {
                            BeverageNutrition nutrition = sizeInfo.getBeverageNutrition();
                            if (nutrition != null) {
                                beverage.sugarLevel =
                                        SugarLevel.valueOfSugar(
                                                nutrition.getSugarG(),
                                                sizeInfo.getSizeType().getVolume());
                            }
                        });

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
