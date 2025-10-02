package be.ddd.domain.entity.crawling;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BeverageSize {
    TALL("Tall", 355),
    GRANDE("Grande", 473),
    VENTI("Venti", 591),
    SHORT("Short", 237),
    OTHER("Other", 0);

    private final String displayName;
    private final int volume;

    public static BeverageSize fromString(String text) {
        return Arrays.stream(values())
                .filter(size -> size.name().equalsIgnoreCase(text))
                .findFirst()
                .orElse(OTHER);
    }
}
