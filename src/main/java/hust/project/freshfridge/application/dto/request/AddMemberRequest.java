package hust.project.freshfridge.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddMemberRequest {
    @NotBlank(message = "Email không được để trống")
    private String email;
}
