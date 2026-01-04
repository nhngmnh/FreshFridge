package hust.project.freshfridge.application.usecase;

import hust.project.freshfridge.application.dto.request.*;
import hust.project.freshfridge.application.dto.response.*;
import hust.project.freshfridge.domain.constant.NotificationType;
import hust.project.freshfridge.domain.entity.*;
import hust.project.freshfridge.domain.exception.BusinessException;
import hust.project.freshfridge.domain.exception.ErrorCode;
import hust.project.freshfridge.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupUseCase {

    private final IGroupRepository groupRepository;
    private final IGroupMemberRepository groupMemberRepository;
    private final IUserRepository userRepository;
    private final NotificationUseCase notificationUseCase;

    @Transactional
    public GroupResponse createGroup(Long userId, CreateGroupRequest request) {
        // Check if user already belongs to a group
        if (groupMemberRepository.existsByUserId(userId)) {
            throw new BusinessException(ErrorCode.ALREADY_IN_GROUP);
        }

        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // Create group
        Group group = Group.builder()
                .name(request.getName())
                .adminId(userId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        group = groupRepository.save(group);

        // Add creator as member
        GroupMember member = GroupMember.builder()
                .groupId(group.getId())
                .userId(userId)
                .joinedAt(LocalDateTime.now())
                .build();

        groupMemberRepository.save(member);

        return buildGroupResponse(group);
    }

    public GroupResponse getGroup(Long userId) {
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Group group = groupRepository.findById(membership.getGroupId())
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_NOT_FOUND));

        return buildGroupResponse(group);
    }

    public List<MemberResponse> getGroupMembers(Long userId) {
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Group group = groupRepository.findById(membership.getGroupId())
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_NOT_FOUND));

        List<GroupMember> members = groupMemberRepository.findByGroupId(group.getId());

        return members.stream()
                .map(member -> {
                    User user = userRepository.findById(member.getUserId()).orElse(null);
                    if (user == null) return null;

                    return MemberResponse.builder()
                            .userId(user.getId())
                            .name(user.getName())
                            .email(user.getEmail())
                            .avatarUrl(user.getAvatarUrl())
                            .isAdmin(user.getId().equals(group.getAdminId()))
                            .joinedAt(member.getJoinedAt())
                            .build();
                })
                .filter(m -> m != null)
                .collect(Collectors.toList());
    }

    @Transactional
    public GroupResponse addMember(Long adminId, AddMemberRequest request) {
        // Verify admin
        GroupMember adminMembership = groupMemberRepository.findByUserId(adminId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Group group = groupRepository.findById(adminMembership.getGroupId())
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_NOT_FOUND));

        if (!group.getAdminId().equals(adminId)) {
            throw new BusinessException(ErrorCode.NOT_GROUP_OWNER);
        }

        // Find user by email
        User userToAdd = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, 
                        "Không tìm thấy người dùng với email: " + request.getEmail()));

        // Check if user already in a group
        if (groupMemberRepository.existsByUserId(userToAdd.getId())) {
            throw new BusinessException(ErrorCode.ALREADY_IN_GROUP, 
                    "Người dùng này đã thuộc về một nhóm khác");
        }

        // Add member
        GroupMember member = GroupMember.builder()
                .groupId(group.getId())
                .userId(userToAdd.getId())
                .joinedAt(LocalDateTime.now())
                .build();

        groupMemberRepository.save(member);

        // Send notification to the added user
        notificationUseCase.createNotification(
                userToAdd.getId(),
                NotificationType.GROUP_INVITE,
                "Bạn đã được thêm vào nhóm",
                String.format("Bạn đã được thêm vào nhóm '%s'", group.getName()),
                "GROUP",
                group.getId()
        );

        // Notify other members about new member
        List<GroupMember> otherMembers = groupMemberRepository.findByGroupId(group.getId());
        for (GroupMember m : otherMembers) {
            if (!m.getUserId().equals(userToAdd.getId()) && !m.getUserId().equals(adminId)) {
                notificationUseCase.createNotification(
                        m.getUserId(),
                        NotificationType.GROUP_JOIN,
                        "Thành viên mới",
                        String.format("%s đã tham gia nhóm", userToAdd.getName()),
                        "GROUP",
                        group.getId()
                );
            }
        }

        return buildGroupResponse(group);
    }

    @Transactional
    public void removeMember(Long adminId, RemoveMemberRequest request) {
        // Verify admin
        GroupMember adminMembership = groupMemberRepository.findByUserId(adminId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Group group = groupRepository.findById(adminMembership.getGroupId())
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_NOT_FOUND));

        if (!group.getAdminId().equals(adminId)) {
            throw new BusinessException(ErrorCode.NOT_GROUP_OWNER);
        }

        // Find user by email
        User userToRemove = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND,
                        "Không tìm thấy người dùng với email: " + request.getEmail()));

        // Cannot remove admin
        if (userToRemove.getId().equals(adminId)) {
            throw new BusinessException(ErrorCode.CANNOT_REMOVE_OWNER);
        }

        // Check if user is in this group
        groupMemberRepository.findByGroupIdAndUserId(group.getId(), userToRemove.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_IN_GROUP));

        groupMemberRepository.deleteByGroupIdAndUserId(group.getId(), userToRemove.getId());

        // Notify removed user
        notificationUseCase.createNotification(
                userToRemove.getId(),
                NotificationType.MEMBER_REMOVED,
                "Bạn đã bị xóa khỏi nhóm",
                String.format("Bạn đã bị xóa khỏi nhóm '%s'", group.getName()),
                "GROUP",
                group.getId()
        );
    }

    @Transactional
    public void leaveGroup(Long userId) {
        GroupMember membership = groupMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_IN_GROUP));

        Group group = groupRepository.findById(membership.getGroupId())
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_NOT_FOUND));

        User leavingUser = userRepository.findById(userId).orElse(null);
        String leavingUserName = leavingUser != null ? leavingUser.getName() : "Một thành viên";

        // If admin leaves, delete the entire group
        if (group.getAdminId().equals(userId)) {
            // Notify all members that group is being deleted
            List<GroupMember> members = groupMemberRepository.findByGroupId(group.getId());
            for (GroupMember member : members) {
                if (!member.getUserId().equals(userId)) {
                    notificationUseCase.createNotification(
                            member.getUserId(),
                            NotificationType.SYSTEM,
                            "Nhóm đã bị xóa",
                            String.format("Nhóm '%s' đã bị xóa bởi quản trị viên", group.getName()),
                            "GROUP",
                            group.getId()
                    );
                }
            }
            groupMemberRepository.deleteByGroupId(group.getId());
            groupRepository.deleteById(group.getId());
        } else {
            groupMemberRepository.deleteByGroupIdAndUserId(group.getId(), userId);
            
            // Notify admin that member left
            notificationUseCase.createNotification(
                    group.getAdminId(),
                    NotificationType.GROUP_LEAVE,
                    "Thành viên rời nhóm",
                    String.format("%s đã rời khỏi nhóm '%s'", leavingUserName, group.getName()),
                    "GROUP",
                    group.getId()
            );
        }
    }

    private GroupResponse buildGroupResponse(Group group) {
        List<GroupMember> members = groupMemberRepository.findByGroupId(group.getId());

        List<MemberResponse> memberResponses = members.stream()
                .map(member -> {
                    User user = userRepository.findById(member.getUserId()).orElse(null);
                    if (user == null) return null;

                    return MemberResponse.builder()
                            .userId(user.getId())
                            .name(user.getName())
                            .email(user.getEmail())
                            .avatarUrl(user.getAvatarUrl())
                            .isAdmin(user.getId().equals(group.getAdminId()))
                            .joinedAt(member.getJoinedAt())
                            .build();
                })
                .filter(m -> m != null)
                .collect(Collectors.toList());

        User admin = userRepository.findById(group.getAdminId()).orElse(null);

        return GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .adminId(group.getAdminId())
                .adminName(admin != null ? admin.getName() : null)
                .members(memberResponses)
                .createdAt(group.getCreatedAt())
                .build();
    }
}
