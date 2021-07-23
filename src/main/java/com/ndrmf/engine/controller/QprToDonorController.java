package com.ndrmf.engine.controller;

import com.ndrmf.common.ApiResponse;
import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.engine.dto.*;
import com.ndrmf.engine.service.CommentService;
import com.ndrmf.engine.service.QprToDonorService;
import com.ndrmf.util.constants.SystemRoles;
import com.ndrmf.util.enums.FormAction;
import com.ndrmf.util.enums.ProcessStatus;
import com.ndrmf.util.enums.ProcessType;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
//import javax.validation.ValidationException;
import java.io.IOException;
import java.util.*;

@Api(tags = "Qpr To Donor")
@RestController
@RequestMapping("/qpr-to-donor")
public class QprToDonorController {
	@Autowired private QprToDonorService qprToDonorService;
	@Autowired private CommentService commentService;
	
	@GetMapping("/")
	public ResponseEntity<List<QprToDonorListItem>> getQprToDonors(@AuthenticationPrincipal AuthPrincipal principal,
			@RequestParam(name = "status", required = false) ProcessStatus status) throws IOException{
		return new ResponseEntity<>(qprToDonorService.getQprToDonorRequests(principal, status), HttpStatus.OK);
	}
	
//	@RolesAllowed({SystemRoles.ORG_FIP, SystemRoles.ORG_GOVT})
	@PostMapping("/commence")
	public ResponseEntity<?> commenceQprToDonorRequest(@AuthenticationPrincipal AuthPrincipal principal,
			@RequestBody(required = false) CommenceQprToDonorRequest body){
		Map<String, UUID> dto = new HashMap<>();
		dto.put("id", qprToDonorService.commenceQprToDonor(principal));
		return new ResponseEntity<>(dto, HttpStatus.CREATED);
	}
	
