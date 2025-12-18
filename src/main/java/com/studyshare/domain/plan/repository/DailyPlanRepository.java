package com.studyshare.domain.plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.studyshare.domain.plan.entity.DailyPlan;

public interface DailyPlanRepository extends JpaRepository<DailyPlan, Long> {

}
