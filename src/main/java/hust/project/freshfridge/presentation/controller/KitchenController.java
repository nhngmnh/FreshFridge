package hust.project.freshfridge.presentation.controller;

import hust.project.freshfridge.application.dto.request.*;
import hust.project.freshfridge.application.dto.response.*;
import hust.project.freshfridge.application.usecase.KitchenUseCase;
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
@RequestMapping("/kitchen")
@RequiredArgsConstructor
public class KitchenController {

    private final KitchenUseCase kitchenUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<KitchenItemResponse>> createKitchenItem(
            @CurrentUser UserPrincipal principal,
            @Valid @RequestBody CreateKitchenItemRequest request) {
        KitchenItemResponse response = kitchenUseCase.createKitchenItem(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Thêm thực phẩm vào bếp thành công", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<KitchenItemListResponse>> getKitchenItems(
            @CurrentUser UserPrincipal principal,
            @RequestParam(required = false) Long foodId,
            @RequestParam(required = false) String foodName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryBefore,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {
        KitchenItemListResponse response = kitchenUseCase.getKitchenItems(
                principal.getId(), foodId, foodName, expiryBefore, categoryId, status, sortBy, sortOrder);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ApiResponse<KitchenItemResponse>> getKitchenItemById(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long itemId) {
        KitchenItemResponse response = kitchenUseCase.getKitchenItemById(principal.getId(), itemId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/expiring")
    public ResponseEntity<ApiResponse<List<KitchenItemResponse>>> getExpiringItems(
            @CurrentUser UserPrincipal principal,
            @RequestParam(defaultValue = "3") int days) {
        List<KitchenItemResponse> response = kitchenUseCase.getExpiringItems(principal.getId(), days);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ApiResponse<KitchenItemResponse>> updateKitchenItem(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateKitchenItemRequest request) {
        KitchenItemResponse response = kitchenUseCase.updateKitchenItem(principal.getId(), itemId, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công", response));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<ApiResponse<Void>> deleteKitchenItem(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long itemId) {
        kitchenUseCase.deleteKitchenItem(principal.getId(), itemId);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}
