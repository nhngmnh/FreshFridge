package hust.project.freshfridge.domain.entity;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingList {
    private Long id;
    private String name;
    private Long groupId;
    private Long assignedToUserId;
    private String note;
    private LocalDate shoppingDate;
    private String status;              // PENDING, IN_PROGRESS, COMPLETED
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
