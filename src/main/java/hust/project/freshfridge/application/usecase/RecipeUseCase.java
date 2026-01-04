package hust.project.freshfridge.application.usecase;

import hust.project.freshfridge.application.dto.request.*;
import hust.project.freshfridge.application.dto.response.*;
import hust.project.freshfridge.domain.entity.*;
import hust.project.freshfridge.domain.exception.BusinessException;
import hust.project.freshfridge.domain.exception.ErrorCode;
import hust.project.freshfridge.domain.repository.*;
import hust.project.freshfridge.domain.service.IImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeUseCase {

    private final IRecipeRepository recipeRepository;
    private final IRecipeIngredientRepository recipeIngredientRepository;
    private final IFoodRepository foodRepository;
    private final IUnitRepository unitRepository;
    private final IUserRepository userRepository;
    private final IGroupMemberRepository groupMemberRepository;
    private final IImageStorageService imageStorageService;

    @Transactional
    public RecipeResponse createRecipe(Long userId, CreateRecipeRequest request, MultipartFile image) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Long groupId = null;
        boolean isSystem = false;

        if ("ADMIN".equals(user.getRole())) {
            isSystem = true;
        } else {
            GroupMember membership = groupMemberRepository.findByUserId(userId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));
            groupId = membership.getGroupId();
        }

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = imageStorageService.uploadImage(image, "recipes");
        }

        Recipe recipe = Recipe.builder()
                .name(request.getName())
                .description(request.getDescription())
                .htmlContent(request.getHtmlContent())
                .imageUrl(imageUrl)
                .groupId(groupId)
                .isSystem(isSystem)
                .createdBy(userId)
                .difficulty(request.getDifficulty() != null ? request.getDifficulty() : "MEDIUM")
                .prepTime(request.getPrepTime() != null ? request.getPrepTime() : 0)
                .cookTime(request.getCookTime() != null ? request.getCookTime() : 0)
                .servings(request.getServings() != null ? request.getServings() : 2)
                .tags(request.getTags())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        recipe = recipeRepository.save(recipe);

        List<RecipeIngredient> ingredients = new ArrayList<>();
        if (request.getIngredients() != null && !request.getIngredients().isEmpty()) {
            for (CreateRecipeRequest.IngredientItem item : request.getIngredients()) {
                foodRepository.findById(item.getFoodId())
                        .orElseThrow(() -> new BusinessException(ErrorCode.FOOD_NOT_FOUND));

                unitRepository.findById(item.getUnitId())
                        .orElseThrow(() -> new BusinessException(ErrorCode.UNIT_NOT_FOUND));

                RecipeIngredient ingredient = RecipeIngredient.builder()
                        .recipeId(recipe.getId())
                        .foodId(item.getFoodId())
                        .quantity(item.getQuantity())
                        .unitId(item.getUnitId())
                        .isMainIngredient(item.getIsMainIngredient() != null ? item.getIsMainIngredient() : false)
                        .note(item.getNote())
                        .createdAt(LocalDateTime.now())
                        .build();

                ingredients.add(ingredient);
            }
            recipeIngredientRepository.saveAll(ingredients);
        }

        return buildRecipeResponse(recipe, ingredients);
    }

    @Transactional
    public RecipeResponse updateRecipe(Long userId, Long recipeId, UpdateRecipeRequest request, MultipartFile image) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECIPE_NOT_FOUND));

        verifyRecipePermission(userId, recipe);

        if (request.getName() != null) {
            recipe.setName(request.getName());
        }
        if (request.getDescription() != null) {
            recipe.setDescription(request.getDescription());
        }
        if (request.getHtmlContent() != null) {
            recipe.setHtmlContent(request.getHtmlContent());
        }
        if (request.getDifficulty() != null) {
            recipe.setDifficulty(request.getDifficulty());
        }
        if (request.getPrepTime() != null) {
            recipe.setPrepTime(request.getPrepTime());
        }
        if (request.getCookTime() != null) {
            recipe.setCookTime(request.getCookTime());
        }
        if (request.getServings() != null) {
            recipe.setServings(request.getServings());
        }
        if (request.getTags() != null) {
            recipe.setTags(request.getTags());
        }
        if (image != null && !image.isEmpty()) {
            if (recipe.getImageUrl() != null) {
                imageStorageService.deleteImage(recipe.getImageUrl());
            }
            String imageUrl = imageStorageService.uploadImage(image, "recipes");
            recipe.setImageUrl(imageUrl);
        }

        recipe.setUpdatedAt(LocalDateTime.now());
        recipe = recipeRepository.save(recipe);

        List<RecipeIngredient> ingredients = recipeIngredientRepository.findByRecipeId(recipe.getId());
        return buildRecipeResponse(recipe, ingredients);
    }

    @Transactional
    public void deleteRecipe(Long userId, Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECIPE_NOT_FOUND));

        verifyRecipePermission(userId, recipe);

        recipeIngredientRepository.deleteByRecipeId(recipeId);
        recipeRepository.deleteById(recipeId);
    }

    public List<RecipeResponse> getRecipes(Long userId, String name) {
        GroupMember membership = groupMemberRepository.findByUserId(userId).orElse(null);
        Long groupId = membership != null ? membership.getGroupId() : null;

        List<Recipe> recipes = recipeRepository.findAllWithFilters(groupId, name);

        return recipes.stream()
                .map(recipe -> {
                    List<RecipeIngredient> ingredients = recipeIngredientRepository.findByRecipeId(recipe.getId());
                    return buildRecipeResponse(recipe, ingredients);
                })
                .collect(Collectors.toList());
    }

    public RecipeResponse getRecipeById(Long userId, Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECIPE_NOT_FOUND));

        if (!recipe.getIsSystem()) {
            GroupMember membership = groupMemberRepository.findByUserId(userId).orElse(null);
            if (membership == null || !membership.getGroupId().equals(recipe.getGroupId())) {
                throw new BusinessException(ErrorCode.RECIPE_ACCESS_DENIED);
            }
        }

        List<RecipeIngredient> ingredients = recipeIngredientRepository.findByRecipeId(recipe.getId());
        return buildRecipeResponse(recipe, ingredients);
    }

    public List<RecipeResponse> findRecipesByIngredient(Long groupId, Long foodId) {
        List<RecipeIngredient> mainIngredients = recipeIngredientRepository.findMainIngredientsByFoodId(foodId);

        return mainIngredients.stream()
                .map(ing -> recipeRepository.findById(ing.getRecipeId()).orElse(null))
                .filter(recipe -> recipe != null && (recipe.getIsSystem() || recipe.getGroupId().equals(groupId)))
                .map(recipe -> {
                    List<RecipeIngredient> ingredients = recipeIngredientRepository.findByRecipeId(recipe.getId());
                    return buildRecipeResponse(recipe, ingredients);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public RecipeResponse addIngredient(Long userId, Long recipeId, AddRecipeIngredientRequest request) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECIPE_NOT_FOUND));

        verifyRecipePermission(userId, recipe);

        foodRepository.findById(request.getFoodId())
                .orElseThrow(() -> new BusinessException(ErrorCode.FOOD_NOT_FOUND));

        unitRepository.findById(request.getUnitId())
                .orElseThrow(() -> new BusinessException(ErrorCode.UNIT_NOT_FOUND));

        RecipeIngredient ingredient = RecipeIngredient.builder()
                .recipeId(recipe.getId())
                .foodId(request.getFoodId())
                .quantity(request.getQuantity())
                .unitId(request.getUnitId())
                .isMainIngredient(request.getIsMainIngredient() != null ? request.getIsMainIngredient() : false)
                .note(request.getNote())
                .createdAt(LocalDateTime.now())
                .build();

        recipeIngredientRepository.save(ingredient);

        List<RecipeIngredient> ingredients = recipeIngredientRepository.findByRecipeId(recipe.getId());
        return buildRecipeResponse(recipe, ingredients);
    }

    @Transactional
    public RecipeResponse updateIngredient(Long userId, Long ingredientId, UpdateRecipeIngredientRequest request) {
        RecipeIngredient ingredient = recipeIngredientRepository.findById(ingredientId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECIPE_NOT_FOUND, "Không tìm thấy nguyên liệu"));

        Recipe recipe = recipeRepository.findById(ingredient.getRecipeId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RECIPE_NOT_FOUND));

        verifyRecipePermission(userId, recipe);

        if (request.getQuantity() != null) {
            ingredient.setQuantity(request.getQuantity());
        }
        if (request.getUnitId() != null) {
            unitRepository.findById(request.getUnitId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.UNIT_NOT_FOUND));
            ingredient.setUnitId(request.getUnitId());
        }
        if (request.getIsMainIngredient() != null) {
            ingredient.setIsMainIngredient(request.getIsMainIngredient());
        }
        if (request.getNote() != null) {
            ingredient.setNote(request.getNote());
        }

        recipeIngredientRepository.save(ingredient);

        List<RecipeIngredient> ingredients = recipeIngredientRepository.findByRecipeId(recipe.getId());
        return buildRecipeResponse(recipe, ingredients);
    }

    @Transactional
    public void deleteIngredient(Long userId, Long ingredientId) {
        RecipeIngredient ingredient = recipeIngredientRepository.findById(ingredientId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECIPE_NOT_FOUND, "Không tìm thấy nguyên liệu"));

        Recipe recipe = recipeRepository.findById(ingredient.getRecipeId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RECIPE_NOT_FOUND));

        verifyRecipePermission(userId, recipe);

        recipeIngredientRepository.deleteById(ingredientId);
    }

    private void verifyRecipePermission(Long userId, Recipe recipe) {
        User user = userRepository.findById(userId).orElse(null);

        if (recipe.getIsSystem()) {
            if (user == null || !"ADMIN".equals(user.getRole())) {
                throw new BusinessException(ErrorCode.RECIPE_ACCESS_DENIED, "Bạn không có quyền chỉnh sửa công thức hệ thống");
            }
        } else {
            GroupMember membership = groupMemberRepository.findByUserId(userId).orElse(null);
            if (membership == null || !membership.getGroupId().equals(recipe.getGroupId())) {
                throw new BusinessException(ErrorCode.RECIPE_ACCESS_DENIED);
            }
        }
    }

    private RecipeResponse buildRecipeResponse(Recipe recipe, List<RecipeIngredient> ingredients) {
        List<RecipeIngredientResponse> ingredientResponses = ingredients.stream()
                .map(ing -> {
                    Food food = foodRepository.findById(ing.getFoodId()).orElse(null);
                    Unit unit = unitRepository.findById(ing.getUnitId()).orElse(null);

                    return RecipeIngredientResponse.builder()
                            .id(ing.getId())
                            .foodId(ing.getFoodId())
                            .foodName(food != null ? food.getName() : null)
                            .foodImageUrl(food != null ? food.getImageUrl() : null)
                            .quantity(ing.getQuantity())
                            .unitId(ing.getUnitId())
                            .unitName(unit != null ? unit.getName() : null)
                            .isMainIngredient(ing.getIsMainIngredient())
                            .note(ing.getNote())
                            .build();
                })
                .collect(Collectors.toList());

        return RecipeResponse.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .description(recipe.getDescription())
                .htmlContent(recipe.getHtmlContent())
                .imageUrl(recipe.getImageUrl())
                .groupId(recipe.getGroupId())
                .isSystem(recipe.getIsSystem())
                .createdBy(recipe.getCreatedBy())
                .difficulty(recipe.getDifficulty())
                .prepTime(recipe.getPrepTime())
                .cookTime(recipe.getCookTime())
                .servings(recipe.getServings())
                .tags(recipe.getTags())
                .ingredients(ingredientResponses)
                .createdAt(recipe.getCreatedAt())
                .updatedAt(recipe.getUpdatedAt())
                .build();
    }
}
