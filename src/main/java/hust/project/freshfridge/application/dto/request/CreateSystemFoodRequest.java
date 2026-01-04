package hust.project.freshfridge.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateSystemFoodRequest {
    @NotBlank(message = "Tên thực phẩm không được để trống")
    @Size(min = 1, max = 100, message = "Tên thực phẩm phải từ 1-100 ký tự")
    private String name;

    @NotNull(message = "Danh mục không được để trống")
    private Long categoryId;

    private MultipartFile image;
}
