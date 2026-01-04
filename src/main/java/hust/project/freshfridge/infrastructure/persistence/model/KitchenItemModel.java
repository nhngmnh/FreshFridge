package hust.project.freshfridge.infrastructure.persistence.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "kitchen_items",
       uniqueConstraints = @UniqueConstraint(columnNames = {"food_id", "group_id"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KitchenItemModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "food_id", nullable = false)
    private Long foodId;

    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Column(name = "unit_id", nullable = false)
    private Long unitId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "use_within")
    private Integer useWithin;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
