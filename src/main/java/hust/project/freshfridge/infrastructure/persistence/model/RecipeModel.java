package hust.project.freshfridge.infrastructure.persistence.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "html_content", columnDefinition = "TEXT")
    private String htmlContent;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "is_system")
    private Boolean isSystem;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(length = 20)
    private String difficulty;

    @Column(name = "prep_time")
    private Integer prepTime;

    @Column(name = "cook_time")
    private Integer cookTime;

    @Column
    private Integer servings;

    @Column(columnDefinition = "TEXT[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> tags;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
