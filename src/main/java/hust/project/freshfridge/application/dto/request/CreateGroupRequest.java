package hust.project.freshfridge.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateGroupRequest {
    @NotBlank(message = "Tên nhóm không được để trống")
    @Size(min = 1, max = 100, message = "Tên nhóm phải từ 1-100 ký tự")
    private String name;
}
