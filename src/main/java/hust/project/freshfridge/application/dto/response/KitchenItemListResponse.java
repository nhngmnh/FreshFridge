package hust.project.freshfridge.application.dto.response;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KitchenItemListResponse {
    private List<KitchenItemResponse> items;
    private int total;
    private int expiringCount;          // Số item sắp hết hạn (trong 3 ngày)
    private int expiredCount;           // Số item đã hết hạn
}
