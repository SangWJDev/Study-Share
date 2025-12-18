package com.studyshare.domain.plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.studyshare.domain.plan.entity.LongTermGoal;

public interface LongTermGoalRepository extends JpaRepository<LongTermGoal, Long> {
    
}
