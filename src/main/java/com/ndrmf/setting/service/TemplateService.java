package com.ndrmf.setting.service;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ndrmf.exception.ValidationException;
import com.ndrmf.setting.dto.AddSectionRequest;
import com.ndrmf.setting.dto.AddSectionTemplateRequest;
import com.ndrmf.setting.dto.ProcessTemplateItem;
import com.ndrmf.setting.dto.ProcessTypeWithSectionsItem;
import com.ndrmf.setting.dto.UpdateProcessMetaRequest;
import com.ndrmf.setting.model.ProcessType;
import com.ndrmf.setting.model.Section;
import com.ndrmf.setting.model.SectionTemplate;
import com.ndrmf.setting.repository.ProcessTypeRepository;
import com.ndrmf.setting.repository.SectionRepository;
import com.ndrmf.setting.repository.SectionTemplateRepository;
import com.ndrmf.user.repository.ProcessMetaRepository;
import com.ndrmf.user.repository.UserRepository;

@Service
public class TemplateService {
	@Autowired private SectionRepository sectionRepo;
	@Autowired private SectionTemplateRepository templateRepo;
	@Autowired private ProcessMetaRepository processMetaRepo;
	@Autowired private UserRepository userRepo;
	@Autowired private ProcessTypeRepository processTypeRepo;
	
	public void addSectionForProcess(String processType, AddSectionRequest body) {
		Section section = new Section();
		
		section.setEnabled(body.isEnabled());
		section.setName(body.getName());
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
	
	public ProcessTypeWithSectionsItem getMetaForProcess(String processType) {
		List<Section> sections = sectionRepo.findAllSectionsForProcessType(processType);
		
		ProcessTypeWithSectionsItem dto = new ProcessTypeWithSectionsItem();
		sections.forEach(s -> {
			dto.addSection(s.getId(), s.getName(), s.isEnabled());
		});
		
		return dto;
	}
	
	public ProcessTemplateItem getTemplateForProcessType(String processType) {
		List<SectionTemplate> sts = templateRepo.findTemplatesForProcessType(processType);
		
		ProcessTemplateItem dto = new ProcessTemplateItem();
		dto.setProcessType(processType);
		
		sts.forEach(st -> {
			dto.addSection(st.getSection().getName(), st.getTotalScore(), st.getPassingScore(), st.getTemplateType(), st.getTemplate());
		});
		
		return dto;
	}
	
	@Transactional
	public void updateProcessMeta(String processType, UpdateProcessMetaRequest body) {
		ProcessType meta = processTypeRepo.findById(processType)
				.orElseThrow(() -> new ValidationException("Invalid Process Type"));
		
		meta.setOwner(userRepo.getOne(body.getProcessOwnerId()));
		
		processMetaRepo.save(meta);
		
		if(body.getSections() != null) {
			body.getSections().forEach(s -> {
				Section section = sectionRepo.findById(s.getId())
						.orElseThrow(() -> new ValidationException("Invalid Section ID"));
				
				section.setSme(userRepo.getOne(s.getSmeId()));
			});
		}
	}
}
