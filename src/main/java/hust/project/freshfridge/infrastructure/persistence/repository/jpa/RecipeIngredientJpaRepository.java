package hust.project.freshfridge.infrastructure.persistence.repository.jpa;

import hust.project.freshfridge.infrastructure.persistence.model.RecipeIngredientModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeIngredientJpaRepository extends JpaRepository<RecipeIngredientModel, Long> {
    List<RecipeIngredientModel> findByRecipeId(Long recipeId);
    List<RecipeIngredientModel> findByFoodId(Long foodId);
    List<RecipeIngredientModel> findByFoodIdAndIsMainIngredientTrue(Long foodId);
    void deleteByRecipeId(Long recipeId);
}
