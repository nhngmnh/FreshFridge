package hust.project.freshfridge.application.dto.response;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyMealPlanResponse {
    private LocalDate date;
    private List<MealPlanResponse> breakfast;
    private List<MealPlanResponse> lunch;
    private List<MealPlanResponse> dinner;
}
