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
    @Autowired
    private QprToDonorService qprToDonorService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/")
    public ResponseEntity<List<QprToDonorListItem>> getQprToDonors(@AuthenticationPrincipal AuthPrincipal principal,
                                                                   @RequestParam(name = "status", required = false) ProcessStatus status) throws IOException {
        return new ResponseEntity<>(qprToDonorService.getQprToDonorRequests(principal, status), HttpStatus.OK);
    }

    //	@RolesAllowed({SystemRoles.ORG_FIP, SystemRoles.ORG_GOVT})
    @PostMapping("/commence")
    public ResponseEntity<?> commenceQprToDonorRequest(@AuthenticationPrincipal AuthPrincipal principal,
                                                       @RequestBody(required = false) CommenceQprToDonorRequest body) {
        Map<String, UUID> dto = new HashMap<>();
        dto.put("id", qprToDonorService.commenceQprToDonor(principal));
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<QprToDonorItem> getQprToDonorRequest(@AuthenticationPrincipal AuthPrincipal principal,
                                                               @PathVariable(name = "id", required = true) UUID id) {
        QprToDonorItem dto = qprToDonorService.getQprToDonorRequest(id, principal);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    //	@RolesAllowed({SystemRoles.ORG_FIP, SystemRoles.ORG_GOVT})
    @PostMapping("/{qprtodonorId}/section/add")
    public ResponseEntity<ApiResponse> submitSection(@AuthenticationPrincipal AuthPrincipal principal,
                                                     @RequestParam(name = "action", required = true) FormAction action,
                                                     @PathVariable(name = "qprtodonorId", required = true) UUID qprtodonorId,
                                                     @RequestBody @Valid QprToDonorSectionRequest body) {
        qprToDonorService.submitQprToDonorSection(principal.getUserId(), qprtodonorId, body, action);

        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Section saved successfully."), HttpStatus.CREATED);
    }

    @PostMapping("/section/{sectionId}/task/add")
    public ResponseEntity<ApiResponse> addTaskForProposalSection(@AuthenticationPrincipal AuthPrincipal principal,
                                                                 @PathVariable(name = "sectionId", required = true) UUID sectionId,
                                                                 @RequestBody @Valid AddProposalTaskRequest body) {
        qprToDonorService.addQprToDonorTask(sectionId, principal.getUserId(), body);
        return new ResponseEntity<>(new ApiResponse(true, "Task added successfully."), HttpStatus.OK);
    }

    @PostMapping("/{qprtodonorId}/reassign")
    public ResponseEntity<ApiResponse> reassignToFIP(@AuthenticationPrincipal AuthPrincipal principal,
                                                     @PathVariable(name = "qprtodonorId") UUID qprtodonorId,
                                                     @RequestBody ReassignPrposalToFIPRequest body) {
        qprToDonorService.reassignQprToDonorSections(qprtodonorId, principal.getUserId(), body.getSectionIds(), body.getComments());

        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Sections reassigned successfully"), HttpStatus.ACCEPTED);
    }

}