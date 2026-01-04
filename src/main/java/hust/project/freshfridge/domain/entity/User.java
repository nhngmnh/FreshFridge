package hust.project.freshfridge.domain.entity;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String avatarUrl;
    private String role;
    private Boolean isVerified;
    private Boolean isActive;
    private String fcmToken;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
