package hust.project.freshfridge.domain.repository;

import hust.project.freshfridge.domain.entity.User;
import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    List<User> findAll();
    List<User> findByFilters(String email, String name, String role, Boolean isActive);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    long count();
    long countByIsActive(Boolean isActive);
    long countByIsVerified(Boolean isVerified);
    void deleteById(Long id);
    void updateFcmToken(Long userId, String fcmToken);
}
