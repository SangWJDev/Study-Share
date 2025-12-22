package com.studyshare.domain.group.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.studyshare.domain.group.entity.StudyGroup;

public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {

    boolean existsByInviteCode(String inviteCode);

    Optional<StudyGroup> findByInviteCode(String invitedCode);
    
}
