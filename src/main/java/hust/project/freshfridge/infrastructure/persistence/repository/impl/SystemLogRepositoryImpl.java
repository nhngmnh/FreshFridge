package hust.project.freshfridge.infrastructure.persistence.repository.impl;

import hust.project.freshfridge.domain.entity.SystemLog;
import hust.project.freshfridge.domain.repository.ISystemLogRepository;
import hust.project.freshfridge.infrastructure.persistence.mapper.SystemLogMapper;
import hust.project.freshfridge.infrastructure.persistence.model.SystemLogModel;
import hust.project.freshfridge.infrastructure.persistence.repository.jpa.SystemLogJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SystemLogRepositoryImpl implements ISystemLogRepository {

    private final SystemLogJpaRepository systemLogJpaRepository;
    private final SystemLogMapper systemLogMapper;

    @Override
    public SystemLog save(SystemLog log) {
        SystemLogModel model = systemLogMapper.toModel(log);
        SystemLogModel savedModel = systemLogJpaRepository.save(model);
        return systemLogMapper.toDomain(savedModel);
    }

    @Override
    public List<SystemLog> findByUserId(Long userId) {
        return systemLogJpaRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(systemLogMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<SystemLog> findByAction(String action) {
        return systemLogJpaRepository.findByAction(action).stream()
                .map(systemLogMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<SystemLog> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return systemLogJpaRepository.findByDateRange(start, end).stream()
                .map(systemLogMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<SystemLog> findAll(int page, int size) {
        return systemLogJpaRepository.findAllOrderByCreatedAtDesc(PageRequest.of(page, size)).stream()
                .map(systemLogMapper::toDomain)
                .collect(Collectors.toList());
    }
}
