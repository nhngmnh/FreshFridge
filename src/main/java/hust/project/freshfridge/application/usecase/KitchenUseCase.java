package hust.project.freshfridge.application.usecase;

import hust.project.freshfridge.application.dto.request.*;
import hust.project.freshfridge.application.dto.response.*;
import hust.project.freshfridge.domain.entity.*;
import hust.project.freshfridge.domain.exception.BusinessException;
import hust.project.freshfridge.domain.exception.ErrorCode;
import hust.project.freshfridge.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KitchenUseCase {

    private final IKitchenItemRepository kitchenItemRepository;
    private final IFoodRepository foodRepository;
    private final ICategoryRepository categoryRepository;
    private final IUnitRepository unitRepository;
    private final IGroupMemberRepository groupMemberRepository;

    private static final int EXPIRING_THRESHOLD_DAYS = 3;

    @Transactional
    public KitchenItemResponse createKitchenItem(Long userId, CreateKitchenItemRequest request) {
        // Get user's group
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        // Find food
        Food food = foodRepository.findById(request.getFoodId())
                .orElseThrow(() -> new BusinessException(ErrorCode.FOOD_NOT_FOUND));

        // Verify food belongs to group or is system food
        if (food.getGroupId() != null && !food.getGroupId().equals(groupId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "Thực phẩm không thuộc nhóm của bạn");
        }

        // Validate unit
        Unit unit = unitRepository.findById(request.getUnitId())
                .orElseThrow(() -> new BusinessException(ErrorCode.UNIT_NOT_FOUND));

        // Check if kitchen item already exists for this food
        if (kitchenItemRepository.existsByFoodIdAndGroupId(food.getId(), groupId)) {
            throw new BusinessException(ErrorCode.KITCHEN_ITEM_ALREADY_EXISTS);
        }

        // Calculate expiry date
        LocalDate expiryDate = null;
        if (request.getUseWithin() != null) {
            expiryDate = LocalDate.now().plusDays(request.getUseWithin());
        }

        // Create kitchen item
        KitchenItem item = KitchenItem.builder()
                .foodId(food.getId())
                .groupId(groupId)
                .unitId(unit.getId())
                .quantity(request.getQuantity())
                .useWithin(request.getUseWithin())
                .expiryDate(expiryDate)
                .note(request.getNote())
                .createdBy(userId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        item = kitchenItemRepository.save(item);

        return buildKitchenItemResponse(item, food, unit);
    }

    @Transactional
    public KitchenItemResponse updateKitchenItem(Long userId, Long itemId, UpdateKitchenItemRequest request) {
        // Get user's group
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        // Find kitchen item
        KitchenItem item = kitchenItemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.KITCHEN_ITEM_NOT_FOUND));

        // Verify item belongs to user's group
        if (!item.getGroupId().equals(groupId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        Food food = foodRepository.findById(item.getFoodId()).orElse(null);
        Unit unit = unitRepository.findById(item.getUnitId()).orElse(null);

        // Update fields
        if (request.getQuantity() != null) {
            item.setQuantity(request.getQuantity());
        }

        if (request.getUnitId() != null) {
            unit = unitRepository.findById(request.getUnitId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.UNIT_NOT_FOUND));
            item.setUnitId(unit.getId());
        }

        if (request.getUseWithin() != null) {
            item.setUseWithin(request.getUseWithin());
            item.setExpiryDate(LocalDate.now().plusDays(request.getUseWithin()));
        }

        if (request.getNote() != null) {
            item.setNote(request.getNote());
        }

        if (request.getFoodId() != null) {
            Food newFood = foodRepository.findById(request.getFoodId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.FOOD_NOT_FOUND));
            
            // Verify new food belongs to group or is system food
            if (newFood.getGroupId() != null && !newFood.getGroupId().equals(groupId)) {
                throw new BusinessException(ErrorCode.ACCESS_DENIED, "Thực phẩm không thuộc nhóm của bạn");
            }
            
            // Check if kitchen item already exists for new food
            if (!newFood.getId().equals(item.getFoodId()) 
                    && kitchenItemRepository.existsByFoodIdAndGroupId(newFood.getId(), groupId)) {
                throw new BusinessException(ErrorCode.KITCHEN_ITEM_ALREADY_EXISTS);
            }
            
            item.setFoodId(newFood.getId());
            food = newFood;
        }

        item.setUpdatedAt(LocalDateTime.now());
        item = kitchenItemRepository.save(item);

        return buildKitchenItemResponse(item, food, unit);
    }

    @Transactional
    public void deleteKitchenItem(Long userId, Long itemId) {
        // Get user's group
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        // Find kitchen item
        KitchenItem item = kitchenItemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.KITCHEN_ITEM_NOT_FOUND));

        // Verify item belongs to user's group
        if (!item.getGroupId().equals(groupId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        kitchenItemRepository.deleteById(itemId);
    }

    public KitchenItemListResponse getKitchenItems(Long userId, Long foodId, String foodName, LocalDate expiryBefore,
                                                   Long categoryId, String status, String sortBy, String sortOrder) {
        // Get user's group
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        List<KitchenItem> items = kitchenItemRepository.findAll(groupId, foodId, foodName, expiryBefore, 
                categoryId, sortBy, sortOrder);

        LocalDate today = LocalDate.now();
        LocalDate thresholdDate = today.plusDays(EXPIRING_THRESHOLD_DAYS);

        // Filter by status if provided
        if (status != null && !status.isEmpty()) {
            items = items.stream()
                    .filter(item -> {
                        String itemStatus = calculateExpiryStatus(item.getExpiryDate(), today);
                        return status.equalsIgnoreCase(itemStatus);
                    })
                    .collect(Collectors.toList());
        }

        List<KitchenItemResponse> itemResponses = items.stream()
                .map(item -> {
                    Food food = foodRepository.findById(item.getFoodId()).orElse(null);
                    Unit unit = unitRepository.findById(item.getUnitId()).orElse(null);
                    return buildKitchenItemResponse(item, food, unit);
                })
                .collect(Collectors.toList());

        // Count expiring items (within 3 days but not expired)
        int expiringCount = (int) items.stream()
                .filter(item -> item.getExpiryDate() != null 
                        && !item.getExpiryDate().isBefore(today)
                        && !item.getExpiryDate().isAfter(thresholdDate))
                .count();

        // Count expired items
        int expiredCount = (int) items.stream()
                .filter(item -> item.getExpiryDate() != null 
                        && item.getExpiryDate().isBefore(today))
                .count();

        return KitchenItemListResponse.builder()
                .items(itemResponses)
                .total(itemResponses.size())
                .expiringCount(expiringCount)
                .expiredCount(expiredCount)
                .build();
    }

    private String calculateExpiryStatus(LocalDate expiryDate, LocalDate today) {
        if (expiryDate == null) {
            return "FRESH";
        }
        int daysUntilExpiry = (int) java.time.temporal.ChronoUnit.DAYS.between(today, expiryDate);
        if (daysUntilExpiry < 0) {
            return "EXPIRED";
        } else if (daysUntilExpiry <= EXPIRING_THRESHOLD_DAYS) {
            return "EXPIRING_SOON";
        }
        return "FRESH";
    }

    public KitchenItemResponse getKitchenItemById(Long userId, Long itemId) {
        // Get user's group
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        // Find kitchen item
        KitchenItem item = kitchenItemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.KITCHEN_ITEM_NOT_FOUND));

        // Verify item belongs to user's group
        if (!item.getGroupId().equals(groupId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        Food food = foodRepository.findById(item.getFoodId()).orElse(null);
        Unit unit = unitRepository.findById(item.getUnitId()).orElse(null);

        return buildKitchenItemResponse(item, food, unit);
    }

    public List<KitchenItemResponse> getExpiringItems(Long userId, int withinDays) {
        // Get user's group
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();
        LocalDate thresholdDate = LocalDate.now().plusDays(withinDays);

        List<KitchenItem> items = kitchenItemRepository.findExpiringItems(groupId, thresholdDate);

        return items.stream()
                .map(item -> {
                    Food food = foodRepository.findById(item.getFoodId()).orElse(null);
                    Unit unit = unitRepository.findById(item.getUnitId()).orElse(null);
                    return buildKitchenItemResponse(item, food, unit);
                })
                .collect(Collectors.toList());
    }

    private KitchenItemResponse buildKitchenItemResponse(KitchenItem item, Food food, Unit unit) {
        Category category = null;

        if (food != null) {
            category = categoryRepository.findById(food.getCategoryId()).orElse(null);
        }

        Integer daysUntilExpiry = null;
        String expiryStatus = "FRESH";
        
        if (item.getExpiryDate() != null) {
            LocalDate today = LocalDate.now();
            daysUntilExpiry = (int) ChronoUnit.DAYS.between(today, item.getExpiryDate());
            
            if (daysUntilExpiry < 0) {
                expiryStatus = "EXPIRED";
            } else if (daysUntilExpiry <= EXPIRING_THRESHOLD_DAYS) {
                expiryStatus = "EXPIRING_SOON";
            }
        }

        return KitchenItemResponse.builder()
                .id(item.getId())
                .foodId(item.getFoodId())
                .foodName(food != null ? food.getName() : null)
                .categoryName(category != null ? category.getName() : null)
                .unitId(item.getUnitId())
                .unitName(unit != null ? unit.getName() : null)
                .foodImageUrl(food != null ? food.getImageUrl() : null)
                .quantity(item.getQuantity())
                .useWithin(item.getUseWithin())
                .expiryDate(item.getExpiryDate())
                .daysUntilExpiry(daysUntilExpiry)
                .expiryStatus(expiryStatus)
                .note(item.getNote())
                .groupId(item.getGroupId())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }
}
