package com.ndrmf.notification.service;

import static com.google.common.collect.Lists.newArrayList;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.mail.internet.InternetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ndrmf.common.PagedList;
import com.ndrmf.notification.dto.NotificationItem;
import com.ndrmf.notification.model.Notification;
import com.ndrmf.notification.repository.NotificationRepository;
import com.ndrmf.user.repository.UserRepository;

import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;

@Service
public class NotificationService {
	@Autowired private NotificationRepository notificationRepo;
	@Autowired private UserRepository userRepo;
	@Autowired private EmailService emailService;
	
	@Value("${notifier.mail.fromEmail}") private String fromEmail;
	@Value("${notifier.mail.fromName}") private String fromName;
	
	public PagedList<NotificationItem> getNotifications(UUID userId, Pageable pageable) {
		Page<Notification> nots = notificationRepo.findNotificationsForUser(userId, pageable);
		
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
	
	@Async
	public void sendPlainTextEmail(String toEmail, String toName, String subject, String body)
			throws UnsupportedEncodingException{
		
		final Email email = DefaultEmail.builder()
                .from(new InternetAddress(fromEmail,
                        fromName))
                .to(newArrayList(
                        new InternetAddress(toEmail,
                                toName)))
                .subject(subject)
                .body(body)
                .encoding("UTF-8").build();

        emailService.send(email);
//		System.out.println("SYSTEM EMAIL SEND");
	}
}
