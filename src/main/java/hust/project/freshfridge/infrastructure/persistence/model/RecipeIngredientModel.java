package hust.project.freshfridge.infrastructure.persistence.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "recipe_ingredients")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredientModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipe_id", nullable = false)
    private Long recipeId;

    @Column(name = "food_id", nullable = false)
    private Long foodId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "unit_id", nullable = false)
    private Long unitId;

    @Column(name = "is_main_ingredient")
    private Boolean isMainIngredient;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
