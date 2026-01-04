package hust.project.freshfridge.domain.entity;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Food {
    private Long id;
    private String name;
    private Long categoryId;
    private Long groupId;           // null = system food, not null = group food
    private String imageUrl;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
