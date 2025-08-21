package be.ddd.api.auth;

import be.ddd.application.member.MemberBootstrapService;
import be.ddd.application.member.MemberCommandService;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
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

    // 퍼블릭 헬스 체크
    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of("ok", true);
    }

    // 인증 필요 : 현재 사용자/토큰 정보
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(HttpServletRequest req) {
        UUID fakeId = extractAuthenticatedFakeId(req);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("ok", true);
        body.put("fakeId", fakeId.toString());

        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<Map<String, Object>> withdraw(HttpServletRequest req) {
        UUID fakeId = extractAuthenticatedFakeId(req);
        memberCommandService.withdrawMember(fakeId);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("ok", true);
        body.put("message", "회원 탈퇴가 완료되었습니다.");

        return ResponseEntity.ok(body);
    }

    private UUID extractAuthenticatedFakeId(HttpServletRequest req) {
        DecodedJWT jwt = (DecodedJWT) req.getAttribute("auth0.jwt");
        String sub = (String) req.getAttribute("auth0.sub");

        if (jwt == null || sub == null) {
            throw new RuntimeException("Authentication required");
        }

        return bootstrapService.ensureAndGetFakeId(jwt);
    }
}
