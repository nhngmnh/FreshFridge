package hust.project.freshfridge.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingListResponse {
    private Long id;
    private String name;
    private Long groupId;
    private Long assignedToUserId;
    private String assignedToEmail;
    private String assignedToName;
    private String note;
    private LocalDate shoppingDate;
    private String status;
    private List<ShoppingItemResponse> items;
    private int totalItems;
    private int purchasedItems;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
