package hust.project.freshfridge.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class AddRecipeIngredientRequest {
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
