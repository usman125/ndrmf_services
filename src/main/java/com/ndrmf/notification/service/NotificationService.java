package com.ndrmf.notification.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ndrmf.common.PagedList;
import com.ndrmf.notification.dto.NotificationItem;
import com.ndrmf.notification.model.Notification;
import com.ndrmf.notification.repository.NotificationRepository;
import com.ndrmf.user.repository.UserRepository;

@Service
public class NotificationService {
	@Autowired private NotificationRepository notificationRepo;
	@Autowired private UserRepository userRepo;
	
	public PagedList<NotificationItem> getNotifications(String username, Pageable pageable) {
		Page<Notification> nots = notificationRepo.findNotificationsForUser(username, pageable);
		
		List<NotificationItem> nItems = nots.get().map(n -> new NotificationItem(n.getId(), n.getCreatedDate(), n.getC2a(), n.getText()))
				.collect(Collectors.toList());
		
		PagedList<NotificationItem> pagedList = new PagedList<>();
		pagedList.setData(nItems);
		pagedList.setHasNext(nots.hasNext());
		pagedList.setHasPrevious(nots.hasPrevious());
		pagedList.setTotalElements(nots.getTotalElements());
		pagedList.setTotalPages(nots.getTotalPages());
		
		
		return pagedList;
	}
	
	public void addNotification(UUID userId, String c2a, String text) {
		Notification noti = new Notification();
		noti.setTo(userRepo.getOne(userId));
		noti.setC2a(c2a);
		noti.setText(text);
		
		notificationRepo.save(noti);
	}
}
