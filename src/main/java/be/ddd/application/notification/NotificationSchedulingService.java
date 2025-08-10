package be.ddd.application.notification;

import be.ddd.common.util.CustomClock;
import be.ddd.domain.entity.member.Member;
import be.ddd.domain.repo.IntakeHistoryRepository;
import be.ddd.domain.repo.MemberRepository;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationSchedulingService {

    private final MemberRepository memberRepository;
    private final IntakeHistoryRepository intakeHistoryRepository;
    private final FCMService fcmService;

    @Async
    @Scheduled(cron = "0 * * * * *")
    public void sendSugarIntakeNotifications() {
        LocalTime now = CustomClock.now().toLocalTime();

        List<Member> members = memberRepository.findAllByNotificationEnabledAndReminderTime(now);

        if (members.isEmpty()) {
            return;
        }

        List<Long> memberIds = members.stream().map(Member::getId).collect(Collectors.toList());

        Map<Long, Double> totalSugars =
                intakeHistoryRepository.sumSugarByMemberIdsAndDate(memberIds, LocalDateTime.now());

        for (Member member : members) {
            Double totalSugarToday = totalSugars.get(member.getId());
            if (totalSugarToday != null) {
                fcmService.sendNotification(
                        member.getDeviceToken(),
                        "오늘의 당 섭취량 알림",
                        "오늘 하루 총 " + totalSugarToday + "g의 당을 섭취했습니다.");
            }
        }
    }
}
