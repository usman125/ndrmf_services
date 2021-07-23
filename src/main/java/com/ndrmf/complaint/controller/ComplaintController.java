package com.ndrmf.complaint.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ndrmf.common.ApiResponse;
import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.complaint.dto.AcknowledgeComplaint;
import com.ndrmf.complaint.dto.AddComplaint;
import com.ndrmf.complaint.dto.AddComplaintAssignee;
import com.ndrmf.complaint.dto.AddComplaintReview;
import com.ndrmf.complaint.dto.ComplainantReviewDto;
import com.ndrmf.complaint.dto.ComplaintAppealDto;
import com.ndrmf.complaint.dto.ListComplaint;
import com.ndrmf.complaint.dto.ListComplaintAssignee;
import com.ndrmf.complaint.dto.ListComplaintAttachment;
import com.ndrmf.complaint.dto.ListComplaintReview;
import com.ndrmf.complaint.service.ComplaintService;
import com.ndrmf.notification.service.NotificationService;
import com.ndrmf.user.dto.UserLookupItem;
import com.ndrmf.util.enums.ComplaintStatus;

import io.swagger.annotations.Api;

@Api(tags = "Complaint")
@RestController
@RequestMapping("/complaint")
public class ComplaintController {

	@Autowired
	private ComplaintService complaintService;
	@Autowired
	private NotificationService notificationService;

	@PostMapping("/add")
	public ResponseEntity<Map<String, Object>> addComplaint(@Valid @RequestBody AddComplaint body) {
		UUID complaintId = complaintService.addComplaint(body);
		Map<String, Object> map = new HashMap<>();
		map.put("complaintId", complaintId);
		return ResponseEntity.ok().body(map);
	}

	@GetMapping("/findByUser/{userId}")
	public ResponseEntity<List<ListComplaint>> getComplaintsByUserId(@PathVariable UUID userId) {
		List<ListComplaint> dtos = complaintService.getAllComplaintsByUser(userId);
		return new ResponseEntity<List<ListComplaint>>(dtos, HttpStatus.OK);
	}

	@GetMapping("/find/all")
	public ResponseEntity<List<ListComplaint>> getAllComplaints() {
		List<ListComplaint> dtos = complaintService.getAllComplaints();
		return new ResponseEntity<List<ListComplaint>>(dtos, HttpStatus.OK);
	}

	@GetMapping("/find/{id}")
	public ResponseEntity<ListComplaint> getComplaint(@PathVariable("id") UUID id) {
		return new ResponseEntity<ListComplaint>(complaintService.getComplaint(id), HttpStatus.OK);
	}

	@GetMapping("/assignee/findByComplaintId/{complaintId}")
	public ResponseEntity<List<ListComplaintAssignee>> getComplaintAssigneeByComplaintId(
			@PathVariable("complaintId") UUID complaintId) {
		List<ListComplaintAssignee> assignee = complaintService.getComplaintAssigneeByComplaintId(complaintId);
		return new ResponseEntity<List<ListComplaintAssignee>>(assignee, HttpStatus.OK);
	}

	@GetMapping("/attachment/find/{complaintId}")
	public ResponseEntity<List<ListComplaintAttachment>> getComplaintAttachmentByComplaintId(
			@PathVariable("complaintId") UUID complaintId) {
		return ResponseEntity.ok().body(complaintService.getComplaintAttachmentByComplaintId(complaintId));
	}

	@GetMapping("/review/find/{complaintId}")
	public ResponseEntity<List<ListComplaintReview>> getComplaintReviewByComplaintId(
			@PathVariable("complaintId") UUID complaintId) {
		List<ListComplaintReview> review = complaintService.getComplaintReviewByComplaintId(complaintId);
		return new ResponseEntity<List<ListComplaintReview>>(review, HttpStatus.OK);
	}

