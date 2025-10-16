package be.ddd.domain.entity.crawling;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum CafeBrand {
    STARBUCKS("starbucks"),
    MEGA_COFFEE("megacoffee"),
    TEST("test");

    private static final Map<String, CafeBrand> BY_DISPLAY_NAME =
            Arrays.stream(values())
                    .collect(
                            Collectors.toMap(
                                    brand -> brand.displayName.toLowerCase(), brand -> brand));

    private final String displayName;

    CafeBrand(String displayName) {
        this.displayName = displayName;
    }

    @JsonCreator
    public static CafeBrand fromDisplayName(String name) {
        if (name == null) {
            return null;
        }
        String lowerCaseName = name.toLowerCase().replace("_", "");
        // Handle Korean name for Mega Coffee
        if ("메가커피".equals(lowerCaseName)) {
            return MEGA_COFFEE;
        }
        return BY_DISPLAY_NAME.get(lowerCaseName);
    }

    public static Optional<CafeBrand> findByDisplayName(String name) {
        return Optional.ofNullable(fromDisplayName(name));
    }
}
