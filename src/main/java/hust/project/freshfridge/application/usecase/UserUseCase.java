package hust.project.freshfridge.application.usecase;

import hust.project.freshfridge.application.dto.request.*;
import hust.project.freshfridge.application.dto.response.*;
import hust.project.freshfridge.domain.constant.Role;
import hust.project.freshfridge.domain.entity.EmailVerification;
import hust.project.freshfridge.domain.entity.User;
import hust.project.freshfridge.domain.exception.BusinessException;
import hust.project.freshfridge.domain.exception.ErrorCode;
import hust.project.freshfridge.domain.repository.IEmailVerificationRepository;
import hust.project.freshfridge.domain.repository.IUserRepository;
import hust.project.freshfridge.domain.service.IImageStorageService;
import hust.project.freshfridge.infrastructure.external.EmailService;
import hust.project.freshfridge.infrastructure.security.JwtTokenProvider;
import hust.project.freshfridge.infrastructure.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserUseCase {

    private final IUserRepository userRepository;
    private final IEmailVerificationRepository verificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;
    private final IImageStorageService imageStorageService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Check email exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // Check phone exists
        if (request.getPhone() != null && !request.getPhone().isBlank() 
                && userRepository.existsByPhone(request.getPhone())) {
            throw new BusinessException(ErrorCode.PHONE_ALREADY_EXISTS);
        }

        // Create user
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phone(request.getPhone())
                .role(Role.USER.getValue())
                .isVerified(false)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        // Send verification email
        sendVerificationEmail(user);

        // Generate token
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        String token = jwtTokenProvider.generateToken(userPrincipal);

        return AuthResponse.of(token, jwtTokenProvider.getExpirationMs(), toUserResponse(user));
    }

    public AuthResponse login(LoginRequest request) {
        // Find user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));

        // Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        // Check active
        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new BusinessException(ErrorCode.ACCOUNT_LOCKED);
        }

        if (!Boolean.TRUE.equals(user.getIsVerified())) {
            throw new BusinessException(ErrorCode.EMAIL_NOT_VERIFIED);
        }

        // Generate token
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        String token = jwtTokenProvider.generateToken(userPrincipal);

        return AuthResponse.of(token, jwtTokenProvider.getExpirationMs(), toUserResponse(user));
    }

    @Transactional
    public void verifyEmail(Long userId, VerifyEmailRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (Boolean.TRUE.equals(user.getIsVerified())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_VERIFIED);
        }

        EmailVerification verification = verificationRepository.findByUserIdAndCode(userId, request.getCode())
                .orElseThrow(() -> new BusinessException(ErrorCode.VERIFICATION_CODE_INVALID));

        if (verification.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.VERIFICATION_CODE_EXPIRED);
        }

        if (Boolean.TRUE.equals(verification.getIsUsed())) {
            throw new BusinessException(ErrorCode.VERIFICATION_CODE_INVALID);
        }

        // Update user
        user.setIsVerified(true);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        // Mark verification as used
        verification.setIsUsed(true);
        verificationRepository.save(verification);
    }

    @Transactional
    public void resendVerification(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (Boolean.TRUE.equals(user.getIsVerified())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_VERIFIED);
        }

        sendVerificationEmail(user);
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        // Validate confirm password
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_MISMATCH);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // Check old password
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_OLD_PASSWORD);
        }

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public UserResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return toUserResponse(user);
    }

    @Transactional
    public UserResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }

        if (request.getPhone() != null) {
            // Check phone exists for other user
            userRepository.findByPhone(request.getPhone())
                    .filter(u -> !u.getId().equals(userId))
                    .ifPresent(u -> {
                        throw new BusinessException(ErrorCode.PHONE_ALREADY_EXISTS);
                    });
            user.setPhone(request.getPhone());
        }

        // Handle avatar upload
        if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
            // Delete old avatar if exists
            if (user.getAvatarUrl() != null && !user.getAvatarUrl().isBlank()) {
                imageStorageService.deleteImage(user.getAvatarUrl());
            }
            String avatarUrl = imageStorageService.uploadAvatar(request.getAvatar(), userId);
            user.setAvatarUrl(avatarUrl);
        }

        user.setUpdatedAt(LocalDateTime.now());
        user = userRepository.save(user);

        return toUserResponse(user);
    }

    private void sendVerificationEmail(User user) {
        // Generate 6-digit code
        String code = generateVerificationCode();

        // Save verification
        EmailVerification verification = EmailVerification.builder()
                .userId(user.getId())
                .code(code)
                .expiresAt(LocalDateTime.now().plusHours(24))
                .isUsed(false)
                .createdAt(LocalDateTime.now())
                .build();

        verificationRepository.save(verification);

        // Send email
        emailService.sendVerificationEmail(user.getEmail(), user.getName(), code);
    }

    private String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole())
                .isVerified(user.getIsVerified())
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Transactional
    public void updateFcmToken(Long userId, String fcmToken) {
        userRepository.updateFcmToken(userId, fcmToken);
    }

    public String getFcmToken(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return user.getFcmToken();
    }
}
