package com.studyshare.domain.group.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studyshare.domain.group.dto.CreateGroupRequest;
import com.studyshare.domain.group.dto.CreateGroupResponse;
import com.studyshare.domain.group.entity.StudyGroup;
import com.studyshare.domain.group.repository.StudyGroupRepository;
import com.studyshare.domain.user.entity.User;
import com.studyshare.domain.user.exception.UserErrorCode;
import com.studyshare.domain.user.exception.UserException;
import com.studyshare.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudyGroupService {


    private final UserRepository userRepository;

    private final StudyGroupRepository studyGroupRepository;

    @Transactional
    public CreateGroupResponse createGroup(CreateGroupRequest request, String email) {

        String inviteCode = createInviteCode();
        for (int i = 0; i < 3; i++) {
            if (!studyGroupRepository.existsByInviteCode(inviteCode)) {
                break;
            }
            inviteCode = createInviteCode();
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        return CreateGroupResponse.from(studyGroupRepository.save(
                StudyGroup.builder().name(request.getName()).maxMembers(request.getMaxMembers()).inviteCode(inviteCode)
                        .leaderId(user.getId()).build()));
    }

    private String createInviteCode() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid.substring(0, 12);
    }

}
