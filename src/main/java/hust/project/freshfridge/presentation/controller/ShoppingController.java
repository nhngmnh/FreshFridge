package hust.project.freshfridge.presentation.controller;

import hust.project.freshfridge.application.dto.request.AddShoppingItemsRequest;
import hust.project.freshfridge.application.dto.request.CreateShoppingListRequest;
import hust.project.freshfridge.application.dto.request.MarkItemPurchasedRequest;
import hust.project.freshfridge.application.dto.request.UpdateShoppingItemRequest;
import hust.project.freshfridge.application.dto.request.UpdateShoppingListRequest;
import hust.project.freshfridge.application.dto.response.ApiResponse;
import hust.project.freshfridge.application.dto.response.ShoppingItemResponse;
import hust.project.freshfridge.application.dto.response.ShoppingListResponse;
import hust.project.freshfridge.application.dto.response.ShoppingListsResponse;
import hust.project.freshfridge.application.usecase.ShoppingUseCase;
import hust.project.freshfridge.infrastructure.security.CurrentUser;
import hust.project.freshfridge.infrastructure.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shopping")
@RequiredArgsConstructor
public class ShoppingController {

    private final ShoppingUseCase shoppingUseCase;

    @PostMapping("/lists")
    public ResponseEntity<ApiResponse<ShoppingListResponse>> createShoppingList(
            @CurrentUser UserPrincipal principal,
            @Valid @RequestBody CreateShoppingListRequest request) {
        ShoppingListResponse response = shoppingUseCase.createShoppingList(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo danh sách mua sắm thành công", response));
    }

    @GetMapping("/lists")
    public ResponseEntity<ApiResponse<ShoppingListsResponse>> getShoppingLists(
            @CurrentUser UserPrincipal principal,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String name) {
        ShoppingListsResponse response = shoppingUseCase.getShoppingLists(principal.getId(), status, name);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách mua sắm thành công", response));
    }

    @GetMapping("/lists/{listId}")
    public ResponseEntity<ApiResponse<ShoppingListResponse>> getShoppingListById(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long listId) {
        ShoppingListResponse response = shoppingUseCase.getShoppingListById(principal.getId(), listId);
        return ResponseEntity.ok(ApiResponse.success("Lấy chi tiết danh sách thành công", response));
    }

    @GetMapping("/lists/my-assigned")
    public ResponseEntity<ApiResponse<ShoppingListsResponse>> getMyAssignedLists(
            @CurrentUser UserPrincipal principal) {
        ShoppingListsResponse response = shoppingUseCase.getMyAssignedLists(principal.getId());
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách được giao thành công", response));
    }

    @PutMapping("/lists/{listId}")
    public ResponseEntity<ApiResponse<ShoppingListResponse>> updateShoppingList(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long listId,
            @Valid @RequestBody UpdateShoppingListRequest request) {
        ShoppingListResponse response = shoppingUseCase.updateShoppingList(principal.getId(), listId, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật danh sách thành công", response));
    }

    @DeleteMapping("/lists/{listId}")
    public ResponseEntity<ApiResponse<Void>> deleteShoppingList(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long listId) {
        shoppingUseCase.deleteShoppingList(principal.getId(), listId);
        return ResponseEntity.ok(ApiResponse.success("Xóa danh sách thành công", null));
    }

    @PostMapping("/lists/{listId}/complete")
    public ResponseEntity<ApiResponse<ShoppingListResponse>> completeShoppingList(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long listId) {
        ShoppingListResponse response = shoppingUseCase.completeShoppingList(principal.getId(), listId);
        return ResponseEntity.ok(ApiResponse.success("Hoàn thành danh sách mua sắm", response));
    }

    @PostMapping("/lists/{listId}/items")
    public ResponseEntity<ApiResponse<ShoppingListResponse>> addItems(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long listId,
            @Valid @RequestBody AddShoppingItemsRequest request) {
        ShoppingListResponse response = shoppingUseCase.addItems(principal.getId(), listId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Thêm mục thành công", response));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<ShoppingItemResponse>> updateItem(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateShoppingItemRequest request) {
        ShoppingItemResponse response = shoppingUseCase.updateItem(principal.getId(), itemId, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật mục thành công", response));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long itemId) {
        shoppingUseCase.deleteItem(principal.getId(), itemId);
        return ResponseEntity.ok(ApiResponse.success("Xóa mục thành công", null));
    }

    @PostMapping("/items/{itemId}/purchased")
    public ResponseEntity<ApiResponse<ShoppingItemResponse>> markItemPurchased(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long itemId,
            @Valid @RequestBody MarkItemPurchasedRequest request) {
        ShoppingItemResponse response = shoppingUseCase.markItemPurchased(principal.getId(), itemId, request);
        return ResponseEntity.ok(ApiResponse.success("Đánh dấu đã mua thành công", response));
    }
}
