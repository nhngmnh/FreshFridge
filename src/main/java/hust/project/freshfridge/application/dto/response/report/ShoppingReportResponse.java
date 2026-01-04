package hust.project.freshfridge.application.dto.response.report;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingReportResponse {
    // Thống kê danh sách mua sắm
    private ListStats listStats;

    // Chi tiêu theo tuần
    private List<WeeklySpendingItem> weeklySpending;

    // Top món hay mua
    private List<FrequentItem> frequentItems;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListStats {
        private int totalLists;
        private int pendingLists;
        private int inProgressLists;
        private int completedLists;
        private int totalItemsBought;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeeklySpendingItem {
        private String date;
        private String dayOfWeek;
        private int itemCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FrequentItem {
        private Long foodId;
        private String foodName;
        private int purchaseCount;
        private BigDecimal totalQuantity;
        private String unitName;
    }
}
