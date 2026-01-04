package hust.project.freshfridge.application.usecase;

import hust.project.freshfridge.application.dto.request.*;
import hust.project.freshfridge.application.dto.response.*;
import hust.project.freshfridge.domain.constant.MealType;
import hust.project.freshfridge.domain.entity.*;
import hust.project.freshfridge.domain.exception.BusinessException;
import hust.project.freshfridge.domain.exception.ErrorCode;
import hust.project.freshfridge.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealPlanUseCase {

    private final IMealPlanRepository mealPlanRepository;
    private final IRecipeRepository recipeRepository;
    private final IGroupMemberRepository groupMemberRepository;
    private final IGroupRepository groupRepository;

    @Transactional
    public MealPlanResponse createMealPlan(Long userId, CreateMealPlanRequest request) {
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        MealType mealType;
        try {
            mealType = MealType.fromDisplayName(request.getMealName());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_MEAL_DATE, "Tên bữa ăn không hợp lệ (sáng, trưa, tối)");
        }

        Recipe recipe = recipeRepository.findById(request.getRecipeId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RECIPE_NOT_FOUND));

        if (!recipe.getIsSystem() && !groupId.equals(recipe.getGroupId())) {
            throw new BusinessException(ErrorCode.RECIPE_ACCESS_DENIED);
        }

        MealPlan plan = MealPlan.builder()
                .groupId(groupId)
                .recipeId(request.getRecipeId())
                .mealDate(request.getMealDate())
                .mealName(mealType.name())
                .servings(request.getServings() != null ? request.getServings() : 1)
                .note(request.getNote())
                .createdBy(userId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        plan = mealPlanRepository.save(plan);

        return buildMealPlanResponse(plan, recipe);
    }

    @Transactional
    public MealPlanResponse updateMealPlan(Long userId, Long planId, UpdateMealPlanRequest request) {
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        MealPlan plan = mealPlanRepository.findById(planId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEAL_PLAN_NOT_FOUND));

        if (!plan.getGroupId().equals(groupId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        Recipe recipe = recipeRepository.findById(plan.getRecipeId()).orElse(null);

        if (request.getRecipeId() != null) {
            Recipe newRecipe = recipeRepository.findById(request.getRecipeId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.RECIPE_NOT_FOUND));
            if (!newRecipe.getIsSystem() && !groupId.equals(newRecipe.getGroupId())) {
                throw new BusinessException(ErrorCode.RECIPE_ACCESS_DENIED);
            }
            plan.setRecipeId(request.getRecipeId());
            recipe = newRecipe;
        }

        if (request.getMealDate() != null) {
            plan.setMealDate(request.getMealDate());
        }

        if (request.getMealName() != null) {
            try {
                MealType mealType = MealType.fromDisplayName(request.getMealName());
                plan.setMealName(mealType.name());
            } catch (IllegalArgumentException e) {
                throw new BusinessException(ErrorCode.INVALID_MEAL_DATE, "Tên bữa ăn không hợp lệ (sáng, trưa, tối)");
            }
        }

        if (request.getServings() != null) {
            plan.setServings(request.getServings());
        }

        if (request.getNote() != null) {
            plan.setNote(request.getNote());
        }

        plan.setUpdatedAt(LocalDateTime.now());
        plan = mealPlanRepository.save(plan);

        return buildMealPlanResponse(plan, recipe);
    }

    @Transactional
    public void deleteMealPlan(Long userId, Long planId) {
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        MealPlan plan = mealPlanRepository.findById(planId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEAL_PLAN_NOT_FOUND));

        if (!plan.getGroupId().equals(groupId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        mealPlanRepository.deleteById(plan.getId());
    }

    public List<MealPlanResponse> getMealPlans(Long userId) {
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        List<MealPlan> plans = mealPlanRepository.findByGroupId(groupId);

        return plans.stream()
                .map(plan -> {
                    Recipe recipe = recipeRepository.findById(plan.getRecipeId()).orElse(null);
                    return buildMealPlanResponse(plan, recipe);
                })
                .collect(Collectors.toList());
    }

    public MealPlanResponse getMealPlanById(Long userId, Long planId) {
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        MealPlan plan = mealPlanRepository.findById(planId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEAL_PLAN_NOT_FOUND));

        if (!plan.getGroupId().equals(groupId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        Recipe recipe = recipeRepository.findById(plan.getRecipeId()).orElse(null);
        return buildMealPlanResponse(plan, recipe);
    }

    public List<MealPlanResponse> getMealPlansByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        List<MealPlan> plans = mealPlanRepository.findByGroupIdAndMealDateBetween(groupId, startDate, endDate);

        return plans.stream()
                .map(plan -> {
                    Recipe recipe = recipeRepository.findById(plan.getRecipeId()).orElse(null);
                    return buildMealPlanResponse(plan, recipe);
                })
                .collect(Collectors.toList());
    }

    public DailyMealPlanResponse getDailyMealPlan(Long userId, LocalDate date) {
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Long groupId = membership.getGroupId();

        List<MealPlan> plans = mealPlanRepository.findByGroupIdAndMealDate(groupId, date);

        Map<String, List<MealPlanResponse>> byMealType = plans.stream()
                .map(plan -> {
                    Recipe recipe = recipeRepository.findById(plan.getRecipeId()).orElse(null);
                    return buildMealPlanResponse(plan, recipe);
                })
                .collect(Collectors.groupingBy(MealPlanResponse::getMealName));

        return DailyMealPlanResponse.builder()
                .date(date)
                .breakfast(byMealType.getOrDefault("SANG", Collections.emptyList()))
                .lunch(byMealType.getOrDefault("TRUA", Collections.emptyList()))
                .dinner(byMealType.getOrDefault("TOI", Collections.emptyList()))
                .build();
    }

    public List<MealPlanResponse> getUpcomingMeals(Long groupId, int days) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(days);

        List<MealPlan> plans = mealPlanRepository.findByGroupIdAndMealDateBetween(groupId, today, endDate);

        return plans.stream()
                .map(plan -> {
                    Recipe recipe = recipeRepository.findById(plan.getRecipeId()).orElse(null);
                    return buildMealPlanResponse(plan, recipe);
                })
                .collect(Collectors.toList());
    }

    private MealPlanResponse buildMealPlanResponse(MealPlan plan, Recipe recipe) {
        String mealDisplayName = null;
        try {
            mealDisplayName = MealType.valueOf(plan.getMealName()).getDisplayName();
        } catch (Exception e) {
            mealDisplayName = plan.getMealName();
        }

        return MealPlanResponse.builder()
                .id(plan.getId())
                .recipeId(plan.getRecipeId())
                .recipeName(recipe != null ? recipe.getName() : null)
                .recipeDescription(recipe != null ? recipe.getDescription() : null)
                .recipeImageUrl(recipe != null ? recipe.getImageUrl() : null)
                .mealDate(plan.getMealDate())
                .mealName(plan.getMealName())
                .mealDisplayName(mealDisplayName)
                .servings(plan.getServings())
                .note(plan.getNote())
                .groupId(plan.getGroupId())
                .createdBy(plan.getCreatedBy())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .build();
    }
}
