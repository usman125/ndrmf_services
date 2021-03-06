package com.ndrmf.setting.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.util.enums.ProcessStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ndrmf.exception.ValidationException;
import com.ndrmf.setting.dto.AddSectionRequest;
import com.ndrmf.setting.dto.AddSectionTemplateRequest;
import com.ndrmf.setting.dto.ProcessTemplateItem;
import com.ndrmf.setting.dto.ProcessTypeWithSectionsItem;
import com.ndrmf.setting.dto.UpdateProcessMetaRequest;
import com.ndrmf.setting.dto.UpdateSectionTemplateRequest;
import com.ndrmf.setting.model.ProcessType;
import com.ndrmf.setting.model.Section;
import com.ndrmf.setting.model.SectionTemplate;
import com.ndrmf.setting.repository.ProcessTypeRepository;
import com.ndrmf.setting.repository.SectionRepository;
import com.ndrmf.setting.repository.SectionTemplateRepository;
import com.ndrmf.user.dto.UserLookupItem;
import com.ndrmf.user.repository.UserRepository;


@Service
public class TemplateService {
	@Autowired private SectionRepository sectionRepo;
	@Autowired private SectionTemplateRepository templateRepo;
	@Autowired private UserRepository userRepo;
	@Autowired private ProcessTypeRepository processTypeRepo;
	
	public void addSectionForProcess(String processType, AddSectionRequest body) {
		
		/*Section section = new Section();
		ProcessType p = new ProcessType();
		p.setName(processType);
		UUID ownerId = processTypeRepo.getPOByProcessType(processType);
		
		p.setOwner(userRepo.getOne(ownerId));
		section.setProcessType(p);
		section.setEnabled(body.isEnabled());
		section.setName(body.getName());
		
		sectionRepo.save(section);*/
		
		Section section = new Section();
		
		section.setEnabled(body.isEnabled());
		section.setName(body.getName());
		section.setOrderNum(body.getOrderNum());
		section.setProcessType(processTypeRepo.getOne(processType));
		
		sectionRepo.save(section);
	}
	
	@Transactional
	public void addTemplateForSection(UUID sectionId, AddSectionTemplateRequest body) {
		SectionTemplate template = new SectionTemplate();
		
		template.setSection(sectionRepo.getOne(sectionId));
		template.setEnabled(body.isEnableAndEffective());
		template.setPassingScore(body.getPassingScore());
		template.setTotalScore(body.getTotalScore());
		template.setTemplateType(body.getTemplateType());
		template.setTemplate(body.getTemplate());
		
		
		
		if(body.isEnableAndEffective()) {
			List<SectionTemplate> existingTemplatesForSection = templateRepo.findBySectionId(sectionId);
			
			existingTemplatesForSection.forEach(t -> {
				t.setEnabled(false);
			});
		}
		
		templateRepo.save(template);
	}
	
	@Transactional
	public void updateTemplateForSection(UUID sectionId, UUID templateId, UpdateSectionTemplateRequest body) {
		SectionTemplate template = templateRepo.findATemplateOfASection(sectionId, templateId);
		
		template.setEnabled(body.isEnableAndEffective());
		template.setPassingScore(body.getPassingScore());
		template.setTotalScore(body.getTotalScore());
		template.setTemplateType(body.getTemplateType());
		template.setTemplate(body.getTemplate());
		
		
		
		if(body.isEnableAndEffective()) {
			List<SectionTemplate> existingTemplatesForSection = templateRepo.findBySectionId(sectionId);
			
			existingTemplatesForSection.forEach(t -> {
				t.setEnabled(false);
			});
		}
		
		templateRepo.save(template);
	}
	
