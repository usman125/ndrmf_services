package com.ndrmf.config.security;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndrmf.common.ApiResponse;
import com.ndrmf.user.service.UserDetailsServiceImpl;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private CustomAuthenticationProvider customAuthProvider;

	private JWTAuthenticationFilter authenticationFilter() {
		JWTAuthenticationFilter filter = new JWTAuthenticationFilter(authManager, userDetailsService, objectMapper);
		
		filter.setAuthenticationManager(authManager);

		return filter;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors()
			.and()
			.csrf().disable()
			.authorizeRequests()
				.antMatchers(
						SecurityConstants.SIGN_UP_URL,
						SecurityConstants.COMPLAINT_ADD,
						SecurityConstants.COMPLAINT_FIND_BY_USER,
						SecurityConstants.COMPLAINT_FIND_USER,
						SecurityConstants.COMPLAINT_FIND,
						SecurityConstants.COMPLAINT_APPEAL).permitAll()
				.anyRequest().authenticated()
				.and().addFilter(authenticationFilter()).addFilter(new JWTAuthorizationFilter(authManager, userDetailsService))
				.exceptionHandling()
				  .authenticationEntryPoint((request, response, e)-> {
				  response.setStatus(HttpStatus.UNAUTHORIZED.value());
				  response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				  objectMapper.writeValue(response.getWriter(), new ApiResponse(false,
				  e.getMessage())); })
				 
				.and()
				// this disables session creation on Spring Security
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(customAuthProvider);
	}

	@Override
	public void configure(WebSecurity registry) throws Exception {
		registry.ignoring()
		.antMatchers("/docs/**").antMatchers("/actuator/**").antMatchers("/v2/api-docs", "/configuration/ui",
				"/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/webjars/**")
		.antMatchers("/notifications/**");
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
		configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
