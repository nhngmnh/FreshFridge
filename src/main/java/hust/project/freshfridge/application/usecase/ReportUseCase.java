package hust.project.freshfridge.application.usecase;

import hust.project.freshfridge.application.dto.response.report.*;
import hust.project.freshfridge.domain.constant.MealType;
import hust.project.freshfridge.domain.constant.ShoppingListStatus;
import hust.project.freshfridge.domain.entity.*;
import hust.project.freshfridge.domain.exception.BusinessException;
import hust.project.freshfridge.domain.exception.ErrorCode;
import hust.project.freshfridge.domain.repository.*;
import hust.project.freshfridge.infrastructure.persistence.repository.jpa.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportUseCase {

    private final IKitchenItemRepository kitchenItemRepository;
    private final IShoppingListRepository shoppingListRepository;
    private final IMealPlanRepository mealPlanRepository;
    private final IRecipeRepository recipeRepository;
    private final IFoodRepository foodRepository;
    private final ICategoryRepository categoryRepository;
    private final IUnitRepository unitRepository;
    private final IGroupMemberRepository groupMemberRepository;

    // JPA repositories for complex queries
    private final KitchenItemJpaRepository kitchenItemJpaRepository;
    private final ShoppingListJpaRepository shoppingListJpaRepository;
    private final ShoppingListItemJpaRepository shoppingListItemJpaRepository;

    private static final int WARNING_DAYS = 3;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Get overview report
     */
    public OverviewReportResponse getOverviewReport(Long userId, String period, Integer topItemsLimit) {
        Long groupId = getGroupIdForUser(userId);
        LocalDate today = LocalDate.now();
        LocalDate warningDate = today.plusDays(WARNING_DAYS);

        // Get all kitchen items for the group
        List<KitchenItem> items = kitchenItemRepository.findByGroupId(groupId);

        // Count by status
        int expiredItems = 0;
        int expiringItems = 0;
        int goodItems = 0;

        for (KitchenItem item : items) {
            if (item.getExpiryDate() == null) {
                goodItems++;
            } else if (item.getExpiryDate().isBefore(today)) {
                expiredItems++;
            } else if (!item.getExpiryDate().isAfter(warningDate)) {
                expiringItems++;
            } else {
                goodItems++;
            }
        }

        // Category stats
        Map<Long, Integer> categoryCount = new HashMap<>();
        for (KitchenItem item : items) {
            Food food = foodRepository.findById(item.getFoodId()).orElse(null);
            if (food != null) {
                categoryCount.merge(food.getCategoryId(), 1, Integer::sum);
            }
        }

        List<OverviewReportResponse.CategoryStat> categoryStats = new ArrayList<>();
        int totalItems = items.size();
        for (Map.Entry<Long, Integer> entry : categoryCount.entrySet()) {
            Category category = categoryRepository.findById(entry.getKey()).orElse(null);
            if (category != null) {
                categoryStats.add(OverviewReportResponse.CategoryStat.builder()
                        .categoryId(category.getId())
                        .name(category.getName())
                        .count(entry.getValue())
                        .percentage(totalItems > 0 ? (double) entry.getValue() / totalItems : 0)
                        .build());
            }
        }
        categoryStats.sort((a, b) -> Integer.compare(b.getCount(), a.getCount()));

        // Top items (most frequent foods)
        Map<Long, Integer> foodCount = new HashMap<>();
        for (KitchenItem item : items) {
            foodCount.merge(item.getFoodId(), 1, Integer::sum);
        }

        int limit = topItemsLimit != null ? topItemsLimit : 5;
        List<OverviewReportResponse.TopItem> topItems = foodCount.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(limit)
                .map(entry -> {
                    Food food = foodRepository.findById(entry.getKey()).orElse(null);
                    Category category = food != null ? categoryRepository.findById(food.getCategoryId()).orElse(null) : null;
                    return OverviewReportResponse.TopItem.builder()
                            .foodId(entry.getKey())
                            .foodName(food != null ? food.getName() : "Unknown")
                            .count(entry.getValue())
                            .categoryId(food != null ? food.getCategoryId() : null)
                            .categoryName(category != null ? category.getName() : null)
                            .build();
                })
                .collect(Collectors.toList());

        return OverviewReportResponse.builder()
                .totalItems(totalItems)
                .expiredItems(expiredItems)
                .expiringItems(expiringItems)
                .goodItems(goodItems)
                .categoryStats(categoryStats)
                .topItems(topItems)
                .build();
    }

    /**
     * Get kitchen report
     */
    public KitchenReportResponse getKitchenReport(Long userId, LocalDate startDate, LocalDate endDate) {
        Long groupId = getGroupIdForUser(userId);
        LocalDate today = LocalDate.now();
        LocalDate warningDate = today.plusDays(WARNING_DAYS);

        if (startDate == null) {
            startDate = today.minusDays(6);
        }
        if (endDate == null) {
            endDate = today;
        }

        // Get all kitchen items
        List<KitchenItem> items = kitchenItemRepository.findByGroupId(groupId);

        // Status breakdown
        int expiredItems = 0;
        int warningItems = 0;
        int goodItems = 0;

        for (KitchenItem item : items) {
            if (item.getExpiryDate() == null) {
                goodItems++;
            } else if (item.getExpiryDate().isBefore(today)) {
                expiredItems++;
            } else if (!item.getExpiryDate().isAfter(warningDate)) {
                warningItems++;
            } else {
                goodItems++;
            }
        }

        KitchenReportResponse.StatusBreakdown statusBreakdown = KitchenReportResponse.StatusBreakdown.builder()
                .totalItems(items.size())
                .goodItems(goodItems)
                .warningItems(warningItems)
                .expiredItems(expiredItems)
                .build();

        // Weekly trend
        List<KitchenReportResponse.WeeklyTrendItem> weeklyTrend = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            LocalDateTime dayStart = date.atStartOfDay();
            LocalDateTime dayEnd = date.plusDays(1).atStartOfDay();

            long itemsAdded = kitchenItemJpaRepository.countItemsAddedBetween(groupId, dayStart, dayEnd);

            weeklyTrend.add(KitchenReportResponse.WeeklyTrendItem.builder()
                    .date(date.format(DATE_FORMATTER))
                    .dayOfWeek(getDayOfWeekVietnamese(date.getDayOfWeek()))
                    .itemsAdded((int) itemsAdded)
                    .itemsUsed(0) // Would need usage tracking to compute
                    .itemsExpired(0) // Would need historical data
                    .build());
        }

        // Category values
        Map<Long, Integer> categoryCount = new HashMap<>();
        for (KitchenItem item : items) {
            Food food = foodRepository.findById(item.getFoodId()).orElse(null);
            if (food != null) {
                categoryCount.merge(food.getCategoryId(), 1, Integer::sum);
            }
        }

        List<KitchenReportResponse.CategoryValue> categoryValues = categoryCount.entrySet().stream()
                .map(entry -> {
                    Category category = categoryRepository.findById(entry.getKey()).orElse(null);
                    return KitchenReportResponse.CategoryValue.builder()
                            .categoryId(entry.getKey())
                            .name(category != null ? category.getName() : "Unknown")
                            .itemCount(entry.getValue())
                            .build();
                })
                .sorted((a, b) -> Integer.compare(b.getItemCount(), a.getItemCount()))
                .collect(Collectors.toList());

        return KitchenReportResponse.builder()
                .statusBreakdown(statusBreakdown)
                .weeklyTrend(weeklyTrend)
                .categoryValues(categoryValues)
                .build();
    }

    /**
     * Get shopping report
     */
    public ShoppingReportResponse getShoppingReport(Long userId, String period, Integer frequentItemsLimit) {
        Long groupId = getGroupIdForUser(userId);
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(6);

        // List stats
        long totalLists = shoppingListJpaRepository.countByGroupId(groupId);
        long pendingLists = shoppingListJpaRepository.countByGroupIdAndStatus(groupId, ShoppingListStatus.PENDING.name());
        long inProgressLists = shoppingListJpaRepository.countByGroupIdAndStatus(groupId, ShoppingListStatus.IN_PROGRESS.name());
        long completedLists = shoppingListJpaRepository.countByGroupIdAndStatus(groupId, ShoppingListStatus.COMPLETED.name());
        long totalItemsBought = shoppingListItemJpaRepository.countPurchasedItemsByGroupId(groupId);

        ShoppingReportResponse.ListStats listStats = ShoppingReportResponse.ListStats.builder()
                .totalLists((int) totalLists)
                .pendingLists((int) pendingLists)
                .inProgressLists((int) inProgressLists)
                .completedLists((int) completedLists)
                .totalItemsBought((int) totalItemsBought)
                .build();

        // Weekly spending (items bought per day)
        List<ShoppingReportResponse.WeeklySpendingItem> weeklySpending = new ArrayList<>();
        List<ShoppingList> allLists = shoppingListRepository.findByGroupId(groupId);
        List<Long> listIds = allLists.stream().map(ShoppingList::getId).collect(Collectors.toList());
        
        // Get all items for these lists
        List<hust.project.freshfridge.infrastructure.persistence.model.ShoppingListItemModel> allItems = 
                listIds.isEmpty() ? Collections.emptyList() : shoppingListItemJpaRepository.findByShoppingListIdIn(listIds);

        for (LocalDate date = startDate; !date.isAfter(today); date = date.plusDays(1)) {
            final LocalDate currentDate = date;
            // Count items from lists created on this date
            int itemCount = 0;
            for (ShoppingList list : allLists) {
                if (list.getCreatedAt() != null && list.getCreatedAt().toLocalDate().equals(currentDate)) {
                    itemCount += (int) allItems.stream()
                            .filter(i -> i.getShoppingListId().equals(list.getId()) && Boolean.TRUE.equals(i.getIsPurchased()))
                            .count();
                }
            }

            weeklySpending.add(ShoppingReportResponse.WeeklySpendingItem.builder()
                    .date(date.format(DATE_FORMATTER))
                    .dayOfWeek(getDayOfWeekVietnamese(date.getDayOfWeek()))
                    .itemCount(itemCount)
                    .build());
        }

        // Frequent items
        Map<Long, Integer> foodPurchaseCount = new HashMap<>();
        Map<Long, java.math.BigDecimal> foodTotalQuantity = new HashMap<>();
        Map<Long, Long> foodUnitId = new HashMap<>();

        for (var item : allItems) {
            if (Boolean.TRUE.equals(item.getIsPurchased())) {
                foodPurchaseCount.merge(item.getFoodId(), 1, Integer::sum);
                foodTotalQuantity.merge(item.getFoodId(), 
                        item.getActualQuantity() != null ? item.getActualQuantity() : item.getQuantity(),
                        java.math.BigDecimal::add);
                foodUnitId.putIfAbsent(item.getFoodId(), item.getUnitId());
            }
        }

        int limit = frequentItemsLimit != null ? frequentItemsLimit : 5;
        List<ShoppingReportResponse.FrequentItem> frequentItems = foodPurchaseCount.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(limit)
                .map(entry -> {
                    Food food = foodRepository.findById(entry.getKey()).orElse(null);
                    Unit unit = unitRepository.findById(foodUnitId.get(entry.getKey())).orElse(null);
                    return ShoppingReportResponse.FrequentItem.builder()
                            .foodId(entry.getKey())
                            .foodName(food != null ? food.getName() : "Unknown")
                            .purchaseCount(entry.getValue())
                            .totalQuantity(foodTotalQuantity.get(entry.getKey()))
                            .unitName(unit != null ? unit.getName() : null)
                            .build();
                })
                .collect(Collectors.toList());

        return ShoppingReportResponse.builder()
                .listStats(listStats)
                .weeklySpending(weeklySpending)
                .frequentItems(frequentItems)
                .build();
    }

    /**
     * Get meal report
     */
    public MealReportResponse getMealReport(Long userId, String period, Integer topRecipesLimit) {
        Long groupId = getGroupIdForUser(userId);
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(29); // Last 30 days

        // Get all meal plans
        List<MealPlan> allPlans = mealPlanRepository.findByGroupIdAndMealDateBetween(groupId, startDate, today);

        // Meal stats
        int breakfasts = 0;
        int lunches = 0;
        int dinners = 0;

        for (MealPlan plan : allPlans) {
            try {
                MealType mealType = MealType.fromDisplayName(plan.getMealName());
                switch (mealType) {
                    case SANG -> breakfasts++;
                    case TRUA -> lunches++;
                    case TOI -> dinners++;
                }
            } catch (Exception e) {
                // Unknown meal type
            }
        }

        MealReportResponse.MealStats mealStats = MealReportResponse.MealStats.builder()
                .totalMeals(allPlans.size())
                .breakfasts(breakfasts)
                .lunches(lunches)
                .dinners(dinners)
                .build();

        // Recipe usage
        Set<Long> uniqueRecipeIds = allPlans.stream()
                .map(MealPlan::getRecipeId)
                .collect(Collectors.toSet());

        MealReportResponse.RecipeUsage recipeUsage = MealReportResponse.RecipeUsage.builder()
                .totalRecipesUsed(allPlans.size())
                .uniqueRecipes(uniqueRecipeIds.size())
                .usagePercentage(allPlans.isEmpty() ? 0 : 1.0) // All meal plans have recipes
                .build();

        // Top recipes
        Map<Long, Integer> recipeUsageCount = new HashMap<>();
        Map<Long, LocalDate> recipeLastUsed = new HashMap<>();

        for (MealPlan plan : allPlans) {
            recipeUsageCount.merge(plan.getRecipeId(), 1, Integer::sum);
            LocalDate existing = recipeLastUsed.get(plan.getRecipeId());
            if (existing == null || plan.getMealDate().isAfter(existing)) {
                recipeLastUsed.put(plan.getRecipeId(), plan.getMealDate());
            }
        }

        int limit = topRecipesLimit != null ? topRecipesLimit : 5;
        List<MealReportResponse.TopRecipe> topRecipes = recipeUsageCount.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(limit)
                .map(entry -> {
                    Recipe recipe = recipeRepository.findById(entry.getKey()).orElse(null);
                    return MealReportResponse.TopRecipe.builder()
                            .recipeId(entry.getKey())
                            .recipeName(recipe != null ? recipe.getName() : "Unknown")
                            .imageUrl(recipe != null ? recipe.getImageUrl() : null)
                            .usageCount(entry.getValue())
                            .lastUsedDate(recipeLastUsed.get(entry.getKey()).format(DATE_FORMATTER))
                            .build();
                })
                .collect(Collectors.toList());

        // Weekly meal trend (last 7 days)
        List<MealReportResponse.WeeklyMealItem> weeklyMealTrend = new ArrayList<>();
        LocalDate weekStart = today.minusDays(6);

        for (LocalDate date = weekStart; !date.isAfter(today); date = date.plusDays(1)) {
            final LocalDate currentDate = date;
            List<MealPlan> dayPlans = allPlans.stream()
                    .filter(p -> p.getMealDate().equals(currentDate))
                    .collect(Collectors.toList());

            int dayBreakfasts = 0;
            int dayLunches = 0;
            int dayDinners = 0;

            for (MealPlan plan : dayPlans) {
                try {
                    MealType mealType = MealType.fromDisplayName(plan.getMealName());
                    switch (mealType) {
                        case SANG -> dayBreakfasts++;
                        case TRUA -> dayLunches++;
                        case TOI -> dayDinners++;
                    }
                } catch (Exception e) {
                    // Unknown meal type
                }
            }

            weeklyMealTrend.add(MealReportResponse.WeeklyMealItem.builder()
                    .date(date.format(DATE_FORMATTER))
                    .dayOfWeek(getDayOfWeekVietnamese(date.getDayOfWeek()))
                    .breakfastCount(dayBreakfasts)
                    .lunchCount(dayLunches)
                    .dinnerCount(dayDinners)
                    .totalMeals(dayPlans.size())
                    .build());
        }

        return MealReportResponse.builder()
                .mealStats(mealStats)
                .recipeUsage(recipeUsage)
                .topRecipes(topRecipes)
                .weeklyMealTrend(weeklyMealTrend)
                .build();
    }

    /**
     * Get full report (all reports combined)
     */
    public FullReportResponse getFullReport(Long userId, String period) {
        return FullReportResponse.builder()
                .overview(getOverviewReport(userId, period, 5))
                .kitchen(getKitchenReport(userId, null, null))
                .shopping(getShoppingReport(userId, period, 5))
                .meal(getMealReport(userId, period, 5))
                .period(period != null ? period : "month")
                .build();
    }

    private Long getGroupIdForUser(Long userId) {
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));
        return membership.getGroupId();
    }

    private String getDayOfWeekVietnamese(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> "T2";
            case TUESDAY -> "T3";
            case WEDNESDAY -> "T4";
            case THURSDAY -> "T5";
            case FRIDAY -> "T6";
            case SATURDAY -> "T7";
            case SUNDAY -> "CN";
        };
    }
}
