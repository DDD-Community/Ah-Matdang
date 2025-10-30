package be.ddd.application.discord;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class DiscordNotificationService {

    private final WebClient webClient;

    public DiscordNotificationService(
            WebClient.Builder webClientBuilder,
            @Value("${notification.failure-discord-webhook-uri}") String webhookUri) {
        this.webClient = webClientBuilder.baseUrl(webhookUri).build();
    }

    public void sendNotification(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("content", message);
        webClient.post().bodyValue(body).retrieve().toBodilessEntity().block();
    }
}
