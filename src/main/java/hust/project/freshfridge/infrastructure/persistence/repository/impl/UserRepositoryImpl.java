package hust.project.freshfridge.infrastructure.persistence.repository.impl;

import hust.project.freshfridge.domain.entity.User;
import hust.project.freshfridge.domain.repository.IUserRepository;
import hust.project.freshfridge.infrastructure.persistence.mapper.UserMapper;
import hust.project.freshfridge.infrastructure.persistence.repository.jpa.UserJpaRepository;
import hust.project.freshfridge.infrastructure.persistence.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements IUserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserMapper mapper;

    @Override
    public User save(User user) {
        var model = mapper.toModel(user);
        var saved = jpaRepository.save(model);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        return jpaRepository.findByPhone(phone).map(mapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findByFilters(String email, String name, String role, Boolean isActive) {
        var spec = UserSpecification.buildSpec(email, name, role, isActive);
        return jpaRepository.findAll(spec).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return jpaRepository.existsByPhone(phone);
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public long countByIsActive(Boolean isActive) {
        return jpaRepository.countByIsActive(isActive);
    }

    @Override
    public long countByIsVerified(Boolean isVerified) {
        return jpaRepository.countByIsVerified(isVerified);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void updateFcmToken(Long userId, String fcmToken) {
        jpaRepository.updateFcmToken(userId, fcmToken);
    }
}
