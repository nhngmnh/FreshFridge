package hust.project.freshfridge.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RemoveMemberRequest {
    @NotBlank(message = "Email không được để trống")
    private String email;
}
