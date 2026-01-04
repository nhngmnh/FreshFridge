package hust.project.freshfridge.infrastructure.persistence.repository.jpa;

import hust.project.freshfridge.infrastructure.persistence.model.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryJpaRepository extends JpaRepository<CategoryModel, Long> {
    Optional<CategoryModel> findByName(String name);
}
