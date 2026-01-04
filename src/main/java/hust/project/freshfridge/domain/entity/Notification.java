package hust.project.freshfridge.domain.entity;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private Long id;
    private Long userId;
    private String title;
    private String message;
    private String type;
    private String referenceType;
    private Long referenceId;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
