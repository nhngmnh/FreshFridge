package hust.project.freshfridge.infrastructure.persistence.repository.jpa;

import hust.project.freshfridge.infrastructure.persistence.model.GroupModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupJpaRepository extends JpaRepository<GroupModel, Long> {
    Optional<GroupModel> findByAdminId(Long adminId);
}
