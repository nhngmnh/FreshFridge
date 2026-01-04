package hust.project.freshfridge.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class UpdateRecipeIngredientRequest {
    @Positive
    private BigDecimal quantity;

    private Long unitId;

    private Boolean isMainIngredient;

    private String note;
}
