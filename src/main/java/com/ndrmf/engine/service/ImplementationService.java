package com.ndrmf.engine.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.print.attribute.standard.DateTimeAtCompleted;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ndrmf.engine.dto.AddSubProjectSectionReviewRequest;
import com.ndrmf.engine.dto.SubProjectDocumentItem;
import com.ndrmf.engine.dto.SubProjectDocumentItem.SubProjectDocumentSectionItem;
import com.ndrmf.engine.dto.SubProjectDocumentListItem;
import com.ndrmf.engine.dto.SubProjectDocumentSectionRequest;
import com.ndrmf.engine.model.ExtendedAppraisalSection;
import com.ndrmf.engine.model.ProjectProposal;
import com.ndrmf.engine.model.SubProjectDocument;
import com.ndrmf.engine.model.SubProjectDocumentSection;
import com.ndrmf.engine.repository.ProjectProposalRepository;
import com.ndrmf.engine.repository.SubProjectDocumentRepository;
import com.ndrmf.engine.repository.SubProjectDocumentSectionRepository;
import com.ndrmf.exception.ValidationException;
import com.ndrmf.setting.model.SectionTemplate;
import com.ndrmf.setting.repository.ProcessTypeRepository;
import com.ndrmf.setting.repository.SectionTemplateRepository;
import com.ndrmf.user.dto.UserLookupItem;
import com.ndrmf.util.enums.ProcessStatus;
import com.ndrmf.util.enums.ProcessType;

@Service
public class ImplementationService {
	@Autowired private ProjectProposalRepository projProposalRepo;
	@Autowired private SectionTemplateRepository sectionTemplateRepo;
	@Autowired private ProcessTypeRepository processTypeRepo;
	@Autowired private SubProjectDocumentRepository subProjectRepo;
	@Autowired private SubProjectDocumentSectionRepository subProjectSectionRepo;
	/*
	 * After signing GIA, FIP has to submit sub project documents (Template shared by PAM)
	 * within 120 days. The system provides triggers at 90, 100 and then 120 days about the
	 * submission of the documents.
	 * 
	 * This method will iterate through all proposals -> PIP and commence subproject document phase
	 * for proposals wherever applicable
	 */
	//@Scheduled(cron = "0 0 1 * * ?")
	public void commenceSubProjectDocument(UUID proposalId) {
		
		ProjectProposal p = projProposalRepo.findById(proposalId)
				.orElseThrow(() -> new ValidationException("Invalid request ID"));
		
		List<SectionTemplate> sts = sectionTemplateRepo
				.findTemplatesForProcessType(ProcessType.SUB_PROJECT_DOCUMENT.name());

		if (sts == null || sts.size() == 0) {
			throw new ValidationException("No template defined for SUB_PROJECT_DOCUMENT process");
		}
		
		com.ndrmf.setting.model.ProcessType processType = processTypeRepo.findById(ProcessType.SUB_PROJECT_DOCUMENT.name())
				.orElseThrow(() -> new RuntimeException("Process Owner not defined for SUB_PROJECT_DOCUMENT"));
		
		SubProjectDocument doc = new SubProjectDocument();
		doc.setProcessOwner(processType.getOwner());
		doc.setStartDate(LocalDate.now());
		doc.setEndDate(LocalDate.now().plusDays(30));
		doc.setStatus(ProcessStatus.PENDING.getPersistenceValue());
		doc.setProposalRef(p);
		
		for (SectionTemplate t : sts) {
			SubProjectDocumentSection s = new SubProjectDocumentSection();
			s.setSme(t.getSection().getSme());
			s.setTemplate(t.getTemplate());
			s.setTemplateType(t.getTemplateType());
			s.setStatus(ProcessStatus.PENDING.getPersistenceValue());
			s.setSectionRef(t.getSection());

			doc.addSection(s);
		}
		
		subProjectRepo.save(doc);
	}
	
