package be.ddd.domain.entity.crawling;

import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SugarLevel {
    ZERO("무당"),
    LOW("저당"),
    HIGH("고당");

    private final String description;

    public static SugarLevel valueOfSugar(Integer sugarG, int volume) {
        if (sugarG == null) {
            return HIGH;
        }
        if (sugarG == 0) {
            return ZERO;
        }
        if (volume > 0) {
            double sugarPer100ml = (double) sugarG / volume * 100;
            if (sugarPer100ml <= 2.5) {
                return LOW;
            }
        }
        return HIGH;
    }

    public static Optional<SugarLevel> fromParam(String param) {
        if (param == null || param.isBlank()) {
            return Optional.empty();
        }
        switch (param.trim().toUpperCase()) {
            case "ZERO":
                return Optional.of(ZERO);

            case "LOW":
                return Optional.of(LOW);

            case "HIGH":
                return Optional.of(HIGH);

            default:
                return Optional.empty();
        }
    }
}
