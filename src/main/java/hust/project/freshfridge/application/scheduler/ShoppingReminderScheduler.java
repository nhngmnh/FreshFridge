package hust.project.freshfridge.application.scheduler;

import hust.project.freshfridge.application.usecase.NotificationUseCase;
import hust.project.freshfridge.domain.constant.NotificationType;
import hust.project.freshfridge.domain.constant.ShoppingListStatus;
import hust.project.freshfridge.domain.entity.ShoppingList;
import hust.project.freshfridge.domain.repository.IShoppingListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * Scheduler to send shopping list reminders
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ShoppingReminderScheduler {

    private final IShoppingListRepository shoppingListRepository;
    private final NotificationUseCase notificationUseCase;

    /**
     * Send shopping reminder at 9:00 AM for lists scheduled for today
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void sendShoppingReminder() {
        log.info("Starting shopping reminder scheduler");
        
        try {
            LocalDate today = LocalDate.now();
            
            // Find pending shopping lists scheduled for today
            List<ShoppingList> todayLists = shoppingListRepository.findByShoppingDateAndStatus(
                    today, ShoppingListStatus.PENDING.name());
            
            if (todayLists.isEmpty()) {
                log.info("No pending shopping lists for today");
                return;
            }
            
            for (ShoppingList list : todayLists) {
                if (list.getAssignedToUserId() != null) {
                    notificationUseCase.createNotification(
                            list.getAssignedToUserId(),
                            NotificationType.SHOPPING_REMINDER,
                            "Nhắc nhở mua sắm",
                            String.format("Hôm nay bạn có danh sách mua sắm '%s' cần hoàn thành", list.getName()),
                            "SHOPPING_LIST",
                            list.getId()
                    );
                    log.info("Sent shopping reminder for list {} to user {}", 
                            list.getId(), list.getAssignedToUserId());
                }
            }
            
            log.info("Shopping reminder scheduler completed, sent {} reminders", todayLists.size());
        } catch (Exception e) {
            log.error("Error during shopping reminder: {}", e.getMessage(), e);
        }
    }
}
