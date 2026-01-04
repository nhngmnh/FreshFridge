package hust.project.freshfridge.presentation.controller;

import hust.project.freshfridge.application.dto.request.AdminUpdateUserRequest;
import hust.project.freshfridge.application.dto.response.AdminUserListResponse;
import hust.project.freshfridge.application.dto.response.AdminUserResponse;
import hust.project.freshfridge.application.dto.response.ApiResponse;
import hust.project.freshfridge.application.usecase.AdminUserUseCase;
import hust.project.freshfridge.infrastructure.security.CurrentUser;
import hust.project.freshfridge.infrastructure.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminUserUseCase adminUserUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<AdminUserListResponse>> getUsers(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean isActive) {
        AdminUserListResponse response = adminUserUseCase.getUsers(email, name, role, isActive);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<AdminUserUseCase.UserStatistics>> getStatistics() {
        AdminUserUseCase.UserStatistics statistics = adminUserUseCase.getStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<AdminUserResponse>> getUserById(@PathVariable Long userId) {
        AdminUserResponse response = adminUserUseCase.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<AdminUserResponse>> updateUser(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long userId,
            @Valid @RequestBody AdminUpdateUserRequest request) {
        AdminUserResponse response = adminUserUseCase.updateUser(principal.getId(), userId, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật người dùng thành công", response));
    }

    @PatchMapping("/{userId}/toggle-status")
    public ResponseEntity<ApiResponse<AdminUserResponse>> toggleUserStatus(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long userId) {
        AdminUserResponse response = adminUserUseCase.toggleUserStatus(principal.getId(), userId);
        String message = Boolean.TRUE.equals(response.getIsActive()) 
                ? "Kích hoạt tài khoản thành công" 
                : "Vô hiệu hóa tài khoản thành công";
        return ResponseEntity.ok(ApiResponse.success(message, response));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long userId) {
        adminUserUseCase.deleteUser(principal.getId(), userId);
        return ResponseEntity.ok(ApiResponse.success("Xóa người dùng thành công", null));
    }
}
