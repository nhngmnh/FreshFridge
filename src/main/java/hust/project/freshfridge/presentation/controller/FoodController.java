package hust.project.freshfridge.presentation.controller;

import hust.project.freshfridge.application.dto.request.*;
import hust.project.freshfridge.application.dto.response.*;
import hust.project.freshfridge.application.usecase.FoodUseCase;
import hust.project.freshfridge.infrastructure.security.CurrentUser;
import hust.project.freshfridge.infrastructure.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodUseCase foodUseCase;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FoodResponse>> createFood(
            @CurrentUser UserPrincipal principal,
            @Valid @ModelAttribute CreateFoodRequest request) {
        FoodResponse response = foodUseCase.createFood(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<FoodListResponse>> getFoods(
            @CurrentUser UserPrincipal principal,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String name) {
        FoodListResponse response = foodUseCase.getFoods(principal.getId(), categoryId, name);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{foodId}")
    public ResponseEntity<ApiResponse<FoodResponse>> getFoodById(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long foodId) {
        FoodResponse response = foodUseCase.getFoodById(principal.getId(), foodId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping(value = "/{foodId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FoodResponse>> updateFood(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long foodId,
            @Valid @ModelAttribute UpdateFoodRequest request) {
        FoodResponse response = foodUseCase.updateFood(principal.getId(), foodId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{foodId}")
    public ResponseEntity<ApiResponse<Void>> deleteFood(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long foodId) {
        foodUseCase.deleteFood(principal.getId(), foodId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
