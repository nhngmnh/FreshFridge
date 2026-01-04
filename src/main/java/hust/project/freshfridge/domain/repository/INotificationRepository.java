package hust.project.freshfridge.domain.repository;

import hust.project.freshfridge.domain.entity.Notification;
import java.util.List;
import java.util.Optional;

public interface INotificationRepository {
    Notification save(Notification notification);
    List<Notification> saveAll(List<Notification> notifications);
    Optional<Notification> findById(Long id);
    List<Notification> findByUserId(Long userId);
    List<Notification> findUnreadByUserId(Long userId);
    int countUnreadByUserId(Long userId);
    void markAsRead(Long id);
    void markAllAsRead(Long userId);
    void deleteById(Long id);
    void deleteByUserId(Long userId);
}
