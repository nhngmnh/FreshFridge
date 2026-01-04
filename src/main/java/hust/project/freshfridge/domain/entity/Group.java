package hust.project.freshfridge.domain.entity;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    private Long id;
    private String name;
    private Long adminId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
