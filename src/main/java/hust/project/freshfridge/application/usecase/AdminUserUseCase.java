package hust.project.freshfridge.application.usecase;

import hust.project.freshfridge.application.dto.request.AdminUpdateUserRequest;
import hust.project.freshfridge.application.dto.response.AdminUserListResponse;
import hust.project.freshfridge.application.dto.response.AdminUserResponse;
import hust.project.freshfridge.domain.entity.Group;
import hust.project.freshfridge.domain.entity.GroupMember;
import hust.project.freshfridge.domain.entity.User;
import hust.project.freshfridge.domain.exception.BusinessException;
import hust.project.freshfridge.domain.exception.ErrorCode;
import hust.project.freshfridge.domain.repository.IGroupMemberRepository;
import hust.project.freshfridge.domain.repository.IGroupRepository;
import hust.project.freshfridge.domain.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserUseCase {

    private final IUserRepository userRepository;
    private final IGroupRepository groupRepository;
    private final IGroupMemberRepository groupMemberRepository;

    /**
     * Get all users with optional filters
     */
    public AdminUserListResponse getUsers(String email, String name, String role, Boolean isActive) {
        List<User> users = userRepository.findByFilters(email, name, role, isActive);
        
        List<AdminUserResponse> userResponses = users.stream()
                .map(this::buildAdminUserResponse)
                .collect(Collectors.toList());

        int activeCount = (int) userResponses.stream().filter(u -> Boolean.TRUE.equals(u.getIsActive())).count();
        int inactiveCount = (int) userResponses.stream().filter(u -> Boolean.FALSE.equals(u.getIsActive())).count();
        int verifiedCount = (int) userResponses.stream().filter(u -> Boolean.TRUE.equals(u.getIsVerified())).count();
        int unverifiedCount = (int) userResponses.stream().filter(u -> Boolean.FALSE.equals(u.getIsVerified())).count();

        return AdminUserListResponse.builder()
                .users(userResponses)
                .total(userResponses.size())
                .activeCount(activeCount)
                .inactiveCount(inactiveCount)
                .verifiedCount(verifiedCount)
                .unverifiedCount(unverifiedCount)
                .build();
    }

    public AdminUserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return buildAdminUserResponse(user);
    }

    @Transactional
    public AdminUserResponse updateUser(Long adminId, Long userId, AdminUpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // Prevent admin from deactivating themselves
        if (userId.equals(adminId) && Boolean.FALSE.equals(request.getIsActive())) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Không thể vô hiệu hóa tài khoản của chính mình");
        }

        // Prevent admin from changing their own role
        if (userId.equals(adminId) && request.getRole() != null && !request.getRole().equals(user.getRole())) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Không thể thay đổi vai trò của chính mình");
        }

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }

        if (request.getPhone() != null) {
            // Check if phone is already used by another user
            if (!request.getPhone().isBlank() && !request.getPhone().equals(user.getPhone())) {
                if (userRepository.existsByPhone(request.getPhone())) {
                    throw new BusinessException(ErrorCode.PHONE_ALREADY_EXISTS);
                }
                user.setPhone(request.getPhone());
            }
        }

        if (request.getRole() != null && !request.getRole().isBlank()) {
            // Validate role
            if (!request.getRole().equals("USER") && !request.getRole().equals("ADMIN")) {
                throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Vai trò không hợp lệ (USER hoặc ADMIN)");
            }
            user.setRole(request.getRole());
        }

        if (request.getIsActive() != null) {
            user.setIsActive(request.getIsActive());
        }

        if (request.getIsVerified() != null) {
            user.setIsVerified(request.getIsVerified());
        }

        user.setUpdatedAt(LocalDateTime.now());
        user = userRepository.save(user);

        return buildAdminUserResponse(user);
    }

    @Transactional
    public AdminUserResponse toggleUserStatus(Long adminId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // Prevent admin from deactivating themselves
        if (userId.equals(adminId)) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Không thể thay đổi trạng thái tài khoản của chính mình");
        }

        user.setIsActive(!Boolean.TRUE.equals(user.getIsActive()));
        user.setUpdatedAt(LocalDateTime.now());
        user = userRepository.save(user);

        return buildAdminUserResponse(user);
    }

    /**
     * Delete user (hard delete)
     */
    @Transactional
    public void deleteUser(Long adminId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // Prevent admin from deleting themselves
        if (userId.equals(adminId)) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Không thể xóa tài khoản của chính mình");
        }

        // Check if user is in a group
        GroupMember membership = groupMemberRepository.findByUserId(userId).orElse(null);
        if (membership != null) {
            Group group = groupRepository.findById(membership.getGroupId()).orElse(null);
            
            // If user is admin of group, delete the entire group
            if (group != null && group.getAdminId().equals(userId)) {
                groupMemberRepository.deleteByGroupId(group.getId());
                groupRepository.deleteById(group.getId());
            } else {
                // Just remove from group
                groupMemberRepository.deleteByGroupIdAndUserId(membership.getGroupId(), userId);
            }
        }

        userRepository.deleteById(userId);
    }

    public UserStatistics getStatistics() {
        long total = userRepository.count();
        long activeCount = userRepository.countByIsActive(true);
        long inactiveCount = userRepository.countByIsActive(false);
        long verifiedCount = userRepository.countByIsVerified(true);
        long unverifiedCount = userRepository.countByIsVerified(false);

        return UserStatistics.builder()
                .totalUsers(total)
                .activeUsers(activeCount)
                .inactiveUsers(inactiveCount)
                .verifiedUsers(verifiedCount)
                .unverifiedUsers(unverifiedCount)
                .build();
    }

    private AdminUserResponse buildAdminUserResponse(User user) {
        String groupName = null;
        Long groupId = null;

        GroupMember membership = groupMemberRepository.findByUserId(user.getId()).orElse(null);
        if (membership != null) {
            Group group = groupRepository.findById(membership.getGroupId()).orElse(null);
            if (group != null) {
                groupName = group.getName();
                groupId = group.getId();
            }
        }

        return AdminUserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole())
                .isVerified(user.getIsVerified())
                .isActive(user.getIsActive())
                .groupName(groupName)
                .groupId(groupId)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class UserStatistics {
        private long totalUsers;
        private long activeUsers;
        private long inactiveUsers;
        private long verifiedUsers;
        private long unverifiedUsers;
    }
}