	@GetMapping(path = "/{id}", produces = "application/json")
	public ResponseEntity<QprToDonorItem> getQprToDonorRequest(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "id", required = true) UUID id){
		QprToDonorItem dto = qprToDonorService.getQprToDonorRequest(id, principal);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
//	@RolesAllowed({SystemRoles.ORG_FIP, SystemRoles.ORG_GOVT})
	@PostMapping("/{qprtodonorId}/section/add")
	public ResponseEntity<ApiResponse> submitSection(@AuthenticationPrincipal AuthPrincipal principal,
			@RequestParam(name = "action", required = true) FormAction action,
			@PathVariable(name = "qprtodonorId", required = true) UUID qprtodonorId,
			@RequestBody @Valid QprToDonorSectionRequest body){
		qprToDonorService.submitQprToDonorSection(principal.getUserId(), qprtodonorId, body, action);
	
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Section saved successfully."), HttpStatus.CREATED);
	}
	
//	@PostMapping("/{proposalId}/pre-appraisal/commence")
//	public ResponseEntity<ApiResponse> commencePreliminaryAppraisal(@AuthenticationPrincipal AuthPrincipal principal,
//			@PathVariable(name = "proposalId") UUID proposalId,
//			@RequestBody CommencePreliminaryAppraisalRequest body){
//		qprToDonorService.commencePreliminaryAppraisal(principal.getUserId(), proposalId, body);
//		return new ResponseEntity<>(new ApiResponse(true, "Pre-Appraisal Commenced successfully"), HttpStatus.CREATED);
//	}
//
//	@PostMapping("/{proposalId}/pre-appraisal/submit")
//	public ResponseEntity<ApiResponse> addPreliminaryAppraisal(@AuthenticationPrincipal AuthPrincipal principal,
//			@PathVariable(name = "proposalId") UUID proposalId,
//			@RequestBody PreliminaryAppraisalRequest body){
//		qprToDonorService.submitPreliminaryAppraisal(principal.getUserId(), proposalId, body);
//		return new ResponseEntity<>(new ApiResponse(true, "Pre-Appraisal added successfully"), HttpStatus.CREATED);
//	}
//
//	@GetMapping("/pre-appraisal")
//	public ResponseEntity<List<PreliminaryAppraisalListItem>> getPreliminaryAppraisals(@AuthenticationPrincipal AuthPrincipal principal,
//			@RequestParam(name = "status", required = false) ProcessStatus status){
//		return new ResponseEntity<>(qprToDonorService.getAllPreliminaryAppraisals(principal.getUserId(), status), HttpStatus.OK);
//	}
//
//	@GetMapping("/pre-appraisal/{id}")
//	public ResponseEntity<PreliminaryAppraisalItem> getPreliminaryAppraisal(@PathVariable(name = "id", required = true) UUID id){
//		return new ResponseEntity<>(qprToDonorService.getPreliminaryAppraisal(id), HttpStatus.OK);
//	}
	
//	@PostMapping("/{proposalId}/ext-appraisal/commence")
//	public ResponseEntity<ExtendedAppraisalItem> commenceExtendedAppraisal(@AuthenticationPrincipal AuthPrincipal principal,
//			@PathVariable(name = "proposalId") UUID proposalId,
//			@RequestBody CommenceExtendedAppraisalRequest body){
//
//		ExtendedAppraisalItem dto = qprToDonorService.commenceExtendedAppraisal(principal.getUserId(), proposalId, body);
//
//		return new ResponseEntity<ExtendedAppraisalItem>(dto, HttpStatus.CREATED);
//	}
	
//	@PostMapping("/ext-appraisal/{extendedAppraisalId}/section/submit")
//	public ResponseEntity<ApiResponse> submitExtendedAppraisalSection(@AuthenticationPrincipal AuthPrincipal principal,
//			@PathVariable(name = "extendedAppraisalId") UUID extendedAppraisalId,
//			@RequestBody ExtendedAppraisalSectionRequest body){
//
//		qprToDonorService.submitExtendedAppraisalSection(principal.getUserId(), extendedAppraisalId, body);
//
//		return new ResponseEntity<>(new ApiResponse(true, "Section submitted successfully."), HttpStatus.CREATED);
//	}

//	@PostMapping("/ext-appraisal/{extendedAppraisalId}/section/assign")
//	public ResponseEntity<ApiResponse> assignExtendedAppraisalSection(@AuthenticationPrincipal AuthPrincipal principal,
//																	  @PathVariable(name = "extendedAppraisalId") UUID extendedAppraisalId,
//																	  @RequestBody AssignExtendedAppraisalSectionRequest body){
//		qprToDonorService.assignExtendedAppraisalSection(principal.getUserId(), extendedAppraisalId, body);
//		return new ResponseEntity<>(new ApiResponse(true, "Section assigned successfully."), HttpStatus.OK);
//	}
//
//	@PutMapping("/ext-appraisal/{extendedAppraisalId}/decisionbydm")
//	public ResponseEntity<ApiResponse> extendedAppraisalDecisionByDm(@AuthenticationPrincipal AuthPrincipal principal,
//																	  @PathVariable(name = "extendedAppraisalId") UUID extendedAppraisalId){
//		qprToDonorService.extendedAppraisalDecisionByDm(principal.getUserId(), extendedAppraisalId);
//		return new ResponseEntity<>(new ApiResponse(true, "Appraisal approved successfully."), HttpStatus.OK);
//	}
	
	@PostMapping("/section/{sectionId}/task/add")
	public ResponseEntity<ApiResponse> addTaskForProposalSection(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "sectionId", required = true) UUID sectionId,
			@RequestBody @Valid AddProposalTaskRequest body){
		qprToDonorService.addQprToDonorTask(sectionId, principal.getUserId(), body);
		return new ResponseEntity<>(new ApiResponse(true, "Task added successfully."), HttpStatus.OK);
	}
	
//	@PostMapping("/section/{sectionId}/review/add")
//	public ResponseEntity<ApiResponse> addReview(@AuthenticationPrincipal AuthPrincipal principal,
//			@PathVariable(name = "sectionId", required = true) UUID sectionId,
//			@RequestBody @Valid AddProposalSectionReviewRequest body){
//
//		commentService.addQprToDonorSectionReview(principal.getUserId(), sectionId, body);
//
//		return new ResponseEntity<>(new ApiResponse(true, "Review added successfully."), HttpStatus.OK);
//	}
//
//	@PostMapping("/{id}/comment/add")
//	public ResponseEntity<ApiResponse> addGeneralComments(@AuthenticationPrincipal AuthPrincipal principal,
//			@PathVariable(name = "id", required = true) UUID proposalId,
//			@RequestBody @Valid AddProposalGeneralCommentRequest body){
//
//		commentService.addQprToDonorGeneralComment(proposalId, principal, body);
//
//		return new ResponseEntity<>(new ApiResponse(true, "Comment added successfully."), HttpStatus.CREATED);
//	}
	
