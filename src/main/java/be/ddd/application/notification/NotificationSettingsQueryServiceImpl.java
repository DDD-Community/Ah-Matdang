package be.ddd.application.notification;

import be.ddd.api.dto.res.notification.NotificationSettingsResponseDto;
import be.ddd.domain.entity.member.Member;
import be.ddd.domain.exception.MemberNotFoundException;
import be.ddd.domain.repo.MemberRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationSettingsQueryServiceImpl implements NotificationSettingsQueryService {

    private final MemberRepository memberRepository;

    @Override
    public NotificationSettingsResponseDto getNotificationSettings(UUID memberFakeId) {
        Member member =
                memberRepository
                        .findByFakeIdAndDeletedAtIsNull(memberFakeId)
                        .orElseThrow(MemberNotFoundException::new);

        return NotificationSettingsResponseDto.from(member.getNotificationSettings());
    }
}
