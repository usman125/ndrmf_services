package com.ndrmf.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ndrmf.user.model.User;
import com.ndrmf.user.service.UserDetailsServiceImpl;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = (String) authentication.getCredentials();
		
		User user = userDetailsService.findByUsername(username);

		if (user == null || !user.getUsername().equalsIgnoreCase(username)) {
			throw new BadCredentialsException("Username not found");
		}
		
		else if (StringUtils.isEmpty(password)) {
			throw new BadCredentialsException("Provide passsword");
		}
		
		else if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
			throw new BadCredentialsException("Wrong password");
		}
		else if(!user.isEnabled()) {
			throw new DisabledException("Account has been disabled");
		}
		
		return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getRoles());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

}
