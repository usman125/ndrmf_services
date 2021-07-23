package com.ndrmf.engine.controller;

import java.io.IOException;

import java.util.*;

import javax.annotation.security.RolesAllowed;

import javax.validation.Valid;
import javax.validation.ValidationException;


import com.ndrmf.engine.dto.*;
import com.ndrmf.engine.model.ProjectProposalAttachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.ndrmf.common.ApiResponse;
import com.ndrmf.common.AuthPrincipal;

import com.ndrmf.engine.service.CommentService;
import com.ndrmf.engine.service.ProjectProposalService;
import com.ndrmf.util.constants.SystemRoles;
import com.ndrmf.util.enums.FormAction;
import com.ndrmf.util.enums.ProcessStatus;
import com.ndrmf.util.enums.ProcessType;

import io.swagger.annotations.Api;

@Api(tags = "Project Proposal Process")
@RestController
@RequestMapping("/project-proposal")
public class ProjectProposalController {
	@Autowired private ProjectProposalService projProposalService;
	@Autowired private CommentService commentService;
	
	@GetMapping("/")
	public ResponseEntity<List<ProjectProposalListItem>> getProjectProposals(@AuthenticationPrincipal AuthPrincipal principal,
			@RequestParam(name = "status", required = false) ProcessStatus status) throws IOException{
		return new ResponseEntity<>(projProposalService.getProjectProposalRequests(principal, status), HttpStatus.OK);
	}
	
	@RolesAllowed({SystemRoles.ORG_FIP, SystemRoles.ORG_GOVT})
	@PostMapping("/commence")
	public ResponseEntity<?> commenceProjectProposalRequest(@AuthenticationPrincipal AuthPrincipal principal,
			@RequestBody(required = false) CommenceProjectProposalRequest body){
		Map<String, UUID> dto = new HashMap<>();
		dto.put("id", projProposalService.commenceProjectProposal(principal, body));
		return new ResponseEntity<>(dto, HttpStatus.CREATED);
	}
	
