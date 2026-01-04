package hust.project.freshfridge.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateShoppingListRequest {
    @NotBlank(message = "Tên danh sách không được để trống")
    @Size(max = 100, message = "Tên danh sách tối đa 100 ký tự")
    private String name;

    @NotNull(message = "ID người được giao không được để trống")
    private Long assignedToUserId;

    private String note;

    private LocalDate shoppingDate;
}
