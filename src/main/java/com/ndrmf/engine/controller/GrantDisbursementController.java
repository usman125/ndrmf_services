package com.ndrmf.engine.controller;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.ndrmf.engine.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.ndrmf.common.ApiResponse;
import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.engine.service.GrantDisbursmentService;
import com.ndrmf.util.enums.ProcessStatus;

import io.swagger.annotations.Api;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Api(tags = "Grant Disbursement Process")
@RestController
@RequestMapping("/grant-disbursement")
public class GrantDisbursementController {
	@Autowired private GrantDisbursmentService grantDisbursmentService;

	@GetMapping("/project-proposals/grant-disbursment-phase/{init-advance-status}")
	public ResponseEntity<List<ProjectProposalListItem>> getProjectProposalsInGrantDisbursmentWithInitAdvanceStatus(
			@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "init-advance-status", required = true) ProcessStatus status) throws IOException{
		return new ResponseEntity<>(grantDisbursmentService.getProjectProposalsInGrantDisbursmentWithInitAdvanceStatus(principal, status), HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<List<GrantDisbursmentListItem>> getAllDisbursments(
			@AuthenticationPrincipal AuthPrincipal principal) throws IOException{
		return new ResponseEntity<>(grantDisbursmentService.getAllDisbursments(principal), HttpStatus.OK);
	}

	@GetMapping("/{disbursmentId}")
	public ResponseEntity<GrantDisbursmentItem> getSingleDisbursment(
			@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "disbursmentId") UUID disbursmentId) throws IOException{
		return new ResponseEntity<>(grantDisbursmentService.getSingleDisbursment(principal, disbursmentId), HttpStatus.OK);
	}

	@PutMapping("/{disbursmentId}/initial-advance/submit")
	public ResponseEntity<ApiResponse> submitInitalAdvance(
			@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "disbursmentId", required = true) UUID disbursmentId,
			@RequestBody(required = true) @Valid InitialAdvanceSubmitRequest body) throws IOException{
		grantDisbursmentService.submitInitalAdvance(principal, disbursmentId, body);
		return new ResponseEntity<>(new ApiResponse(true, "Initial Advance Updated Successfully."), HttpStatus.OK);
	}

	@PostMapping("/commence/{proposalId}")
	public ResponseEntity<ApiResponse> commenceGrantDisbursment(
			@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "proposalId", required = true) UUID proposalId,
			@RequestBody @Valid GrantDisbursmentAdvanceRequest body){
		grantDisbursmentService.commenceGrantDisbursment(principal, proposalId, body);
		return new ResponseEntity<>(new ApiResponse(true, "Grant Disbursment Generated Successfully."), HttpStatus.CREATED);
	}

	@PostMapping("/{disbursmentId}/assign/reviews/")
	public ResponseEntity<ApiResponse> assignInitialAdvanceReviews(
			@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "disbursmentId", required = true) UUID disbursmentId,
			@RequestBody(required = true) InitialAdvanceAssignReviewsRequest body){
		grantDisbursmentService.assignInitialAdvanceReviews(principal, disbursmentId, body);
		return new ResponseEntity<>(new ApiResponse(true, "Users Assigned Successfully."), HttpStatus.CREATED);
	}

	@PutMapping("/{disbursmentId}/submit/review/")
	public ResponseEntity<ApiResponse> submitInitialAdvanceReview(
			@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "disbursmentId", required = true) UUID disbursmentId,
			@RequestBody(required = true) InitialAdvanceSubmitReviewRequest body){
		grantDisbursmentService.submitInitialAdvanceReview(principal, disbursmentId, body);
		return new ResponseEntity<>(new ApiResponse(true, "Users Assigned Successfully."), HttpStatus.CREATED);
	}

	@PutMapping("/{disbursmentId}/quarter-advance/submit/")
	public ResponseEntity<ApiResponse> submitQuarterAdvance(
			@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "disbursmentId", required = true) UUID disbursmentId,
			@RequestBody(required = true) QuarterAdvanceSubmitRequest body){
		grantDisbursmentService.submitQuarterAdvance(principal, disbursmentId, body);
		return new ResponseEntity<>(new ApiResponse(true, "Quarter Advance Submitted Successfully."), HttpStatus.CREATED);
	}

	@PostMapping("/{disbursmentId}/quarter-advance/assign/reviews/")
	public ResponseEntity<ApiResponse> assignQuarterAdvanceReviews(
			@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "disbursmentId", required = true) UUID disbursmentId,
			@RequestBody(required = true) InitialAdvanceAssignReviewsRequest body){
		grantDisbursmentService.assignQuarterAdvanceReviews(principal, disbursmentId, body);
		return new ResponseEntity<>(new ApiResponse(true, "Users Assigned Successfully."), HttpStatus.CREATED);
	}

	@PostMapping("/{disbursmentId}/initial-advance/commence/liquidation/")
	public ResponseEntity<ApiResponse> commenceInitialAdvanceLiquidation(
			@PathVariable(name = "disbursmentId", required = true) UUID disbursmentId){
		grantDisbursmentService.commenceInitialAdvanceLiquidation(disbursmentId);
		return new ResponseEntity<>(new ApiResponse(true, "Liquidation Generated Successfully."), HttpStatus.CREATED);
	}

	@PostMapping("/{advanceId}/quarter-advance/commence/liquidation/")
	public ResponseEntity<ApiResponse> commenceQuarterAdvanceLiquidation(
			@PathVariable(name = "advanceId", required = true) UUID advanceId){
		grantDisbursmentService.commenceQuarterAdvanceLiquidation(advanceId);
		return new ResponseEntity<>(new ApiResponse(true, "Liquidation Generated Successfully."), HttpStatus.CREATED);
	}

	@PutMapping("/{disbursmentId}/initial-advance/approve")
	public ResponseEntity<ApiResponse> approveInitialAdvance(
			@AuthenticationPrincipal AuthPrincipal principal,
			@RequestParam(name = "status", required = true) ProcessStatus status,
			@PathVariable(name = "disbursmentId", required = true) UUID disbursmentId){
		grantDisbursmentService.approveInitialAdvance(principal, disbursmentId, status);
		return new ResponseEntity<>(new ApiResponse(true, "Advance Status Changed Successfully."), HttpStatus.CREATED);
	}

	@PutMapping("/{disbursmentId}/initial-advance/reassign")
	public ResponseEntity<ApiResponse> reassignInitialAdvance(
			@AuthenticationPrincipal AuthPrincipal principal,
			@RequestBody(required = true) Comment body,
			@PathVariable(name = "disbursmentId", required = true) UUID disbursmentId){
		grantDisbursmentService.reassignInitialAdvance(principal, disbursmentId, body);
		return new ResponseEntity<>(new ApiResponse(true, "Advance Re-Assigned Successfully."), HttpStatus.CREATED);
	}

	@PutMapping("/{advanceId}/quarter-advance/approve")
	public ResponseEntity<ApiResponse> approveQuarterAdvance(
			@AuthenticationPrincipal AuthPrincipal principal,
			@RequestParam(name = "status", required = true) ProcessStatus status,
			@PathVariable(name = "advanceId", required = true) UUID advanceId){
		grantDisbursmentService.approveQuarterAdvance(principal, advanceId, status);
		return new ResponseEntity<>(new ApiResponse(true, "Advance Status Changed Successfully."), HttpStatus.CREATED);
	}

	@PostMapping("/initial-advance/{liquidationId}/liquidation/submit")
	public ResponseEntity<ApiResponse> submitInitialAdvanceLiquidationWithSoes(
			@AuthenticationPrincipal AuthPrincipal principal,
			@RequestBody(required = true) GrantDisbursmentAdvanceLiquidationSubmitRequest body,
			@PathVariable(name = "liquidationId", required = true) UUID liquidationId){
		grantDisbursmentService.submitInitialAdvanceLiquidationWithSoes(
				principal,
				liquidationId,
				body
		);
		return new ResponseEntity<>(new ApiResponse(true, "Liquidation Saved Successfully."), HttpStatus.CREATED);
	}

	@PostMapping("/{advanceId}/upload-file")
	public ResponseEntity<?> uploadFileForGrantDisbursmentAdvance(
			@AuthenticationPrincipal AuthPrincipal principal,
			@RequestParam(name = "file", required = true) MultipartFile file,
			@RequestParam(name = "type", required = true) String type,
			@PathVariable(name = "advanceId", required = true) UUID advanceId) throws IOException {
		String path = grantDisbursmentService.uploadFileForGrantDisbursmentAdvance(
				principal,
				advanceId,
				file,
				type
		);
		Map<String, String> dto = new HashMap<>();
		dto.put("filePath", path);

		return new ResponseEntity<>(dto, HttpStatus.CREATED);
	}

	@PutMapping("/advance-liquidation/{liquidationId}/approve")
	public ResponseEntity<ApiResponse> approveAdvanceLiquidation(
			@AuthenticationPrincipal AuthPrincipal principal,
			@PathVariable(name = "liquidationId", required = true) UUID liquidationId){
		grantDisbursmentService.approveAdvanceLiquidation(liquidationId, "Approved");

		return new ResponseEntity<>(new ApiResponse(true, "Liquidation Approved Successfully"), HttpStatus.OK);
	}

	@PutMapping("/advance-liquidation/{liquidationId}/reassign")
	public ResponseEntity<ApiResponse> reassignAdvanceLiquidation(
			@AuthenticationPrincipal AuthPrincipal principal,
			@RequestBody(required = true) Comment body,
			@PathVariable(name = "liquidationId", required = true) UUID liquidationId){
		grantDisbursmentService.reassignAdvanceLiquidation(
				liquidationId,
				"Approved",
				body
		);

		return new ResponseEntity<>(new ApiResponse(true, "Liquidation Re-Assigned Successfully"), HttpStatus.OK);
	}

	@PutMapping("/advance/{advanceId}/reassign")
	public ResponseEntity<ApiResponse> reassignQuarterAdvance(
			@AuthenticationPrincipal AuthPrincipal principal,
			@RequestBody(required = true) Comment body,
			@PathVariable(name = "advanceId", required = true) UUID advanceId){
		grantDisbursmentService.reassignQuarterAdvance(
				advanceId,
				"Approved",
				body
		);
		return new ResponseEntity<>(new ApiResponse(true, "Advance Re-Assigned Successfully"), HttpStatus.OK);
	}

	@GetMapping("/advance/{advanceId}/files")
	public ResponseEntity<List<GrantDisbursmentWithdrawalFilesListItem>> getFilesForAdvance(
			@AuthenticationPrincipal AuthPrincipal principle,
			@PathVariable(name = "advanceId", required = true) UUID advanceId
	){
		return new ResponseEntity<>(grantDisbursmentService.getFilesForAdvance(advanceId), HttpStatus.OK);
	}

}
