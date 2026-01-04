package hust.project.freshfridge.domain.repository;

import hust.project.freshfridge.domain.entity.Recipe;
import java.util.List;
import java.util.Optional;

public interface IRecipeRepository {
    Recipe save(Recipe recipe);
    Optional<Recipe> findById(Long id);
    List<Recipe> findByGroupId(Long groupId);
    List<Recipe> findSystemRecipes();
    List<Recipe> findByGroupIdOrSystem(Long groupId);
    List<Recipe> searchByName(String name, Long groupId);
    List<Recipe> findAllWithFilters(Long groupId, String name);
    void deleteById(Long id);
}
