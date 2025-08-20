package be.ddd.application.member;

import be.ddd.api.dto.req.MemberProfileModifyDto;
import be.ddd.api.dto.req.MemberProfileRegistrationDto;
import be.ddd.api.dto.res.MemberModifyDetailsDto;
import be.ddd.api.dto.res.MemberRegistrationDetailsDto;
import be.ddd.application.member.dto.res.RecommendedSugar;
import be.ddd.common.mapper.MemberProfileMapper;
import be.ddd.domain.entity.member.Member;
import be.ddd.domain.entity.member.MemberHealthMetric;
import be.ddd.domain.entity.member.NotificationSettings;
import be.ddd.domain.exception.InvalidInputException;
import be.ddd.domain.exception.MemberNotFoundException;
import be.ddd.domain.repo.MemberRepository;
import java.time.LocalDate;
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
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final MemberProfileMapper memberProfileMapper;
    private final SugarRecommendationService sugarRecommendationService;

    @Override
    public MemberRegistrationDetailsDto registerMemberProfile(
            UUID fakeId, MemberProfileRegistrationDto req) {
        Member member =
                memberRepository
                        .findByFakeIdAndDeletedAtIsNull(fakeId)
                        .orElseThrow(MemberNotFoundException::new);

        LocalDate birthDay = req.birthDay();
        MemberHealthMetric healthMetric =
                new MemberHealthMetric(
                        calculateAge(birthDay),
                        req.heightCm(),
                        req.weightKg(),
                        req.gender(),
                        req.activityRange(),
                        req.sugarIntakeLevel());

        member.ofProfile(req.nickname(), birthDay, healthMetric);

        NotificationSettings notificationSettings = new NotificationSettings(member);
        member.notificationSettings(notificationSettings);

        RecommendedSugar recommendedSugar = updateSugarRecommendation(member);

        return new MemberRegistrationDetailsDto(member.getFakeId(), recommendedSugar);
    }

    @Override
    public MemberModifyDetailsDto modifyMemberProfile(UUID fakeId, MemberProfileModifyDto req) {
        Member member =
                memberRepository
                        .findByFakeIdAndDeletedAtIsNull(fakeId)
                        .orElseThrow(MemberNotFoundException::new);

        memberProfileMapper.modifyFromDto(req, member);

        RecommendedSugar recommendedSugar = updateSugarRecommendation(member);

        return MemberModifyDetailsDto.from(member, recommendedSugar);
    }

    @Override
    public void withdrawMember(UUID fakeId) {
        Member member =
                memberRepository
                        .findByFakeIdAndDeletedAtIsNull(fakeId)
                        .orElseThrow(MemberNotFoundException::new);

        if (member.isDeleted()) {
            throw new InvalidInputException("이미 탈퇴한 회원입니다.");
        }

        member.withdraw();
        memberRepository.save(member);
    }

    private RecommendedSugar updateSugarRecommendation(Member member) {
        RecommendedSugar baseRecommendation = sugarRecommendationService.calculate(member);
        MemberHealthMetric healthMetric = member.getMemberHealthMetric();

        healthMetric.calculatePersonalSugar(
                baseRecommendation.sugarMaxG(), baseRecommendation.sugarIdealG());

        // 3. DB에 저장된 최종 개인화 값을 DTO에 담아 반환
        return RecommendedSugar.fromIntegerValues(
                healthMetric.getSugarMaxG().doubleValue(),
                healthMetric.getSugarIdealG().doubleValue());
    }

    private Integer calculateAge(LocalDate birthday) {
        if (Objects.isNull(birthday)) {
            throw new InvalidInputException("생년 월일이 비었습니다.");
        }
        LocalDate today = LocalDate.now();
        if (birthday.isAfter(today)) {
            throw new InvalidInputException("생년월일이 오늘 이후일 수 없습니다: " + birthday);
        }
        return LocalDate.now().getYear() - birthday.getYear() + 1;
    }
}
