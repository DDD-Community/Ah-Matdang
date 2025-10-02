package be.ddd.application.member;

import be.ddd.api.dto.res.MemberDetailsDto;
import be.ddd.domain.entity.member.Member;
import be.ddd.domain.exception.MemberNotFoundException;
import be.ddd.domain.repo.MemberRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;

    @Override
    public MemberDetailsDto checkMemberProfile(UUID fakeId) {
        Member member =
                memberRepository
                        .findByFakeIdAndDeletedAtIsNull(fakeId)
                        .orElseThrow(MemberNotFoundException::new);
        return MemberDetailsDto.from(member);
    }

    @Override
    public Long getMemberIdByProviderId(String providerId) {
        Member member =
                memberRepository
                        .findByProviderId(providerId)
                        .orElseThrow(MemberNotFoundException::new);
        return member.getId();
    }

    @Override
    public Member getMemberByFakeId(UUID fakeId) {
        return memberRepository
                .findByFakeIdAndDeletedAtIsNull(fakeId)
                .orElseThrow(MemberNotFoundException::new);
    }
}
