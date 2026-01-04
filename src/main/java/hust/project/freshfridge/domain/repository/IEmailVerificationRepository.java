package hust.project.freshfridge.domain.repository;

import hust.project.freshfridge.domain.entity.EmailVerification;
import java.util.Optional;

public interface IEmailVerificationRepository {
    EmailVerification save(EmailVerification verification);
    Optional<EmailVerification> findByUserIdAndCode(Long userId, String code);
    Optional<EmailVerification> findLatestByUserId(Long userId);
    void deleteByUserId(Long userId);
}
