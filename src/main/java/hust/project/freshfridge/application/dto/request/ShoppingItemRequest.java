package hust.project.freshfridge.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShoppingItemRequest {
    @NotNull(message = "ID thực phẩm không được để trống")
    private Long foodId;

    @NotNull(message = "ID đơn vị không được để trống")
    private Long unitId;

    @NotNull(message = "Số lượng không được để trống")
    @Positive(message = "Số lượng phải lớn hơn 0")
    private BigDecimal quantity;
}
