package hust.project.freshfridge.presentation.controller;

import hust.project.freshfridge.application.dto.response.*;
import hust.project.freshfridge.application.usecase.NotificationUseCase;
import hust.project.freshfridge.infrastructure.security.CurrentUser;
import hust.project.freshfridge.infrastructure.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationUseCase notificationUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<NotificationListResponse>> getNotifications(
            @CurrentUser UserPrincipal principal) {
        NotificationListResponse response = notificationUseCase.getNotifications(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long notificationId) {
        notificationUseCase.markAsRead(principal.getId(), notificationId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(
            @CurrentUser UserPrincipal principal) {
        notificationUseCase.markAllAsRead(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            @CurrentUser UserPrincipal principal,
            @PathVariable Long notificationId) {
        notificationUseCase.deleteNotification(principal.getId(), notificationId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
