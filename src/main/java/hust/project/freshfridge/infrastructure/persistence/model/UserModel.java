package hust.project.freshfridge.infrastructure.persistence.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String phone;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(nullable = false)
    private String role;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
