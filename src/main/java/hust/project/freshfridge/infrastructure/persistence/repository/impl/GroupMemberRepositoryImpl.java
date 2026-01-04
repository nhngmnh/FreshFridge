package hust.project.freshfridge.infrastructure.persistence.repository.impl;

import hust.project.freshfridge.domain.entity.GroupMember;
import hust.project.freshfridge.domain.repository.IGroupMemberRepository;
import hust.project.freshfridge.infrastructure.persistence.mapper.GroupMemberMapper;
import hust.project.freshfridge.infrastructure.persistence.model.GroupMemberModel;
import hust.project.freshfridge.infrastructure.persistence.repository.jpa.GroupMemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GroupMemberRepositoryImpl implements IGroupMemberRepository {

    private final GroupMemberJpaRepository groupMemberJpaRepository;
    private final GroupMemberMapper groupMemberMapper;

    @Override
    public GroupMember save(GroupMember member) {
        GroupMemberModel model = groupMemberMapper.toModel(member);
        GroupMemberModel savedModel = groupMemberJpaRepository.save(model);
        return groupMemberMapper.toDomain(savedModel);
    }

    @Override
    public Optional<GroupMember> findByUserId(Long userId) {
        return groupMemberJpaRepository.findByUserId(userId)
                .map(groupMemberMapper::toDomain);
    }

    @Override
    public Optional<GroupMember> findByGroupIdAndUserId(Long groupId, Long userId) {
        return groupMemberJpaRepository.findByGroupIdAndUserId(groupId, userId)
                .map(groupMemberMapper::toDomain);
    }

    @Override
    public List<GroupMember> findByGroupId(Long groupId) {
        return groupMemberJpaRepository.findByGroupId(groupId).stream()
                .map(groupMemberMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return groupMemberJpaRepository.existsByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteByGroupIdAndUserId(Long groupId, Long userId) {
        groupMemberJpaRepository.deleteByGroupIdAndUserId(groupId, userId);
    }

    @Override
    @Transactional
    public void deleteByGroupId(Long groupId) {
        groupMemberJpaRepository.deleteByGroupId(groupId);
    }
}
