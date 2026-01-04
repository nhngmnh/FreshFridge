package hust.project.freshfridge.domain.repository;

import hust.project.freshfridge.domain.entity.Group;
import java.util.List;
import java.util.Optional;

public interface IGroupRepository {
    Group save(Group group);
    Optional<Group> findById(Long id);
    Optional<Group> findByAdminId(Long adminId);
    List<Group> findAll();
    void deleteById(Long id);
}
