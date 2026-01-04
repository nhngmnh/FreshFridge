package hust.project.freshfridge.infrastructure.persistence.repository.jpa;

import hust.project.freshfridge.infrastructure.persistence.model.GroupMemberModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberJpaRepository extends JpaRepository<GroupMemberModel, Long> {
    Optional<GroupMemberModel> findByUserId(Long userId);
    Optional<GroupMemberModel> findByGroupIdAndUserId(Long groupId, Long userId);
    List<GroupMemberModel> findByGroupId(Long groupId);
    boolean existsByUserId(Long userId);
    
    @Modifying
    void deleteByGroupIdAndUserId(Long groupId, Long userId);
    
    @Modifying
    void deleteByGroupId(Long groupId);
}
