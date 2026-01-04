package hust.project.freshfridge.presentation.controller;

import hust.project.freshfridge.application.dto.request.CreateSystemFoodRequest;
import hust.project.freshfridge.application.dto.request.UpdateSystemFoodRequest;
import hust.project.freshfridge.application.dto.response.ApiResponse;
import hust.project.freshfridge.application.dto.response.FoodListResponse;
import hust.project.freshfridge.application.dto.response.FoodResponse;
import hust.project.freshfridge.application.usecase.AdminFoodUseCase;
import hust.project.freshfridge.infrastructure.security.CurrentUser;
import hust.project.freshfridge.infrastructure.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/food")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminFoodController {

    private final AdminFoodUseCase adminFoodUseCase;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FoodResponse>> createSystemFood(
            @CurrentUser UserPrincipal principal,
            @Valid @ModelAttribute CreateSystemFoodRequest request) {
        FoodResponse response = adminFoodUseCase.createSystemFood(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo thực phẩm hệ thống thành công", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<FoodListResponse>> getSystemFoods(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String name) {
        FoodListResponse response = adminFoodUseCase.getSystemFoods(categoryId, name);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{foodId}")
    public ResponseEntity<ApiResponse<FoodResponse>> getSystemFoodById(@PathVariable Long foodId) {
        FoodResponse response = adminFoodUseCase.getSystemFoodById(foodId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping(value = "/{foodId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FoodResponse>> updateSystemFood(
            @PathVariable Long foodId,
            @Valid @ModelAttribute UpdateSystemFoodRequest request) {
        FoodResponse response = adminFoodUseCase.updateSystemFood(foodId, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thực phẩm hệ thống thành công", response));
    }

    @DeleteMapping("/{foodId}")
    public ResponseEntity<ApiResponse<Void>> deleteSystemFood(@PathVariable Long foodId) {
        adminFoodUseCase.deleteSystemFood(foodId);
        return ResponseEntity.ok(ApiResponse.success("Xóa thực phẩm hệ thống thành công", null));
    }
}
