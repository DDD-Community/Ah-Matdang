package be.ddd.domain.entity.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SugarRecommendationDifficulty {
    EASY(1.0),
    NORMAL(0.5),
    HARD(0.2);

    private final double multiplier;
}
