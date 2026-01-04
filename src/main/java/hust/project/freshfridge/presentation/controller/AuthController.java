package hust.project.freshfridge.presentation.controller;

import hust.project.freshfridge.application.dto.request.*;
import hust.project.freshfridge.application.dto.response.*;
import hust.project.freshfridge.application.usecase.UserUseCase;
import hust.project.freshfridge.infrastructure.security.CurrentUser;
import hust.project.freshfridge.infrastructure.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserUseCase userUseCase;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = userUseCase.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Đăng ký thành công", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = userUseCase.login(request);
        return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công", response));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(
            @CurrentUser UserPrincipal principal,
            @Valid @RequestBody VerifyEmailRequest request) {
        userUseCase.verifyEmail(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Xác thực email thành công", null));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<Void>> resendVerification(@CurrentUser UserPrincipal principal) {
        userUseCase.resendVerification(principal.getId());
        return ResponseEntity.ok(ApiResponse.success("Đã gửi lại mã xác thực", null));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @CurrentUser UserPrincipal principal,
            @Valid @RequestBody ChangePasswordRequest request) {
        userUseCase.changePassword(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Đổi mật khẩu thành công", null));
    }
}
