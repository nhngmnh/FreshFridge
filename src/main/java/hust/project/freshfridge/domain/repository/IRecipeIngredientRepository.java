package hust.project.freshfridge.domain.repository;

import hust.project.freshfridge.domain.entity.RecipeIngredient;
import java.util.List;
import java.util.Optional;

public interface IRecipeIngredientRepository {
    RecipeIngredient save(RecipeIngredient ingredient);
    List<RecipeIngredient> saveAll(List<RecipeIngredient> ingredients);
    Optional<RecipeIngredient> findById(Long id);
    List<RecipeIngredient> findByRecipeId(Long recipeId);
    List<RecipeIngredient> findByFoodId(Long foodId);
    List<RecipeIngredient> findMainIngredientsByFoodId(Long foodId);
    void deleteByRecipeId(Long recipeId);
    void deleteById(Long id);
}
