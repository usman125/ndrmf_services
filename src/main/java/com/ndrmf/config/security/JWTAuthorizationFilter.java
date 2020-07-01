package com.ndrmf.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.user.service.UserDetailsServiceImpl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	private final UserDetailsServiceImpl userDetailsService;
	
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService) {
		super(authenticationManager);
		
		this.userDetailsService = userDetailsService;
	}
	
	@Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(SecurityConstants.HEADER_STRING);

        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.HEADER_STRING);
        if(token == null) {
        	return null;
        }
        
        //parse the token.
        DecodedJWT jwtToken = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
                .build()
                .verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""));
        
        if(jwtToken == null) {
        	return null;
        }
        
        String username = jwtToken.getSubject();
        
        
        if(!this.userDetailsService.isUserEnabled(username)) {
        	throw new DisabledException("User has been disabled");
        }
        
        String userId = jwtToken.getClaim("userId").asString();
        String fullName = jwtToken.getClaim("fullName").asString();
        List<String> roleClaims = jwtToken.getClaim("roles").asList(String.class);
        
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if(roleClaims != null) {
        	for(String r: roleClaims) {
            	grantedAuthorities.add(new SimpleGrantedAuthority(SecurityConstants.ROLE_PREFIX + r));
            }	
        }

        return new UsernamePasswordAuthenticationToken(new AuthPrincipal(userId, username, fullName, roleClaims), null, grantedAuthorities);
    }
}
