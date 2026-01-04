package hust.project.freshfridge.presentation.controller;

import hust.project.freshfridge.application.dto.response.*;
import hust.project.freshfridge.application.usecase.CategoryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryUseCase categoryUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<CategoryListResponse>> getAllCategories() {
        CategoryListResponse response = categoryUseCase.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
