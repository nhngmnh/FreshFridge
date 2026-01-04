package hust.project.freshfridge.domain.entity;

import lombok.*;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemLog {
    private Long id;
    private Long userId;
    private String action;
    private String entityType;
    private Long entityId;
    private Map<String, Object> details;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime createdAt;
}
