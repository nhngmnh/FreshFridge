package hust.project.freshfridge.infrastructure.persistence.repository.jpa;

import hust.project.freshfridge.infrastructure.persistence.model.MealPlanModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MealPlanJpaRepository extends JpaRepository<MealPlanModel, Long> {

    List<MealPlanModel> findByGroupIdOrderByMealDateDescMealNameAsc(Long groupId);

    List<MealPlanModel> findByGroupIdAndMealDateOrderByMealNameAsc(Long groupId, LocalDate mealDate);

    List<MealPlanModel> findByGroupIdAndMealDateBetweenOrderByMealDateAscMealNameAsc(
            Long groupId, LocalDate startDate, LocalDate endDate);

    List<MealPlanModel> findByGroupIdAndMealDateAndMealNameOrderByIdAsc(
            Long groupId, LocalDate mealDate, String mealName);

    List<MealPlanModel> findByMealDateOrderByGroupIdAscMealNameAsc(LocalDate mealDate);

    boolean existsByGroupIdAndMealDateAndMealNameAndRecipeId(
            Long groupId, LocalDate mealDate, String mealName, Long recipeId);
}
