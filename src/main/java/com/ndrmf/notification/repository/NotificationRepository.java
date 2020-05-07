package com.ndrmf.notification.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ndrmf.notification.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, UUID>{
	
	@Query(value = "SELECT n FROM Notification n JOIN n.to t WHERE t.id = :userId ORDER BY n.createdDate DESC")
	Page<Notification> findNotificationsForUser(@Param("userId") UUID userId, Pageable pageable);
}
