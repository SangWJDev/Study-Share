package com.studyshare.domain.plan.entity;

import java.time.LocalDate;

import com.studyshare.domain.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "weekly_schedules")
public class WeeklySchedule extends Plan {

    private LocalDate startDate;

    private LocalDate endDate;

    @Builder
    public WeeklySchedule(String title, String content, User user, LocalDate startDate, LocalDate endDate) {
        super(title, content, user);
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
