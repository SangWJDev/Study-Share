package com.studyshare.domain.group.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studyshare.domain.group.dto.CreateGroupRequest;
import com.studyshare.domain.group.dto.CreateGroupResponse;
import com.studyshare.domain.group.dto.DelegateLeaderRequest;
import com.studyshare.domain.group.dto.JoinGroupRequest;
import com.studyshare.domain.group.service.StudyGroupService;
import com.studyshare.global.common.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/study-groups")
@RequiredArgsConstructor
public class StudyGroupController {

    private final StudyGroupService studyGroupService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CreateGroupResponse>> createGroup(@AuthenticationPrincipal String email,
            @Valid @RequestBody CreateGroupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(studyGroupService.createGroup(request, email)));

    }

    @PostMapping("/{groupId}/join")
    public ResponseEntity<ApiResponse<Void>> joinGroup(@AuthenticationPrincipal String email,
            @Valid @RequestBody JoinGroupRequest request,
            @PathVariable Long groupId) {
        studyGroupService.joinGroup(request, email, groupId);
        return ResponseEntity.ok().body(ApiResponse.ok());
    }

    @DeleteMapping("/{groupId}/exit")
    public ResponseEntity<ApiResponse<Void>> exitGroup(@AuthenticationPrincipal String email,
        @PathVariable Long groupId
    ) {
        studyGroupService.exitGroup(groupId, email);
        return ResponseEntity.ok().body(ApiResponse.ok());
    }

    @PatchMapping("/{groupId}/leader/{delegatedUserId}")
    public ResponseEntity<ApiResponse<Void>> delegateLeader(@AuthenticationPrincipal String email,
            @PathVariable Long groupId,
            @PathVariable Long delegatedUserId) {
        studyGroupService.delegateLeader(groupId, email, delegatedUserId);
        return ResponseEntity.ok().body(ApiResponse.ok());
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<ApiResponse<Void>> deleteGroup(@AuthenticationPrincipal String email,
            @PathVariable Long groupId) {
        studyGroupService.deleteGroup(email, groupId);
        return ResponseEntity.ok().body(ApiResponse.ok());
    }

}
