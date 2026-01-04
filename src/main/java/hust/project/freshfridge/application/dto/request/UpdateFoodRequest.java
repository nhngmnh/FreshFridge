package hust.project.freshfridge.application.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateFoodRequest {
    @Size(min = 1, max = 100, message = "Tên thực phẩm phải từ 1-100 ký tự")
    private String name;

    private Long categoryId;

    private MultipartFile image;
}
