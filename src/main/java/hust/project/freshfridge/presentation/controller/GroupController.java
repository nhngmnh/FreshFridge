package hust.project.freshfridge.presentation.controller;

import hust.project.freshfridge.application.dto.request.AddMemberRequest;
import hust.project.freshfridge.application.dto.request.CreateGroupRequest;
import hust.project.freshfridge.application.dto.request.RemoveMemberRequest;
import hust.project.freshfridge.application.dto.response.ApiResponse;
import hust.project.freshfridge.application.dto.response.GroupResponse;
import hust.project.freshfridge.application.dto.response.MemberResponse;
import hust.project.freshfridge.application.usecase.GroupUseCase;
import hust.project.freshfridge.infrastructure.security.CurrentUser;
import hust.project.freshfridge.infrastructure.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupUseCase groupUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<GroupResponse>> createGroup(
            @CurrentUser UserPrincipal currentUser,
            @Valid @RequestBody CreateGroupRequest request
    ) {
        GroupResponse response = groupUseCase.createGroup(currentUser.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<GroupResponse>> getMyGroup(
            @CurrentUser UserPrincipal currentUser
    ) {
        GroupResponse response = groupUseCase.getGroup(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/members")
    public ResponseEntity<ApiResponse<List<MemberResponse>>> getGroupMembers(
            @CurrentUser UserPrincipal currentUser
    ) {
        List<MemberResponse> members = groupUseCase.getGroupMembers(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách thành viên thành công", members));
    }

    @PostMapping("/add-member")
    public ResponseEntity<ApiResponse<GroupResponse>> addMember(
            @CurrentUser UserPrincipal currentUser,
            @Valid @RequestBody AddMemberRequest request
    ) {
        GroupResponse response = groupUseCase.addMember(currentUser.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/remove-member")
    public ResponseEntity<ApiResponse<Void>> removeMember(
            @CurrentUser UserPrincipal currentUser,
            @Valid @RequestBody RemoveMemberRequest request
    ) {
        groupUseCase.removeMember(currentUser.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/leave")
    public ResponseEntity<ApiResponse<Void>> leaveGroup(
            @CurrentUser UserPrincipal currentUser
    ) {
        groupUseCase.leaveGroup(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
