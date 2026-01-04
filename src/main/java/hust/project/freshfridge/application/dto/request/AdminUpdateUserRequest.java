package hust.project.freshfridge.application.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateUserRequest {
    private String name;
    private String phone;
    private String role;
    private Boolean isActive;
    private Boolean isVerified;
}
