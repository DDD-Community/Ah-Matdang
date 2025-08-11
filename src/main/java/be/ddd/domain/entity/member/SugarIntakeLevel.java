package be.ddd.domain.entity.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SugarIntakeLevel {
    EASY("쉬움", 1.0),
    NORMAL("보통", 0.5),
    HARD("어려움", 0.2);

    private final String displayName;
    private final double multiplier;
}
