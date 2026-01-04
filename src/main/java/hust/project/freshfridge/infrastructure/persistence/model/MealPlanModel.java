package hust.project.freshfridge.infrastructure.persistence.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "meal_plans", indexes = {
    @Index(name = "idx_meal_plan_group_date", columnList = "group_id, meal_date"),
    @Index(name = "idx_meal_plan_recipe", columnList = "recipe_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealPlanModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Column(name = "recipe_id", nullable = false)
    private Long recipeId;

    @Column(name = "meal_date", nullable = false)
    private LocalDate mealDate;

    @Column(name = "meal_name", nullable = false, length = 20)
    private String mealName;

    @Column(nullable = false)
    private Integer servings;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
