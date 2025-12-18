package com.studyshare.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.studyshare.domain.notification.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long>{
    
}
