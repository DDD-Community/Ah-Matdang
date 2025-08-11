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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
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
        if (Objects.isNull(settings)) {
            settings = new NotificationSettings(member);
            member.notificationSettings(settings);
        }

        notificationSettingsMapper.updateFromDto(dto, settings);

        return NotificationSettingsResponseDto.from(settings);
    }
}
