package com.ndrmf.config.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndrmf.common.ApiResponse;
import com.ndrmf.request.LoginRequest;
import com.ndrmf.user.service.UserDetailsServiceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authManager;
	private final UserDetailsServiceImpl userDetailsService;
	private final ObjectMapper objectMapper;

	public JWTAuthenticationFilter(AuthenticationManager authManager,
			UserDetailsServiceImpl userDetailsService,
			ObjectMapper objectMapper) {
		this.authManager = authManager;
		this.userDetailsService = userDetailsService;
		this.objectMapper = objectMapper;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		try {
			LoginRequest creds = new ObjectMapper().readValue(req.getInputStream(), LoginRequest.class);

			return authManager.authenticate(new UsernamePasswordAuthenticationToken(creds.getUsername(),
					creds.getPassword(), new ArrayList<>()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {

		LoginResponse resBody = this.userDetailsService.getUserDetailsForJWTToken(((User) auth.getPrincipal()).getUsername());
		
		String token = JWT.create()
				.withSubject(resBody.getUser().getUsername())
				.withClaim("orgId", resBody.getUser().getOrgId())
				.withClaim("orgName", resBody.getUser().getOrgName())
				.withArrayClaim("roles", resBody.getUser().getRoles())
				.withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.sign(HMAC512(SecurityConstants.SECRET.getBytes()));
		
		String tokenWithPrefix = SecurityConstants.TOKEN_PREFIX + token;
		
		
		resBody.setAccessToken(tokenWithPrefix);
		
		res.setStatus(HttpStatus.OK.value());
		res.setContentType(MediaType.APPLICATION_JSON_VALUE);
		objectMapper.writeValue(res.getWriter(), resBody);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res,
			AuthenticationException authEx) throws IOException, ServletException {
		ApiResponse apiResponse = new ApiResponse(false, authEx.getMessage());
		
		res.setStatus(HttpStatus.UNAUTHORIZED.value());
		res.setContentType(MediaType.APPLICATION_JSON_VALUE);
		objectMapper.writeValue(res.getWriter(), apiResponse);
	}
}
