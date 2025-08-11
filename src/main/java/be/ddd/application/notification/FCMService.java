package be.ddd.application.notification;

public interface FCMService {
    void sendMessage(String deviceToken, String title, String body);

    void sendNotification(String deviceToken, String title, String body);
}
