package hust.project.freshfridge.infrastructure.persistence.repository.jpa;

import hust.project.freshfridge.infrastructure.persistence.model.EmailVerificationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationJpaRepository extends JpaRepository<EmailVerificationModel, Long> {
    Optional<EmailVerificationModel> findByUserIdAndCode(Long userId, String code);

    @Query("SELECT e FROM EmailVerificationModel e WHERE e.userId = :userId ORDER BY e.createdAt DESC LIMIT 1")
    Optional<EmailVerificationModel> findLatestByUserId(@Param("userId") Long userId);

    void deleteByUserId(Long userId);
}
