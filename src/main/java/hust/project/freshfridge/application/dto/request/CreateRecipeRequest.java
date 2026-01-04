package hust.project.freshfridge.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateRecipeRequest {
    @NotBlank(message = "Recipe name is required")
    @Size(max = 200)
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    private String htmlContent;

    @Pattern(regexp = "EASY|MEDIUM|HARD", message = "Difficulty must be EASY, MEDIUM or HARD")
    private String difficulty = "MEDIUM";

    @Min(value = 0, message = "Prep time must be non-negative")
    private Integer prepTime = 0;

    @Min(value = 0, message = "Cook time must be non-negative")
    private Integer cookTime = 0;

    @Min(value = 1, message = "Servings must be at least 1")
    private Integer servings = 2;

    private List<String> tags;

    @Valid
    private List<IngredientItem> ingredients;

    @Data
    public static class IngredientItem {
        @NotNull(message = "Food ID is required")
        private Long foodId;

        @NotNull(message = "Quantity is required")
        @Positive
        private BigDecimal quantity;

        @NotNull(message = "Unit ID is required")
        private Long unitId;

        private Boolean isMainIngredient = false;

        private String note;
    }
}
