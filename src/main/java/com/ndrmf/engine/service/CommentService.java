package com.ndrmf.engine.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ndrmf.engine.dto.AddCommentRequest;
import com.ndrmf.engine.model.QualificationComment;
import com.ndrmf.engine.repository.QualificationCommentRepository;
import com.ndrmf.engine.repository.QualificationRepository;
import com.ndrmf.engine.repository.QualificationSectionRepository;
import com.ndrmf.user.repository.UserRepository;

@Service
public class CommentService {
	@Autowired private QualificationCommentRepository qualCommentRepo;
	@Autowired private UserRepository userRepo;
	@Autowired private QualificationRepository qualRepo;
	@Autowired private QualificationSectionRepository qualSectionRepo;
	
	public long addQualificationComment(UUID byUserId, UUID qualId, UUID sectionId, AddCommentRequest body) {
		QualificationComment c = new QualificationComment();
		
		c.setQualification(qualRepo.getOne(qualId));
		c.setSection(qualSectionRepo.getOne(sectionId));
		c.setForFip(body.isToFip());
		c.setBy(userRepo.getOne(byUserId));
		c.setText(body.getText());
		
		if(body.getThreadId() != null) {
			c.setThread(qualCommentRepo.getOne(body.getThreadId()));	
		}
		
		return qualCommentRepo.save(c).getId();
	}
}
