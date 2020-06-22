package com.ndrmf.engine.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ndrmf.engine.model.QuarterlyProgressReport;
import com.ndrmf.engine.model.QuarterlyProgressReportSection;
import com.ndrmf.engine.repository.ProjectProposalRepository;
import com.ndrmf.engine.repository.QuarterlyProgressReportRepository;
import com.ndrmf.exception.ValidationException;
import com.ndrmf.setting.model.SectionTemplate;
import com.ndrmf.setting.repository.ProcessTypeRepository;
import com.ndrmf.setting.repository.SectionTemplateRepository;
import com.ndrmf.util.enums.ProcessStatus;
import com.ndrmf.util.enums.ProcessType;

@Service
public class QPRService {
	@Autowired private ProjectProposalRepository projProposalRepo;
	@Autowired private SectionTemplateRepository sectionTemplateRepo;
	@Autowired private ProcessTypeRepository processTypeRepo;
	@Autowired private QuarterlyProgressReportRepository qprRepo;
	
	public UUID commenceQPR(UUID proposalId) {
		com.ndrmf.setting.model.ProcessType  processType = processTypeRepo.findById(ProcessType.QPR.toString())
				.orElseThrow(() -> new RuntimeException("QPR Process Type is undefined in the system."));
		
		if(processType.getOwner() == null) {
			new RuntimeException("Process owner undefined for QPR Process");
		}
		
		QuarterlyProgressReport qpr = new QuarterlyProgressReport();
		qpr.setQuarter(1);
		qpr.setDueDate(LocalDate.now().withMonth(3).withDayOfMonth(31));
		qpr.setProposalRef(projProposalRepo.getOne(proposalId));
		qpr.setProcessOwner(processType.getOwner());
		qpr.setStatus(ProcessStatus.DRAFT.getPersistenceValue());
		
		List<SectionTemplate> sts = sectionTemplateRepo
				.findTemplatesForProcessType(ProcessType.QPR.toString());

		if (sts == null || sts.size() == 0) {
			throw new ValidationException("No template defined for QPR process");
		}

		for (SectionTemplate st : sts) {
			QuarterlyProgressReportSection ps = new QuarterlyProgressReportSection();
			ps.setName(st.getSection().getName());
			ps.setTemplateType(st.getTemplateType());
			ps.setTemplate(st.getTemplate());
			ps.setSme(st.getSection().getSme());
			ps.setSectionRef(st.getSection());
			ps.setStatus(ProcessStatus.DRAFT.getPersistenceValue());
			
			qpr.addSection(ps);
		}
		
		return qprRepo.save(qpr).getId();
	}
}
