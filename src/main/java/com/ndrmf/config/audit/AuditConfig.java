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
			AuthPrincipal principal = (AuthPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(principal == null) {
				return null;
			}
			
			return Optional.of(principal.getUsername());
		}
    }
}