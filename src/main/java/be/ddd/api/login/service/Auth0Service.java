package be.ddd.api.login.service;

import be.ddd.api.dto.res.LoginDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class Auth0Service {

    @Value("${auth0.domain}")
    private String domain;

    @Value("${auth0.client-id}")
    private String clientId;

    @Value("${auth0.client-secret}")
    private String clientSecret;

    @Value("${auth0.redirect-uri}")
    private String redirectUri;

    @Value("${auth.jwt.secret}")
    private String jwtSecret;

    @Value("${auth.jwt.access.expiration-ms}")
    private long accessExpirationMs;

    private final TokenService tokenService;
    private final RestTemplate restTemplate = new RestTemplate();
    private Key jwtKey;

    @PostConstruct
    public void init() {
        jwtKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public LoginDto loginWithCode(String code) {
        // 1. code로 access_token 요청
        String tokenUrl = "https://" + domain + "/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "authorization_code");
        body.put("client_id", clientId);
        body.put("client_secret", clientSecret);
        body.put("code", code);
        body.put("redirect_uri", redirectUri);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenUrl, request, Map.class);

        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("토큰 요청 실패");
        }

        String accessToken = (String) tokenResponse.getBody().get("access_token");

        // 2. access_token으로 사용자 정보 요청
        String userInfoUrl = "https://" + domain + "/userinfo";
        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.setBearerAuth(accessToken);
        HttpEntity<Void> userInfoRequest = new HttpEntity<>(userInfoHeaders);

        ResponseEntity<Map> userInfoResponse =
            restTemplate.exchange(userInfoUrl, HttpMethod.GET, userInfoRequest, Map.class);

        if (!userInfoResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("사용자 정보 요청 실패");
        }

        Map<String, Object> userInfo = userInfoResponse.getBody();
        String email = (String) userInfo.get("email");
        String userId = (String) userInfo.get("sub");

        // 3. JWT 생성
        String jwt = Jwts.builder()
            .setSubject(userId)
            .claim("email", email)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + accessExpirationMs))
            .signWith(jwtKey, SignatureAlgorithm.HS256)
            .compact();


        // 4. Refresh Token 생성 및 로컬 캐시에 저장
        String refreshToken = tokenService.issueAndStoreRefreshToken(userId);

        // 5. userInfo에 refresh_token 포함시켜 응답
        userInfo.put("refresh_token", refreshToken);

        return new LoginDto(jwt, userInfo);
    }

    public void logout() {
    }

}
