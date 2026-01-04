package hust.project.freshfridge.application.dto.response;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodListResponse {
    private List<FoodResponse> foods;
    private int total;
}
