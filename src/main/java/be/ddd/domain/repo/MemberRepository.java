package be.ddd.domain.repo;

import be.ddd.domain.entity.member.Member;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Optional<Member> findById(Long id);

    Optional<Member> findByIdAndDeletedAtIsNull(Long id);

    Optional<Member> findByFakeIdAndDeletedAtIsNull(UUID fakeId);

    Optional<Member> findByProviderIdAndDeletedAtIsNull(String providerId);

    Optional<Member> findByProviderId(String providerId);

    List<Member> findByDeletedAtIsNotNullAndDeletedAtLessThanEqual(LocalDate deleteDate);
}
