package be.ddd.api.login.service;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenStore {

    private final Map<String, TokenEntry> store = new ConcurrentHashMap<>();

    public void save(String refreshToken, String userId, Instant expiresAt) {
        store.put(refreshToken, new TokenEntry(userId, expiresAt));
    }

    public String getUserId(String refreshToken) {
        TokenEntry entry = store.get(refreshToken);
        if (entry == null || entry.expiresAt().isBefore(Instant.now())) {
            return null;
        }
        return entry.userId();
    }

    public void delete(String refreshToken) {
        store.remove(refreshToken);
    }

    public record TokenEntry(String userId, Instant expiresAt) {}
}
