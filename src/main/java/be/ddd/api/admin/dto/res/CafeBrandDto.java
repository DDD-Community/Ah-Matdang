package be.ddd.api.admin.dto.res;

import be.ddd.domain.entity.crawling.CafeBrand;
import lombok.Getter;

@Getter
public class CafeBrandDto {
    private final String name;
    private final String displayName;

    public CafeBrandDto(CafeBrand cafeBrand) {
        this.name = cafeBrand.name();
        this.displayName = cafeBrand.getDisplayName();
    }
}
