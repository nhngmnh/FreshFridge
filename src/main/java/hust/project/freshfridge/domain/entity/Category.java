package hust.project.freshfridge.domain.entity;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private LocalDateTime createdAt;
}
