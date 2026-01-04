package hust.project.freshfridge.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingItemResponse {
    private Long id;
    private Long foodId;
    private String foodName;
    private String categoryName;
    private Long unitId;
    private String unitName;
    private BigDecimal quantity;
    private BigDecimal actualQuantity;
    private Boolean isPurchased;
}
