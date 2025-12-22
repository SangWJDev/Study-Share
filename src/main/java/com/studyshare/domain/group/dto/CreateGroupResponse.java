package com.studyshare.domain.group.dto;

import java.time.LocalDateTime;

import com.studyshare.domain.group.entity.StudyGroup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateGroupResponse {

    private long id;

    private String name;

    private String inviteCode;

    private int maxMembers;

    private long leaderId;

    private LocalDateTime createdAt;

    public static CreateGroupResponse from(StudyGroup studyGroup) {

        return CreateGroupResponse.builder().id(studyGroup.getId()).name(studyGroup.getName())
                .inviteCode(studyGroup.getInviteCode()).maxMembers(studyGroup.getMaxMembers())
                .leaderId(studyGroup.getLeaderId()).createdAt(studyGroup.getCreatedAt()).build();

    }

}
