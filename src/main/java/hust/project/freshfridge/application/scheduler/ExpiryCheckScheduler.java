package hust.project.freshfridge.application.scheduler;

import hust.project.freshfridge.application.usecase.NotificationUseCase;
import hust.project.freshfridge.domain.entity.*;
import hust.project.freshfridge.domain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Scheduler to check for expiring kitchen items and send notifications
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ExpiryCheckScheduler {

    private final IKitchenItemRepository kitchenItemRepository;
    private final IFoodRepository foodRepository;
    private final IGroupMemberRepository groupMemberRepository;
    private final IGroupRepository groupRepository;
    private final NotificationUseCase notificationUseCase;

    private static final int WARNING_DAYS = 3; // Warn 3 days before expiry
    private static final int ALERT_DAYS = 0;   // Alert on expiry day

    /**
     * Run every day at 8:00 AM to check for expiring items
     */
    @Scheduled(cron = "0 0 8 * * *")
    public void checkExpiringItems() {
        log.info("Starting expiry check scheduler");
        
        try {
            LocalDate today = LocalDate.now();
            LocalDate warningDate = today.plusDays(WARNING_DAYS);
            
            List<Group> allGroups = groupRepository.findAll();
            
            for (Group group : allGroups) {
                checkExpiringItemsForGroup(group.getId(), today, warningDate);
            }
            
            log.info("Expiry check scheduler completed");
        } catch (Exception e) {
            log.error("Error during expiry check: {}", e.getMessage(), e);
        }
    }

    private void checkExpiringItemsForGroup(Long groupId, LocalDate today, LocalDate warningDate) {
        // Get all items expiring within warning period
        List<KitchenItem> expiringItems = kitchenItemRepository.findExpiringItems(groupId, warningDate);
        
        if (expiringItems.isEmpty()) {
            return;
        }

        // Get all members of the group
        List<GroupMember> members = groupMemberRepository.findByGroupId(groupId);
        
        for (KitchenItem item : expiringItems) {
            Food food = foodRepository.findById(item.getFoodId()).orElse(null);
            if (food == null) continue;
            
            String foodName = food.getName();
            LocalDate expiryDate = item.getExpiryDate();
            long daysUntilExpiry = ChronoUnit.DAYS.between(today, expiryDate);
            
            // Notify all group members
            for (GroupMember member : members) {
                if (daysUntilExpiry <= ALERT_DAYS) {
                    // Item has expired or expires today
                    notificationUseCase.createExpiryAlert(member.getUserId(), foodName, item.getId());
                } else if (daysUntilExpiry <= WARNING_DAYS) {
                    // Item is expiring soon
                    notificationUseCase.createExpiryWarning(member.getUserId(), foodName, (int) daysUntilExpiry, item.getId());
                }
            }
        }
        
        log.info("Processed {} expiring items for group {}", expiringItems.size(), groupId);
    }
}
