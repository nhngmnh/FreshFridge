package hust.project.freshfridge.application.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipeResponse {
    private Long id;
    private String name;
    private String description;
    private String htmlContent;
    private String imageUrl;
    private Long groupId;
    private Boolean isSystem;
    private Long createdBy;
    private String difficulty;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private List<String> tags;
    private List<RecipeIngredientResponse> ingredients;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer matchedIngredients;
    private Integer totalIngredients;
}
