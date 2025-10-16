package be.ddd.application.member.dto.res;

import lombok.Builder;

@Builder
public record AllDifficultyRecommendedSugars(
        RecommendedSugar easy, RecommendedSugar normal, RecommendedSugar hard) {}
