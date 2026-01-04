package hust.project.freshfridge.infrastructure.persistence.repository.impl;

import hust.project.freshfridge.domain.entity.Notification;
import hust.project.freshfridge.domain.repository.INotificationRepository;
import hust.project.freshfridge.infrastructure.persistence.mapper.NotificationMapper;
import hust.project.freshfridge.infrastructure.persistence.model.NotificationModel;
import hust.project.freshfridge.infrastructure.persistence.repository.jpa.NotificationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements INotificationRepository {

    private final NotificationJpaRepository notificationJpaRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public Notification save(Notification notification) {
        NotificationModel model = notificationMapper.toModel(notification);
        NotificationModel savedModel = notificationJpaRepository.save(model);
        return notificationMapper.toDomain(savedModel);
    }

    @Override
    public List<Notification> saveAll(List<Notification> notifications) {
        List<NotificationModel> models = notifications.stream()
                .map(notificationMapper::toModel)
                .collect(Collectors.toList());
        List<NotificationModel> savedModels = notificationJpaRepository.saveAll(models);
        return savedModels.stream()
                .map(notificationMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationJpaRepository.findById(id)
                .map(notificationMapper::toDomain);
    }

    @Override
    public List<Notification> findByUserId(Long userId) {
        return notificationJpaRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(notificationMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification> findUnreadByUserId(Long userId) {
        return notificationJpaRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId).stream()
                .map(notificationMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public int countUnreadByUserId(Long userId) {
        return notificationJpaRepository.countUnreadByUserId(userId);
    }

    @Override
    @Transactional
    public void markAsRead(Long id) {
        notificationJpaRepository.markAsRead(id);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationJpaRepository.markAllAsRead(userId);
    }

    @Override
    public void deleteById(Long id) {
        notificationJpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByUserId(Long userId) {
        notificationJpaRepository.deleteByUserId(userId);
    }
}
