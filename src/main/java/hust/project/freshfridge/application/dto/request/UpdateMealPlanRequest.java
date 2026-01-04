package hust.project.freshfridge.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UpdateMealPlanRequest {
    private Long recipeId;

    private LocalDate mealDate;

    private String mealName;

    @Min(value = 1)
    private Integer servings;

    private String note;
}
