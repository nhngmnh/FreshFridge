package hust.project.freshfridge.infrastructure.service;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class FCMService {

    private final FirebaseMessaging firebaseMessaging;

    @Async
    public void sendNotification(String token, String title, String body) {
        sendNotification(token, title, body, null);
    }

    @Async
    public void sendNotification(String token, String title, String body, Map<String, String> data) {
        if (firebaseMessaging == null) {
            log.warn("Firebase Messaging chưa được khởi tạo. Bỏ qua gửi notification.");
            return;
        }

        if (token == null || token.isBlank()) {
            log.warn("FCM token rỗng. Bỏ qua gửi notification.");
            return;
        }

        try {
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            Message.Builder messageBuilder = Message.builder()
                    .setToken(token)
                    .setNotification(notification);

            if (data != null && !data.isEmpty()) {
                messageBuilder.putAllData(data);
            }

            // Cấu hình cho Android
            messageBuilder.setAndroidConfig(AndroidConfig.builder()
                    .setPriority(AndroidConfig.Priority.HIGH)
                    .setNotification(AndroidNotification.builder()
                            .setSound("default")
                            .setClickAction("OPEN_ACTIVITY")
                            .build())
                    .build());

            // Cấu hình cho iOS
            messageBuilder.setApnsConfig(ApnsConfig.builder()
                    .setAps(Aps.builder()
                            .setSound("default")
                            .setBadge(1)
                            .build())
                    .build());

            String response = firebaseMessaging.send(messageBuilder.build());
            log.info("Gửi FCM notification thành công: {}", response);
        } catch (FirebaseMessagingException e) {
            log.error("Lỗi gửi FCM notification: {}", e.getMessage());
            handleFCMError(e, token);
        }
    }

    @Async
    public void sendToMultipleDevices(List<String> tokens, String title, String body, Map<String, String> data) {
        if (firebaseMessaging == null) {
            log.warn("Firebase Messaging chưa được khởi tạo. Bỏ qua gửi notification.");
            return;
        }

        if (tokens == null || tokens.isEmpty()) {
            log.warn("Danh sách token rỗng. Bỏ qua gửi notification.");
            return;
        }

        // Lọc bỏ token null/empty
        List<String> validTokens = tokens.stream()
                .filter(t -> t != null && !t.isBlank())
                .toList();

        if (validTokens.isEmpty()) {
            return;
        }

        try {
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            MulticastMessage.Builder messageBuilder = MulticastMessage.builder()
                    .addAllTokens(validTokens)
                    .setNotification(notification);

            if (data != null && !data.isEmpty()) {
                messageBuilder.putAllData(data);
            }

            BatchResponse response = firebaseMessaging.sendEachForMulticast(messageBuilder.build());
            log.info("Gửi FCM multicast: {} thành công, {} thất bại",
                    response.getSuccessCount(), response.getFailureCount());

            // Log các token lỗi
            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();
                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        log.warn("Lỗi gửi tới token {}: {}",
                                validTokens.get(i),
                                responses.get(i).getException().getMessage());
                    }
                }
            }
        } catch (FirebaseMessagingException e) {
            log.error("Lỗi gửi FCM multicast: {}", e.getMessage());
        }
    }

    @Async
    public void sendDataMessage(String token, Map<String, String> data) {
        if (firebaseMessaging == null || token == null || token.isBlank()) {
            return;
        }

        try {
            Message message = Message.builder()
                    .setToken(token)
                    .putAllData(data)
                    .build();

            String response = firebaseMessaging.send(message);
            log.info("Gửi FCM data message thành công: {}", response);
        } catch (FirebaseMessagingException e) {
            log.error("Lỗi gửi FCM data message: {}", e.getMessage());
        }
    }

    private void handleFCMError(FirebaseMessagingException e, String token) {
        MessagingErrorCode errorCode = e.getMessagingErrorCode();
        if (errorCode == MessagingErrorCode.UNREGISTERED || 
            errorCode == MessagingErrorCode.INVALID_ARGUMENT) {
            log.warn("Token không hợp lệ hoặc đã hết hạn: {}. Cần xóa token này.", token);
            // TODO: Có thể emit event để xóa token không hợp lệ khỏi database
        }
    }
}
