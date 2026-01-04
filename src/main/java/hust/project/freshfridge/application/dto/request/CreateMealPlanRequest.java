package hust.project.freshfridge.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CreateMealPlanRequest {
    @NotNull(message = "Recipe ID is required")
    private Long recipeId;

    @NotNull(message = "Meal date is required")
    private LocalDate mealDate;

    @NotBlank(message = "Meal name is required")
    private String mealName;

    @Min(value = 1)
    private Integer servings = 1;

    private String note;
}
