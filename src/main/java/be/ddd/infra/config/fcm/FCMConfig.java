package be.ddd.infra.config.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@Slf4j
@Configuration
@EnableConfigurationProperties(FcmProperties.class)
public class FCMConfig {

    @Bean
    public FirebaseApp firebaseApp(FcmProperties props, ResourceLoader resourceLoader)
            throws IOException {
        Resource resource = resourceLoader.getResource(props.getServiceAccountPath());

        if (!resource.exists()) {
            throw new IllegalStateException(
                    "FCM service account file not found: " + props.getServiceAccountPath());
        }

        try (var is = resource.getInputStream()) {
            FirebaseOptions options =
                    FirebaseOptions.builder()
                            .setCredentials(GoogleCredentials.fromStream(is))
                            .build();

            // 이미 초기화되어 있다면 재사용 (테스트/재기동 대비)
            return FirebaseApp.getApps().stream()
                    .findFirst()
                    .orElse(FirebaseApp.initializeApp(options));
        }
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}
