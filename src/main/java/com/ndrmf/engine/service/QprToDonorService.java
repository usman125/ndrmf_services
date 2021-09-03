package com.ndrmf.engine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.common.rFile;
import com.ndrmf.engine.dto.*;
import com.ndrmf.engine.model.*;
import com.ndrmf.engine.repository.*;
import com.ndrmf.exception.ValidationException;
import com.ndrmf.notification.dto.TaskItem;
import com.ndrmf.notification.service.NotificationService;
import com.ndrmf.setting.model.SectionTemplate;
import com.ndrmf.setting.repository.ProcessTypeRepository;
import com.ndrmf.setting.repository.SectionTemplateRepository;
import com.ndrmf.user.dto.UserLookupItem;
import com.ndrmf.user.model.User;
import com.ndrmf.user.repository.UserRepository;
import com.ndrmf.user.service.UserService;
import com.ndrmf.util.KeyValue;
import com.ndrmf.util.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;


@Service
public class QprToDonorService {
	@Autowired
	private QprToDonorRepository qprToDonorRepo;
	@Autowired
	private SectionTemplateRepository sectionTemplateRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private QprToDonorTaskRepository qprtodnortaskRepo;
	@Autowired
	private UserService userService;
	@Autowired
	private QprToDonorTaskRepository qprToDonorTaskRepo;
	@Autowired
	private QprToDonorSectionRepository projPropSectionRepo;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProcessTypeRepository processTypeRepo;
	@Autowired
	private NotificationService notificationService;


