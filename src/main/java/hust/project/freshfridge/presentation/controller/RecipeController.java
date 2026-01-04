package hust.project.freshfridge.presentation.controller;

import hust.project.freshfridge.application.dto.request.*;
import hust.project.freshfridge.application.dto.response.*;
import hust.project.freshfridge.application.usecase.RecipeUseCase;
import hust.project.freshfridge.infrastructure.security.CurrentUser;
import hust.project.freshfridge.infrastructure.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeUseCase recipeUseCase;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<RecipeResponse>> createRecipe(
            @CurrentUser UserPrincipal principal,
            @Valid @ModelAttribute CreateRecipeRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        RecipeResponse response = recipeUseCase.createRecipe(principal.getId(), request, image);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo công thức thành công", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RecipeResponse>>> getRecipes(
            @CurrentUser UserPrincipal principal,
            @RequestParam(required = false) String name) {
        List<RecipeResponse> response = recipeUseCase.getRecipes(principal.getId(), name);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách công thức thành công", response));
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<ApiResponse<RecipeResponse>> getRecipeById(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long recipeId) {
        RecipeResponse response = recipeUseCase.getRecipeById(principal.getId(), recipeId);
        return ResponseEntity.ok(ApiResponse.success("Lấy công thức thành công", response));
    }

    @PutMapping(value = "/{recipeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<RecipeResponse>> updateRecipe(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long recipeId,
            @Valid @ModelAttribute UpdateRecipeRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        RecipeResponse response = recipeUseCase.updateRecipe(principal.getId(), recipeId, request, image);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật công thức thành công", response));
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<ApiResponse<Void>> deleteRecipe(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long recipeId) {
        recipeUseCase.deleteRecipe(principal.getId(), recipeId);
        return ResponseEntity.ok(ApiResponse.success("Xóa công thức thành công", (Void) null));
    }

    @PostMapping("/{recipeId}/ingredients")
    public ResponseEntity<ApiResponse<RecipeResponse>> addIngredient(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long recipeId,
            @Valid @RequestBody AddRecipeIngredientRequest request) {
        RecipeResponse response = recipeUseCase.addIngredient(principal.getId(), recipeId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Thêm nguyên liệu thành công", response));
    }

    @PutMapping("/ingredients/{ingredientId}")
    public ResponseEntity<ApiResponse<RecipeResponse>> updateIngredient(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long ingredientId,
            @Valid @RequestBody UpdateRecipeIngredientRequest request) {
        RecipeResponse response = recipeUseCase.updateIngredient(principal.getId(), ingredientId, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật nguyên liệu thành công", response));
    }

    @DeleteMapping("/ingredients/{ingredientId}")
    public ResponseEntity<ApiResponse<Void>> deleteIngredient(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long ingredientId) {
        recipeUseCase.deleteIngredient(principal.getId(), ingredientId);
        return ResponseEntity.ok(ApiResponse.success("Xóa nguyên liệu thành công", (Void) null));
    }

    @GetMapping("/by-ingredient/{foodId}")
    public ResponseEntity<ApiResponse<List<RecipeResponse>>> findRecipesByIngredient(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long foodId) {
        List<RecipeResponse> response = recipeUseCase.findRecipesByIngredient(null, foodId);
        return ResponseEntity.ok(ApiResponse.success("Tìm công thức thành công", response));
    }
}
