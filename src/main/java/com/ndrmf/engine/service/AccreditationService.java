package com.ndrmf.engine.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ndrmf.engine.dto.EligibilityListItem;
import com.ndrmf.engine.dto.EligibilityRequest;
import com.ndrmf.engine.model.Eligibility;
import com.ndrmf.engine.repository.EligibilityRepository;
import com.ndrmf.exception.ValidationException;
import com.ndrmf.setting.repository.ProcessTypeRepository;
import com.ndrmf.user.repository.UserRepository;
import com.ndrmf.util.enums.ProcessStatus;
import com.ndrmf.util.enums.ProcessType;

@Service
public class AccreditationService {
	@Autowired private UserRepository userRepo;
	@Autowired private ProcessTypeRepository processTypeRepo;
	@Autowired private EligibilityRepository eligbiligyRepo;
	
	public void addEligibility(String initiatedByUsername, EligibilityRequest body) {
		Set<String> constraintStatuses = new HashSet<>(); 
		constraintStatuses.add(ProcessStatus.DRAFT.getPersistenceValue());
		constraintStatuses.add(ProcessStatus.APPROVED.getPersistenceValue());
		constraintStatuses.add(ProcessStatus.UNDER_REVIEW.getPersistenceValue());
		
		int existingRequests = eligbiligyRepo.checkCountForUserWithStatuses(initiatedByUsername, constraintStatuses);
		
		if(existingRequests > 0) {
			throw new ValidationException("A request for this username already exists which is either APPROVED, UNDER REVIEW or in DRAFT Status");
		}
		
		Eligibility elig = new Eligibility();
		
		elig.setInitiatedBy(userRepo.findByUsername(initiatedByUsername));
		com.ndrmf.setting.model.ProcessType processMeta = processTypeRepo.findById(ProcessType.ELIGIBILITY.name()).get();
		
		elig.setProcessOwner(processMeta.getOwner());
		elig.setStatus(ProcessStatus.UNDER_REVIEW.getPersistenceValue());
		elig.setTemplate(body.getTemplate());
		elig.setData(body.getData());
		
		eligbiligyRepo.save(elig);
	}
	
	public List<EligibilityListItem> getRequests(String ownerUsername, ProcessStatus status) {
		List<Eligibility> eligs = Collections.emptyList();
		
		if(status == null) {
			eligs = eligbiligyRepo.findAllRequestsForOwner(ownerUsername);
		}
		else {
			eligs = eligbiligyRepo.findRequestsForOwnerByStatus(ownerUsername, status.getPersistenceValue());	
		}
		
		List<EligibilityListItem> dtos = eligs.stream()
				.map(e -> new EligibilityListItem(e.getId(), e.getProcessOwner().getFullName(), e.getCreatedDate(), e.getStatus()))
				.collect(Collectors.toList());
		
		return dtos;
	}
}