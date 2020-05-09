package com.ndrmf.config.audit;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ndrmf.common.AuthPrincipal;

@Configuration
@EnableJpaAuditing
class AuditConfig {
	
    @Bean
    public AuditorAware<String> createAuditorProvider() {
        return new SecurityAuditor();
    }

    @Bean
    public AuditingEntityListener createAuditingListener() {
        return new AuditingEntityListener();
    }

    public static class SecurityAuditor implements AuditorAware<String> {
    	
		@Override
		public Optional<String> getCurrentAuditor() {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			if(principal instanceof AuthPrincipal) {
				AuthPrincipal customPrincipal = (AuthPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				
				return Optional.of(customPrincipal.getUsername());	
			}
			else {
				return Optional.of(principal.toString());
			}
		}
    }
}