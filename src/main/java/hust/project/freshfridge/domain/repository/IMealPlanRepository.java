package hust.project.freshfridge.domain.repository;

import hust.project.freshfridge.domain.entity.MealPlan;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IMealPlanRepository {
    MealPlan save(MealPlan plan);
    Optional<MealPlan> findById(Long id);
    List<MealPlan> findByGroupId(Long groupId);
    List<MealPlan> findByGroupIdAndMealDate(Long groupId, LocalDate date);
    List<MealPlan> findByGroupIdAndMealDateBetween(Long groupId, LocalDate startDate, LocalDate endDate);
    List<MealPlan> findByGroupIdAndMealDateAndMealName(Long groupId, LocalDate date, String mealName);
    List<MealPlan> findByMealDate(LocalDate date);
    void deleteById(Long id);
}
