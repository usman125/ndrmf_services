package com.ndrmf.integration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ndrmf.integration.dto.UserSyncStatsItem;
import com.ndrmf.integration.service.SAPIntegrationService;

import io.swagger.annotations.Api;

@Api(tags = "SAP Bridge")
@RestController
@RequestMapping("/bridge")
public class IntegrationController {
	@Autowired private SAPIntegrationService integrationService;
	
	@GetMapping(path="/users/fetch", produces = "application/json")
	public ResponseEntity<UserSyncStatsItem> syncUsers(){
		UserSyncStatsItem stats = integrationService.downloadUsers();
		return new ResponseEntity<>(stats, HttpStatus.OK);
	}
}