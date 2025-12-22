package com.studyshare.domain.group.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studyshare.domain.group.dto.CreateGroupRequest;
import com.studyshare.domain.group.dto.CreateGroupResponse;
import com.studyshare.domain.group.dto.DelegateLeaderRequest;
import com.studyshare.domain.group.dto.JoinGroupRequest;
import com.studyshare.domain.group.entity.GroupMember;
import com.studyshare.domain.group.entity.GroupMemberRole;
import com.studyshare.domain.group.entity.StudyGroup;
import com.studyshare.domain.group.exception.GroupErrorCode;
import com.studyshare.domain.group.exception.GroupException;
import com.studyshare.domain.group.repository.GroupMemberRepository;
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

    private final GroupMemberRepository groupMemberRepository;

    @Transactional
    public CreateGroupResponse createGroup(CreateGroupRequest request, String email) {

        String inviteCode;

        while (true) {
            inviteCode = createInviteCode();
            if (!studyGroupRepository.existsByInviteCode(inviteCode)) {
                break;
            }
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        StudyGroup studyGroup = studyGroupRepository.save(
                StudyGroup.builder().name(request.getName()).maxMembers(request.getMaxMembers()).inviteCode(inviteCode)
                        .leaderId(user.getId()).build());

        groupMemberRepository.save(GroupMember.builder().userId(user.getId()).studyGroupId(studyGroup.getId())
                .role(GroupMemberRole.LEADER).build());

        return CreateGroupResponse.from(studyGroup);
    }

    @Transactional
    public void joinGroup(JoinGroupRequest request, String email, Long groupId) {
        StudyGroup studyGroup = studyGroupRepository.findById(groupId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.GROUP_NOT_FOUND));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        int countGroupMembers = groupMemberRepository.countByStudyGroupId(groupId);

        if (!studyGroup.getInviteCode().equals(request.getInvitedCode())) {
            throw new GroupException(GroupErrorCode.INVALID_INVITE_CODE);
        }

        if (studyGroup.getMaxMembers() <= countGroupMembers) {
            throw new GroupException(GroupErrorCode.GROUP_MEMBER_FULL);
        }

        if (groupMemberRepository.existsByUserIdAndStudyGroupId(user.getId(), groupId)) {
            throw new GroupException(GroupErrorCode.ALREADY_GROUP_MEMBER);
        }

        groupMemberRepository.save(GroupMember.builder().userId(user.getId()).studyGroupId(groupId)
                .role(GroupMemberRole.MEMBER).build());

    }

    @Transactional
    public void exitGroup(Long groupId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        GroupMember groupMember = groupMemberRepository.findByUserIdAndStudyGroupId(user.getId(), groupId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.NOT_GROUP_MEMBER));

        if (groupMember.getRole().equals(GroupMemberRole.LEADER)) {
            throw new GroupException(GroupErrorCode.CANNOT_LEAVE_AS_LEADER);
        } else {
            groupMemberRepository.delete(groupMember);
        }

    }

    @Transactional
    public void delegateLeader(Long groupId, String email, Long delegatedUserId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        GroupMember delegateMember = groupMemberRepository
                .findByUserIdAndStudyGroupId(user.getId(), groupId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.NOT_GROUP_MEMBER));

        GroupMember delegatedMemeber = groupMemberRepository
                .findByUserIdAndStudyGroupId(delegatedUserId, groupId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.NOT_GROUP_MEMBER));

        StudyGroup studyGroup = studyGroupRepository.findById(groupId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.GROUP_NOT_FOUND));

        if (!studyGroup.checkLeader(user.getId())) {
            throw new GroupException(GroupErrorCode.NOT_GROUP_LEADER);
        }

        delegateMember.setRole(GroupMemberRole.MEMBER);
        delegatedMemeber.setRole(GroupMemberRole.LEADER);
        studyGroup.setLeaderId(delegatedUserId);

    }

    @Transactional
    public void deleteGroup(String email, Long groupId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        GroupMember member = groupMemberRepository
                .findByUserIdAndStudyGroupId(user.getId(), groupId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.NOT_GROUP_MEMBER));

        if (!member.getRole().equals(GroupMemberRole.LEADER)) {
            throw new GroupException(GroupErrorCode.NOT_GROUP_LEADER);
        }

        studyGroupRepository.deleteById(member.getStudyGroupId());
        groupMemberRepository.deleteAllByStudyGroupId(member.getStudyGroupId());

    }

    private String createInviteCode() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid.substring(0, 12);
    }

}
