package be.ddd.domain.repo;

import be.ddd.domain.entity.member.Member;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Optional<Member> findById(Long id);

    Optional<Member> findByFakeId(UUID fakeId);

    Optional<Member> findByProviderId(String providerId);
}
