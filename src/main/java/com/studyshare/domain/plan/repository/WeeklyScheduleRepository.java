package com.studyshare.domain.plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.studyshare.domain.plan.entity.WeeklySchedule;

public interface WeeklyScheduleRepository extends JpaRepository<WeeklySchedule, Long> {

}
