package be.ddd.api.member;

import be.ddd.api.dto.req.MemberProfileModifyDto;
import be.ddd.api.dto.req.MemberProfileRegistrationDto;
import be.ddd.api.dto.res.MemberDetailsDto;
import be.ddd.api.dto.res.MemberModifyDetailsDto;
import be.ddd.application.member.MemberCommandService;
import be.ddd.application.member.MemberQueryService;
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

    @PostMapping("/{fakeId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<?> registerMemberProfile(
            @PathVariable("fakeId") UUID fakeId, @RequestBody MemberProfileRegistrationDto req) {
        log.info("fakeId:{}", fakeId);
        UUID memberFakeId = memberCommandService.registerMemberProfile(fakeId, req);

        return ApiResponse.success(memberFakeId);
    }

    @PatchMapping("/{fakeId}")
    public ApiResponse<?> modifyMemberProfile(
            @PathVariable("fakeId") UUID fakeId, @RequestBody MemberProfileModifyDto req) {
        MemberModifyDetailsDto res = memberCommandService.modifyMemberProfile(fakeId, req);

        return ApiResponse.success(res);
    }

    @GetMapping("/{fakeId}")
    public ApiResponse<?> checkMemberProfile(@PathVariable("fakeId") UUID fakeId) {
        MemberDetailsDto memberDetailsDto = memberQueryService.checkMemberProfile(fakeId);

        return ApiResponse.success(memberDetailsDto);
    }
}
