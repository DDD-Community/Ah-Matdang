package be.ddd.application.member;

import be.ddd.api.dto.res.MemberDetailsDto;
import java.util.UUID;

public interface MemberQueryService {
    MemberDetailsDto checkMemberProfile(UUID fakeId);
}
