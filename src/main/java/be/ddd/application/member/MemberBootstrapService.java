package be.ddd.application.member;

import be.ddd.domain.entity.member.AuthProvider;
import be.ddd.domain.entity.member.Member;
import be.ddd.domain.repo.MemberRepository;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberBootstrapService {

    private final MemberRepository memberRepository;

    public UUID ensureAndGetFakeId(DecodedJWT jwt) {
        String sub = jwt.getSubject();

        int bar = sub.indexOf('|');
        String providerPrefix = (bar > 0) ? sub.substring(0, bar) : "auth0";
        String providerId = (bar > 0) ? sub.substring(bar + 1) : sub;

        AuthProvider authProvider = mapToAuthProvider(providerPrefix);

        return memberRepository.findByProviderId(providerId)
            .map(Member::getFakeId)
            .orElseGet(() -> {
                UUID fakeId = UUID.randomUUID();
                Member member = new Member(fakeId, authProvider, providerId);
                memberRepository.save(member);
                return fakeId;
            });
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
