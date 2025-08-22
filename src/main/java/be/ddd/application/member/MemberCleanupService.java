package be.ddd.application.member;

import be.ddd.domain.entity.member.Member;
import be.ddd.domain.repo.MemberRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class MemberCleanupService {

    private final MemberRepository memberRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupExpiredMembers() {
        LocalDate deleteThreshold = LocalDate.now().minusDays(90);
        List<Member> membersToDelete =
                memberRepository.findByDeletedAtIsNotNullAndDeletedAtLessThanEqual(deleteThreshold);

        if (!membersToDelete.isEmpty()) {
            log.info("Deleting {} expired members", membersToDelete.size());
            memberRepository.deleteAll(membersToDelete);
            log.info("Successfully deleted {} expired members", membersToDelete.size());
        }
    }
}
