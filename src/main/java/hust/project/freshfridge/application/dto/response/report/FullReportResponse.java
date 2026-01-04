package hust.project.freshfridge.application.dto.response.report;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FullReportResponse {
    private OverviewReportResponse overview;
    private KitchenReportResponse kitchen;
    private ShoppingReportResponse shopping;
    private MealReportResponse meal;
    private String period;
}
