package be.ddd.api.login.test;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/demo")
public class DemoController {

    // 공개 엔드포인트 (토큰 불필요)
    @GetMapping("/public")
    public Map<String, Object> publicEndpoint() {
        return Map.of("ok", true, "message", "public ok");
    }

    @GetMapping("/protected")
    public Map<String, Object> protectedEndpoint(HttpServletRequest req) {
        Object sub = req.getAttribute("auth0.sub");
        Object email = req.getAttribute("auth0.email");
        return Map.of("ok", true, "sub", sub, "email", email);
    }

}
