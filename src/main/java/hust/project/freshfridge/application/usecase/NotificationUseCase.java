package hust.project.freshfridge.application.usecase;

import hust.project.freshfridge.application.dto.response.*;
import hust.project.freshfridge.domain.constant.NotificationType;
import hust.project.freshfridge.domain.entity.Notification;
import hust.project.freshfridge.domain.entity.User;
import hust.project.freshfridge.domain.exception.BusinessException;
import hust.project.freshfridge.domain.exception.ErrorCode;
import hust.project.freshfridge.domain.repository.INotificationRepository;
import hust.project.freshfridge.domain.repository.IUserRepository;
import hust.project.freshfridge.infrastructure.service.FCMService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationUseCase {

    private final INotificationRepository notificationRepository;
    private final IUserRepository userRepository;
    private final FCMService fcmService;

    public NotificationListResponse getNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        int unreadCount = notificationRepository.countUnreadByUserId(userId);

        List<NotificationResponse> responses = notifications.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return NotificationListResponse.builder()
                .notifications(responses)
                .unreadCount(unreadCount)
                .total(responses.size())
                .build();
    }

    @Transactional
    public void markAsRead(Long userId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOTIFICATION_NOT_FOUND));

        if (!notification.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        notificationRepository.markAsRead(notificationId);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsRead(userId);
    }

    @Transactional
    public void deleteNotification(Long userId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOTIFICATION_NOT_FOUND));

        if (!notification.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        notificationRepository.deleteById(notificationId);
    }

    // Helper method to create notification
    public void createNotification(Long userId, NotificationType type, String title, 
                                    String message, String referenceType, Long referenceId) {
        Notification notification = Notification.builder()
                .userId(userId)
                .type(type.name())
                .title(title)
                .message(message)
                .referenceType(referenceType)
                .referenceId(referenceId)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        // Send push notification via FCM
        sendPushNotification(userId, title, message, type.name(), referenceType, referenceId);
    }

    private void sendPushNotification(Long userId, String title, String message, 
                                      String type, String referenceType, Long referenceId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null && user.getFcmToken() != null && !user.getFcmToken().isBlank()) {
            Map<String, String> data = new HashMap<>();
            data.put("type", type);
            data.put("referenceType", referenceType != null ? referenceType : "");
            data.put("referenceId", referenceId != null ? referenceId.toString() : "");

            fcmService.sendNotification(user.getFcmToken(), title, message, data);
        }
    }

    // Create expiry warning notifications
    public void createExpiryWarning(Long userId, String foodName, int daysUntilExpiry, Long kitchenItemId) {
        String title = "Cảnh báo hết hạn";
        String message = String.format("%s sẽ hết hạn trong %d ngày", foodName, daysUntilExpiry);
        createNotification(userId, NotificationType.EXPIRY_WARNING, title, message, "KITCHEN_ITEM", kitchenItemId);
    }

    // Create expired alert
    public void createExpiryAlert(Long userId, String foodName, Long kitchenItemId) {
        String title = "Thực phẩm hết hạn";
        String message = String.format("%s đã hết hạn!", foodName);
        createNotification(userId, NotificationType.EXPIRY_ALERT, title, message, "KITCHEN_ITEM", kitchenItemId);
    }

    // Create group notification
    public void createGroupNotification(Long userId, NotificationType type, String title, String message, Long groupId) {
        createNotification(userId, type, title, message, "GROUP", groupId);
    }

    private NotificationResponse toResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .referenceType(notification.getReferenceType())
                .referenceId(notification.getReferenceId())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
