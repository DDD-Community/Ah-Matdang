package be.ddd.domain.repo;

import be.ddd.domain.entity.member.Member;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.Repository;

public interface MemberRepository extends Repository<Member, Long> {
    Optional<Member> findById(Long id);

    Optional<Member> findByFakeId(UUID fakeId);
}
