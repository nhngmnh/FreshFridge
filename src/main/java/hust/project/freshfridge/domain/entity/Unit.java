package hust.project.freshfridge.domain.entity;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Unit {
    private Long id;
    private String name;
    private String abbreviation;
    private String description;
    private LocalDateTime createdAt;
}