	public ProcessTypeWithSectionsItem getMetaForProcess(String processType) {
		ProcessType meta = processTypeRepo.findById(processType)
				.orElseThrow(() -> new ValidationException("Invalid Process Type"));
		
		
		List<Section> sections = sectionRepo.findAllSectionsForProcessType(processType);
		ProcessTypeWithSectionsItem dto = new ProcessTypeWithSectionsItem();
//		System.out.println(processType);
//		System.out.println(ProcessStatus.GIA);
//		System.out.println(processType.equals(ProcessStatus.GIA.toString()));

		if (sections.isEmpty())
		{
			if (processType.equals(ProcessStatus.GIA.toString())){
				if(meta.getOwner() != null) {
					dto.setProcessOwner(new UserLookupItem(meta.getOwner().getId(), meta.getOwner().getFullName()));
					return dto;
				}else{
					throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GIA PROCESS OWNER NOT DEFINED");
				}
			}else{
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
			}

		}


		if(meta.getOwner() != null) {
			dto.setProcessOwner(new UserLookupItem(meta.getOwner().getId(), meta.getOwner().getFullName()));	
		}
		
		sections.forEach(s -> {
			if(s.getSme() != null) {
				UserLookupItem sme = new UserLookupItem(s.getSme().getId(), s.getSme().getFullName());
				
				dto.addSection(s.getId(), s.getName(), s.isEnabled(), sme, s.getOrderNum());
			}
			else {
				dto.addSection(s.getId(), s.getName(), s.isEnabled(), null, s.getOrderNum());
			}
		});
		
		return dto;
	}
	
	public ProcessTemplateItem getTemplateForProcessType(String processType) {
		ProcessType pt = processTypeRepo.findById(processType)
				.orElseThrow(() -> new ValidationException("Invalid Process Type"));
		
		if((processType.equals(com.ndrmf.util.enums.ProcessType.ELIGIBILITY.toString())
				|| processType.equals(com.ndrmf.util.enums.ProcessType.QUALIFICATION.toString())
			) && pt.getOwner() == null) {
			throw new ValidationException("Incomplete Process Meta. Missing Process Owner");
		}
		
		List<SectionTemplate> sts = templateRepo.findTemplatesForProcessType(processType);
		
		List<String> invalidSections = new ArrayList<>();
		
		if(!processType.equals(com.ndrmf.util.enums.ProcessType.GIA_CHECKLIST.toString())) {
			sts.forEach(st -> {	
				if(st.getSection().getSme() == null) {
					invalidSections.add(st.getSection().getName());
				}
			});	
		}
		
		if(invalidSections.size() > 0) {
			throw new ValidationException(String.format("Incomplete Process Meta. Missing SME for section(s): %s", invalidSections.stream().collect(Collectors.joining(" , "))));
		}
		
		ProcessTemplateItem dto = new ProcessTemplateItem();
		dto.setProcessType(processType);
		
		sts.forEach(st -> {	
			dto.addSection(st.getSection().getId(), st.getSection().getName(), st.getTotalScore(), st.getPassingScore(), st.getTemplateType(),
					st.getTemplate());
		});
		
		return dto;
	}
	
	@Transactional
	public void updateProcessMeta(String processType, UpdateProcessMetaRequest body) {
		ProcessType meta = processTypeRepo.findById(processType)
				.orElseThrow(() -> new ValidationException("Invalid Process Type"));
		
		if(body.getProcessOwnerId() != null) {
			meta.setOwner(userRepo.getOne(body.getProcessOwnerId()));	
		}
		
		if(body.getSections() != null) {
			body.getSections().forEach(s -> {
				Section section = sectionRepo.findById(s.getId())
						.orElseThrow(() -> new ValidationException("Invalid Section ID"));
				
				section.setSme(userRepo.getOne(s.getSmeId()));
			});
		}
	}
	
	public String addSubProcessType(com.ndrmf.util.enums.ProcessType processType, String name) {
		
		if(name == null) {
			throw new ValidationException("Process Type name cannot be null");
		}
		
		name = name.toUpperCase().replace(" ", "_");
		
		ProcessType pt = new ProcessType();
		pt.setName(name);
		pt.setParent(processTypeRepo.findById(processType.name())
				.orElseThrow(() -> new ValidationException("Invalid Parent Process Type")));
		
		processTypeRepo.save(pt);
		
		return name;		
	}
	
	public Set<String> getSubProcessTypesForProcess(com.ndrmf.util.enums.ProcessType processType){
		final Set<String> subProcessTypes = processTypeRepo.getSubProcessTypes(processType.name());
		return subProcessTypes;
	}

	public List<String> getProcessTypesByOwner(AuthPrincipal principal){
		final List<ProcessType> processTypes = processTypeRepo.getProcessTypesByOwner(principal.getUserId());
		final List<String> pts = new ArrayList<>();
		processTypes.forEach(pt -> {
			pts.add(pt.getName());
		});
		return pts;
	}
}
