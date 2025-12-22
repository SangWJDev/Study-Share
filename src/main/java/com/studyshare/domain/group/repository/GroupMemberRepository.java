package com.studyshare.domain.group.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.studyshare.domain.group.entity.GroupMember;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    Optional<GroupMember> findByUserIdAndStudyGroupId(Long userId, Long groupId);

    int countByStudyGroupId(Long groupId);

    boolean existsByUserIdAndStudyGroupId(Long userId, Long groupId);
}
