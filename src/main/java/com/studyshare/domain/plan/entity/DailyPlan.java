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
@Table(name = "daily_plans")
public class DailyPlan extends Plan {

    private LocalDate planDate;

    @Setter
    private boolean completed = false;

    @Builder
    public DailyPlan(String title, String content, User user,  LocalDate planDate) {
        super(title, content, user);
        this.planDate = planDate;
    }
    
}
