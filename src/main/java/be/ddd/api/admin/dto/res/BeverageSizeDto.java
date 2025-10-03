package be.ddd.api.admin.dto.res;

import be.ddd.domain.entity.crawling.BeverageSize;
import lombok.Getter;

@Getter
public class BeverageSizeDto {
    private final String name;
    private final String displayName;

    public BeverageSizeDto(BeverageSize beverageSize) {
        this.name = beverageSize.name();
        this.displayName = beverageSize.getDisplayName();
    }
}