	public List<SubProjectDocumentListItem> getPendingSubProjectDocuments(UUID userId) {
		List<SubProjectDocument> docs = subProjectRepo.getSubProjectsForFIPByStatus(userId, ProcessStatus.PENDING.getPersistenceValue());
		
		List<SubProjectDocumentListItem> dtos = new ArrayList<>();
		docs.forEach(d -> {
			SubProjectDocumentListItem item = new SubProjectDocumentListItem();
			item.setId(d.getId());
			item.setProposalName(d.getProposalRef().getName());
			item.setStartDate(d.getStartDate());
			item.setEndDate(d.getEndDate());
			item.setStatus(d.getStatus());
			
			dtos.add(item);
		});
		
		return dtos;
	}
	
	public List<SubProjectDocumentListItem> getSubProjectDocuments(UUID userId) {
		List<SubProjectDocument> docs = subProjectRepo.getSubProjectsForPOOrReviewer(userId, Arrays.asList(ProcessStatus.DRAFT.getPersistenceValue()));
		
		List<SubProjectDocumentListItem> dtos = new ArrayList<>();
		docs.forEach(d -> {
			SubProjectDocumentListItem item = new SubProjectDocumentListItem();
			item.setId(d.getId());
			item.setProposalName(d.getProposalRef().getName());
			item.setStartDate(d.getStartDate());
			item.setEndDate(d.getEndDate());
			item.setStatus(d.getStatus());
			
			dtos.add(item);
		});
		
		return dtos;
	}
	
	public SubProjectDocumentItem getSubProjectDocument(UUID id, UUID userId) {
		SubProjectDocument doc = subProjectRepo.findById(id)
				.orElseThrow(() -> new ValidationException("Invalid ID"));
		
		
		SubProjectDocumentItem dto = new SubProjectDocumentItem();
		dto.setStartDate(doc.getStartDate());
		dto.setEndDate(doc.getEndDate());
		dto.setStatus(doc.getStatus());
		
		doc.getSections().forEach(s -> {
			SubProjectDocumentSectionItem item = new SubProjectDocumentSectionItem();
			item.setId(s.getId());
			item.setAssigned(userId.equals(s.getSme().getId()));
			item.setComments(s.getComments());
			item.setData(s.getData());
			item.setSme(new UserLookupItem(s.getSme().getId(), s.getSme().getFullName()));
			item.setStatus(s.getStatus());
			item.setTemplate(s.getTemplate());
			item.setTemplateType(s.getTemplateType());
			
			dto.addSection(item);
		});
		
		return dto;
	}
	
	@Transactional
	public void submitSubProjectDocumentSection(UUID docId, UUID userId, SubProjectDocumentSectionRequest body) {
		SubProjectDocument doc = subProjectRepo.findById(docId)
				.orElseThrow(() -> new ValidationException("Invalid ID"));
		
		SubProjectDocumentSection section = doc.getSections().stream()
				.filter(s -> s.getId().equals(body.getId()))
				.findFirst()
				.orElseThrow(() -> new ValidationException("Invalid Section ID"));
		
		section.setData(body.getData());
		section.setStatus(ProcessStatus.COMPLETED.getPersistenceValue());
	}
	
	
	@Transactional
	public void requestSubProjectDocumentSectionReview(UUID sectionId) {
		SubProjectDocumentSection section = subProjectSectionRepo.findById(sectionId)
				.orElseThrow(() -> new ValidationException("Invalid Section ID"));
		
		section.setReviewStatus(ProcessStatus.REVIEW_PENDING.getPersistenceValue());
		section.getSubProjectDocumentRef().setStatus(ProcessStatus.REVIEW_PENDING.getPersistenceValue());
	}
	
	@Transactional
	public void submitSubProjectDocumentSectionReview(UUID sectionId, AddSubProjectSectionReviewRequest body) {
		SubProjectDocumentSection section = subProjectSectionRepo.findById(sectionId)
				.orElseThrow(() -> new ValidationException("Invalid Section ID"));
		
		section.setComments(body.getComments());
		
		section.setReviewStatus(ProcessStatus.REVIEW_COMPLETED.getPersistenceValue());
		
		//TODO: update status of doc
	}
}
