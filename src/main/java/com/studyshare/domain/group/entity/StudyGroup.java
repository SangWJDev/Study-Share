package com.studyshare.domain.group.entity;

import com.studyshare.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "study_groups", uniqueConstraints = @UniqueConstraint(name = "uk_study_group_invite_code", columnNames = "invite_code"))
public class StudyGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    @Column(name = "invite_code", nullable = false, length = 12)
    private String inviteCode;

    private int maxMembers;

    @Setter
    private Long leaderId;

    @Builder
    public StudyGroup(String name, int maxMembers, String inviteCode, Long leaderId) {
        this.name = name;
        this.maxMembers = maxMembers;
        this.inviteCode = inviteCode;
        this.leaderId = leaderId;
    }

    public boolean checkLeader(Long id) {
        return leaderId == id ? true : false;
    }
}
