package hust.project.freshfridge.domain.repository;

import hust.project.freshfridge.domain.entity.Food;
import java.util.List;
import java.util.Optional;

public interface IFoodRepository {
    Food save(Food food);
    Optional<Food> findById(Long id);
    Optional<Food> findByNameAndGroupId(String name, Long groupId);
    List<Food> findAll(Long groupId, Long categoryId, String name);
    List<Food> findSystemFoods(Long categoryId, String name);
    boolean existsByNameAndGroupId(String name, Long groupId);
    boolean existsSystemFoodByName(String name);
    void deleteById(Long id);
}
