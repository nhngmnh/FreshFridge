package hust.project.freshfridge.infrastructure.persistence.repository.jpa;

import hust.project.freshfridge.infrastructure.persistence.model.SystemLogModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SystemLogJpaRepository extends JpaRepository<SystemLogModel, Long> {
    List<SystemLogModel> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    List<SystemLogModel> findByAction(String action);

    @Query("SELECT l FROM SystemLogModel l WHERE l.createdAt BETWEEN :start AND :end ORDER BY l.createdAt DESC")
    List<SystemLogModel> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT l FROM SystemLogModel l ORDER BY l.createdAt DESC")
    List<SystemLogModel> findAllOrderByCreatedAtDesc(Pageable pageable);
}
