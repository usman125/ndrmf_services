package com.ndrmf.mobile.controller;

import com.ndrmf.common.ApiResponse;
import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.engine.dto.CommenceTpv;
import com.ndrmf.engine.dto.TpvTaskSubmitRequest;
import com.ndrmf.engine.dto.TpvTasksListItem;
import com.ndrmf.engine.service.TpvService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Api(tags = "Mobile Services")
@RestController
@RequestMapping("/mobile")
public class MobileController {
//	@Autowired private TpvService tpvService;
//
//	@GetMapping("/{proposalId}")
//	public ResponseEntity<List<TpvTasksListItem>> getTpvs(@AuthenticationPrincipal AuthPrincipal principal,
//			@PathVariable(name = "proposalId", required = false) UUID proposalId) throws IOException{
//		return new ResponseEntity<>(tpvService.getTpvRequestsByProposalId(principal, proposalId), HttpStatus.OK);
//	}
//
//	@PostMapping("/commence")
//	public ResponseEntity<?> commenceTpvRequest(
//			@AuthenticationPrincipal AuthPrincipal principal,
//			@RequestBody @Valid CommenceTpv body){
//		tpvService.commenceTpv(principal, body);
//		return new ResponseEntity<>(new ApiResponse(true, "TPV initiated successfully."), HttpStatus.CREATED);
//	}
//
//	@PostMapping("/{taskId}/submit")
//	public ResponseEntity<ApiResponse> submitTpvTask(
//			@AuthenticationPrincipal AuthPrincipal principal,
//			@PathVariable(name = "taskId", required = true) UUID taskId,
//			@RequestBody @Valid TpvTaskSubmitRequest body) throws IOException {
//		tpvService.submitTpvTask(principal.getUserId(), taskId, body);
//		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Task submitted successfully."), HttpStatus.CREATED);
//	}
//
//	@PostMapping("/{taskId}/upload-file")
//	public ResponseEntity<?> uploadFileForTpvTask(
//			@AuthenticationPrincipal AuthPrincipal principal,
//			@RequestParam(name = "file", required = true) MultipartFile file,
//			@PathVariable(name = "taskId", required = true) UUID taskId) throws IOException {
//		String path = tpvService.uploadFileForTpvTask(
//				principal,
//				taskId,
//				file
//		);
//		Map<String, String> dto = new HashMap<>();
//		dto.put("filePath", path);
//		return new ResponseEntity<>(dto, HttpStatus.CREATED);
//	}
}