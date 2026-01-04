package hust.project.freshfridge.application.dto.response.report;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealReportResponse {
    // Thống kê bữa ăn
    private MealStats mealStats;

    // Thống kê sử dụng công thức
    private RecipeUsage recipeUsage;

    // Top công thức hay dùng
    private List<TopRecipe> topRecipes;

    // Xu hướng bữa ăn theo tuần
    private List<WeeklyMealItem> weeklyMealTrend;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealStats {
        private int totalMeals;
        private int breakfasts;
        private int lunches;
        private int dinners;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecipeUsage {
        private int totalRecipesUsed;
        private int uniqueRecipes;
        private double usagePercentage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopRecipe {
        private Long recipeId;
        private String recipeName;
        private String imageUrl;
        private int usageCount;
        private String lastUsedDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeeklyMealItem {
        private String date;
        private String dayOfWeek;
        private int breakfastCount;
        private int lunchCount;
        private int dinnerCount;
        private int totalMeals;
    }
}
