package hust.project.freshfridge.presentation.controller;

import hust.project.freshfridge.application.dto.request.CreateMealPlanRequest;
import hust.project.freshfridge.application.dto.request.UpdateMealPlanRequest;
import hust.project.freshfridge.application.dto.response.ApiResponse;
import hust.project.freshfridge.application.dto.response.DailyMealPlanResponse;
import hust.project.freshfridge.application.dto.response.MealPlanResponse;
import hust.project.freshfridge.application.usecase.MealPlanUseCase;
import hust.project.freshfridge.infrastructure.security.CurrentUser;
import hust.project.freshfridge.infrastructure.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/meal-plans")
@RequiredArgsConstructor
public class MealPlanController {

    private final MealPlanUseCase mealPlanUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<MealPlanResponse>> createMealPlan(
            @CurrentUser UserPrincipal principal,
            @Valid @RequestBody CreateMealPlanRequest request) {
        var response = mealPlanUseCase.createMealPlan(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo kế hoạch bữa ăn thành công", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MealPlanResponse>>> getMealPlans(
            @CurrentUser UserPrincipal principal) {
        var response = mealPlanUseCase.getMealPlans(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MealPlanResponse>> getMealPlanById(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long id) {
        var response = mealPlanUseCase.getMealPlanById(principal.getId(), id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MealPlanResponse>> updateMealPlan(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody UpdateMealPlanRequest request) {
        var response = mealPlanUseCase.updateMealPlan(principal.getId(), id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật kế hoạch bữa ăn thành công", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMealPlan(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long id) {
        mealPlanUseCase.deleteMealPlan(principal.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Xóa kế hoạch bữa ăn thành công", null));
    }

    @GetMapping("/daily")
    public ResponseEntity<ApiResponse<DailyMealPlanResponse>> getDailyMealPlan(
            @CurrentUser UserPrincipal principal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        var response = mealPlanUseCase.getDailyMealPlan(principal.getId(), date);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/range")
    public ResponseEntity<ApiResponse<List<MealPlanResponse>>> getMealPlansByDateRange(
            @CurrentUser UserPrincipal principal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        var response = mealPlanUseCase.getMealPlansByDateRange(principal.getId(), startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponse<List<MealPlanResponse>>> getUpcomingMeals(
            @CurrentUser UserPrincipal principal,
            @RequestParam(defaultValue = "7") int days) {
        var response = mealPlanUseCase.getUpcomingMeals(principal.getId(), days);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
