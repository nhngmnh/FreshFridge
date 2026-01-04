package hust.project.freshfridge.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateKitchenItemRequest {
    @NotNull(message = "Food ID không được để trống")
    private Long foodId;

    @NotNull(message = "Unit ID không được để trống")
    private Long unitId;

    @NotNull(message = "Số lượng không được để trống")
    @Positive(message = "Số lượng phải lớn hơn 0")
    private BigDecimal quantity;

    @Min(value = 1, message = "Số ngày sử dụng phải ít nhất là 1")
    private Integer useWithin;          // Số ngày sử dụng

    private String note;
}
