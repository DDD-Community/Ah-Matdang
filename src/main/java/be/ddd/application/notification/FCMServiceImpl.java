package be.ddd.application.notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMServiceImpl implements FCMService {

    private final FirebaseMessaging firebaseMessaging;

    @Override
    public void sendMessage(String deviceToken, String title, String body) {
        if (deviceToken == null || deviceToken.isBlank()) {
            log.warn("Device token is empty. Cannot send FCM message.");
            return;
        }

        Notification notification = Notification.builder().setTitle(title).setBody(body).build();

        Message message =
                Message.builder()
                        .setToken(deviceToken)
                        .setNotification(notification)
                        // 추가적인 데이터 페이로드를 여기에 설정할 수 있습니다.
                        // .putData("key", "value")
                        .build();

        try {
            firebaseMessaging.send(message);
            log.info("Successfully sent FCM message to token: {}", deviceToken);
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send FCM message to token: {}", deviceToken, e);
            // 여기에 실패 시 재시도 로직 등을 추가할 수 있습니다.
        }
    }

    @Override
    public void sendNotification(String deviceToken, String title, String body) {
        sendMessage(deviceToken, title, body);
    }
}
