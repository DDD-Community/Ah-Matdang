package be.ddd.api.login.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final SecureRandom RND = new SecureRandom();
    private final TokenStore tokenStore;

    @Value("${auth.jwt.refresh.expiration-ms}")
    private long refreshExpirationMs;

    public String issueAndStoreRefreshToken(String userId) {
        String raw = generate();
        Instant exp = Instant.now().plusMillis(refreshExpirationMs);
        tokenStore.save(hash(raw), userId, exp);
        return raw;
    }

    public String generate() {
        byte[] buf = new byte[32];
        RND.nextBytes(buf);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
    }

    private String hash(String token) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return Base64.getEncoder().encodeToString(md.digest(token.getBytes()));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void revoke(String rawToken) { tokenStore.delete(hash(rawToken)); }

}
