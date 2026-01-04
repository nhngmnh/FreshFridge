package hust.project.freshfridge.application.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodResponse {
    private Long id;
    private String name;
    private Long categoryId;
    private String categoryName;
    private String imageUrl;
    private Long groupId;
    private Boolean isSystemFood;
    private LocalDateTime createdAt;
}
