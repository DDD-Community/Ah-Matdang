package be.ddd.application.member;

import be.ddd.api.dto.res.MemberDetailsDto;
import be.ddd.domain.entity.member.Member;
import java.util.UUID;

public interface MemberQueryService {
    MemberDetailsDto checkMemberProfile(UUID fakeId);

    Long getMemberIdByProviderId(String providerId);

    Member getMemberByFakeId(UUID fakeId);
}
