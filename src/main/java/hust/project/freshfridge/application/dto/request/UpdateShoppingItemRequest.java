package hust.project.freshfridge.application.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateShoppingItemRequest {
    private Long foodId;

    private Long unitId;

    @Positive(message = "Số lượng phải lớn hơn 0")
    private BigDecimal quantity;
}
