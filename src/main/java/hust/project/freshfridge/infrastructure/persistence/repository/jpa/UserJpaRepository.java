package hust.project.freshfridge.infrastructure.persistence.repository.jpa;

import hust.project.freshfridge.infrastructure.persistence.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserModel, Long>, JpaSpecificationExecutor<UserModel> {
    Optional<UserModel> findByEmail(String email);
    Optional<UserModel> findByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    long countByIsActive(Boolean isActive);
    long countByIsVerified(Boolean isVerified);

    @Modifying
    @Query("UPDATE UserModel u SET u.fcmToken = :fcmToken WHERE u.id = :userId")
    void updateFcmToken(@Param("userId") Long userId, @Param("fcmToken") String fcmToken);
}
