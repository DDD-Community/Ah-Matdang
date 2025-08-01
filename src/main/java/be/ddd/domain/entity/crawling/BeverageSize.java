package be.ddd.domain.entity.crawling;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BeverageSize {
    TALL("Tall"),
    GRANDE("Grande"),
    VENTI("Venti"),
    SHORT("Short"),
    OTHER("Other");

    private final String displayName;

    public static BeverageSize fromDisplayName(String displayName) {
        return Arrays.stream(BeverageSize.values())
                .filter(size -> size.displayName.equalsIgnoreCase(displayName))
                .findFirst()
                .orElse(OTHER);
    }
}
