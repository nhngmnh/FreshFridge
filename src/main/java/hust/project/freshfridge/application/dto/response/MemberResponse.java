package hust.project.freshfridge.application.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private Long userId;
    private String name;
    private String email;
    private String avatarUrl;
    private Boolean isAdmin;
    private LocalDateTime joinedAt;
}
