package com.studyshare.domain.group.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studyshare.domain.group.dto.CreateGroupRequest;
import com.studyshare.domain.group.dto.CreateGroupResponse;
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

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<Void>> joinGroup(@AuthenticationPrincipal String email,
        @Valid @RequestBody JoinGroupRequest request) {
            studyGroupService.joinGroup(request, email);
            return ResponseEntity.ok().body(ApiResponse.ok());
        }

}
