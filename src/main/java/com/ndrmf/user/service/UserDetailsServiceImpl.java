package com.ndrmf.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ndrmf.config.security.LoginResponse;
import com.ndrmf.user.model.User;
import com.ndrmf.user.repository.UserRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	@Autowired
	private UserRepository userRepo;
	
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getRoles());
    }
	
	public LoginResponse getUserDetailsForJWTToken(String username) {
		User user = userRepo.findByUsername(username);
		
		LoginResponse dto = new LoginResponse();
		LoginResponse.User respUser = new LoginResponse.User();
		
		respUser.setUsername(user.getUsername());
		respUser.setEmail(user.getEmail());
		respUser.setFirstName(user.getFirstName());
		respUser.setLastName(user.getFamilyName());
		respUser.setOrgId(user.getOrg().getId());
		respUser.setOrgName(user.getOrg().getName());
		
		if(user.getRoles() != null) {
			String[] roles = user.getRoles().stream().map(r -> r.getName()).toArray(String[]::new);
			respUser.setRoles(roles);
		}
		
		dto.setUser(respUser);
		
		return dto;
	}
}
