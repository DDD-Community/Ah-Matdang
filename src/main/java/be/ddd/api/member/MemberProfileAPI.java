package be.ddd.api.member;

import be.ddd.api.dto.req.MemberProfileModifyDto;
import be.ddd.api.dto.req.MemberProfileRegistrationDto;
import be.ddd.api.dto.res.MemberDetailsDto;
import be.ddd.api.dto.res.MemberModifyDetailsDto;
import be.ddd.api.dto.res.MemberRegistrationDetailsDto;
import be.ddd.api.member.dto.req.FCMTokenUpdateRequest;
import be.ddd.api.security.custom.CurrentUser;
import be.ddd.application.member.MemberCommandService;
import be.ddd.application.member.MemberQueryService;
import be.ddd.application.member.SugarRecommendationService;
import be.ddd.application.member.dto.res.RecommendedSugar;
import be.ddd.common.dto.ApiResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Log4j2
public class MemberProfileAPI {
    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;
    private final SugarRecommendationService sugarRecommendationService;

    @PostMapping("/{fakeId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<?> registerMemberProfile(
            @PathVariable("fakeId") UUID fakeId, @RequestBody MemberProfileRegistrationDto req) {
        log.info("fakeId:{}", fakeId);
        MemberRegistrationDetailsDto res = memberCommandService.registerMemberProfile(fakeId, req);

        return ApiResponse.success(res);
    }

    @PatchMapping("/{fakeId}")
    public ApiResponse<?> modifyMemberProfile(
            @PathVariable("fakeId") UUID fakeId, @RequestBody MemberProfileModifyDto req) {
        MemberModifyDetailsDto res = memberCommandService.modifyMemberProfile(fakeId, req);

        return ApiResponse.success(res);
    }

    @PatchMapping("/{fakeId}/fcm-token")
    public ApiResponse<?> updateFCMToken(
            @PathVariable("fakeId") UUID fakeId,
            @RequestBody FCMTokenUpdateRequest request,
            @CurrentUser String providerId) {
        Long memberId = memberQueryService.getMemberIdByProviderId(providerId);
        memberCommandService.updateFCMToken(memberId, request.getFcmToken());
        return ApiResponse.success("FCM token updated successfully.");
    }

    @GetMapping("/{fakeId}")
    public ApiResponse<?> checkMemberProfile(@PathVariable("fakeId") UUID fakeId) {
        MemberDetailsDto memberDetailsDto = memberQueryService.checkMemberProfile(fakeId);

        return ApiResponse.success(memberDetailsDto);
    }

    @GetMapping("/{fakeId}/sugar")
    public ApiResponse<RecommendedSugar> getSugarRecommendation(
            @PathVariable("fakeId") UUID fakeId) {
        RecommendedSugar res =
                sugarRecommendationService.calculate(memberQueryService.getMemberByFakeId(fakeId));
        return ApiResponse.success(res);
    }
}
