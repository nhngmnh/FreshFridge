package hust.project.freshfridge.application.usecase;

import hust.project.freshfridge.application.dto.response.*;
import hust.project.freshfridge.domain.entity.*;
import hust.project.freshfridge.domain.exception.BusinessException;
import hust.project.freshfridge.domain.exception.ErrorCode;
import hust.project.freshfridge.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SuggestionUseCase {

    private final IKitchenItemRepository kitchenItemRepository;
    private final IRecipeRepository recipeRepository;
    private final IRecipeIngredientRepository recipeIngredientRepository;
    private final IFoodRepository foodRepository;
    private final IUnitRepository unitRepository;
    private final IGroupMemberRepository groupMemberRepository;
    private final RecipeUseCase recipeUseCase;

    public List<RecipeResponse> getSuggestedRecipes(Long userId) {
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        List<KitchenItem> kitchenItems = kitchenItemRepository.findByGroupId(groupId);

        if (kitchenItems.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> availableFoodIds = kitchenItems.stream()
                .map(KitchenItem::getFoodId)
                .collect(Collectors.toSet());

        LocalDate warningDate = LocalDate.now().plusDays(3);
        Set<Long> expiringFoodIds = kitchenItems.stream()
                .filter(item -> item.getExpiryDate() != null && item.getExpiryDate().isBefore(warningDate))
                .map(KitchenItem::getFoodId)
                .collect(Collectors.toSet());

        List<Recipe> allRecipes = recipeRepository.findByGroupIdOrSystem(groupId);

        List<ScoredRecipe> scoredRecipes = new ArrayList<>();

        for (Recipe recipe : allRecipes) {
            List<RecipeIngredient> ingredients = recipeIngredientRepository.findByRecipeId(recipe.getId());

            if (ingredients.isEmpty()) continue;

            int totalIngredients = ingredients.size();
            int matchedIngredients = 0;
            int expiringMatched = 0;
            boolean hasMainIngredient = false;

            for (RecipeIngredient ingredient : ingredients) {
                if (availableFoodIds.contains(ingredient.getFoodId())) {
                    matchedIngredients++;
                    if (expiringFoodIds.contains(ingredient.getFoodId())) {
                        expiringMatched++;
                    }
                    if (Boolean.TRUE.equals(ingredient.getIsMainIngredient())) {
                        hasMainIngredient = true;
                    }
                }
            }

            double matchRatio = (double) matchedIngredients / totalIngredients;
            if (matchRatio >= 0.5 || hasMainIngredient) {
                double score = matchRatio * 100 + expiringMatched * 20 + (hasMainIngredient ? 10 : 0);
                scoredRecipes.add(new ScoredRecipe(recipe, score, matchedIngredients, totalIngredients));
            }
        }

        scoredRecipes.sort((a, b) -> Double.compare(b.score, a.score));

        return scoredRecipes.stream()
                .limit(10)
                .map(sr -> {
                    List<RecipeIngredient> ingredients = recipeIngredientRepository.findByRecipeId(sr.recipe.getId());
                    return buildRecipeResponseWithAvailability(sr.recipe, ingredients, availableFoodIds, sr.matchedIngredients, sr.totalIngredients);
                })
                .collect(Collectors.toList());
    }

    public List<RecipeResponse> getSuggestedRecipesByFood(Long userId, Long foodId) {
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        return recipeUseCase.findRecipesByIngredient(groupId, foodId);
    }

    private RecipeResponse buildRecipeResponseWithAvailability(Recipe recipe,
            List<RecipeIngredient> ingredients, Set<Long> availableFoodIds,
            int matchedCount, int totalCount) {

        List<RecipeIngredientResponse> ingredientResponses = ingredients.stream()
                .map(ing -> {
                    Food food = foodRepository.findById(ing.getFoodId()).orElse(null);
                    Unit unit = unitRepository.findById(ing.getUnitId()).orElse(null);
                    boolean isAvailable = availableFoodIds.contains(ing.getFoodId());

                    return RecipeIngredientResponse.builder()
                            .id(ing.getId())
                            .foodId(ing.getFoodId())
                            .foodName(food != null ? food.getName() : null)
                            .quantity(ing.getQuantity())
                            .unitId(ing.getUnitId())
                            .unitName(unit != null ? unit.getName() : null)
                            .isMainIngredient(ing.getIsMainIngredient())
                            .note(ing.getNote())
                            .isAvailable(isAvailable)
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
                .createdAt(recipe.getCreatedAt())
                .updatedAt(recipe.getUpdatedAt())
                .ingredients(ingredientResponses)
                .matchedIngredients(matchedCount)
                .totalIngredients(totalCount)
                .build();
    }

    private static class ScoredRecipe {
        Recipe recipe;
        double score;
        int matchedIngredients;
        int totalIngredients;

        ScoredRecipe(Recipe recipe, double score, int matched, int total) {
            this.recipe = recipe;
            this.score = score;
            this.matchedIngredients = matched;
            this.totalIngredients = total;
        }
    }
}
