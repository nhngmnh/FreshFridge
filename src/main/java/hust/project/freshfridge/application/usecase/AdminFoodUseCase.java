package hust.project.freshfridge.application.usecase;

import hust.project.freshfridge.application.dto.request.CreateSystemFoodRequest;
import hust.project.freshfridge.application.dto.request.UpdateSystemFoodRequest;
import hust.project.freshfridge.application.dto.response.FoodListResponse;
import hust.project.freshfridge.application.dto.response.FoodResponse;
import hust.project.freshfridge.domain.entity.Category;
import hust.project.freshfridge.domain.entity.Food;
import hust.project.freshfridge.domain.exception.BusinessException;
import hust.project.freshfridge.domain.exception.ErrorCode;
import hust.project.freshfridge.domain.repository.ICategoryRepository;
import hust.project.freshfridge.domain.repository.IFoodRepository;
import hust.project.freshfridge.infrastructure.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminFoodUseCase {

    private final IFoodRepository foodRepository;
    private final ICategoryRepository categoryRepository;
    private final CloudinaryService cloudinaryService;

    /**
     * Admin creates a new system food (groupId = null)
     */
    @Transactional
    public FoodResponse createSystemFood(Long adminId, CreateSystemFoodRequest request) {
        // Check food name exists in system foods
        if (foodRepository.existsSystemFoodByName(request.getName())) {
            throw new BusinessException(ErrorCode.FOOD_ALREADY_EXISTS, "Thực phẩm hệ thống với tên này đã tồn tại");
        }

        // Validate category exists
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        // Upload image if provided
        String imageUrl = null;
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            imageUrl = cloudinaryService.uploadImage(request.getImage(), "system-foods");
        }

        // Create system food (groupId = null)
        Food food = Food.builder()
                .name(request.getName())
                .categoryId(category.getId())
                .groupId(null) // System food
                .imageUrl(imageUrl)
                .createdBy(adminId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        food = foodRepository.save(food);

        return buildFoodResponse(food, category);
    }

    /**
     * Admin updates a system food
     */
    @Transactional
    public FoodResponse updateSystemFood(Long foodId, UpdateSystemFoodRequest request) {
        // Find food
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FOOD_NOT_FOUND));

        // Check if it's a system food
        if (food.getGroupId() != null) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "Chỉ có thể cập nhật thực phẩm hệ thống");
        }

        Category category = categoryRepository.findById(food.getCategoryId()).orElse(null);

        // Update fields
        if (request.getName() != null && !request.getName().isBlank()) {
            // Check name uniqueness among system foods
            if (foodRepository.existsSystemFoodByName(request.getName())
                    && !request.getName().equals(food.getName())) {
                throw new BusinessException(ErrorCode.FOOD_ALREADY_EXISTS, "Thực phẩm hệ thống với tên này đã tồn tại");
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
                cloudinaryService.deleteImage(food.getImageUrl());
            }
            String imageUrl = cloudinaryService.uploadImage(request.getImage(), "system-foods");
            food.setImageUrl(imageUrl);
        }

        food.setUpdatedAt(LocalDateTime.now());
        food = foodRepository.save(food);

        return buildFoodResponse(food, category);
    }

    /**
     * Admin deletes a system food
     */
    @Transactional
    public void deleteSystemFood(Long foodId) {
        // Find food
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FOOD_NOT_FOUND));

        // Check if it's a system food
        if (food.getGroupId() != null) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "Chỉ có thể xóa thực phẩm hệ thống");
        }

        // Delete image from Cloudinary if exists
        if (food.getImageUrl() != null && !food.getImageUrl().isBlank()) {
            cloudinaryService.deleteImage(food.getImageUrl());
        }

        foodRepository.deleteById(foodId);
    }

    /**
     * Admin gets all system foods with optional filters
     */
    public FoodListResponse getSystemFoods(Long categoryId, String name) {
        List<Food> foods = foodRepository.findSystemFoods(categoryId, name);

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

    /**
     * Admin gets a system food by ID
     */
    public FoodResponse getSystemFoodById(Long foodId) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FOOD_NOT_FOUND));

        // Check if it's a system food
        if (food.getGroupId() != null) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "Thực phẩm này không phải là thực phẩm hệ thống");
        }

        Category category = categoryRepository.findById(food.getCategoryId()).orElse(null);

        return buildFoodResponse(food, category);
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
