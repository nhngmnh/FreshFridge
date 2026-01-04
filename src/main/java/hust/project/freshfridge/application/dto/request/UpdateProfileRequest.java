package hust.project.freshfridge.application.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateProfileRequest {
    private String name;
    private String phone;
    private MultipartFile avatar;
}
