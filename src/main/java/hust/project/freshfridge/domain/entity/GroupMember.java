package hust.project.freshfridge.domain.entity;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMember {
    private Long id;
    private Long groupId;
    private Long userId;
    private LocalDateTime joinedAt;
}
