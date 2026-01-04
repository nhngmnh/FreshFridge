package hust.project.freshfridge.domain.entity;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealPlan {
    private Long id;
    private Long groupId;
    private Long recipeId;
    private LocalDate mealDate;
    private String mealName;
    private Integer servings;
    private String note;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
