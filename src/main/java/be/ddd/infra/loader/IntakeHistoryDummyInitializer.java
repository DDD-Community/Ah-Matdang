package be.ddd.infra.loader;

import be.ddd.domain.entity.crawling.CafeBeverage;
import be.ddd.domain.entity.member.Member;
import be.ddd.domain.entity.member.intake.IntakeHistory;
import be.ddd.domain.exception.CafeBeverageNotFoundException;
import be.ddd.domain.exception.MemberNotFoundException;
import be.ddd.domain.repo.CafeBeverageRepository;
import be.ddd.domain.repo.IntakeHistoryRepository;
import be.ddd.domain.repo.MemberRepository;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IntakeHistoryDummyInitializer {

    private final CafeBeverageRepository beverageRepository;
    private final IntakeHistoryRepository intakeHistoryRepository;
    private final MemberRepository memberRepository;
    private final Random random = new Random();
    private final Month targetMonth = Month.JULY;

    @EventListener(ApplicationReadyEvent.class)
    public void insertDummyIntakeHistories() {
        // 이미 데이터가 있다면 스킵
        if (intakeHistoryRepository.count() > 0) {
            return;
        }

        // 등록 가능한 음료 UUID 목록
        Pageable firstHundred = PageRequest.of(0, 100);
        List<UUID> productIds =
                beverageRepository.findAll(firstHundred).getContent().stream()
                        .map(CafeBeverage::getProductId)
                        .toList();

        if (productIds.isEmpty()) {
            System.out.println("🍹 음료 데이터가 없어 섭취 이력 더미 생성 취소");
            return;
        }

        LocalDateTime start = LocalDateTime.of(2025, targetMonth, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, targetMonth, 31, 23, 59);
        Member targetMember =
                memberRepository.findById(1L).orElseThrow(MemberNotFoundException::new);
        // 7월 한 달치
        for (LocalDateTime date = start; !date.isAfter(end); date = date.plusDays(1)) {
            // 50% 확률로 스킵
            if (random.nextBoolean()) {
                continue;
            }
            // 하루 1~3건 랜덤 생성
            int cnt = random.nextInt(3) + 1;
            for (int i = 0; i < cnt; i++) {
                // 랜덤 음료
                UUID pid = productIds.get(random.nextInt(productIds.size()));
                // 랜덤 시각
                LocalDateTime when =
                        date.withHour(random.nextInt(24))
                                .withMinute(random.nextInt(60))
                                .withSecond(random.nextInt(60));

                CafeBeverage targetBeverage =
                        beverageRepository
                                .findByProductId(pid)
                                .orElseThrow(CafeBeverageNotFoundException::new);
                IntakeHistory history = new IntakeHistory(targetMember, when, targetBeverage);
                intakeHistoryRepository.save(history);
            }
        }

        System.out.println("✅ 7월 더미 음료 섭취 기록 생성 완료");
    }
}
