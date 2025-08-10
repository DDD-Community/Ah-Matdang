package be.ddd.domain.entity.member;

import lombok.Getter;

@Getter
public enum ActivityRange {
    LOOSE(1.45), // 1.4–1.5 중간값
    NORMAL(1.65), // 1.6–1.7 중간값
    TIGHT(1.85); // 1.8–1.9 중간값

    private final double pal;

    ActivityRange(double pal) {
        this.pal = pal;
    }
}
