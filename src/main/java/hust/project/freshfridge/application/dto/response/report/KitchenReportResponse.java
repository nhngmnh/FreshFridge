package hust.project.freshfridge.application.dto.response.report;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KitchenReportResponse {
    // Thống kê theo trạng thái
    private StatusBreakdown statusBreakdown;

    // Xu hướng theo tuần (7 ngày gần nhất)
    private List<WeeklyTrendItem> weeklyTrend;

    // Giá trị theo category
    private List<CategoryValue> categoryValues;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusBreakdown {
        private int totalItems;
        private int goodItems;
        private int warningItems;
        private int expiredItems;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeeklyTrendItem {
        private String date;
        private String dayOfWeek;
        private int itemsAdded;
        private int itemsUsed;
        private int itemsExpired;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryValue {
        private Long categoryId;
        private String name;
        private int itemCount;
    }
}
