package hust.project.freshfridge.infrastructure.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "shopping_list_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"shopping_list_id", "food_id"}),
        indexes = {
                @Index(name = "idx_shopping_list_items_list_id", columnList = "shopping_list_id"),
                @Index(name = "idx_shopping_list_items_food_id", columnList = "food_id")
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingListItemModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shopping_list_id", nullable = false)
    private Long shoppingListId;

    @Column(name = "food_id", nullable = false)
    private Long foodId;

    @Column(name = "unit_id", nullable = false)
    private Long unitId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "actual_quantity", precision = 10, scale = 2)
    private BigDecimal actualQuantity;

    @Column(name = "is_purchased")
    private Boolean isPurchased;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
