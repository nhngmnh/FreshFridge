package hust.project.freshfridge.application.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private String avatarUrl;
    private String role;
    private Boolean isVerified;
    private LocalDateTime createdAt;
}
