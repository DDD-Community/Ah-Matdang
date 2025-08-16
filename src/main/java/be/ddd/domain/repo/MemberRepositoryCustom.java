package be.ddd.domain.repo;

import be.ddd.domain.entity.member.Member;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepositoryCustom {
    List<Member> findAllByNotificationEnabledAndReminderTime(LocalTime reminderTime);
    Optional<Member> findByProviderId(String providerId);
}
