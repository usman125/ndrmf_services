package com.ndrmf.complaint.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

//import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ndrmf.complaint.dto.AcknowledgeComplaint;
import com.ndrmf.complaint.dto.AddComplaint;
import com.ndrmf.complaint.dto.AddComplaintAssignee;
import com.ndrmf.complaint.dto.AddComplaintReview;
import com.ndrmf.complaint.dto.ComplainantReviewDto;
import com.ndrmf.complaint.dto.ComplaintAppealDto;
import com.ndrmf.complaint.dto.ListComplainantReview;
import com.ndrmf.complaint.dto.ListComplaint;
import com.ndrmf.complaint.dto.ListComplaintAssignee;
import com.ndrmf.complaint.dto.ListComplaintAttachment;
import com.ndrmf.complaint.dto.ListComplaintReview;
import com.ndrmf.complaint.model.ComplainantReview;
import com.ndrmf.complaint.model.Complaint;
import com.ndrmf.complaint.model.ComplaintAppeal;
import com.ndrmf.complaint.model.ComplaintAssignee;
import com.ndrmf.complaint.model.ComplaintAttachment;
import com.ndrmf.complaint.model.ComplaintReview;
import com.ndrmf.complaint.repository.ComplainantReviewRepository;
import com.ndrmf.complaint.repository.ComplaintAppealRepository;
import com.ndrmf.complaint.repository.ComplaintAssigneeRepository;
import com.ndrmf.complaint.repository.ComplaintAttachmentRepository;
import com.ndrmf.complaint.repository.ComplaintRepository;
import com.ndrmf.complaint.repository.ComplaintReviewRepository;
import com.ndrmf.engine.service.FileStoreService;
import com.ndrmf.exception.ValidationException;
import com.ndrmf.notification.service.NotificationService;
import com.ndrmf.user.dto.UserLookupItem;
import com.ndrmf.user.model.User;
import com.ndrmf.user.repository.UserRepository;
import com.ndrmf.util.enums.ComplaintStatus;

@Service
public class ComplaintService {
	@Autowired
	private ComplaintRepository complaintRepo;
	@Autowired
	private ComplaintReviewRepository reviewRepo;
	@Autowired
	private ComplaintAttachmentRepository attachmentRepo;
	@Autowired
	private ComplaintAssigneeRepository assigneeRepo;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private FileStoreService fileStoreService;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	@Value("${image.upload.dir}")
	private File uploadDirRoot;
//	@Autowired
//	private ModelMapper modelMapper;
	@Autowired
	private ComplainantReviewRepository complainantReviewRepo;
	@Autowired
	private ComplaintAppealRepository appealRepo;

	@Transactional
	public UUID addComplaint(AddComplaint body) {
		Complaint complaint = new Complaint();

		complaint.setComplainantName(body.getComplainantName());
		complaint.setAddress(body.getAddress());
		complaint.setGender(body.getGender());
		complaint.setContactNo(body.getContactNo());
		complaint.setCnic(body.getCnic());
		complaint.setEmail(body.getEmail());
		complaint.setComplaintAgainstPersonOrDepartment(body.getComplaintAgainstPersonOrDepartment());
		complaint.setOrganizationName(body.getOrganizationName());
		complaint.setVendor(body.getVendor());
		complaint.setConsultantName(body.getConsultantName());
		complaint.setComplaintCategory(body.getComplaintCategory());
		complaint.setShortDescription(body.getShortDescription());
		complaint.setShortDescFectorCausingProblem(body.getShortDescFectorCausingProblem());
		complaint.setComplaintDateTime(LocalDateTime.now());
		complaint.setComplainantSignature(body.getComplainantSignature());
		complaint.setComplaintLocation(body.getComplaintLocation());
		complaint.setComplaintSubLocation(body.getComplaintSubLocation());
		complaint.setInternalStatus(ComplaintStatus.INITIATED.getPersistenceValue());

		return complaintRepo.save(complaint).getId();
	}

	@Transactional
	public void acknowledgeComplaint(UUID complaintId, AcknowledgeComplaint body) {
		Complaint complaint = complaintRepo.findById(complaintId)
				.orElseThrow(() -> new ValidationException("Invalid complaint ID"));

		complaint.setStatus(ComplaintStatus.IN_PROCESS.getPersistenceValue());
		complaint.setInternalStatus(ComplaintStatus.MARKED_TO_FOCAL_PERSON.getPersistenceValue());
		complaint.setSeqNo(body.getSeqNo());
		complaint.setPriority(body.getPriority());

		List<ComplaintAssignee> assigneeList = new ArrayList<>();
		body.getAssignee().forEach(x -> {
			ComplaintAssignee assignee = new ComplaintAssignee();
			assignee.setAssignedDateTime(LocalDateTime.now());
			assignee.setAssignedPerson(userRepo.findById(x.getUserId()).get());
			assignee.setComplaintRef(complaint);
			assigneeRepo.save(assignee);

		});

		complaintRepo.save(complaint);

		this.intimateComplainantThroughEmail(complaint.getId(), "Complaint Received",
				"Your complaint has been received. Complaint response/status will be communicated within 5 working days.");

	}

