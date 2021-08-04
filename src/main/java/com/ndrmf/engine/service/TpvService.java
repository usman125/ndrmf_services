package com.ndrmf.engine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.common.rFile;
import com.ndrmf.engine.dto.CommenceTpv;
import com.ndrmf.engine.dto.TpvTasksFilesListItem;
import com.ndrmf.engine.dto.TpvTaskSubmitRequest;
import com.ndrmf.engine.dto.TpvTasksListItem;
import com.ndrmf.engine.model.*;
import com.ndrmf.engine.repository.TpvRepository;
import com.ndrmf.engine.repository.TpvTasksRepository;
import com.ndrmf.engine.repository.ProjectProposalRepository;
import com.ndrmf.exception.ValidationException;
import com.ndrmf.notification.service.NotificationService;
import com.ndrmf.engine.service.FileStoreService;
import com.ndrmf.setting.repository.ProcessTypeRepository;
import com.ndrmf.setting.repository.SectionTemplateRepository;
import com.ndrmf.user.dto.UserLookupItem;
import com.ndrmf.user.model.User;
import com.ndrmf.user.repository.UserRepository;
import com.ndrmf.user.service.UserService;
import com.ndrmf.util.enums.ProcessStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class TpvService {
	@Autowired
	private SectionTemplateRepository sectionTemplateRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private UserService userService;
	@Autowired
	private TpvRepository tpvRepo;
	@Autowired
	private TpvTasksRepository tpvTasksRepo;
	@Autowired
	private ProjectProposalRepository projPropRepo;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProcessTypeRepository processTypeRepo;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private FileStoreService fileStoreService;


	public void commenceTpv(AuthPrincipal initiatorPrincipal, CommenceTpv body) {

		ProjectProposal p = projPropRepo.findById(body.getProposalId())
				.orElseThrow(() -> new ValidationException("INALID PROPOSAL ID"));

		Tpv tpv = new Tpv();

		tpv.setInitiatedBy(userRepo.findById(initiatorPrincipal.getUserId()).get());
		tpv.setStatus(ProcessStatus.PENDING.getPersistenceValue());
		tpv.setCreated_at(new Date());
		tpv.setProjectProposalRef(p);

		TpvTasks tpv1 = new TpvTasks();
		tpv1.setInitiatedBy(userRepo.findById(initiatorPrincipal.getUserId()).get());
		tpv1.setStatus(ProcessStatus.PENDING.getPersistenceValue());
		tpv1.setCreated_at(new Date());
		tpv1.setName("TORs");
		tpv1.setOrderNum(1);

		tpv.addTask(tpv1);

		TpvTasks tpv2 = new TpvTasks();
		tpv2.setInitiatedBy(userRepo.findById(initiatorPrincipal.getUserId()).get());
		tpv2.setStatus(ProcessStatus.PENDING.getPersistenceValue());
		tpv2.setCreated_at(new Date());
		tpv2.setName("Approval");
		tpv2.setOrderNum(2);

		tpv.addTask(tpv2);

		TpvTasks tpv3 = new TpvTasks();
		tpv3.setInitiatedBy(userRepo.findById(initiatorPrincipal.getUserId()).get());
		tpv3.setStatus(ProcessStatus.PENDING.getPersistenceValue());
		tpv3.setCreated_at(new Date());
		tpv3.setName("EOI");
		tpv3.setOrderNum(3);

		tpv.addTask(tpv3);

		TpvTasks tpv4 = new TpvTasks();
		tpv4.setInitiatedBy(userRepo.findById(initiatorPrincipal.getUserId()).get());
		tpv4.setStatus(ProcessStatus.PENDING.getPersistenceValue());
		tpv4.setCreated_at(new Date());
		tpv4.setName("RFP");
		tpv4.setOrderNum(4);

		tpv.addTask(tpv4);

		TpvTasks tpv5 = new TpvTasks();
		tpv5.setInitiatedBy(userRepo.findById(initiatorPrincipal.getUserId()).get());
		tpv5.setStatus(ProcessStatus.PENDING.getPersistenceValue());
		tpv5.setCreated_at(new Date());
		tpv5.setName("Shortlisting");
		tpv5.setOrderNum(5);

		tpv.addTask(tpv5);

		TpvTasks tpv6 = new TpvTasks();
		tpv6.setInitiatedBy(userRepo.findById(initiatorPrincipal.getUserId()).get());
		tpv6.setStatus(ProcessStatus.PENDING.getPersistenceValue());
		tpv6.setCreated_at(new Date());
		tpv6.setName("Draft TPV");
		tpv6.setOrderNum(6);


		tpv.addTask(tpv6);

		TpvTasks tpv7 = new TpvTasks();
		tpv7.setInitiatedBy(userRepo.findById(initiatorPrincipal.getUserId()).get());
		tpv7.setStatus(ProcessStatus.PENDING.getPersistenceValue());
		tpv7.setCreated_at(new Date());
		tpv7.setName("Comments");
		tpv7.setOrderNum(7);

		tpv.addTask(tpv7);

		TpvTasks tpv8 = new TpvTasks();
		tpv8.setInitiatedBy(userRepo.findById(initiatorPrincipal.getUserId()).get());
		tpv8.setStatus(ProcessStatus.PENDING.getPersistenceValue());
		tpv8.setCreated_at(new Date());
		tpv8.setName("Final Report");
		tpv8.setOrderNum(8);

		tpv.addTask(tpv8);

		TpvTasks tpv9 = new TpvTasks();
		tpv9.setInitiatedBy(userRepo.findById(initiatorPrincipal.getUserId()).get());
		tpv9.setStatus(ProcessStatus.PENDING.getPersistenceValue());
		tpv9.setCreated_at(new Date());
		tpv9.setName("Ratings of project");
		tpv9.setOrderNum(9);

		tpv.addTask(tpv9);

		p.setTpv(tpv);

		tpvRepo.save(tpv);
	}


	@Transactional
	public List<TpvTasksListItem> getTpvRequestsByProposalId(AuthPrincipal currentUser, UUID proposalId) throws IOException {
		List<TpvTasks> props = new ArrayList<TpvTasks>();
		props.addAll(tpvTasksRepo.findRequestsForProjectPropsal(proposalId));
		List<TpvTasksListItem> dtos = new ArrayList<>();
		props.forEach(q -> {
			TpvTasksListItem ppli = new TpvTasksListItem();
			ppli.setId(q.getId());
			ppli.setStatus(q.getStatus());
			ppli.setInitiatedBy(new UserLookupItem(q.getInitiatedBy().getId(), q.getInitiatedBy().getFullName()));
			ppli.setGeneralFields(q.getGeneralFields());
			ppli.setCreated_at(q.getCreated_at());
			ppli.setOrderNum(q.getOrderNum());
			ppli.setName(q.getName());
			q.getFiles().stream().forEach(c -> {
				TpvTasksFilesListItem tpvfli = new TpvTasksFilesListItem();
				tpvfli.setName(c.getFileRef().getFileName());
				tpvfli.setPath(c.getFileRef().getPath());
				tpvfli.setCreated_by(c.getCreatedBy());
				ppli.addFile(tpvfli);
			});
			dtos.add(ppli);
		});
		return dtos;
	}

	@Transactional
	public void submitTpvTask(UUID currentUserId, UUID taskId, TpvTaskSubmitRequest body) throws IOException {
		TpvTasks props;
		props = tpvTasksRepo.findById(taskId)
				.orElseThrow(() -> new ValidationException("INVALID TASK ID"));
		props.setGeneralFields(body.getData());
		props.setStatus(ProcessStatus.COMPLETED.getPersistenceValue());
		if(props.getTpvRef().getTasks().stream().allMatch(
				r -> r.getStatus().equals(ProcessStatus.COMPLETED.getPersistenceValue())
		)){
			props.getTpvRef().setStatus(ProcessStatus.COMPLETED.getPersistenceValue());
		}
	}


	@Transactional
	public String uploadFileForTpvTask(
			AuthPrincipal principle,
			UUID advanceId,
			MultipartFile file
	) throws IOException {

		TpvTasks tpv = tpvTasksRepo.findById(advanceId)
				.orElseThrow(() -> new ValidationException("TASK ID is not valid."));

		rFile persistedFile;
		persistedFile = fileStoreService.saveFile(file, principle.getUserId());

		TpvTasksFiles tpvf = new TpvTasksFiles();
		tpvf.setFileRef(persistedFile);
		tpvf.setPicByte(file.getBytes());
		tpv.addFile(tpvf);

		return persistedFile.getPath();
	}
}