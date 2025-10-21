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
        log.info("[NotificationTest] 1. Current fake time: {}", now);

        List<Member> members = memberRepository.findAllByNotificationEnabledAndReminderTime(now);
        log.info(
                "[NotificationTest] 2. Found {} members with reminder time {}",
                members.size(),
                now);

        if (members.isEmpty()) {
            log.info("[NotificationTest] No members found. Exiting.");
            return;
        }

        List<String> memberIds =
                members.stream().map(Member::getProviderId).collect(Collectors.toList());

        Map<Long, Double> totalSugars =
                intakeHistoryRepository.sumSugarByMemberIdsAndDate(memberIds, LocalDateTime.now());
        log.info(
                "[NotificationTest] 3. Found sugar intake data for {} members.",
                totalSugars.size());

        for (Member member : members) {
            log.info("[NotificationTest] 4. Checking member with ID: {}", member.getId());
            Double totalSugarToday = totalSugars.get(member.getId());
            if (totalSugarToday != null) {
                log.info(
                        "[NotificationTest] 5. Member {} has sugar intake ({}g). Sending notification.",
                        member.getId(),
                        totalSugarToday);
                fcmService.sendNotification(
                        member.getDeviceToken(),
                        "오늘의 당 섭취량 알림",
                        "오늘 하루 총 " + totalSugarToday + "g의 당을 섭취했습니다.");
            } else {
                log.info(
                        "[NotificationTest] 5. Member {} has NO sugar intake today. Skipping.",
                        member.getId());
            }
        }
    }
}
