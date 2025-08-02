package be.ddd.application.member;

import be.ddd.api.dto.req.MemberProfileModifyDto;
import be.ddd.api.dto.req.MemberProfileRegistrationDto;
import be.ddd.api.dto.res.MemberModifyDetailsDto;
import java.util.UUID;

public interface MemberCommandService {
    UUID registerMemberProfile(MemberProfileRegistrationDto memberProfileRegistrationDto);

    MemberModifyDetailsDto modifyMemberProfile(MemberProfileModifyDto memberProfileModifyDto);
}