	public File uploadPath(Complaint e, MultipartFile file) throws IOException {
		File uploadPath = Paths.get(this.uploadDirRoot.getPath(), e.getId().toString()).toFile();
		if (uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}
		return new File(uploadPath.getAbsolutePath(), file.getOriginalFilename());
	}

	@Transactional
	public void addComplaintReview(UUID byUserId, UUID complaintId, AddComplaintReview body) {
		Complaint complaint = complaintRepo.findById(complaintId)
				.orElseThrow(() -> new ValidationException("Invalid Complaint ID"));

		ComplaintReview review = new ComplaintReview();
		if (byUserId != null) {
			review.setUserRef(userRepo.findById(byUserId).get());
		}
		review.setComplaintRef(complaint);
		review.setDateTime(LocalDateTime.now());
		review.setComments(body.getComments());
		review.setSatisfied(body.isSatisfied());

		reviewRepo.save(review);
	}

	public void intimateComplainantThroughEmail(UUID complaintId, String subject, String body) {

		Complaint complaint = complaintRepo.findById(complaintId)
				.orElseThrow(() -> new ValidationException("Invalid complaint ID"));

		try {
			notificationService.sendPlainTextEmail(complaint.getEmail(), complaint.getComplainantName(), subject, body);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@Transactional
	public void updateInternalStatus(UUID complaintId, String status) {

		Complaint complaint = complaintRepo.findById(complaintId)
				.orElseThrow(() -> new ValidationException("Invalid complaint ID"));
		complaint.setInternalStatus(status);
		complaintRepo.save(complaint);
	}

	@Transactional
	public void updateComplaintStatus(UUID complaintId, String status) {
		Complaint complaint = complaintRepo.findById(complaintId)
				.orElseThrow(() -> new ValidationException("Invalid complaint ID"));
		complaint.setStatus(status);
		complaintRepo.save(complaint);

	}

	public Complaint findComplaintById(UUID id) {
		return complaintRepo.findById(id).get();

	}

	public void addComplaintAssignee(List<AddComplaintAssignee> dto, UUID complaintId, String status, String title,
			String body) {
		Complaint complaint = complaintRepo.findById(complaintId)
				.orElseThrow(() -> new ValidationException("Invalid complaint ID"));
		dto.forEach(x -> {
			ComplaintAssignee assignee = new ComplaintAssignee();
			assignee.setComplaintRef(complaint);
			assignee.setAssignedDateTime(LocalDateTime.now());
			assignee.setAssignedPerson(userRepo.findById(x.getAssignedPersonId()).get());
			assigneeRepo.save(assignee);

			notificationService.addNotification(userRepo.findById(x.getAssignedPersonId()).get().getId(), title, body);
		});

		complaint.setInternalStatus(status);
		complaintRepo.save(complaint);

	}

	public List<ListComplaint> getAllComplaintsByUser(UUID userId) {

		List<Complaint> dtos = complaintRepo.findComplaintByUserId(userId);

		List<ListComplaint> complaintList = new ArrayList<>();

		dtos.forEach(x -> {
			ListComplaint dto = new ListComplaint();

			dto.setId(x.getId());
			dto.setComplainantName(x.getComplainantName());
			dto.setAddress(x.getAddress());
			dto.setGender(x.getGender());
			dto.setContactNo(x.getContactNo());
			dto.setCnic(x.getCnic());
			dto.setEmail(x.getEmail());
			dto.setComplaintAgainstPersonOrDepartment(x.getComplaintAgainstPersonOrDepartment());
			dto.setOrganizationName(x.getOrganizationName());
			dto.setVendor(x.getVendor());
			dto.setConsultantName(x.getConsultantName());
			dto.setComplaintCategory(x.getComplaintCategory());
			dto.setShortDescription(x.getShortDescription());
			dto.setShortDescFectorCausingProblem(x.getShortDescFectorCausingProblem());
			dto.setComplaintDateTime(x.getComplaintDateTime());
			dto.setComplainantSignature(x.getComplainantSignature());
			dto.setComplaintLocation(x.getComplaintLocation());
			dto.setComplaintSubLocation(x.getComplaintSubLocation());
			dto.setStatus(x.getStatus());
			dto.setInternalStatus(x.getInternalStatus());
			dto.setSeqNo(x.getSeqNo());
			dto.setPriority(x.getPriority());
			dto.setAssignees(this.getComplaintAssigneeByComplaintId(x.getId()));
			dto.setReviews(this.getComplaintReviewByComplaintId(x.getId()));
			dto.setComplainantReviews(this.getComplainantReviews(x.getId()));

			complaintList.add(dto);
		});

		return complaintList;
	}

	public List<ListComplaint> getAllComplaints() {

		List<Complaint> complaint = complaintRepo.findAll();
		List<ListComplaint> dtos = new ArrayList<ListComplaint>();
		complaint.forEach(x -> {
			ListComplaint dto = new ListComplaint();
			dto.setId(x.getId());
			dto.setComplainantName(x.getComplainantName());
			dto.setAddress(x.getAddress());
			dto.setGender(x.getGender());
			dto.setContactNo(x.getContactNo());
			dto.setCnic(x.getCnic());
			dto.setEmail(x.getEmail());
			dto.setComplaintAgainstPersonOrDepartment(x.getComplaintAgainstPersonOrDepartment());
			dto.setOrganizationName(x.getOrganizationName());
			dto.setVendor(x.getVendor());
			dto.setConsultantName(x.getConsultantName());
			dto.setComplaintCategory(x.getComplaintCategory());
			dto.setShortDescription(x.getShortDescription());
			dto.setShortDescFectorCausingProblem(x.getShortDescFectorCausingProblem());
			dto.setComplaintDateTime(x.getComplaintDateTime());
//			dto.setComplainantSignature(complainantSignature);
			dto.setComplaintLocation(x.getComplaintLocation());
			dto.setComplaintSubLocation(x.getComplaintSubLocation());
			dto.setStatus(x.getStatus());
			dto.setInternalStatus(x.getInternalStatus());
			dto.setSeqNo(x.getSeqNo());
			dto.setPriority(x.getPriority());
			dto.setAssignees(this.getComplaintAssigneeByComplaintId(x.getId()));
			dto.setReviews(this.getComplaintReviewByComplaintId(x.getId()));
			dto.setComplainantReviews(this.getComplainantReviews(x.getId()));

			dtos.add(dto);
		});

		return dtos;
	}

	public ListComplaint getComplaint(UUID complaintId) {
		Complaint complaint = complaintRepo.findById(complaintId)
				.orElseThrow(() -> new ValidationException("Invalid complaint ID"));

		ListComplaint dto = new ListComplaint();
		dto.setId(complaint.getId());
		dto.setComplainantName(complaint.getComplainantName());
		dto.setAddress(complaint.getAddress());
		dto.setGender(complaint.getGender());
		dto.setContactNo(complaint.getContactNo());
		dto.setCnic(complaint.getCnic());
		dto.setEmail(complaint.getEmail());
		dto.setComplaintAgainstPersonOrDepartment(complaint.getComplaintAgainstPersonOrDepartment());
		dto.setOrganizationName(complaint.getOrganizationName());
		dto.setVendor(complaint.getVendor());
		dto.setConsultantName(complaint.getConsultantName());
		dto.setComplaintCategory(complaint.getComplaintCategory());
		dto.setShortDescription(complaint.getShortDescription());
		dto.setShortDescFectorCausingProblem(complaint.getShortDescFectorCausingProblem());
		dto.setComplaintDateTime(complaint.getComplaintDateTime());
//		dto.setComplainantSignature(complainantSignature);
		dto.setComplaintLocation(complaint.getComplaintLocation());
		dto.setComplaintSubLocation(complaint.getComplaintSubLocation());
		dto.setStatus(complaint.getStatus());
		dto.setInternalStatus(complaint.getInternalStatus());
		dto.setSeqNo(complaint.getSeqNo());
		dto.setPriority(complaint.getPriority());
		dto.setAssignees(this.getComplaintAssigneeByComplaintId(complaint.getId()));
		dto.setReviews(this.getComplaintReviewByComplaintId(complaint.getId()));
		dto.setComplainantReviews(this.getComplainantReviews(complaint.getId()));

		return dto;

	}

	public List<ListComplaintAssignee> getComplaintAssigneeByComplaintId(UUID complaintId) {
		return assigneeRepo.getComplaintAssigneeByComplaintId(complaintId).stream()
				.map(y -> new ListComplaintAssignee(y.getAssignedPerson().getId(), y.getAssignedDateTime(),
						y.getAssignedPerson().getFullName(), y.getAssignedPerson().getEmail(),
						y.getAssignedPerson().getOrg().getName()))
				.collect(Collectors.toList());
	}

	public List<ListComplaintReview> getComplaintReviewByComplaintId(UUID complaintId) {
		List<ComplaintReview> complaintReviews = reviewRepo.getComplaintReviewByComplaintId(complaintId);
		List<ListComplaintReview> dtos = new ArrayList<>();

		complaintReviews.forEach(x -> {
			ListComplaintReview review = new ListComplaintReview();
			review.setId(x.getId());
			review.setComplaintId(x.getComplaintRef().getId());
			review.setReviewDateTime(x.getDateTime());
			review.setUserId(x.getUserRef().getId());
			review.setComments(x.getComments());
			review.setUserName(x.getUserRef().getFullName());
			review.setEmail(x.getUserRef().getEmail());
			review.setOrg(x.getUserRef().getOrg().getName());
			dtos.add(review);
		});
		return dtos;

	}

	public List<ListComplaintAttachment> getComplaintAttachmentByComplaintId(UUID complaintId) {
		List<ComplaintAttachment> attachment = attachmentRepo.getComplaintAttachmentByComplaintId(complaintId);
		return this.mapList(attachment, ListComplaintAttachment.class);
	}

	private ComplainantReview convertToEntity(ComplainantReviewDto dto) {
//		ComplainantReview review = modelMapper.map(dto, ComplainantReview.class);
		ComplainantReview review =  null;
		return review;
	}

	private <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
//		return source.stream().map(element -> modelMapper.map(element, targetClass)).collect(Collectors.toList());
		return null;
	}

	public void addComplainantReviews(ComplainantReviewDto dto) {
		Complaint complaint = complaintRepo.findById(dto.getComplaintId())
				.orElseThrow(() -> new ValidationException("Invalid Complaint ID."));
		ComplainantReview review = this.convertToEntity(dto);
		review.setComplaintRef(complaint);
		complainantReviewRepo.save(review);
	}

	public List<ListComplainantReview> getComplainantReviews(UUID complaintId) {
		return this.mapList(complainantReviewRepo.getComplainantReviewByComplaintId(complaintId),
				ListComplainantReview.class);
	}

	private ComplaintAppeal convertComplaintAppealDtoToEntity(ComplaintAppealDto dto) {
//		return modelMapper.map(dto, ComplaintAppeal.class);
		return null;
	}
	public void addComplaintAppeal(ComplaintAppealDto body) {
		ComplaintAppeal complaintAppeal= convertComplaintAppealDtoToEntity(body);
		complaintAppeal.setComplaintRef(complaintRepo.findById(body.getComplaintId()).get());
		complaintAppeal.setAppealTo(userRepo.findById(body.getAppealTo()).get());
		complaintAppeal.setStatus(ComplaintStatus.INITIATED.getPersistenceValue());
		appealRepo.save(complaintAppeal);
	}
	
	public List<ComplaintAppealDto> getComplaintAppeals(String status){
		List<ComplaintAppeal> appealList= appealRepo.findByStatusOrderByAppealDateTimeDesc(status);
		List<ComplaintAppealDto> dtos = new ArrayList<>();
		appealList.stream().forEach(appeal->{
			ComplaintAppealDto dto = new ComplaintAppealDto();
			dto.setAppealDateTime(appeal.getAppealDateTime());
			dto.setComplaintId(appeal.getComplaintRef().getId());
			dto.setRemarks(appeal.getRemarks());
			dto.setStatus(appeal.getStatus());
			dto.setAppealTo(appeal.getAppealTo().getId());
			dto.setId(appeal.getId());
			dtos.add(dto);
		});
		return dtos;
	}

	public List<UserLookupItem> getActiveUsersForLookupByRole(String roleName) {
		List<User> users = userRepo.findActiveUsersForRole(roleName);
		
		List<UserLookupItem> dtos = users.stream()
				.map(u -> new UserLookupItem(u.getId(), u.getFullName()))
				.collect(Collectors.toList());
		
		return dtos;
	}
	public void updateAppealStatus(UUID id,String status) {
		ComplaintAppeal appeal = appealRepo.getOne(id);
		this.updateComplaintStatus(appeal.getComplaintRef().getId(),status);
		appeal.setStatus(status);
		appealRepo.save(appeal);
	}
	
	
}
