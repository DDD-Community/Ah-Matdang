package be.ddd.application.member;

import be.ddd.domain.entity.member.AuthProvider;
import be.ddd.domain.entity.member.Member;
import be.ddd.domain.repo.MemberRepository;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberBootstrapService {

    private final MemberRepository memberRepository;

    @Transactional
    public UUID ensureAndGetFakeId(DecodedJWT jwt) {
        String sub = jwt.getSubject();

        int bar = sub.indexOf('|');
        String providerPrefix = (bar > 0) ? sub.substring(0, bar) : "auth0";
        String providerId = (bar > 0) ? sub.substring(bar + 1) : sub;

        AuthProvider authProvider = mapToAuthProvider(providerPrefix);

        // 1. 활성 회원 조회
        Optional<Member> activeMember =
                memberRepository.findByProviderIdAndDeletedAtIsNull(providerId);
        if (activeMember.isPresent()) {
            return activeMember.get().getFakeId();
        }

        // 2. 탈퇴한 회원이 있는지 조회 (provider_id만으로)
        Optional<Member> deletedMember = memberRepository.findByProviderId(providerId);
        if (deletedMember.isPresent()) {
            // 탈퇴한 회원이 있으면 즉시 삭제하고 새 계정 생성
            memberRepository.delete(deletedMember.get());
            memberRepository.flush(); // DB에 즉시 반영
        }

        // 3. 새 계정 생성
        UUID fakeId = UUID.randomUUID();
        Member member = new Member(fakeId, authProvider, providerId);
        memberRepository.save(member);
        return fakeId;
    }

    private AuthProvider mapToAuthProvider(String p) {
        if (p == null) return AuthProvider.UNKNOWN;
        return switch (p) {
            case "apple" -> AuthProvider.APPLE;
            case "google" -> AuthProvider.GOOGLE;
            default -> AuthProvider.UNKNOWN;
        };
    }
}
