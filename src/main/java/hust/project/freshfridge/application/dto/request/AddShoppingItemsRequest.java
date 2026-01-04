package hust.project.freshfridge.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AddShoppingItemsRequest {
    @NotEmpty(message = "Danh sách items không được để trống")
    @Valid
    private List<ShoppingItemRequest> items;
}
