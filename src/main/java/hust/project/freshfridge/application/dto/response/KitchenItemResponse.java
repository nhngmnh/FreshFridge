package hust.project.freshfridge.application.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KitchenItemResponse {
    private Long id;
    private Long foodId;
    private String foodName;
    private String categoryName;
    private Long unitId;
    private String unitName;
    private String foodImageUrl;
    private BigDecimal quantity;
    private Integer useWithin;
    private LocalDate expiryDate;
    private Integer daysUntilExpiry;
    private String expiryStatus;        // FRESH, EXPIRING_SOON, EXPIRED
    private String note;
    private Long groupId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
