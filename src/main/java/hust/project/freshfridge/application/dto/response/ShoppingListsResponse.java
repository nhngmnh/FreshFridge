package hust.project.freshfridge.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingListsResponse {
    private List<ShoppingListResponse> lists;
    private int total;
    private int pendingCount;
    private int inProgressCount;
    private int completedCount;
}
