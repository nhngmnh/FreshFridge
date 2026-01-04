package hust.project.freshfridge.domain.repository;

import hust.project.freshfridge.domain.entity.Category;
import java.util.List;
import java.util.Optional;

public interface ICategoryRepository {
    Category save(Category category);
    Optional<Category> findById(Long id);
    Optional<Category> findByName(String name);
    List<Category> findAll();
    void deleteById(Long id);
}
