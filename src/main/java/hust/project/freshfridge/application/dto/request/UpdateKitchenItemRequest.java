package hust.project.freshfridge.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class UpdateKitchenItemRequest {
    @Positive(message = "Số lượng phải lớn hơn 0")
    private BigDecimal quantity;

    private Long unitId;

    @Min(value = 1, message = "Số ngày sử dụng phải ít nhất là 1")
    private Integer useWithin;

    private String note;

    private Long foodId;         // Đổi sang thực phẩm khác
}
