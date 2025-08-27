package be.ddd.application.notification;

import be.ddd.api.dto.req.notification.NotificationSettingsUpdateRequestDto;
import be.ddd.api.dto.res.notification.NotificationSettingsResponseDto;
import be.ddd.domain.entity.member.Member;
import be.ddd.domain.entity.member.NotificationSettings;
import be.ddd.domain.exception.MemberNotFoundException;
import be.ddd.domain.repo.MemberRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class NotificationSettingsCommandServiceImpl implements NotificationSettingsCommandService {

    private final MemberRepository memberRepository;

    @Override
    public NotificationSettingsResponseDto updateNotificationSettings(
            UUID memberFakeId, NotificationSettingsUpdateRequestDto dto) {
        Member member =
                memberRepository
                        .findByFakeIdAndDeletedAtIsNull(memberFakeId)
                        .orElseThrow(MemberNotFoundException::new);

        //        NotificationSettings settings =
        //                notificationSettingsRepository
        //                        .findByMemberId(member.getId())
        //                        .orElseThrow(
        //                                () ->
        //                                        new IllegalStateException(
        //                                                "NotificationSettings not found for member
        // "
        //                                                        + memberFakeId));
        if (member.getNotificationSettings() == null) {
            NotificationSettings settings = new NotificationSettings(member);
            member.notificationSettings(settings);
            return NotificationSettingsResponseDto.from(settings);
        }
        /*settings.updateSettings(
        dto.isEnabled(),
        dto.remindersEnabled(),
        dto.reminderTime(),
        dto.riskWarningsEnabled(),
        dto.newsUpdatesEnabled());*/

        return NotificationSettingsResponseDto.from(member.getNotificationSettings());
    }
}
