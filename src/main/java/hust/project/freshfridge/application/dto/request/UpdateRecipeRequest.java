package hust.project.freshfridge.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class UpdateRecipeRequest {
    @Size(max = 200)
    private String name;

    private String description;

    private String htmlContent;

    @Pattern(regexp = "EASY|MEDIUM|HARD", message = "Difficulty must be EASY, MEDIUM or HARD")
    private String difficulty;

    @Min(value = 0, message = "Prep time must be non-negative")
    private Integer prepTime;

    @Min(value = 0, message = "Cook time must be non-negative")
    private Integer cookTime;

    @Min(value = 1, message = "Servings must be at least 1")
    private Integer servings;

    private List<String> tags;
}
