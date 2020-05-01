package com.ndrmf.setting.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ndrmf.setting.dto.AddSectionRequest;
import com.ndrmf.setting.dto.AddSectionTemplateRequest;
import com.ndrmf.setting.dto.ProcessTemplateItem;
import com.ndrmf.setting.dto.ProcessTypeWithSectionsItem;
import com.ndrmf.setting.model.Section;
import com.ndrmf.setting.model.SectionTemplate;
import com.ndrmf.setting.repository.SectionRepository;
import com.ndrmf.setting.repository.SectionTemplateRepository;

@Service
public class TemplateService {
	@Autowired private SectionRepository sectionRepo;
	@Autowired private SectionTemplateRepository templateRepo;
	
	public void addSectionForProcess(String processType, AddSectionRequest body) {
		Section section = new Section();
		
		section.setEnabled(body.isEnabled());
		section.setName(body.getName());
		section.setProcessType(processType);
		
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
	
	public List<ProcessTypeWithSectionsItem> getAllProcessesMeta() {
		List<Section> sections = sectionRepo.findAll();
		
		Map<String, List<Section>> groupedSections = 
				sections.stream().collect(Collectors.groupingBy(Section::getProcessType));
		
		List<ProcessTypeWithSectionsItem> dtos = new ArrayList<>();
		
		groupedSections .forEach((k, v) -> {
			ProcessTypeWithSectionsItem dto = new ProcessTypeWithSectionsItem();
			dto.setProcessType(k);
			
			v.forEach(s -> {
				dto.addSection(s.getId(), s.getName(), s.isEnabled());
			});
			
			dtos.add(dto);
		});
		
		return dtos;
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
}
