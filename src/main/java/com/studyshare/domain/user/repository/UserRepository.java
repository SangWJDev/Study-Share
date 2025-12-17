package com.studyshare.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.studyshare.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
