package hust.project.freshfridge.domain.entity;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredient {
    private Long id;
    private Long recipeId;
    private Long foodId;
    private BigDecimal quantity;
    private Long unitId;
    private Boolean isMainIngredient;
    private String note;
    private LocalDateTime createdAt;
}
