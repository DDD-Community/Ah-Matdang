package be.ddd.api.dto.res.auth;

import java.util.UUID;

public record MeResponseDto(UUID fakeId, boolean firstLogin) {}
