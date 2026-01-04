package hust.project.freshfridge.presentation.controller;

import hust.project.freshfridge.application.dto.request.UpdateFcmTokenRequest;
import hust.project.freshfridge.application.dto.request.UpdateProfileRequest;
import hust.project.freshfridge.application.dto.response.*;
import hust.project.freshfridge.application.usecase.UserUseCase;
import hust.project.freshfridge.infrastructure.security.CurrentUser;
import hust.project.freshfridge.infrastructure.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(@CurrentUser UserPrincipal principal) {
        UserResponse response = userUseCase.getProfile(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @CurrentUser UserPrincipal principal,
            @ModelAttribute UpdateProfileRequest request) {
        UserResponse response = userUseCase.updateProfile(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thông tin thành công", response));
    }

    @PutMapping("/fcm-token")
    public ResponseEntity<ApiResponse<Void>> updateFcmToken(
            @CurrentUser UserPrincipal principal,
            @Valid @RequestBody UpdateFcmTokenRequest request) {
        userUseCase.updateFcmToken(principal.getId(), request.getFcmToken());
        return ResponseEntity.ok(ApiResponse.success("Cập nhật FCM token thành công", null));
    }
}
