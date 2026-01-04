package hust.project.freshfridge.application.dto.response;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationListResponse {
    private List<NotificationResponse> notifications;
    private int unreadCount;
    private int total;
}
