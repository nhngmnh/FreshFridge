package hust.project.freshfridge.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyEmailRequest {
    @NotBlank(message = "Mã xác thực không được để trống")
    private String code;
}
