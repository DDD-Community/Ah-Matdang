package be.ddd.application.notification;

import be.ddd.api.dto.req.notification.NotificationSettingsUpdateRequestDto;
import be.ddd.api.dto.res.notification.NotificationSettingsResponseDto;
import be.ddd.common.mapper.NotificationSettingsMapper;
import be.ddd.domain.entity.member.Member;
import be.ddd.domain.entity.member.NotificationSettings;
import be.ddd.domain.exception.MemberNotFoundException;
import be.ddd.domain.repo.MemberRepository;
import java.util.Objects;
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
    private final NotificationSettingsMapper notificationSettingsMapper;

    @Override
    public NotificationSettingsResponseDto updateNotificationSettings(
            UUID memberFakeId, NotificationSettingsUpdateRequestDto dto) {
        Member member =
                memberRepository
                        .findByFakeId(memberFakeId)
                        .orElseThrow(MemberNotFoundException::new);
        NotificationSettings settings = member.getNotificationSettings();
        log.info("setting: {}", dto.isEnabled());
        if (Objects.isNull(settings)) {
            settings = new NotificationSettings(member);
            member.notificationSettings(settings);
        }
        settings.updateSettings(
                dto.isEnabled(),
                dto.remindersEnabled(),
                dto.reminderTime(),
                dto.riskWarningsEnabled(),
                dto.newsUpdatesEnabled());
        //        notificationSettingsMapper.updateSettings(dto, settings);

        return NotificationSettingsResponseDto.from(settings);
    }
}
