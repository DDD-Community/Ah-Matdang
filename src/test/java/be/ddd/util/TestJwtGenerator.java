package be.ddd.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

/**
 * RS256(비대칭키)로 서명하는 테스트용 JWT 생성 유틸리티. - kid 헤더를 포함하여 JWKS 기반 검증 흐름과 형태를 맞춤. -
 * TEST_JWT_RSA_PRIVATE_PEM 환경변수에 PKCS#8 RSA 개인키(PEM) 주입 가능. 미지정 시 테스트 실행 시점에 임시 키페어를 생성.
 */
public class TestJwtGenerator {

    // === 테스트 환경에 맞게 값 조정 ===
    private static final String ISSUER = "https://dev-o4pgsljdj1aisk3y.us.auth0.com/";
    private static final String AUDIENCE = "https://ah-matdang/api";
    private static final String MEMBER_ID_CLAIM = "https://ddd-12-ios-1.com/memberId";

    // 고정 kid (임의 값). 필요 시 변경 가능.
    private static final String KID = "test-rsa-kid-001";

    private static final Algorithm ALGORITHM;
    private static final RSAPublicKey PUBLIC_KEY; // 공개키는 JWK 노출용(검증자에 심거나 모킹 서버에서 사용)
    private static final RSAPrivateKey PRIVATE_KEY;

    static {
        try {
            String pem = System.getenv("TEST_JWT_RSA_PRIVATE_PEM");
            if (pem != null && !pem.isBlank()) {
                // 외부에서 제공된 개인키 사용
                PRIVATE_KEY = loadPrivateKeyFromPem(pem);
                PUBLIC_KEY = null; // 서명만 할 거라면 null이어도 OK
            } else {
                // 런타임 임시 키페어 생성
                KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
                kpg.initialize(2048);
                KeyPair kp = kpg.generateKeyPair();
                PRIVATE_KEY = (RSAPrivateKey) kp.getPrivate();
                PUBLIC_KEY = (RSAPublicKey) kp.getPublic();
            }
            ALGORITHM = Algorithm.RSA256(PUBLIC_KEY, PRIVATE_KEY);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize RS256 Algorithm", e);
        }
    }

    /** RS256로 서명된 테스트 JWT 생성 (유효시간 지정). */
    public static String generateToken(String subject, Long memberId, long validityInSeconds) {
        Instant now = Instant.now();
        return JWT.create()
                .withKeyId(KID) // ⟵ 헤더에 kid 설정
                .withIssuer(ISSUER)
                .withAudience(AUDIENCE)
                .withSubject(subject)
                .withClaim(MEMBER_ID_CLAIM, memberId)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plusSeconds(validityInSeconds)))
                .sign(ALGORITHM);
    }

    /** RS256로 서명된 테스트 JWT 생성 (기본 1시간). */
    public static String generateToken(String subject, Long memberId) {
        return generateToken(subject, memberId, 3600);
    }

    /**
     * (옵션) 현재 공개키로부터 RSA JWK(JSON) 문자열 생성. - 테스트용 JWKS 모킹 서버에 등록하거나, 검증기 대체 설정에 활용 가능. - 런타임 생성키 사용
     * 시에만 동작(PUBLIC_KEY가 null이 아니어야 함).
     */
    public static String getPublicJwkJson() {
        if (PUBLIC_KEY == null) return "{}";
        String n = base64UrlNoPad(PUBLIC_KEY.getModulus().toByteArray());
        String e = base64UrlNoPad(PUBLIC_KEY.getPublicExponent().toByteArray());
        String jwk =
                """
            {
              "kty": "RSA",
              "use": "sig",
              "alg": "RS256",
              "kid": "%s",
              "n": "%s",
              "e": "%s"
            }
            """
                        .formatted(KID, n, e);
        return jwk;
    }

    // ===== helpers =====

    private static RSAPrivateKey loadPrivateKeyFromPem(String pem) throws Exception {
        String clean =
                pem.replace("-----BEGIN PRIVATE KEY-----", "")
                        .replace("-----END PRIVATE KEY-----", "")
                        .replaceAll("\\s", "");
        byte[] pkcs8 = Base64.getDecoder().decode(clean);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(pkcs8);
        return (RSAPrivateKey) kf.generatePrivate(spec);
    }

    private static String base64UrlNoPad(byte[] bytes) {
        // BigInteger → byte[] 변환 시 선행 0x00 제거
        byte[] normalized = stripLeadingZeros(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(normalized);
    }

    private static byte[] stripLeadingZeros(byte[] bytes) {
        int i = 0;
        while (i < bytes.length - 1 && bytes[i] == 0) i++;
        byte[] out = new byte[bytes.length - i];
        System.arraycopy(bytes, i, out, 0, out.length);
        return out;
    }
}
