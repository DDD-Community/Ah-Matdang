package be.ddd.application.member;

import be.ddd.api.dto.req.MemberProfileModifyDto;
import be.ddd.api.dto.req.MemberProfileRegistrationDto;
import be.ddd.api.dto.res.MemberModifyDetailsDto;
import java.util.UUID;

public interface MemberCommandService {
    UUID registerMemberProfile(
            UUID fakeId, MemberProfileRegistrationDto memberProfileRegistrationDto);

    MemberModifyDetailsDto modifyMemberProfile(
            UUID fakeId, MemberProfileModifyDto memberProfileModifyDto);
}
