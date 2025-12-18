package com.studyshare.domain.plan.entity;

import java.time.LocalDate;

import com.studyshare.domain.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "long_term_goals")
public class LongTermGoal extends Plan {

    private LocalDate targetDate;

    @Setter
    private boolean achieved = false;

    @Builder
    public LongTermGoal(String title, String content, User user, LocalDate targetDate) {
        super(title, content, user);
        this.targetDate = targetDate;
    }

}
