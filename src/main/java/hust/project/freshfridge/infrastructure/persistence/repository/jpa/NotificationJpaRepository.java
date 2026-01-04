package hust.project.freshfridge.infrastructure.persistence.repository.jpa;

import hust.project.freshfridge.infrastructure.persistence.model.NotificationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationJpaRepository extends JpaRepository<NotificationModel, Long> {
    List<NotificationModel> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    List<NotificationModel> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);

    @Query("SELECT COUNT(n) FROM NotificationModel n WHERE n.userId = :userId AND n.isRead = false")
    int countUnreadByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE NotificationModel n SET n.isRead = true WHERE n.id = :id")
    void markAsRead(@Param("id") Long id);

    @Modifying
    @Query("UPDATE NotificationModel n SET n.isRead = true WHERE n.userId = :userId")
    void markAllAsRead(@Param("userId") Long userId);

    void deleteByUserId(Long userId);
}
