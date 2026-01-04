package hust.project.freshfridge.application.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Long id;
    private String title;
    private String message;
    private String type;
    private String referenceType;
    private Long referenceId;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
