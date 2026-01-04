package hust.project.freshfridge.domain.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingListItem {
    private Long id;
    private Long shoppingListId;
    private Long foodId;
    private Long unitId;
    private BigDecimal quantity;
    private BigDecimal actualQuantity;
    private Boolean isPurchased;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
