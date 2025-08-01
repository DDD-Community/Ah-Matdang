package be.ddd.api.member;

import be.ddd.api.dto.req.MemberProfileRegistrationDto;
import be.ddd.application.member.MemberCommandService;
import be.ddd.common.dto.ApiResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberProfileAPI {
    private final MemberCommandService memberCommandService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<?> registerMemberProfile(
            @RequestBody MemberProfileRegistrationDto memberProfileRegistrationDto) {
        UUID memberFakeId =
                memberCommandService.registerMemberProfile(memberProfileRegistrationDto);

        return ApiResponse.success(memberFakeId);
    }
}
