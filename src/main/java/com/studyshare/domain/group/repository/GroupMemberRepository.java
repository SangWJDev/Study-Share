package com.studyshare.domain.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.studyshare.domain.group.entity.GroupMember;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    
}
