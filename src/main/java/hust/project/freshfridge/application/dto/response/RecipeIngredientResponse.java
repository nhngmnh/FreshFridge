package hust.project.freshfridge.application.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredientResponse {
    private Long id;
    private Long foodId;
    private String foodName;
    private String foodImageUrl;
    private BigDecimal quantity;
    private Long unitId;
    private String unitName;
    private Boolean isMainIngredient;
    private String note;
    private Boolean isAvailable;
}
