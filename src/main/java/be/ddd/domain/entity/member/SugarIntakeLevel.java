package be.ddd.domain.entity.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SugarIntakeLevel {
    EASY("쉬움"),
    NORMAL("보통"),
    HARD("어려움");

    private final String displayName;
}
