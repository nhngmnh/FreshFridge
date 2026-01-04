package hust.project.freshfridge.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MarkItemPurchasedRequest {
    @NotNull(message = "Số lượng thực tế không được để trống")
    @PositiveOrZero(message = "Số lượng thực tế phải >= 0")
    private BigDecimal actualQuantity;
}