	@GetMapping(path = "/{id}", produces = "application/json")
	public ResponseEntity<ProjectProposalItem> getProjectProposalRequest(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "id", required = true) UUID id){
		ProjectProposalItem dto = projProposalService.getProjectProposalRequest(id, principal);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	@RolesAllowed({SystemRoles.ORG_FIP, SystemRoles.ORG_GOVT})
	@PostMapping("/{proposalId}/section/add")
	public ResponseEntity<ApiResponse> submitSection(@AuthenticationPrincipal AuthPrincipal principal,
			@RequestParam(name = "action", required = true) FormAction action,
			@PathVariable(name = "proposalId", required = true) UUID proposalId,
			@RequestBody @Valid ProjectProposalSectionRequest body){
		projProposalService.submitProposalSection(principal.getUserId(), proposalId, body, action);
	
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Section saved successfully."), HttpStatus.CREATED);
	}
	
	@PostMapping("/{proposalId}/pre-appraisal/commence")
	public ResponseEntity<ApiResponse> commencePreliminaryAppraisal(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "proposalId") UUID proposalId,
			@RequestBody CommencePreliminaryAppraisalRequest body){
		projProposalService.commencePreliminaryAppraisal(principal.getUserId(), proposalId, body);
		return new ResponseEntity<>(new ApiResponse(true, "Pre-Appraisal Commenced successfully"), HttpStatus.CREATED);
	}
	
	@PostMapping("/{proposalId}/pre-appraisal/submit")
	public ResponseEntity<ApiResponse> addPreliminaryAppraisal(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "proposalId") UUID proposalId,
			@RequestBody PreliminaryAppraisalRequest body){
		projProposalService.submitPreliminaryAppraisal(principal.getUserId(), proposalId, body);
		return new ResponseEntity<>(new ApiResponse(true, "Pre-Appraisal added successfully"), HttpStatus.CREATED);
	}
	
	@GetMapping("/pre-appraisal")
	public ResponseEntity<List<PreliminaryAppraisalListItem>> getPreliminaryAppraisals(@AuthenticationPrincipal AuthPrincipal principal,
			@RequestParam(name = "status", required = false) ProcessStatus status){
		return new ResponseEntity<>(projProposalService.getAllPreliminaryAppraisals(principal.getUserId(), status), HttpStatus.OK);
	}
	
	@GetMapping("/pre-appraisal/{id}")
	public ResponseEntity<PreliminaryAppraisalItem> getPreliminaryAppraisal(@PathVariable(name = "id", required = true) UUID id){
		return new ResponseEntity<>(projProposalService.getPreliminaryAppraisal(id), HttpStatus.OK);
	}
	
	@PostMapping("/{proposalId}/ext-appraisal/commence")
	public ResponseEntity<ExtendedAppraisalItem> commenceExtendedAppraisal(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "proposalId") UUID proposalId,
			@RequestBody CommenceExtendedAppraisalRequest body){
		
		ExtendedAppraisalItem dto = projProposalService.commenceExtendedAppraisal(principal.getUserId(), proposalId, body);
		
		return new ResponseEntity<ExtendedAppraisalItem>(dto, HttpStatus.CREATED);
	}
	
	@PostMapping("/ext-appraisal/{extendedAppraisalId}/section/submit")
	public ResponseEntity<ApiResponse> submitExtendedAppraisalSection(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "extendedAppraisalId") UUID extendedAppraisalId,
			@RequestBody ExtendedAppraisalSectionRequest body){
		
		projProposalService.submitExtendedAppraisalSection(principal.getUserId(), extendedAppraisalId, body);
		
		return new ResponseEntity<>(new ApiResponse(true, "Section submitted successfully."), HttpStatus.CREATED);
	}

	@PostMapping("/ext-appraisal/{extendedAppraisalId}/section/assign")
	public ResponseEntity<ApiResponse> assignExtendedAppraisalSection(@AuthenticationPrincipal AuthPrincipal principal,
																	  @PathVariable(name = "extendedAppraisalId") UUID extendedAppraisalId,
																	  @RequestBody AssignExtendedAppraisalSectionRequest body){
		projProposalService.assignExtendedAppraisalSection(principal.getUserId(), extendedAppraisalId, body);
		return new ResponseEntity<>(new ApiResponse(true, "Section assigned successfully."), HttpStatus.OK);
	}

	@PutMapping("/ext-appraisal/{extendedAppraisalId}/decisionbydm")
	public ResponseEntity<ApiResponse> extendedAppraisalDecisionByDm(@AuthenticationPrincipal AuthPrincipal principal,
																	  @PathVariable(name = "extendedAppraisalId") UUID extendedAppraisalId){
		projProposalService.extendedAppraisalDecisionByDm(principal.getUserId(), extendedAppraisalId);
		return new ResponseEntity<>(new ApiResponse(true, "Appraisal approved successfully."), HttpStatus.OK);
	}
	
	@PostMapping("/section/{sectionId}/task/add")
	public ResponseEntity<ApiResponse> addTaskForProposalSection(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "sectionId", required = true) UUID sectionId,
			@RequestBody @Valid AddProposalTaskRequest body){
		projProposalService.addProjectProposalTask(sectionId, principal.getUserId(), body);
		return new ResponseEntity<>(new ApiResponse(true, "Task added successfully."), HttpStatus.OK);
	}
	
	@PostMapping("/section/{sectionId}/review/add")
	public ResponseEntity<ApiResponse> addReview(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "sectionId", required = true) UUID sectionId,
			@RequestBody @Valid AddProposalSectionReviewRequest body){
		
		commentService.addProjectProposalSectionReview(principal.getUserId(), sectionId, body);
		
		return new ResponseEntity<>(new ApiResponse(true, "Review added successfully."), HttpStatus.OK);
	}
	
	@PostMapping("/{id}/comment/add")
	public ResponseEntity<ApiResponse> addGeneralComments(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "id", required = true) UUID proposalId,
			@RequestBody @Valid AddProposalGeneralCommentRequest body){
		
		commentService.addProjectProposalGeneralComment(proposalId, principal, body);
		
		return new ResponseEntity<>(new ApiResponse(true, "Comment added successfully."), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> updateProposalStatus(
			@PathVariable(name = "id", required = true) UUID proposalId,
			@RequestParam(name = "status", required = true) ProcessStatus status,
			@RequestParam(name = "subStatus", required = true) ProcessStatus subStatus,
			@AuthenticationPrincipal AuthPrincipal principal,
			@RequestBody(required = false) @Valid OfferLetterUpdateRequest body){
		
		projProposalService.updateProposalStatus(proposalId, principal.getUserId(), status, subStatus, body);
	
		return new ResponseEntity<>(new ApiResponse(true, "Status updated successfully."), HttpStatus.OK);
	}
	
	@PutMapping("/offerLetterStatus/{id}")
	public ResponseEntity<ApiResponse> updateProposalOfferLetterStatus(
			@PathVariable(name = "id", required = true) UUID proposalId,
			@RequestParam(name = "status", required = true) ProcessStatus status,
			@AuthenticationPrincipal AuthPrincipal principal) throws IOException {
		
		String response = projProposalService.updateProposalOfferLetterStatus(proposalId, principal.getUserId(), status);
	
		return new ResponseEntity<>(new ApiResponse(true, "Status updated successfully."), HttpStatus.OK);
	}
	
	@PostMapping("/{id}/attachment/add")
	public ResponseEntity<?> addAttachement(@PathVariable(name = "id", required = true) UUID proposalId,
			@RequestParam(name = "stage", required = true) ProcessStatus status,
			@RequestParam(name = "file", required = true) MultipartFile file,
			@AuthenticationPrincipal AuthPrincipal principal) throws IOException{
		
		String path = projProposalService.addProposalAttachment(proposalId, principal.getUserId(), status, file);
		Map<String, String> dto = new HashMap<>();
		dto.put("filePath", path);
		
		return new ResponseEntity<>(dto, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}/attachments/")
	public ResponseEntity<?> getAttachementByProposalIdAndProposalStage(@PathVariable(name = "id", required = true) UUID proposalId,
			@RequestParam(name = "stage", required = true) ProcessStatus status,
			@AuthenticationPrincipal AuthPrincipal principal){
		
		List<Object> attachments = projProposalService.readProposalAttachmentByStage(proposalId, status);
		
		return new ResponseEntity<>(attachments, HttpStatus.OK);
	}
	
	@GetMapping("/{id}/allAttachments")
	public ResponseEntity<?>getAllAttachmentsByProposalId(@PathVariable(name = "id", required = true) UUID proposalId){
		List<ProjectProposalAttachment> attachments = projProposalService.readProposalAttachmentsByProposalId(proposalId);

		List<ProjectProposalAttachmentListItem> dto = new ArrayList<>();
		attachments.forEach(c -> {
			ProjectProposalAttachmentListItem ppali;
			ppali = new ProjectProposalAttachmentListItem();
			ppali.setName(c.getFileRef().getFileName());
			ppali.setPath(c.getFileRef().getPath());
			ppali.setPicByte(c.getFileRef().getData());
			ppali.setStatus(c.getStage());
			ppali.setCreated_by(c.getCreatedBy());

			dto.add(ppali);
		});

		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	//@GetMapping("/download/", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@RequestMapping(path = "/attachment/download/", method = RequestMethod.GET)
    public List download(@RequestParam(name = "fileName") String fileName,
						 @RequestParam(name = "filePath") String filePath) throws IOException {

		List dto = projProposalService.getAttachmentsByFileNameAndPath(fileName, filePath);

//        File file = new File(filePath);
//
//        HttpHeaders header = new HttpHeaders();
//        String headerParam = "attachment; " + "filename=" + fileName;
//        header.add(HttpHeaders.CONTENT_DISPOSITION, headerParam);
//        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        header.add("Pragma", "no-cache");
//        header.add("Expires", "0");
//
//        Path path = Paths.get(file.getAbsolutePath());
//        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
//		System.out.prit

        return dto;
//                .headers(header)
//                .contentLength(file.length())
//                .contentType(MediaType.parseMediaType("application/octet-stream"))
//                .body(resource);
    }

	
	@PostMapping("/{proposalId}/pip/submit")
	public ResponseEntity<ApiResponse> addProjectImplementationPlan(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "proposalId") UUID proposalId,
			@RequestBody AddImplementationPlanRequest body){
		projProposalService.submitImplementationPlan(principal.getUserId(), proposalId, body);
		return new ResponseEntity<>(new ApiResponse(true, "Implementation Plan added successfully"), HttpStatus.CREATED);
	}
	
	@PostMapping("/{proposalId}/gia/submit")
	public ResponseEntity<ApiResponse> submitGrantImplementationAgreement(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "proposalId") UUID proposalId,
			@RequestBody AddGrantImplementationAgreementRequest body){
		projProposalService.submitGrantImplementationAgreement(principal.getUserId(), proposalId, body);
		return new ResponseEntity<>(new ApiResponse(true, "GIA added successfully"), HttpStatus.CREATED);
	}
	
	@PostMapping("/{proposalId}/gia-checklist/submit")
	public ResponseEntity<ApiResponse> submitGIAChecklist(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "proposalId") UUID proposalId,
			@RequestBody AddGIAChecklistRequest body){
		projProposalService.submitGIAChecklist(proposalId, body);
		return new ResponseEntity<>(new ApiResponse(true, "GIA checklist submitted successfully"), HttpStatus.CREATED);
	}
	
	@PutMapping("/{proposalId}/gia")
	public ResponseEntity<ApiResponse> updateGrantImplementationAgreementStatus(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "proposalId", required = true) UUID proposalId,
			@RequestParam(name = "status", required = true) ProcessStatus status,
			@RequestParam(name = "checklist-deadline", required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date checklistDeadline){
		if(!status.equals(ProcessStatus.APPROVED) && !status.equals(ProcessStatus.REJECTED)) {
			throw new ValidationException("Invalid status for this phase of the Proposal");
		}
		
		if(status.equals(ProcessStatus.APPROVED) && checklistDeadline == null) {
			throw new ValidationException("Checklist deadline cannot be null");
		}
		
		projProposalService.updateGrantImplementationAgreementStatus(proposalId, status, checklistDeadline);
		return new ResponseEntity<>(new ApiResponse(true, "GIA Status added successfully"), HttpStatus.OK);
	}
	
	@PostMapping("/{proposalId}/gia/review/add")
	public ResponseEntity<ApiResponse> addGIAReview(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "proposalId") UUID proposalId,
			@RequestBody AddGrantImplementationAgreementReviewRequest body){
		projProposalService.addGrantImplementationAgreementReview(principal.getUserId(), proposalId, body);
		return new ResponseEntity<>(new ApiResponse(true, "GIA Review added successfully"), HttpStatus.CREATED);
	}
	
	@PostMapping("/{proposalId}/reassign")
	public ResponseEntity<ApiResponse> reassignToFIP(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "proposalId") UUID proposalId,
			@RequestBody ReassignPrposalToFIPRequest body){
		projProposalService.reassignProposalToFIP(proposalId, principal.getUserId(), body.getSectionIds(), body.getComments());
		
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Sections reassigned successfully"), HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/{proposalId}/report/{subProcessType}/submit")
	public ResponseEntity<ApiResponse> submitPrposalMiscReport(@PathVariable(name = "proposalId") UUID proposalId,
			@PathVariable(name = "subProcessType") ProcessType subProcessType,
			@RequestBody AddProposalMiscReportRequest body){
		
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Report submitted successfully"), HttpStatus.ACCEPTED);
	}
	@GetMapping("/offerLetter/{proposalId}")
	public ResponseEntity<OfferLetterItem> getOfferLetter(@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "proposalId") UUID proposalId) throws IOException {
		OfferLetterItem dto = projProposalService.getOfferLetter(principal, proposalId);
		return new ResponseEntity<OfferLetterItem>(dto, HttpStatus.OK);
	}
}