	@PutMapping("/{complaintId}/acknowledge")
	public ResponseEntity<ApiResponse> acknowledgeComplaint(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "complaintId", required = true) UUID complaintId,
			@RequestBody @Valid AcknowledgeComplaint body) {
		complaintService.acknowledgeComplaint(complaintId, body);
		return new ResponseEntity<>(new ApiResponse(true, "Complaint has been acknowledged."), HttpStatus.OK);

	}

	@PostMapping("/{complaintId}/assign/concerned-person")
	public ResponseEntity<ApiResponse> assignToConcernedPerson(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "complaintId", required = true) UUID complaintId,
			@RequestBody @Valid List<AddComplaintAssignee> assigneeList, @RequestParam String notificationTitle,
			String notificationBody) {

		complaintService.addComplaintAssignee(assigneeList, complaintId,
				ComplaintStatus.MARKED_TO_CONCERNED_PERSON.getPersistenceValue(), "Complaint Received",
				"You got a new complaint. Please submit your remarks/point of view within 3 working days.");
		return new ResponseEntity<>(new ApiResponse(true, "Complaint has been assigned."), HttpStatus.OK);
	}

	@PostMapping("/{complaintId}/assign/grc")
	public ResponseEntity<ApiResponse> assignToGrc(
			@PathVariable(name = "complaintId", required = true) UUID complaintId,
			@RequestBody @Valid List<AddComplaintAssignee> assigneeList, @RequestParam String notificationTitle,
			String notificationBody) {
		complaintService.addComplaintAssignee(assigneeList, complaintId,
				ComplaintStatus.MARKED_TO_GRC.getPersistenceValue(), "Complaint Pending",
				"A Complaint is pending for your approval.");
		return new ResponseEntity<>(new ApiResponse(true, "Complaint has been assigned."), HttpStatus.OK);
	}

	@PostMapping("/{complaintId}/review/add")
	public ResponseEntity<ApiResponse> addReview(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "complaintId", required = true) UUID complaintId,
			@RequestBody @Valid AddComplaintReview body) {

		complaintService.addComplaintReview(principal.getUserId(), complaintId, body);
		return new ResponseEntity<>(new ApiResponse(true, "Review added successfully."), HttpStatus.OK);
	}

	@PostMapping("/{complaintId}/assignee")
	public ResponseEntity<ApiResponse> addComplaintAssignee(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "complaintId", required = true) UUID complaintId,
			@RequestBody @Valid List<AddComplaintAssignee> assigneeList, @RequestParam String status,
			@RequestParam String title, @RequestParam String body) {
		complaintService.addComplaintAssignee(assigneeList, complaintId, status, title, body);
		return new ResponseEntity<>(new ApiResponse(true, "Complaint has been assigned."), HttpStatus.OK);
	}

	@PutMapping("/{complaintId}/status")
	public ResponseEntity<ApiResponse> updateComplaintStatus(
			@PathVariable(name = "complaintId", required = true) UUID complaintId, @RequestParam @Valid String status) {
		complaintService.updateComplaintStatus(complaintId, status);
		return new ResponseEntity<>(new ApiResponse(true, "Complaint Status has been updated."), HttpStatus.OK);

	}

	@PutMapping("/{complaintId}/internalStatus")
	public ResponseEntity<ApiResponse> updateComplaintInternalStatus(
			@PathVariable(name = "complaintId", required = true) UUID complaintId, @RequestParam String status) {
		complaintService.updateInternalStatus(complaintId, status);
		return ResponseEntity.ok().body(new ApiResponse(true, "Complaint Internal Status has been updated."));
	}

	@PostMapping("/user/{userId}/notify")
	public ResponseEntity<ApiResponse> notify(@PathVariable(name = "userId", required = true) UUID userId,
			@RequestParam String title, @RequestParam String body) {
		notificationService.addNotification(userId, title, body);
		return new ResponseEntity<>(new ApiResponse(true, "Notification has been sent."), HttpStatus.OK);

	}

	@PostMapping("/{complaintId}/complainant/notify")
	public ResponseEntity<ApiResponse> EmailToComplainant(
			@PathVariable(name = "complaintId", required = true) UUID complaintId, @RequestParam String body,
			@RequestParam String subject) {
		complaintService.intimateComplainantThroughEmail(complaintId, subject, body);
		return new ResponseEntity<>(new ApiResponse(true, "Email has been sent to the complainant."), HttpStatus.OK);

	}

	@PostMapping("/complainant/feedback")
	public ResponseEntity<ApiResponse> addComplainantReviews(@RequestBody @Valid ComplainantReviewDto dto) {
		complaintService.addComplainantReviews(dto);
		return new ResponseEntity<>(new ApiResponse(true, "Feedback has been updated."), HttpStatus.OK);
	}
	
	@PostMapping("/appeal")
	public ResponseEntity<ApiResponse> addComplaintAppeal(@RequestBody @Valid ComplaintAppealDto dto) {
		complaintService.addComplaintAppeal(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "Appeal has been submitted."));
	}
	@GetMapping("appeal/findall")
	public ResponseEntity<List<ComplaintAppealDto>> getComplaintAppeal(@RequestParam ComplaintStatus status){
		return ResponseEntity.ok().body(complaintService.getComplaintAppeals(status.getPersistenceValue()));		
	}
	@GetMapping("user/findByRole")
	public ResponseEntity<List<UserLookupItem>> getUserListByRole(@RequestParam String role){
		return ResponseEntity.ok().body(complaintService.getActiveUsersForLookupByRole(role));		
	}	

	@PutMapping("/appeal/status/update")
	public ResponseEntity<ApiResponse> updateComplaintStatus(
			@RequestParam(name = "appealId", required = true) UUID appealId, @RequestParam ComplaintStatus status) {
		complaintService.updateAppealStatus(appealId, status.getPersistenceValue());
		return new ResponseEntity<>(new ApiResponse(true, "Status has been updated."), HttpStatus.OK);
	}
	
}
