package hust.project.freshfridge.domain.repository;

import hust.project.freshfridge.domain.entity.GroupMember;
import java.util.List;
import java.util.Optional;

public interface IGroupMemberRepository {
    GroupMember save(GroupMember member);
    Optional<GroupMember> findByUserId(Long userId);
    Optional<GroupMember> findByGroupIdAndUserId(Long groupId, Long userId);
    List<GroupMember> findByGroupId(Long groupId);
    boolean existsByUserId(Long userId);
    void deleteByGroupIdAndUserId(Long groupId, Long userId);
    void deleteByGroupId(Long groupId);
}
