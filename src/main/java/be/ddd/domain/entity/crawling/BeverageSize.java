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

    public static BeverageSize fromString(String text) {
        return Arrays.stream(values())
                .filter(size -> size.name().equalsIgnoreCase(text))
                .findFirst()
                .orElse(OTHER);
    }
}
