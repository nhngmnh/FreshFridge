package hust.project.freshfridge.application.dto.response;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealPlanResponse {
    private Long id;
    private Long recipeId;
    private String recipeName;
    private String recipeDescription;
    private String recipeImageUrl;
    private LocalDate mealDate;
    private String mealName;
    private String mealDisplayName;
    private Integer servings;
    private String note;
    private Long groupId;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
