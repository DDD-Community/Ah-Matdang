package be.ddd.api.dto.res;

import be.ddd.application.member.dto.res.RecommendedSugar;
import java.util.UUID;

public record MemberRegistrationDetailsDto(UUID fakeId, RecommendedSugar recommendedSugar) {}
