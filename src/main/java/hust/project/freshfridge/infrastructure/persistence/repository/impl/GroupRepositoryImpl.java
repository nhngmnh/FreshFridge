package hust.project.freshfridge.infrastructure.persistence.repository.impl;

import hust.project.freshfridge.domain.entity.Group;
import hust.project.freshfridge.domain.repository.IGroupRepository;
import hust.project.freshfridge.infrastructure.persistence.mapper.GroupMapper;
import hust.project.freshfridge.infrastructure.persistence.model.GroupModel;
import hust.project.freshfridge.infrastructure.persistence.repository.jpa.GroupJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GroupRepositoryImpl implements IGroupRepository {

    private final GroupJpaRepository groupJpaRepository;
    private final GroupMapper groupMapper;

    @Override
    public Group save(Group group) {
        GroupModel model = groupMapper.toModel(group);
        GroupModel savedModel = groupJpaRepository.save(model);
        return groupMapper.toDomain(savedModel);
    }

    @Override
    public Optional<Group> findById(Long id) {
        return groupJpaRepository.findById(id)
                .map(groupMapper::toDomain);
    }

    @Override
    public Optional<Group> findByAdminId(Long adminId) {
        return groupJpaRepository.findByAdminId(adminId)
                .map(groupMapper::toDomain);
    }

    @Override
    public List<Group> findAll() {
        return groupJpaRepository.findAll().stream()
                .map(groupMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        groupJpaRepository.deleteById(id);
    }
}
