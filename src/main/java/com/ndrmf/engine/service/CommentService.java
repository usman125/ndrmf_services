package com.ndrmf.engine.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ndrmf.engine.dto.AddQualificationSectionReviewRequest;
import com.ndrmf.engine.model.QualificationSection;
import com.ndrmf.engine.repository.QualificationSectionRepository;
import com.ndrmf.exception.ValidationException;
import com.ndrmf.util.enums.ReviewStatus;

@Service
public class CommentService {
	@Autowired private QualificationSectionRepository qualSectionRepo;
	
	public void addQualificationSectionReview(UUID byUserId, UUID sectionId, AddQualificationSectionReviewRequest body) {
		QualificationSection section = qualSectionRepo.findById(sectionId)
				.orElseThrow(() -> new ValidationException("Invalid Section ID"));
		
		if(!section.getSme().getId().equals(byUserId)) {
			throw new ValidationException("Only SME can add review for this section. Authorized SME is: " + section.getSme().getFullName());
		}
		
		section.setComments(body.getComments());
		section.setControlWiseComments(body.getControlWiseComments());
		section.setRating(body.getRating());
		section.setStatus(body.getStatus());
		
		section.setReviewStatus(ReviewStatus.COMPLETED.getPersistenceValue());
		
		qualSectionRepo.save(section);
	}
}
