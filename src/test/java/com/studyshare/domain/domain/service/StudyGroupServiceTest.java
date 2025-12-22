package com.studyshare.domain.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.studyshare.domain.group.dto.CreateGroupRequest;
import com.studyshare.domain.group.dto.CreateGroupResponse;
import com.studyshare.domain.group.dto.JoinGroupRequest;
import com.studyshare.domain.group.dto.ExitGroupRequest;
import com.studyshare.domain.group.dto.DelegateLeaderRequest;
import com.studyshare.domain.group.entity.GroupMember;
import com.studyshare.domain.group.entity.GroupMemberRole;
import com.studyshare.domain.group.entity.StudyGroup;
import com.studyshare.domain.group.exception.GroupException;
import com.studyshare.domain.group.exception.GroupErrorCode;
import com.studyshare.domain.group.repository.GroupMemberRepository;
import com.studyshare.domain.group.repository.StudyGroupRepository;
import com.studyshare.domain.group.service.StudyGroupService;
import com.studyshare.domain.user.entity.User;
import com.studyshare.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class StudyGroupServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudyGroupRepository studyGroupRepository;

    @Mock
    private GroupMemberRepository groupMemberRepository;

    @InjectMocks
    private StudyGroupService studyGroupService;

    // 테스트 데이터
    private User testUser1;
    private User testUser2;
    private User testUser3;
    private User testUser4;
    private User testUser5;
    private CreateGroupRequest createGroupRequest;
    private StudyGroup testGroup;
    private String inviteCode;
    private GroupMember testGroupMember;

    @BeforeEach
    void setUp() {
        // 테스트용 User 생성
        testUser1 = User.builder().id(1L).email("test1@example.com").password("paswword123").nickname("테스터1")
                .bio("안녕하세요")
                .build();
        testUser2 = User.builder().id(2L).email("test2@example.com").password("paswword123").nickname("테스터2")
                .bio("안녕하세요")
                .build();
        testUser3 = User.builder().id(3L).email("test3@example.com").password("paswword123").nickname("테스터3")
                .bio("안녕하세요")
                .build();
        testUser4 = User.builder().id(4L).email("test4@example.com").password("paswword123").nickname("테스터4")
                .bio("안녕하세요")
                .build();
        testUser5 = User.builder().id(5L).email("test5@example.com").password("paswword123").nickname("테스터5")
                .bio("안녕하세요")
                .build();

        // 테스트용 CreateGroupRequest 생성
        createGroupRequest = new CreateGroupRequest("알고리즘 스터디", 4);

        String uuid = UUID.randomUUID().toString().replace("-", "");
        inviteCode = uuid.substring(0, 12);

        // 테스트용 StudyGroup 생성
        testGroup = StudyGroup.builder().id(1L).name("알고리즘 스터디").inviteCode(inviteCode).maxMembers(4).leaderId(1L)
                .build();

        testGroupMember = GroupMember.builder().id(1L).userId(1L).studyGroupId(1L).role(GroupMemberRole.LEADER).build();

    }

    @Test
    @DisplayName("그룹 생성 성공")
    void createGroup_success() {
        // Given
        when(studyGroupRepository.existsByInviteCode(anyString())).thenReturn(false);
        when(userRepository.findByEmail("test1@example.com")).thenReturn(Optional.of(testUser1));
        when(studyGroupRepository.save(any(StudyGroup.class))).thenReturn(testGroup);
        when(groupMemberRepository.save(any(GroupMember.class))).thenReturn(testGroupMember);

        // When
        CreateGroupResponse response = studyGroupService.createGroup(createGroupRequest, "test1@example.com");

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("알고리즘 스터디");
        assertThat(response.getMaxMembers()).isEqualTo(4);
        assertThat(response.getInviteCode()).isEqualTo(inviteCode);
        assertThat(response.getLeaderId()).isEqualTo(1L);

        verify(studyGroupRepository).existsByInviteCode(anyString());
        verify(userRepository).findByEmail("test1@example.com");
        verify(studyGroupRepository).save(any(StudyGroup.class));
        verify(groupMemberRepository).save(any(GroupMember.class));
    }

    @Test
    @DisplayName("초대 코드 중복 처리")
    void createGroup_InviteCodeDuplicate_Regenerate() {
        // Given
        when(studyGroupRepository.existsByInviteCode(anyString())).thenReturn(true).thenReturn(false);
        when(userRepository.findByEmail("test1@example.com")).thenReturn(Optional.of(testUser1));
        when(studyGroupRepository.save(any(StudyGroup.class))).thenReturn(testGroup);
        when(groupMemberRepository.save(any(GroupMember.class))).thenReturn(testGroupMember);

        // When
        studyGroupService.createGroup(createGroupRequest, "test1@example.com");

        // Then
        verify(studyGroupRepository, atLeast(2)).existsByInviteCode(anyString());
    }

    @Test
    @DisplayName("리더 권한 자동 등록 검증")
    void createGroup_LeaderAutoRegistered() {
     // Given
      String email = "test1@example.com";
      ArgumentCaptor<GroupMember> captor = ArgumentCaptor.forClass(GroupMember.class);

      when(userRepository.findByEmail(email))
          .thenReturn(Optional.of(testUser1));
      when(studyGroupRepository.existsByInviteCode(anyString()))
          .thenReturn(false);
      when(studyGroupRepository.save(any(StudyGroup.class)))
          .thenReturn(testGroup);
      when(groupMemberRepository.save(any(GroupMember.class)))
          .thenReturn(testGroupMember);

      // When
      studyGroupService.createGroup(createGroupRequest, email);

      // Then
      verify(groupMemberRepository).save(captor.capture());

      GroupMember savedMember = captor.getValue();

      assertThat(savedMember.getRole())
          .isEqualTo(GroupMemberRole.LEADER);

      assertThat(savedMember.getUserId()).isEqualTo(1L);
      assertThat(savedMember.getStudyGroupId()).isEqualTo(1L);
    }

    // ==================== joinGroup 테스트 ====================

    @Test
    @DisplayName("그룹 가입 성공")
    void joinGroup_Success() {
        // Given
        JoinGroupRequest request = new JoinGroupRequest(inviteCode);
        Long groupId = 1L;
        String email = "test2@example.com";

        when(studyGroupRepository.findById(groupId)).thenReturn(Optional.of(testGroup));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser2));
        when(groupMemberRepository.countByStudyGroupId(groupId)).thenReturn(1);
        when(groupMemberRepository.existsByUserIdAndStudyGroupId(testUser2.getId(), groupId)).thenReturn(false);

        // When
        studyGroupService.joinGroup(request, email, groupId);

        // Then
        ArgumentCaptor<GroupMember> captor = ArgumentCaptor.forClass(GroupMember.class);
        verify(groupMemberRepository).save(captor.capture());

        GroupMember savedMember = captor.getValue();
        assertThat(savedMember.getUserId()).isEqualTo(testUser2.getId());
        assertThat(savedMember.getStudyGroupId()).isEqualTo(groupId);
        assertThat(savedMember.getRole()).isEqualTo(GroupMemberRole.MEMBER);
    }

    @Test
    @DisplayName("그룹 가입 실패 - 인원 가득참")
    void joinGroup_GroupFull_ThrowsException() {
        // Given
        JoinGroupRequest request = new JoinGroupRequest(inviteCode);
        Long groupId = 1L;
        String email = "test2@example.com";

        when(studyGroupRepository.findById(groupId)).thenReturn(Optional.of(testGroup));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser2));
        when(groupMemberRepository.countByStudyGroupId(groupId)).thenReturn(4); // maxMembers = 4

        // When & Then
        assertThatThrownBy(() -> studyGroupService.joinGroup(request, email, groupId))
            .isInstanceOf(GroupException.class)
            .hasFieldOrPropertyWithValue("errorCode", GroupErrorCode.GROUP_MEMBER_FULL);
    }

    @Test
    @DisplayName("그룹 가입 실패 - 이미 멤버")
    void joinGroup_AlreadyMember_ThrowsException() {
        // Given
        JoinGroupRequest request = new JoinGroupRequest(inviteCode);
        Long groupId = 1L;
        String email = "test2@example.com";

        when(studyGroupRepository.findById(groupId)).thenReturn(Optional.of(testGroup));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser2));
        when(groupMemberRepository.countByStudyGroupId(groupId)).thenReturn(1);
        when(groupMemberRepository.existsByUserIdAndStudyGroupId(testUser2.getId(), groupId)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> studyGroupService.joinGroup(request, email, groupId))
            .isInstanceOf(GroupException.class)
            .hasFieldOrPropertyWithValue("errorCode", GroupErrorCode.ALREADY_GROUP_MEMBER);
    }

    @Test
    @DisplayName("그룹 가입 실패 - 유효하지 않은 초대 코드")
    void joinGroup_InvalidInviteCode_ThrowsException() {
        // Given
        JoinGroupRequest request = new JoinGroupRequest("wrongcode123");
        Long groupId = 1L;
        String email = "test2@example.com";

        when(studyGroupRepository.findById(groupId)).thenReturn(Optional.of(testGroup));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser2));
        when(groupMemberRepository.countByStudyGroupId(groupId)).thenReturn(1);

        // When & Then
        assertThatThrownBy(() -> studyGroupService.joinGroup(request, email, groupId))
            .isInstanceOf(GroupException.class)
            .hasFieldOrPropertyWithValue("errorCode", GroupErrorCode.INVALID_INVITE_CODE);
    }

    // ==================== exitGroup 테스트 ====================

    @Test
    @DisplayName("그룹 탈퇴 성공")
    void exitGroup_Success() {
        // Given
        ExitGroupRequest request = new ExitGroupRequest(1L);
        String email = "test2@example.com";
        GroupMember member = GroupMember.builder()
            .id(2L)
            .userId(testUser2.getId())
            .studyGroupId(1L)
            .role(GroupMemberRole.MEMBER)
            .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser2));
        when(groupMemberRepository.findByUserIdAndStudyGroupId(testUser2.getId(), 1L))
            .thenReturn(Optional.of(member));

        // When
        studyGroupService.exitGroup(request, email);

        // Then
        verify(groupMemberRepository).delete(member);
    }

    @Test
    @DisplayName("그룹 탈퇴 실패 - 리더는 탈퇴 불가")
    void exitGroup_Leader_ThrowsException() {
        // Given
        ExitGroupRequest request = new ExitGroupRequest(1L);
        String email = "test1@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser1));
        when(groupMemberRepository.findByUserIdAndStudyGroupId(testUser1.getId(), 1L))
            .thenReturn(Optional.of(testGroupMember)); // LEADER role

        // When & Then
        assertThatThrownBy(() -> studyGroupService.exitGroup(request, email))
            .isInstanceOf(GroupException.class)
            .hasFieldOrPropertyWithValue("errorCode", GroupErrorCode.CANNOT_LEAVE_AS_LEADER);
    }

    @Test
    @DisplayName("그룹 탈퇴 실패 - 그룹 멤버 아님")
    void exitGroup_NotMember_ThrowsException() {
        // Given
        ExitGroupRequest request = new ExitGroupRequest(1L);
        String email = "test5@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser5));
        when(groupMemberRepository.findByUserIdAndStudyGroupId(testUser5.getId(), 1L))
            .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> studyGroupService.exitGroup(request, email))
            .isInstanceOf(GroupException.class)
            .hasFieldOrPropertyWithValue("errorCode", GroupErrorCode.NOT_GROUP_MEMBER);
    }

    // ==================== delegateLeader 테스트 ====================

    @Test
    @DisplayName("리더 위임 성공")
    void delegateLeader_Success() {
        // Given
        DelegateLeaderRequest request = new DelegateLeaderRequest(1L);
        String email = "test1@example.com";
        Long delegatedUserId = 2L;

        GroupMember currentLeader = GroupMember.builder()
            .id(1L)
            .userId(testUser1.getId())
            .studyGroupId(1L)
            .role(GroupMemberRole.LEADER)
            .build();

        GroupMember newLeader = GroupMember.builder()
            .id(2L)
            .userId(testUser2.getId())
            .studyGroupId(1L)
            .role(GroupMemberRole.MEMBER)
            .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser1));
        when(groupMemberRepository.findByUserIdAndStudyGroupId(testUser1.getId(), 1L))
            .thenReturn(Optional.of(currentLeader));
        when(groupMemberRepository.findByUserIdAndStudyGroupId(delegatedUserId, 1L))
            .thenReturn(Optional.of(newLeader));
        when(studyGroupRepository.findById(1L)).thenReturn(Optional.of(testGroup));

        // When
        studyGroupService.delegateLeader(request, email, delegatedUserId);

        // Then
        assertThat(currentLeader.getRole()).isEqualTo(GroupMemberRole.MEMBER);
        assertThat(newLeader.getRole()).isEqualTo(GroupMemberRole.LEADER);
        verify(studyGroupRepository).findById(1L);
    }

    @Test
    @DisplayName("리더 위임 실패 - 리더가 아님")
    void delegateLeader_NotLeader_ThrowsException() {
        // Given
        DelegateLeaderRequest request = new DelegateLeaderRequest(1L);
        String email = "test2@example.com";
        Long delegatedUserId = 3L;

        GroupMember notLeader = GroupMember.builder()
            .id(2L)
            .userId(testUser2.getId())
            .studyGroupId(1L)
            .role(GroupMemberRole.MEMBER)
            .build();

        StudyGroup group = StudyGroup.builder()
            .id(1L)
            .name("알고리즘 스터디")
            .inviteCode(inviteCode)
            .maxMembers(4)
            .leaderId(testUser1.getId()) // 다른 유저가 리더
            .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser2));
        when(groupMemberRepository.findByUserIdAndStudyGroupId(testUser2.getId(), 1L))
            .thenReturn(Optional.of(notLeader));
        when(groupMemberRepository.findByUserIdAndStudyGroupId(delegatedUserId, 1L))
            .thenReturn(Optional.of(GroupMember.builder().id(3L).userId(testUser3.getId()).studyGroupId(1L).role(GroupMemberRole.MEMBER).build()));
        when(studyGroupRepository.findById(1L)).thenReturn(Optional.of(group));

        // When & Then
        assertThatThrownBy(() -> studyGroupService.delegateLeader(request, email, delegatedUserId))
            .isInstanceOf(GroupException.class)
            .hasFieldOrPropertyWithValue("errorCode", GroupErrorCode.NOT_GROUP_LEADER);
    }

    // ==================== deleteGroup 테스트 ====================

    @Test
    @DisplayName("그룹 삭제 성공")
    void deleteGroup_Success() {
        // Given
        String email = "test1@example.com";
        Long groupId = 1L;

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser1));
        when(groupMemberRepository.findByUserIdAndStudyGroupId(testUser1.getId(), groupId))
            .thenReturn(Optional.of(testGroupMember)); // LEADER role

        // When
        studyGroupService.deleteGroup(email, groupId);

        // Then
        verify(studyGroupRepository).deleteById(groupId);
    }

    @Test
    @DisplayName("그룹 삭제 실패 - 리더가 아님")
    void deleteGroup_NotLeader_ThrowsException() {
        // Given
        String email = "test2@example.com";
        Long groupId = 1L;

        GroupMember notLeader = GroupMember.builder()
            .id(2L)
            .userId(testUser2.getId())
            .studyGroupId(1L)
            .role(GroupMemberRole.MEMBER)
            .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser2));
        when(groupMemberRepository.findByUserIdAndStudyGroupId(testUser2.getId(), groupId))
            .thenReturn(Optional.of(notLeader));

        // When & Then
        assertThatThrownBy(() -> studyGroupService.deleteGroup(email, groupId))
            .isInstanceOf(GroupException.class)
            .hasFieldOrPropertyWithValue("errorCode", GroupErrorCode.NOT_GROUP_LEADER);
    }

}
