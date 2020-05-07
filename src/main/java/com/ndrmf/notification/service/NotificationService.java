package com.ndrmf.notification.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ndrmf.common.PagedList;
import com.ndrmf.notification.dto.NotificationItem;
import com.ndrmf.notification.model.Notification;
import com.ndrmf.notification.repository.NotificationRepository;

@Service
public class NotificationService {
	@Autowired private NotificationRepository notificationRepo;
	
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
}
