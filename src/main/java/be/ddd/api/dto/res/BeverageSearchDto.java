package be.ddd.api.dto.res;

import be.ddd.domain.entity.crawling.BeverageNutrition;
import be.ddd.domain.entity.crawling.BeverageType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BeverageSearchDto {

    private Long beverageId;
    private String beverageName;
    private String brand;
    private String imageUrl;
    private BeverageType beverageType;
    private BeverageNutrition beverageNutrition;
    private boolean isLiked;

    @QueryProjection
    public BeverageSearchDto(
            Long beverageId,
            String beverageName,
            String brand,
            String imageUrl,
            BeverageType beverageType,
            BeverageNutrition beverageNutrition,
            boolean isLiked) {
        this.beverageId = beverageId;
        this.beverageName = beverageName;
        this.brand = brand;
        this.imageUrl = imageUrl;
        this.beverageType = beverageType;
        this.beverageNutrition = beverageNutrition;
        this.isLiked = isLiked;
    }
}
