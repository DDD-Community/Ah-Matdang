package be.ddd.domain.repo;

import be.ddd.domain.entity.member.Member;
import java.time.LocalTime;
import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findAllByNotificationEnabledAndReminderTime(LocalTime reminderTime);
}
