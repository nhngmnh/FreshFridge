package hust.project.freshfridge.application.scheduler;

import hust.project.freshfridge.application.usecase.NotificationUseCase;
import hust.project.freshfridge.domain.constant.MealType;
import hust.project.freshfridge.domain.constant.NotificationType;
import hust.project.freshfridge.domain.entity.*;
import hust.project.freshfridge.domain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Scheduler to send meal plan reminders
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MealPlanReminderScheduler {

    private final IMealPlanRepository mealPlanRepository;
    private final IRecipeRepository recipeRepository;
    private final IGroupMemberRepository groupMemberRepository;
    private final NotificationUseCase notificationUseCase;

    /**
     * Send breakfast reminder at 7:00 AM
     */
    @Scheduled(cron = "0 0 7 * * *")
    public void sendBreakfastReminder() {
        log.info("Sending breakfast reminders");
        sendMealReminder(MealType.SANG, "Bữa sáng hôm nay");
    }

    /**
     * Send lunch reminder at 11:00 AM
     */
    @Scheduled(cron = "0 0 11 * * *")
    public void sendLunchReminder() {
        log.info("Sending lunch reminders");
        sendMealReminder(MealType.TRUA, "Bữa trưa hôm nay");
    }

    /**
     * Send dinner reminder at 17:00 PM
     */
    @Scheduled(cron = "0 0 17 * * *")
    public void sendDinnerReminder() {
        log.info("Sending dinner reminders");
        sendMealReminder(MealType.TOI, "Bữa tối hôm nay");
    }

    private void sendMealReminder(MealType mealType, String title) {
        try {
            LocalDate today = LocalDate.now();
            List<MealPlan> todayPlans = mealPlanRepository.findByMealDate(today);
            
            // Filter by meal type
            List<MealPlan> filteredPlans = todayPlans.stream()
                    .filter(plan -> mealType.getDisplayName().equals(plan.getMealName()))
                    .collect(Collectors.toList());
            
            if (filteredPlans.isEmpty()) {
                log.info("No {} plans for today", mealType.getDisplayName());
                return;
            }

            // Group plans by group ID
            Map<Long, List<MealPlan>> plansByGroup = filteredPlans.stream()
                    .collect(Collectors.groupingBy(MealPlan::getGroupId));

            for (Map.Entry<Long, List<MealPlan>> entry : plansByGroup.entrySet()) {
                Long groupId = entry.getKey();
                List<MealPlan> plans = entry.getValue();
                
                // Build message with recipe names
                List<String> recipeNames = new ArrayList<>();
                for (MealPlan plan : plans) {
                    Recipe recipe = recipeRepository.findById(plan.getRecipeId()).orElse(null);
                    if (recipe != null) {
                        recipeNames.add(recipe.getName());
                    }
                }
                
                if (recipeNames.isEmpty()) {
                    continue;
                }
                
                String message = String.join(", ", recipeNames);
                
                // Send notification to all group members
                List<GroupMember> members = groupMemberRepository.findByGroupId(groupId);
                for (GroupMember member : members) {
                    notificationUseCase.createNotification(
                            member.getUserId(),
                            NotificationType.MEAL_REMINDER,
                            title,
                            message,
                            "MEAL_PLAN",
                            plans.get(0).getId()
                    );
                }
                
                log.info("Sent {} reminder to {} members of group {}", 
                        mealType.getDisplayName(), members.size(), groupId);
            }
        } catch (Exception e) {
            log.error("Error sending {} reminder: {}", mealType.getDisplayName(), e.getMessage(), e);
        }
    }
}
