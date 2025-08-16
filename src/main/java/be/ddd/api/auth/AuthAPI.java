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
@RequestMapping("/demo")
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
    @GetMapping("/auth/me")
    public ResponseEntity<Map<String, Object>> me(HttpServletRequest req) {
        DecodedJWT jwt = (DecodedJWT) req.getAttribute("auth0.jwt");
        String sub = (String) req.getAttribute("auth0.sub");
        String email = (String) req.getAttribute("auth0.email");

        if (jwt == null || sub == null) {
            return ResponseEntity.status(401).build();
        }

        UUID fakeId = bootstrapService.ensureAndGetFakeId(jwt);

        var user = new LinkedHashMap<String, Object>();
        user.put("fakeId", fakeId.toString());
        user.put("sub", sub);
        if (email != null) user.put("email", email); // 없으면 키 생략

        var token = new LinkedHashMap<String, Object>();
        token.put("iss", jwt.getIssuer());
        token.put("aud", jwt.getAudience());
        token.put("sub", sub);
        if (!jwt.getClaim("scope").isMissing()) token.put("scope", jwt.getClaim("scope").asString());
        if (jwt.getIssuedAt() != null) token.put("iat", jwt.getIssuedAt().toInstant());
        if (jwt.getExpiresAt() != null) token.put("exp", jwt.getExpiresAt().toInstant());
        if (jwt.getKeyId() != null) token.put("kid", jwt.getKeyId());

        var body = new LinkedHashMap<String, Object>();
        body.put("ok", true);
        body.put("user", user);
        body.put("token", token);

        return ResponseEntity.ok(body);
    }

}
