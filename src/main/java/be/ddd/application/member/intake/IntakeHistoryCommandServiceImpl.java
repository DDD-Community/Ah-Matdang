package be.ddd.application.member.intake;

import be.ddd.api.dto.req.IntakeRegistrationRequestDto;
import be.ddd.common.util.CustomClock;
import be.ddd.domain.entity.crawling.CafeBeverage;
import be.ddd.domain.entity.member.Member;
import be.ddd.domain.entity.member.intake.IntakeHistory;
import be.ddd.domain.exception.CafeBeverageNotFoundException;
import be.ddd.domain.exception.FutureDateNotAllowedException;
import be.ddd.domain.exception.MemberNotFoundException;
import be.ddd.domain.repo.CafeBeverageRepository;
import be.ddd.domain.repo.IntakeHistoryRepository;
import be.ddd.domain.repo.MemberRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class IntakeHistoryCommandServiceImpl implements IntakeHistoryCommandService {

    private final IntakeHistoryRepository intakeHistoryRepository;
    private final MemberRepository memberRepository;
    private final CafeBeverageRepository cafeBeverageRepository;

    @Override
    public Long registerIntake(Long memberId, IntakeRegistrationRequestDto requestDto) {
        if (isFuture(requestDto.intakeTime())) {
            throw new FutureDateNotAllowedException();
        }

        Member member =
                memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        CafeBeverage beverage =
                cafeBeverageRepository
                        .findByProductId(requestDto.productId())
                        .orElseThrow(CafeBeverageNotFoundException::new);

        IntakeHistory intakeHistory = new IntakeHistory(member, requestDto.intakeTime(), beverage);
        IntakeHistory history = intakeHistoryRepository.save(intakeHistory);

        return history.getId();
    }

    private boolean isFuture(LocalDateTime intakeTime) {
        System.out.println("!!!!!!!!!!!!!!!!!!!");
        return intakeTime.toLocalDate().isAfter(CustomClock.now().toLocalDate());
    }
}
