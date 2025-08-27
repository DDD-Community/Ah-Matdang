package be.ddd.application.member;

import be.ddd.api.dto.req.MemberProfileModifyDto;
import be.ddd.api.dto.req.MemberProfileRegistrationDto;
import be.ddd.api.dto.res.MemberModifyDetailsDto;
import be.ddd.api.dto.res.MemberRegistrationDetailsDto;
import be.ddd.domain.entity.member.Member;
import java.util.UUID;

public interface MemberCommandService {
    MemberRegistrationDetailsDto registerMemberProfile(
            UUID fakeId, MemberProfileRegistrationDto memberProfileRegistrationDto);

    MemberModifyDetailsDto modifyMemberProfile(
            UUID fakeId, MemberProfileModifyDto memberProfileModifyDto);

    void withdrawMember(UUID fakeId);

    Member findOrCreateMemberByProviderId(String providerId);
}
