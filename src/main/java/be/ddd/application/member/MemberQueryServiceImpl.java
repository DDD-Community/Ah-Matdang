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
                memberRepository.findByFakeId(fakeId).orElseThrow(MemberNotFoundException::new);
        return MemberDetailsDto.from(member);
    }
}
