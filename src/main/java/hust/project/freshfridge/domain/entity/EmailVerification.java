package hust.project.freshfridge.domain.entity;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerification {
    private Long id;
    private Long userId;
    private String code;
    private LocalDateTime expiresAt;
    private Boolean isUsed;
    private LocalDateTime createdAt;
}