//	@PutMapping("/{id}")
//	public ResponseEntity<ApiResponse> updateProposalStatus(
//			@PathVariable(name = "id", required = true) UUID proposalId,
//			@RequestParam(name = "status", required = true) ProcessStatus status,
//			@RequestParam(name = "subStatus", required = true) ProcessStatus subStatus,
//			@AuthenticationPrincipal AuthPrincipal principal,
//			@RequestBody(required = false) @Valid OfferLetterUpdateRequest body){
//
//		qprToDonorService.updateProposalStatus(proposalId, principal.getUserId(), status, subStatus, body);
//
//		return new ResponseEntity<>(new ApiResponse(true, "Status updated successfully."), HttpStatus.OK);
//	}
	
//	@PutMapping("/offerLetterStatus/{id}")
//	public ResponseEntity<ApiResponse> updateProposalOfferLetterStatus(
//			@PathVariable(name = "id", required = true) UUID proposalId,
//			@RequestParam(name = "status", required = true) ProcessStatus status,
//			@AuthenticationPrincipal AuthPrincipal principal) throws IOException {
//
//		String response = qprToDonorService.updateProposalOfferLetterStatus(proposalId, principal.getUserId(), status);
//
//		return new ResponseEntity<>(new ApiResponse(true, "Status updated successfully."), HttpStatus.OK);
//	}
	
//	@PostMapping("/{id}/attachment/add")
//	public ResponseEntity<?> addAttachement(@PathVariable(name = "id", required = true) UUID proposalId,
//			@RequestParam(name = "stage", required = true) ProcessStatus status,
//			@RequestParam(name = "file", required = true) MultipartFile file,
//			@AuthenticationPrincipal AuthPrincipal principal) throws IOException{
//
//		String path = qprToDonorService.addProposalAttachment(proposalId, principal.getUserId(), status, file);
//		Map<String, String> dto = new HashMap<>();
//		dto.put("filePath", path);
//
//		return new ResponseEntity<>(dto, HttpStatus.CREATED);
//	}
	
//	@GetMapping("/{id}/attachments/")
//	public ResponseEntity<?> getAttachementByProposalIdAndProposalStage(@PathVariable(name = "id", required = true) UUID proposalId,
//			@RequestParam(name = "stage", required = true) ProcessStatus status,
//			@AuthenticationPrincipal AuthPrincipal principal){
//
//		List<Object> attachments = qprToDonorService.readProposalAttachmentByStage(proposalId, status);
//
//		return new ResponseEntity<>(attachments, HttpStatus.OK);
//	}
	
//	@GetMapping("/{id}/allAttachments")
//	public ResponseEntity<?>getAllAttachmentsByProposalId(@PathVariable(name = "id", required = true) UUID proposalId){
//		List<QprToDonorAttachment> attachments = qprToDonorService.readProposalAttachmentsByProposalId(proposalId);
//
//		List<QprToDonorAttachmentListItem> dto = new ArrayList<>();
//		attachments.forEach(c -> {
//			QprToDonorAttachmentListItem ppali;
//			ppali = new QprToDonorAttachmentListItem();
//			ppali.setName(c.getFileRef().getFileName());
//			ppali.setPath(c.getFileRef().getPath());
//			ppali.setPicByte(c.getFileRef().getData());
//			ppali.setStatus(c.getStage());
//			ppali.setCreated_by(c.getCreatedBy());
//
//			dto.add(ppali);
//		});
//
//		return new ResponseEntity<>(dto, HttpStatus.OK);
//	}
	
	//@GetMapping("/download/", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//	@RequestMapping(path = "/attachment/download/", method = RequestMethod.GET)
//    public List download(@RequestParam(name = "fileName") String fileName,
//						 @RequestParam(name = "filePath") String filePath) throws IOException {
//
//		List dto = qprToDonorService.getAttachmentsByFileNameAndPath(fileName, filePath);
//
////        File file = new File(filePath);
////
////        HttpHeaders header = new HttpHeaders();
////        String headerParam = "attachment; " + "filename=" + fileName;
////        header.add(HttpHeaders.CONTENT_DISPOSITION, headerParam);
////        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
////        header.add("Pragma", "no-cache");
////        header.add("Expires", "0");
////
////        Path path = Paths.get(file.getAbsolutePath());
////        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
////		System.out.prit
//
//        return dto;
////                .headers(header)
////                .contentLength(file.length())
////                .contentType(MediaType.parseMediaType("application/octet-stream"))
////                .body(resource);
//    }

	
//	@PostMapping("/{proposalId}/pip/submit")
//	public ResponseEntity<ApiResponse> addProjectImplementationPlan(@AuthenticationPrincipal AuthPrincipal principal,
//			@PathVariable(name = "proposalId") UUID proposalId,
//			@RequestBody AddImplementationPlanRequest body){
//		qprToDonorService.submitImplementationPlan(principal.getUserId(), proposalId, body);
//		return new ResponseEntity<>(new ApiResponse(true, "Implementation Plan added successfully"), HttpStatus.CREATED);
//	}
//
//	@PostMapping("/{proposalId}/gia/submit")
//	public ResponseEntity<ApiResponse> submitGrantImplementationAgreement(@AuthenticationPrincipal AuthPrincipal principal,
//			@PathVariable(name = "proposalId") UUID proposalId,
//			@RequestBody AddGrantImplementationAgreementRequest body){
//		qprToDonorService.submitGrantImplementationAgreement(principal.getUserId(), proposalId, body);
//		return new ResponseEntity<>(new ApiResponse(true, "GIA added successfully"), HttpStatus.CREATED);
//	}
	
