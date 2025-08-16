package be.ddd.api.auth;

import be.ddd.application.member.MemberBootstrapService;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthAPI {

    private final MemberBootstrapService bootstrapService;

    public AuthAPI(MemberBootstrapService bootstrapService) {
        this.bootstrapService = bootstrapService;
    }

    // 퍼블릭 헬스 체크
    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of("ok", true);
    }

    // 인증 필요 : 현재 사용자/토큰 정보
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(HttpServletRequest req) {
        DecodedJWT jwt = (DecodedJWT) req.getAttribute("auth0.jwt");
        String sub = (String) req.getAttribute("auth0.sub");

        if (jwt == null || sub == null) {
            return ResponseEntity.status(401).build();
        }

        UUID fakeId = bootstrapService.ensureAndGetFakeId(jwt);

        var body = new LinkedHashMap<String, Object>();
        body.put("ok", true);
        body.put("fakeId", fakeId.toString());

        return ResponseEntity.ok(body);
    }

}
