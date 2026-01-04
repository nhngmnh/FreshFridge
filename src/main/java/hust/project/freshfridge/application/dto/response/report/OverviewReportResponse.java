package hust.project.freshfridge.application.dto.response.report;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OverviewReportResponse {
    // Thống kê tủ lạnh tổng quan
    private int totalItems;
    private int expiringItems;      // 1-3 ngày
    private int expiredItems;
    private int goodItems;          // > 3 ngày

    // Phân loại thực phẩm
    private List<CategoryStat> categoryStats;

    // Top thực phẩm phổ biến
    private List<TopItem> topItems;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryStat {
        private Long categoryId;
        private String name;
        private int count;
        private double percentage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopItem {
        private Long foodId;
        private String foodName;
        private int count;
        private Long categoryId;
        private String categoryName;
    }
}
