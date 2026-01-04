package hust.project.freshfridge.application.usecase;

import hust.project.freshfridge.application.dto.request.*;
import hust.project.freshfridge.application.dto.response.*;
import hust.project.freshfridge.domain.constant.Role;
import hust.project.freshfridge.domain.entity.*;
import hust.project.freshfridge.domain.exception.BusinessException;
import hust.project.freshfridge.domain.exception.ErrorCode;
import hust.project.freshfridge.domain.repository.*;
import hust.project.freshfridge.domain.service.IImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodUseCase {

    private final IFoodRepository foodRepository;
    private final ICategoryRepository categoryRepository;
    private final IGroupMemberRepository groupMemberRepository;
    private final IUserRepository userRepository;
    private final IImageStorageService imageStorageService;

    @Transactional
    public FoodResponse createFood(Long userId, CreateFoodRequest request) {
        // Get user's group
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();
        if (user.getRole().equals(Role.ADMIN.getValue())) {
            groupId = null; // System food
        }

        // Check food name exists in group
        if (foodRepository.existsByNameAndGroupId(request.getName(), groupId)) {
            throw new BusinessException(ErrorCode.FOOD_ALREADY_EXISTS);
        }

        // Validate category exists
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        // Upload image if provided
        String imageUrl = null;
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            imageUrl = imageStorageService.uploadFoodImage(request.getImage(), groupId);
        }

        // Create food
        Food food = Food.builder()
                .name(request.getName())
                .categoryId(category.getId())
                .groupId(groupId)
                .imageUrl(imageUrl)
                .createdBy(userId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        food = foodRepository.save(food);

        return buildFoodResponse(food, category);
    }

    @Transactional
    public FoodResponse updateFood(Long userId, Long foodId, UpdateFoodRequest request) {
        // Get user's group
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Long groupId = membership.getGroupId();

        // Find food
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FOOD_NOT_FOUND));
        
        Boolean canUpdate = food.getGroupId() != null && food.getGroupId().equals(groupId);
        canUpdate = canUpdate || (food.getGroupId() == null && user.getRole().equals(Role.ADMIN.getValue()));

        if (!canUpdate) {
            if (food.getGroupId() == null) {
                throw new BusinessException(ErrorCode.ACCESS_DENIED, "Không thể sửa thực phẩm hệ thống");
            } else {
                throw new BusinessException(ErrorCode.ACCESS_DENIED);
            }
        }

        Category category = categoryRepository.findById(food.getCategoryId()).orElse(null);

        // Update fields
        if (request.getName() != null) {
            // Check name uniqueness
            if (foodRepository.existsByNameAndGroupId(request.getName(), groupId)
                    && !request.getName().equals(food.getName())) {
                throw new BusinessException(ErrorCode.FOOD_ALREADY_EXISTS);
            }
            food.setName(request.getName());
        }

        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
            food.setCategoryId(category.getId());
        }

        // Handle image upload
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            // Delete old image if exists
            if (food.getImageUrl() != null && !food.getImageUrl().isBlank()) {
                imageStorageService.deleteImage(food.getImageUrl());
            }
            String imageUrl = imageStorageService.uploadFoodImage(request.getImage(), groupId);
            food.setImageUrl(imageUrl);
        }

        food.setUpdatedAt(LocalDateTime.now());
        food = foodRepository.save(food);

        return buildFoodResponse(food, category);
    }

    @Transactional
    public void deleteFood(Long userId, Long foodId) {
        // Get user's group
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));
        
        Long groupId = membership.getGroupId();

        // Find food
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FOOD_NOT_FOUND));
        
        boolean canDelete = food.getGroupId() != null && food.getGroupId().equals(groupId);
        canDelete = canDelete || (food.getGroupId() == null && user.getRole().equals(Role.ADMIN.getValue()));

        if (canDelete) {
            if (food.getImageUrl() != null && !food.getImageUrl().isBlank()) {
                imageStorageService.deleteImage(food.getImageUrl());
            }
            foodRepository.deleteById(foodId);
        }
        if (food.getGroupId() == null) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "Không thể xóa thực phẩm hệ thống");
        } else {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
    }

    public FoodResponse getFoodById(Long userId, Long foodId) {
        // Get user's group
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        // Find food
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FOOD_NOT_FOUND));

        // Check access - user can access system foods or their group foods
        if (food.getGroupId() != null && !food.getGroupId().equals(groupId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        Category category = categoryRepository.findById(food.getCategoryId()).orElse(null);

        return buildFoodResponse(food, category);
    }

    public FoodListResponse getFoods(Long userId, Long categoryId, String name) {
        // Get user's group
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        // Use Specification to filter foods
        List<Food> foods = foodRepository.findAll(groupId, categoryId, name);

        List<FoodResponse> foodResponses = foods.stream()
                .map(food -> {
                    Category category = categoryRepository.findById(food.getCategoryId()).orElse(null);
                    return buildFoodResponse(food, category);
                })
                .collect(Collectors.toList());

        return FoodListResponse.builder()
                .foods(foodResponses)
                .total(foodResponses.size())
                .build();
    }

    private FoodResponse buildFoodResponse(Food food, Category category) {
        return FoodResponse.builder()
                .id(food.getId())
                .name(food.getName())
                .categoryId(food.getCategoryId())
                .categoryName(category != null ? category.getName() : null)
                .imageUrl(food.getImageUrl())
                .groupId(food.getGroupId())
                .isSystemFood(food.getGroupId() == null)
                .createdAt(food.getCreatedAt())
                .build();
    }
}
