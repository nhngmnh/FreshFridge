package hust.project.freshfridge.domain.entity;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KitchenItem {
    private Long id;
    private Long foodId;
    private Long groupId;
    private Long unitId;
    private BigDecimal quantity;
    private Integer useWithin;          // Số ngày
    private LocalDate expiryDate;
    private String note;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
