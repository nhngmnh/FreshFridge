package hust.project.freshfridge.infrastructure.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "shopping_lists",
        indexes = {
                @Index(name = "idx_shopping_lists_group_id", columnList = "group_id"),
                @Index(name = "idx_shopping_lists_assigned_to", columnList = "assigned_to_user_id"),
                @Index(name = "idx_shopping_lists_status", columnList = "status")
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingListModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Column(name = "assigned_to_user_id")
    private Long assignedToUserId;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name = "shopping_date")
    private LocalDate shoppingDate;

    @Column(length = 20)
    private String status;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
