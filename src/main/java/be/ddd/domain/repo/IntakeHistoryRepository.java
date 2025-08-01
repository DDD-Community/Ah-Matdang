package be.ddd.domain.repo;

import be.ddd.domain.entity.crawling.CafeBeverage;
import be.ddd.domain.entity.member.Member;
import be.ddd.domain.entity.member.intake.IntakeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntakeHistoryRepository
        extends JpaRepository<IntakeHistory, Long>, IntakeHistoryRepositoryCustom {
    void deleteByMemberAndCafeBeverage(Member member, CafeBeverage cafeBeverage);
}
