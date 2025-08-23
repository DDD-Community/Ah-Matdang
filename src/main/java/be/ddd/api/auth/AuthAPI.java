package be.ddd.api.auth;

import be.ddd.api.dto.res.auth.HealthResponseDto;
import be.ddd.api.dto.res.auth.MeResponseDto;
import be.ddd.api.dto.res.auth.WithdrawResponseDto;
import be.ddd.application.member.MemberBootstrapService;
import be.ddd.application.member.MemberCommandService;
import be.ddd.common.dto.ApiResponse;
import be.ddd.domain.exception.AuthenticationBadRequest;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthAPI {

    private final MemberBootstrapService bootstrapService;
    private final MemberCommandService memberCommandService;

    public AuthAPI(
            MemberBootstrapService bootstrapService, MemberCommandService memberCommandService) {
        this.bootstrapService = bootstrapService;
        this.memberCommandService = memberCommandService;
    }

    @GetMapping("/health")
    public ApiResponse<HealthResponseDto> health() {
        return ApiResponse.success(new HealthResponseDto(true));
    }

    @GetMapping("/me")
    public ApiResponse<MeResponseDto> me(HttpServletRequest req) {
        MeResponseDto res = extractAuthenticatedMetadata(req);
        return ApiResponse.success(res);
    }

    @DeleteMapping("/withdraw")
    public ApiResponse<WithdrawResponseDto> withdraw(HttpServletRequest req) {
        MeResponseDto res = extractAuthenticatedMetadata(req);
        memberCommandService.withdrawMember(res.fakeId());
        return ApiResponse.success(new WithdrawResponseDto("회원탈퇴가 완료 되었습니다."));
    }

    private MeResponseDto extractAuthenticatedMetadata(HttpServletRequest req) {
        DecodedJWT jwt = (DecodedJWT) req.getAttribute("auth0.jwt");
        String sub = (String) req.getAttribute("auth0.sub");

        if (jwt == null || sub == null) {
            throw new AuthenticationBadRequest();
        }

        return bootstrapService.ensureAndGetFakeId(jwt);
    }
}
