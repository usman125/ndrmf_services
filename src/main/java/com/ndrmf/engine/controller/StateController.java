package com.ndrmf.engine.controller;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(tags = "State")
@RestController
@RequestMapping("/state")
public class StateController {
	private Set<String> getCurrentUserRoles(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth.getAuthorities() == null) {
			return Collections.emptySet();
		}
		
		Set<String> userRoles = auth.getAuthorities()
				.stream().map(r -> r.getAuthority()).collect(Collectors.toSet());
		
		return userRoles;
	}
}
