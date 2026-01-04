package hust.project.freshfridge.application.dto.response;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryListResponse {
    private List<CategoryResponse> categories;
    private int total;
}
