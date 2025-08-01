package be.ddd.application.member;

import be.ddd.api.dto.req.MemberProfileRegistrationDto;
import java.util.UUID;

public interface MemberCommandService {
    UUID registerMemberProfile(MemberProfileRegistrationDto memberProfileRegistrationDto);
}
