package hust.project.freshfridge.infrastructure.persistence.repository.impl;

import hust.project.freshfridge.domain.entity.EmailVerification;
import hust.project.freshfridge.domain.repository.IEmailVerificationRepository;
import hust.project.freshfridge.infrastructure.persistence.mapper.EmailVerificationMapper;
import hust.project.freshfridge.infrastructure.persistence.repository.jpa.EmailVerificationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmailVerificationRepositoryImpl implements IEmailVerificationRepository {

    private final EmailVerificationJpaRepository jpaRepository;
    private final EmailVerificationMapper mapper;

    @Override
    public EmailVerification save(EmailVerification verification) {
        var model = mapper.toModel(verification);
        var saved = jpaRepository.save(model);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<EmailVerification> findByUserIdAndCode(Long userId, String code) {
        return jpaRepository.findByUserIdAndCode(userId, code).map(mapper::toDomain);
    }

    @Override
    public Optional<EmailVerification> findLatestByUserId(Long userId) {
        return jpaRepository.findLatestByUserId(userId).map(mapper::toDomain);
    }

    @Override
    public void deleteByUserId(Long userId) {
        jpaRepository.deleteByUserId(userId);
    }
}
