package hust.project.freshfridge.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateFcmTokenRequest {
    @NotBlank(message = "FCM token không được trống")
    private String fcmToken;
}
