package com.ndrmf.mobile.controller;

import com.ndrmf.common.ApiResponse;
import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.engine.dto.CommenceTpv;
import com.ndrmf.engine.dto.TpvTaskSubmitRequest;
import com.ndrmf.engine.dto.TpvTasksListItem;
import com.ndrmf.engine.service.TpvService;
import com.ndrmf.mobile.dto.ActivityVerificationItem;
import com.ndrmf.mobile.dto.ActivityVerificationRequest;
import com.ndrmf.mobile.service.ActivityVerificationService;
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

    @Autowired
    private ActivityVerificationService avService;

    @PostMapping("/commence")
    public ResponseEntity<?> commenceTpvRequest(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestBody @Valid ActivityVerificationRequest body) throws IOException {
//        UUID avId = avService.commenceActivityVerification(principal, body);
//        Map<String, UUID> dto = new HashMap<>();
//        dto.put("avId", avId);
//        return new ResponseEntity<>(avId, HttpStatus.CREATED);
        Map<String, UUID> dto = new HashMap<>();
        dto.put("id", avService.commenceActivityVerification(principal, body));
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping("/{avId}/upload-file")
    public ResponseEntity<?> uploadFileForActivityVerification(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(name = "file", required = true) MultipartFile file,
            @PathVariable(name = "avId", required = true) UUID avId) throws IOException {
        String path = avService.uploadFileForActivityVerification(
                principal,
                avId,
                file
        );
        Map<String, String> dto = new HashMap<>();
        dto.put("filePath", path);

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

	@GetMapping("/{activityId}/{quarter}")
	public ResponseEntity<ActivityVerificationItem> getAvItem(@AuthenticationPrincipal AuthPrincipal principal,
                                                            @PathVariable(name = "activityId", required = true) String activityId,
                                                            @PathVariable(name = "quarter", required = true) int quarter) throws IOException{
		return new ResponseEntity<>(avService.getRequestsByActivityAndQuarter(principal, activityId, quarter), HttpStatus.OK);
	}
}