//	@PostMapping("/{proposalId}/gia-checklist/submit")
//	public ResponseEntity<ApiResponse> submitGIAChecklist(@AuthenticationPrincipal AuthPrincipal principal,
//			@PathVariable(name = "proposalId") UUID proposalId,
//			@RequestBody AddGIAChecklistRequest body){
//		qprToDonorService.submitGIAChecklist(proposalId, body);
//		return new ResponseEntity<>(new ApiResponse(true, "GIA checklist submitted successfully"), HttpStatus.CREATED);
//	}
//
//	@PutMapping("/{proposalId}/gia")
//	public ResponseEntity<ApiResponse> updateGrantImplementationAgreementStatus(@AuthenticationPrincipal AuthPrincipal principal,
//			@PathVariable(name = "proposalId", required = true) UUID proposalId,
//			@RequestParam(name = "status", required = true) ProcessStatus status,
//			@RequestParam(name = "checklist-deadline", required = false)
//			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date checklistDeadline){
//		if(!status.equals(ProcessStatus.APPROVED) && !status.equals(ProcessStatus.REJECTED)) {
//			throw new ValidationException("Invalid status for this phase of the Proposal");
//		}
//
//		if(status.equals(ProcessStatus.APPROVED) && checklistDeadline == null) {
//			throw new ValidationException("Checklist deadline cannot be null");
//		}
//
//		qprToDonorService.updateGrantImplementationAgreementStatus(proposalId, status, checklistDeadline);
//		return new ResponseEntity<>(new ApiResponse(true, "GIA Status added successfully"), HttpStatus.OK);
//	}
	
//	@PostMapping("/{proposalId}/gia/review/add")
//	public ResponseEntity<ApiResponse> addGIAReview(@AuthenticationPrincipal AuthPrincipal principal,
//			@PathVariable(name = "proposalId") UUID proposalId,
//			@RequestBody AddGrantImplementationAgreementReviewRequest body){
//		qprToDonorService.addGrantImplementationAgreementReview(principal.getUserId(), proposalId, body);
//		return new ResponseEntity<>(new ApiResponse(true, "GIA Review added successfully"), HttpStatus.CREATED);
//	}
//
@PostMapping("/{qprtodonorId}/reassign")
	public ResponseEntity<ApiResponse> reassignToFIP(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "qprtodonorId") UUID qprtodonorId,
			@RequestBody ReassignPrposalToFIPRequest body){
		qprToDonorService.reassignQprToDonorSections(qprtodonorId, principal.getUserId(), body.getSectionIds(), body.getComments());
		
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Sections reassigned successfully"), HttpStatus.ACCEPTED);
	}
	
//	@PostMapping("/{proposalId}/report/{subProcessType}/submit")
//	public ResponseEntity<ApiResponse> submitPrposalMiscReport(@PathVariable(name = "proposalId") UUID proposalId,
//			@PathVariable(name = "subProcessType") ProcessType subProcessType,
//			@RequestBody AddProposalMiscReportRequest body){
//
//		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Report submitted successfully"), HttpStatus.ACCEPTED);
//	}
//	@GetMapping("/offerLetter/{proposalId}")
//	public ResponseEntity<OfferLetterItem> getOfferLetter(@AuthenticationPrincipal AuthPrincipal principal,
//			@PathVariable(name = "proposalId") UUID proposalId) throws IOException {
//		OfferLetterItem dto = qprToDonorService.getOfferLetter(principal, proposalId);
//		return new ResponseEntity<OfferLetterItem>(dto, HttpStatus.OK);
//	}
}