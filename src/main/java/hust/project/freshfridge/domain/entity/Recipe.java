package hust.project.freshfridge.domain.entity;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
    private Long id;
    private String name;
    private String description;
    private String htmlContent;
    private String imageUrl;
    private Long groupId;
    private Boolean isSystem;
    private Long createdBy;
    private String difficulty;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
