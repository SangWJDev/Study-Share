package com.studyshare.domain.group.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "group_member", uniqueConstraints = @UniqueConstraint(name = "uk_group_member_user_id_and_study_group_id", columnNames = {
        "user_id", "study_group_id" }))
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "study_group_id", nullable = false)
    private Long studyGroupId;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private GroupMemberRole role;

    @CreatedDate
    private LocalDateTime joinedAt;

    @Builder
    public GroupMember(Long id, Long userId, Long studyGroupId, GroupMemberRole role) {
        this.id = id;
        this.userId = userId;
        this.studyGroupId = studyGroupId;
        this.role = role;
    }

}
