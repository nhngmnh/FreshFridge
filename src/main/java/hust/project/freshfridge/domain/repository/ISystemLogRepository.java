package hust.project.freshfridge.domain.repository;

import hust.project.freshfridge.domain.entity.SystemLog;
import java.time.LocalDateTime;
import java.util.List;

public interface ISystemLogRepository {
    SystemLog save(SystemLog log);
    List<SystemLog> findByUserId(Long userId);
    List<SystemLog> findByAction(String action);
    List<SystemLog> findByDateRange(LocalDateTime start, LocalDateTime end);
    List<SystemLog> findAll(int page, int size);
}