	public UUID commenceQprToDonor(AuthPrincipal initiatorPrincipal) {
		final UUID initiatorUserId = initiatorPrincipal.getUserId();
		//we will also receive body.optionalUserId (a JV guy!)


		QprToDonor qprToDonor = new QprToDonor();

		qprToDonor.setInitiatedBy(userRepo.getOne(initiatorUserId));
		qprToDonor.setStatus(ProcessStatus.DRAFT.getPersistenceValue());

		List<SectionTemplate> sts = sectionTemplateRepo
				.findTemplatesForProcessType(ProcessType.QPR_TO_DONOR.toString());

		if (sts == null || sts.size() == 0) {
			throw new ValidationException("No template defined for this process");
		}

		for (SectionTemplate st : sts) {
			QprToDonorSection ps = new QprToDonorSection();
			ps.setName(st.getSection().getName());
			ps.setPassingScore(st.getPassingScore());
			ps.setTotalScore(st.getTotalScore());
			ps.setTemplateType(st.getTemplateType());
			ps.setTemplate(st.getTemplate());
			ps.setSme(st.getSection().getSme());
			ps.setSectionRef(st.getSection());

			qprToDonor.addSection(ps);
		}


		qprToDonor = qprToDonorRepo.save(qprToDonor);
		
		try {
			notificationService.sendPlainTextEmail(
					initiatorPrincipal.getEmail(),
					initiatorPrincipal.getFullName(),
					"Qpr To Donor Added Confirmation",
					"Your qpr to donor has been initiated. Please complete all the sections.");
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

		return qprToDonor.getId();
	}

	public QprToDonorItem getQprToDonorRequest(UUID id, AuthPrincipal principal) {
		final UUID userId = principal.getUserId();

		QprToDonor p = qprToDonorRepo.findById(id)
				.orElseThrow(() -> new ValidationException("Invalid request ID"));

		QprToDonorItem dto = new QprToDonorItem();

		dto.setInitiatedBy(new UserLookupItem(p.getInitiatedBy().getId(), p.getInitiatedBy().getFullName()));
		dto.setStatus(p.getStatus());
		dto.setSubmittedAt(p.getCreatedDate());

		if (p.getInitiatedBy().getId().equals(userId)) {
			List<QprToDonorTask> reassignmentComments = qprtodnortaskRepo.findAllTasksForAssigneeAndRequest(userId, id);

			if (reassignmentComments != null && reassignmentComments.size() > 0) {
				QprToDonorTask lastTask = reassignmentComments.get(reassignmentComments.size() - 1);

				TaskItem ti = new TaskItem();
				ti.setComments(lastTask.getComments());
				ti.setEndDate(lastTask.getEndDate());
				ti.setStartDate(lastTask.getStartDate());
				ti.setStatus(lastTask.getStatus());

				dto.setReassignmentTask(ti);
			}
		}

		p.getSections().forEach(qs -> {
			SectionItem section = new SectionItem();

			section.setRevisionNo(qs.getRevisionNo());
			section.setAssigned(qs.getSme().getId().equals(userId));
			section.setData(qs.getData());
			section.setId(qs.getId());
			section.setName(qs.getName());
			section.setPassingScore(qs.getPassingScore());
			section.setSme(new UserLookupItem(qs.getSme().getId(), qs.getSme().getFullName()));
			section.setTemplate(qs.getTemplate());
			section.setTemplateType(qs.getTemplateType());
			section.setTotalScore(qs.getTotalScore());
			section.setReviewStatus(qs.getReviewStatus());
			section.setReviewCompletedDate(qs.getReviewCompletedOn());
			section.setReassignmentStatus(qs.getReassignmentStatus());
			section.setOrderNum(qs.getSectionRef().getOrderNum());
			section.setReassignmentComments(qs.getReassignmentComments());

			List<QprToDonorTask> tasks = qprToDonorTaskRepo.findTasksForSection(qs.getId(), PageRequest.of(0, 1));

			if (tasks != null && tasks.size() > 0) {
				section.setReviewDeadline(tasks.get(0).getEndDate());
			}

			if (qs.getReviews() != null && qs.getReviews().size() > 0) {
				QprToDonorSectionReview latestReview = qs.getReviews().get(qs.getReviews().size() - 1);

				section.setReview(latestReview.getCreatedDate(), null, null, latestReview.getStatus(),
						latestReview.getComments());

				qs.getReviews().forEach(r -> {
					section.addReviewHistory(r.getCreatedDate(), null, null, r.getStatus(), r.getComments());

					GeneralCommentItem gci = new GeneralCommentItem();
					if (r.getReviewAddedBy() != null) {
						gci.setAddedBy(r.getReviewAddedBy().getFullName());
					}
					gci.setComment(r.getComments());
					gci.setCreatedAt(r.getCreatedDate());
					gci.setSections(Arrays.asList(r.getSectionRef().getName()));
					
					gci.setSectionsWithIds(Stream.of(new KeyValue(r.getSectionRef().getId(), r.getSectionRef().getName())).collect(Collectors.toSet()));
					
					dto.addComment(gci);
				});
			}

			dto.addSection(section);
		});

		return dto;
	}

	public List<QprToDonorListItem> getQprToDonorRequests(AuthPrincipal currentUser, ProcessStatus status) throws IOException {
		List<QprToDonor> props = new ArrayList<QprToDonor>();

		props.addAll(qprToDonorRepo.findRequestsForInitiatorOrSME(currentUser.getUserId()));
		List<QprToDonorListItem> dtos = new ArrayList<>();
		props.forEach(q -> {
			QprToDonorListItem ppli = new QprToDonorListItem(
					q.getId(),
					q.getInitiatedBy().getFullName(),
					q.getCreatedDate(),
					q.getStatus());
			dtos.add(ppli);
		});
		return dtos;
	}
	

	public void submitQprToDonorSection(UUID userId, UUID qprtodonorId, QprToDonorSectionRequest body,
			FormAction action) {
		QprToDonor p = qprToDonorRepo.findById(qprtodonorId)
				.orElseThrow(() -> new ValidationException("Invalid request ID"));

		QprToDonorSection section = p.getSections().stream().filter(s -> s.getId().equals(body.getId())).findAny()
				.orElseThrow(() -> new ValidationException("Invalid section ID"));

		section.setData(body.getData());

//		if (p.getStatus() != null && p.getStatus().equals(ProcessStatus.DRAFT.getPersistenceValue())){
//			if (action.equals(FormAction.SAVE)) {
//				p.setStatus(ProcessStatus.DRAFT.getPersistenceValue());
//			} else if (action.equals(FormAction.SUBMIT)) {
//				p.setStatus(ProcessStatus.UNDER_REVIEW.getPersistenceValue());
//			}
//		}

		if (section.getReassignmentStatus() != null
				&& section.getReassignmentStatus().equals(ReassignmentStatus.PENDING.getPersistenceValue())) {
			section.setReassignmentStatus(ReassignmentStatus.COMPLETED.getPersistenceValue());
		}

		if (section.getReassignmentStatus() != null){
			if (p.getSections().stream()
					.allMatch(r -> r.getData() != null &&
							((r.getReassignmentStatus().equals(ProcessStatus.COMPLETED.getPersistenceValue()) ||
											r.getReassignmentStatus() == null)))) {
				p.setStatus(ProcessStatus.COMPLETED.getPersistenceValue());
				com.ndrmf.setting.model.ProcessType processType = processTypeRepo.findById(ProcessType.QPR_TO_DONOR.name())
						.orElseThrow(() -> new RuntimeException("Process Owner not defined for SUB_PROJECT_DOCUMENT"));
				try {
					notificationService.sendPlainTextEmail(
							processType.getOwner().getEmail(),
							processType.getOwner().getFullName(),
							"Qpr to donor request has been completed at NDRMF",
							"Fip has completed the QPr to donor request."
							+ "\nPlease visit http://ndrmfdev.herokuapp.com/view-qpr/"+p.getId()
							+ " to review and process the request(s).\n"
					);
				}
				catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		if (section.getReassignmentStatus() == null){
			if (p.getSections().stream()
					.allMatch(r -> r.getData() != null &&
							(r.getReassignmentStatus() == null))) {
				p.setStatus(ProcessStatus.COMPLETED.getPersistenceValue());
				com.ndrmf.setting.model.ProcessType processType = processTypeRepo.findById(ProcessType.QPR_TO_DONOR.name())
						.orElseThrow(() -> new RuntimeException("Process Owner not defined for SUB_PROJECT_DOCUMENT"));
				try {
					notificationService.sendPlainTextEmail(
							processType.getOwner().getEmail(),
							processType.getOwner().getFullName(),
							"Qpr to donor request has been completed at NDRMF",
							"Fip has completed the QPr to donor request."
									+ "\nPlease visit http://ndrmfdev.herokuapp.com/view-qpr/"+p.getId()
									+ " to review and process the request(s).\n"
					);
				}
				catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		qprToDonorRepo.save(p);

		if (action == FormAction.SUBMIT) {
			// TODO raise event
			// eventPublisher.publishEvent(new QualificationCreatedEvent(this, q));
		}
	}

	@Transactional
	public void addQprToDonorTask(UUID sectionId, UUID currentUserId, AddProposalTaskRequest body) {
		QprToDonorSection section = projPropSectionRepo.findById(sectionId)
				.orElseThrow(() -> new ValidationException("Invalid Section ID"));

		User dmPAM = userService.getDMPAM()
				.orElseThrow(() -> new ValidationException("Missing Configuration - DM PAM not defined"));

		if (!currentUserId.equals(dmPAM.getId())) {
			throw new ValidationException("Only DM PAM can add tasks. Authorized user is: " + dmPAM.getFullName());
		}

		QprToDonorTask task = new QprToDonorTask();

		task.setStartDate(body.getStartDate());
		task.setEndDate(body.getEndDate());
		task.setComments(body.getComments());
		task.setSection(section);
		task.setQprtodonor(section.getQprtodonorRef());
		task.setAssignee(section.getSme());
		task.setStatus(TaskStatus.PENDING.getPersistenceValue());

		section.setReviewStatus(ReviewStatus.PENDING.getPersistenceValue());

		qprToDonorTaskRepo.save(task);

		// TODO - trigger notification event
	}

	@Transactional
	public void reassignQprToDonorSections(UUID qprtodonorId, UUID userId, Set<UUID> sectionIds, String comments) {
		QprToDonor p = qprToDonorRepo.findById(qprtodonorId)
				.orElseThrow(() -> new ValidationException("Invalid QPR TO DONOR ID"));

		sectionIds.forEach(sId -> {
			QprToDonorSection section = p.getSections()
					.stream()
					.filter(s -> s.getId().equals(sId))
					.findFirst()
					.orElseThrow(() -> new ValidationException("Invalid QPR TO DONOR Section ID"));

			section.setReassignmentStatus(ProcessStatus.PENDING.getPersistenceValue());
			section.setReassignmentComments(comments);
		});

		p.setStatus(ProcessStatus.PENDING.getPersistenceValue());
	}


	// compress the image bytes before storing it in the database
	public static byte[] compressBytes(byte[] data) {
		Deflater deflater = new Deflater();
		deflater.setInput(data);
		deflater.finish();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		try {
			outputStream.close();
		} catch (IOException e) {
		}

		System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

		return outputStream.toByteArray();

	}

	// uncompress the image bytes before returning it to the angular application
	public static byte[] decompressBytes(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		try {
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
		} catch (IOException ioe) {
		} catch (DataFormatException e) {
		}
		return outputStream.toByteArray();
	}
}