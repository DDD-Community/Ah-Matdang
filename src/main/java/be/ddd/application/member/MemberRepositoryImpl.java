package be.ddd.application.member;

import be.ddd.domain.entity.member.Member;
import be.ddd.domain.entity.member.QMember;
import be.ddd.domain.repo.MemberRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QMember member = QMember.member;

    @Override
    public List<Member> findAllByNotificationEnabledAndReminderTime(LocalTime reminderTime) {
        return queryFactory
                .selectFrom(member)
                .where(
                        member.notificationSettings.isEnabled.isTrue(),
                        member.notificationSettings.reminderTime.eq(reminderTime))
                .fetch();
    }

    @Override
    public Optional<Member> findByProviderId(String providerId) {
        Member result = queryFactory
                .selectFrom(member)
                .where(member.providerId.eq(providerId))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
