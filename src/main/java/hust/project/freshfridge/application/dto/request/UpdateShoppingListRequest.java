package hust.project.freshfridge.application.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateShoppingListRequest {
    @Size(max = 100, message = "Tên danh sách tối đa 100 ký tự")
    private String name;

    private Long assignedToUserId;

    private String note;

    private LocalDate shoppingDate;

    private String status;  // PENDING, IN_PROGRESS, COMPLETED
}
