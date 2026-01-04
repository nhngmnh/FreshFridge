package hust.project.freshfridge.application.usecase;

import hust.project.freshfridge.application.dto.request.AddShoppingItemsRequest;
import hust.project.freshfridge.application.dto.request.CreateShoppingListRequest;
import hust.project.freshfridge.application.dto.request.MarkItemPurchasedRequest;
import hust.project.freshfridge.application.dto.request.ShoppingItemRequest;
import hust.project.freshfridge.application.dto.request.UpdateShoppingItemRequest;
import hust.project.freshfridge.application.dto.request.UpdateShoppingListRequest;
import hust.project.freshfridge.application.dto.response.ShoppingItemResponse;
import hust.project.freshfridge.application.dto.response.ShoppingListResponse;
import hust.project.freshfridge.application.dto.response.ShoppingListsResponse;
import hust.project.freshfridge.domain.constant.NotificationType;
import hust.project.freshfridge.domain.constant.ShoppingListStatus;
import hust.project.freshfridge.domain.entity.*;
import hust.project.freshfridge.domain.exception.BusinessException;
import hust.project.freshfridge.domain.exception.ErrorCode;
import hust.project.freshfridge.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingUseCase {

    private final IShoppingListRepository shoppingListRepository;
    private final IShoppingListItemRepository shoppingListItemRepository;
    private final IFoodRepository foodRepository;
    private final IUnitRepository unitRepository;
    private final IUserRepository userRepository;
    private final IGroupMemberRepository groupMemberRepository;
    private final ICategoryRepository categoryRepository;
    private final NotificationUseCase notificationUseCase;

    @Transactional
    public ShoppingListResponse createShoppingList(Long userId, CreateShoppingListRequest request) {
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        User assignedUser = userRepository.findById(request.getAssignedToUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        GroupMember assignedMembership = groupMemberRepository.findByUserId(assignedUser.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_IN_GROUP));

        if (!assignedMembership.getGroupId().equals(groupId)) {
            throw new BusinessException(ErrorCode.USER_NOT_IN_GROUP);
        }

        ShoppingList list = ShoppingList.builder()
                .name(request.getName())
                .groupId(groupId)
                .assignedToUserId(assignedUser.getId())
                .note(request.getNote())
                .shoppingDate(request.getShoppingDate())
                .status(ShoppingListStatus.PENDING.name())
                .createdBy(userId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        list = shoppingListRepository.save(list);

        // Notify assigned user (if different from creator)
        if (!assignedUser.getId().equals(userId)) {
            User creator = userRepository.findById(userId).orElse(null);
            String creatorName = creator != null ? creator.getName() : "Ai đó";
            notificationUseCase.createNotification(
                    assignedUser.getId(),
                    NotificationType.SHOPPING_REMINDER,
                    "Bạn được giao danh sách mua sắm",
                    String.format("%s đã giao cho bạn danh sách mua sắm '%s'", creatorName, list.getName()),
                    "SHOPPING_LIST",
                    list.getId()
            );
        }

        return buildShoppingListResponse(list);
    }

    @Transactional
    public ShoppingListResponse updateShoppingList(Long userId, Long listId, UpdateShoppingListRequest request) {
        if (request.getName() == null && request.getAssignedToUserId() == null
                && request.getNote() == null && request.getShoppingDate() == null
                && request.getStatus() == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Cung cấp ít nhất một trường để cập nhật");
        }

        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        ShoppingList list = shoppingListRepository.findById(listId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SHOPPING_LIST_NOT_FOUND));

        if (!list.getGroupId().equals(groupId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        if (request.getName() != null) {
            list.setName(request.getName());
        }

        if (request.getAssignedToUserId() != null) {
            User newAssignedUser = userRepository.findById(request.getAssignedToUserId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

            GroupMember newMembership = groupMemberRepository.findByUserId(newAssignedUser.getId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_IN_GROUP));

            if (!newMembership.getGroupId().equals(groupId)) {
                throw new BusinessException(ErrorCode.USER_NOT_IN_GROUP);
            }

            Long oldAssignedUserId = list.getAssignedToUserId();
            list.setAssignedToUserId(newAssignedUser.getId());
            
            // Notify new assigned user if different from current user and old assigned
            if (!newAssignedUser.getId().equals(userId) && !newAssignedUser.getId().equals(oldAssignedUserId)) {
                User updater = userRepository.findById(userId).orElse(null);
                String updaterName = updater != null ? updater.getName() : "Ai đó";
                notificationUseCase.createNotification(
                        newAssignedUser.getId(),
                        NotificationType.SHOPPING_REMINDER,
                        "Bạn được giao danh sách mua sắm",
                        String.format("%s đã giao cho bạn danh sách mua sắm '%s'", updaterName, list.getName()),
                        "SHOPPING_LIST",
                        list.getId()
                );
            }
        }

        if (request.getNote() != null) {
            list.setNote(request.getNote());
        }

        if (request.getShoppingDate() != null) {
            list.setShoppingDate(request.getShoppingDate());
        }

        if (request.getStatus() != null) {
            try {
                ShoppingListStatus.valueOf(request.getStatus());
                list.setStatus(request.getStatus());
            } catch (IllegalArgumentException e) {
                throw new BusinessException(ErrorCode.INVALID_SHOPPING_STATUS);
            }
        }

        list.setUpdatedAt(LocalDateTime.now());
        list = shoppingListRepository.save(list);

        return buildShoppingListResponse(list);
    }

    @Transactional
    public void deleteShoppingList(Long userId, Long listId) {
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        ShoppingList list = shoppingListRepository.findById(listId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SHOPPING_LIST_NOT_FOUND));

        if (!list.getGroupId().equals(groupId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        shoppingListItemRepository.deleteByShoppingListId(listId);
        shoppingListRepository.deleteById(listId);
    }

    @Transactional
    public ShoppingListResponse addItems(Long userId, Long listId, AddShoppingItemsRequest request) {
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        ShoppingList list = shoppingListRepository.findById(listId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SHOPPING_LIST_NOT_FOUND));

        if (!list.getGroupId().equals(groupId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        List<ShoppingListItem> items = new ArrayList<>();
        for (ShoppingItemRequest itemRequest : request.getItems()) {
            Food food = foodRepository.findById(itemRequest.getFoodId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.FOOD_NOT_FOUND));

            if (food.getGroupId() != null && !food.getGroupId().equals(groupId)) {
                throw new BusinessException(ErrorCode.FOOD_NOT_FOUND);
            }

            unitRepository.findById(itemRequest.getUnitId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.UNIT_NOT_FOUND));

            if (shoppingListItemRepository.existsByShoppingListIdAndFoodId(list.getId(), food.getId())) {
                throw new BusinessException(ErrorCode.SHOPPING_ITEM_ALREADY_EXISTS,
                        "Thực phẩm '" + food.getName() + "' đã có trong danh sách");
            }

            ShoppingListItem item = ShoppingListItem.builder()
                    .shoppingListId(list.getId())
                    .foodId(food.getId())
                    .unitId(itemRequest.getUnitId())
                    .quantity(itemRequest.getQuantity())
                    .isPurchased(false)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            items.add(item);
        }

        shoppingListItemRepository.saveAll(items);

        return buildShoppingListResponse(list);
    }

    @Transactional
    public ShoppingItemResponse updateItem(Long userId, Long itemId, UpdateShoppingItemRequest request) {
        if (request.getFoodId() == null && request.getQuantity() == null && request.getUnitId() == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Cung cấp ít nhất một trường để cập nhật");
        }

        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        ShoppingListItem item = shoppingListItemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SHOPPING_ITEM_NOT_FOUND));

        ShoppingList list = shoppingListRepository.findById(item.getShoppingListId())
                .orElseThrow(() -> new BusinessException(ErrorCode.SHOPPING_LIST_NOT_FOUND));

        if (!list.getGroupId().equals(groupId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        if (request.getFoodId() != null) {
            Food newFood = foodRepository.findById(request.getFoodId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.FOOD_NOT_FOUND));

            if (shoppingListItemRepository.existsByShoppingListIdAndFoodId(list.getId(), newFood.getId())
                    && !newFood.getId().equals(item.getFoodId())) {
                throw new BusinessException(ErrorCode.SHOPPING_ITEM_ALREADY_EXISTS);
            }

            item.setFoodId(newFood.getId());
        }

        if (request.getUnitId() != null) {
            unitRepository.findById(request.getUnitId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.UNIT_NOT_FOUND));
            item.setUnitId(request.getUnitId());
        }

        if (request.getQuantity() != null) {
            item.setQuantity(request.getQuantity());
        }

        item.setUpdatedAt(LocalDateTime.now());
        item = shoppingListItemRepository.save(item);

        return buildShoppingItemResponse(item);
    }

    @Transactional
    public void deleteItem(Long userId, Long itemId) {
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        ShoppingListItem item = shoppingListItemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SHOPPING_ITEM_NOT_FOUND));

        ShoppingList list = shoppingListRepository.findById(item.getShoppingListId())
                .orElseThrow(() -> new BusinessException(ErrorCode.SHOPPING_LIST_NOT_FOUND));

        if (!list.getGroupId().equals(groupId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        shoppingListItemRepository.deleteById(itemId);
    }

    @Transactional
    public ShoppingItemResponse markItemPurchased(Long userId, Long itemId, MarkItemPurchasedRequest request) {
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        ShoppingListItem item = shoppingListItemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SHOPPING_ITEM_NOT_FOUND));

        ShoppingList list = shoppingListRepository.findById(item.getShoppingListId())
                .orElseThrow(() -> new BusinessException(ErrorCode.SHOPPING_LIST_NOT_FOUND));

        if (!list.getGroupId().equals(groupId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        item.setActualQuantity(request.getActualQuantity());
        item.setIsPurchased(true);
        item.setUpdatedAt(LocalDateTime.now());
        item = shoppingListItemRepository.save(item);

        if (ShoppingListStatus.PENDING.name().equals(list.getStatus())) {
            list.setStatus(ShoppingListStatus.IN_PROGRESS.name());
            list.setUpdatedAt(LocalDateTime.now());
            shoppingListRepository.save(list);
        }

        return buildShoppingItemResponse(item);
    }

    @Transactional
    public ShoppingListResponse completeShoppingList(Long userId, Long listId) {
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        ShoppingList list = shoppingListRepository.findById(listId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SHOPPING_LIST_NOT_FOUND));

        if (!list.getGroupId().equals(groupId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        list.setStatus(ShoppingListStatus.COMPLETED.name());
        list.setUpdatedAt(LocalDateTime.now());
        list = shoppingListRepository.save(list);

        // Notify the creator if different from completer
        if (list.getCreatedBy() != null && !list.getCreatedBy().equals(userId)) {
            User completer = userRepository.findById(userId).orElse(null);
            String completerName = completer != null ? completer.getName() : "Ai đó";
            notificationUseCase.createNotification(
                    list.getCreatedBy(),
                    NotificationType.SYSTEM,
                    "Danh sách mua sắm đã hoàn thành",
                    String.format("%s đã hoàn thành danh sách mua sắm '%s'", completerName, list.getName()),
                    "SHOPPING_LIST",
                    list.getId()
            );
        }

        return buildShoppingListResponse(list);
    }

    public ShoppingListsResponse getShoppingLists(Long userId, String status, String name) {
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        List<ShoppingList> lists = shoppingListRepository.findByGroupId(groupId, status, name);

        List<ShoppingListResponse> responses = lists.stream()
                .map(this::buildShoppingListResponse)
                .collect(Collectors.toList());

        int pendingCount = (int) responses.stream()
                .filter(r -> ShoppingListStatus.PENDING.name().equals(r.getStatus()))
                .count();
        int inProgressCount = (int) responses.stream()
                .filter(r -> ShoppingListStatus.IN_PROGRESS.name().equals(r.getStatus()))
                .count();
        int completedCount = (int) responses.stream()
                .filter(r -> ShoppingListStatus.COMPLETED.name().equals(r.getStatus()))
                .count();

        return ShoppingListsResponse.builder()
                .lists(responses)
                .total(responses.size())
                .pendingCount(pendingCount)
                .inProgressCount(inProgressCount)
                .completedCount(completedCount)
                .build();
    }

    public ShoppingListResponse getShoppingListById(Long userId, Long listId) {
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        ShoppingList list = shoppingListRepository.findById(listId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SHOPPING_LIST_NOT_FOUND));

        if (!list.getGroupId().equals(groupId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        return buildShoppingListResponse(list);
    }

    public ShoppingListsResponse getMyAssignedLists(Long userId) {
        List<ShoppingList> lists = shoppingListRepository.findByAssignedToUserId(userId);

        List<ShoppingListResponse> responses = lists.stream()
                .map(this::buildShoppingListResponse)
                .collect(Collectors.toList());

        int pendingCount = (int) responses.stream()
                .filter(r -> ShoppingListStatus.PENDING.name().equals(r.getStatus()))
                .count();
        int inProgressCount = (int) responses.stream()
                .filter(r -> ShoppingListStatus.IN_PROGRESS.name().equals(r.getStatus()))
                .count();
        int completedCount = (int) responses.stream()
                .filter(r -> ShoppingListStatus.COMPLETED.name().equals(r.getStatus()))
                .count();

        return ShoppingListsResponse.builder()
                .lists(responses)
                .total(responses.size())
                .pendingCount(pendingCount)
                .inProgressCount(inProgressCount)
                .completedCount(completedCount)
                .build();
    }

    private ShoppingListResponse buildShoppingListResponse(ShoppingList list) {
        User assignedUser = null;
        if (list.getAssignedToUserId() != null) {
            assignedUser = userRepository.findById(list.getAssignedToUserId()).orElse(null);
        }

        List<ShoppingListItem> items = shoppingListItemRepository.findByShoppingListId(list.getId());

        List<ShoppingItemResponse> itemResponses = items.stream()
                .map(this::buildShoppingItemResponse)
                .collect(Collectors.toList());

        int purchasedCount = (int) items.stream()
                .filter(item -> Boolean.TRUE.equals(item.getIsPurchased()))
                .count();

        return ShoppingListResponse.builder()
                .id(list.getId())
                .name(list.getName())
                .groupId(list.getGroupId())
                .assignedToUserId(list.getAssignedToUserId())
                .assignedToEmail(assignedUser != null ? assignedUser.getEmail() : null)
                .assignedToName(assignedUser != null ? assignedUser.getName() : null)
                .note(list.getNote())
                .shoppingDate(list.getShoppingDate())
                .status(list.getStatus())
                .items(itemResponses)
                .totalItems(items.size())
                .purchasedItems(purchasedCount)
                .createdBy(list.getCreatedBy())
                .createdAt(list.getCreatedAt())
                .updatedAt(list.getUpdatedAt())
                .build();
    }

    private ShoppingItemResponse buildShoppingItemResponse(ShoppingListItem item) {
        Food food = foodRepository.findById(item.getFoodId()).orElse(null);
        Unit unit = unitRepository.findById(item.getUnitId()).orElse(null);
        Category category = null;
        if (food != null) {
            category = categoryRepository.findById(food.getCategoryId()).orElse(null);
        }

        return ShoppingItemResponse.builder()
                .id(item.getId())
                .foodId(item.getFoodId())
                .foodName(food != null ? food.getName() : null)
                .categoryName(category != null ? category.getName() : null)
                .unitId(item.getUnitId())
                .unitName(unit != null ? unit.getName() : null)
                .quantity(item.getQuantity())
                .actualQuantity(item.getActualQuantity())
                .isPurchased(item.getIsPurchased())
                .build();
    }
}
