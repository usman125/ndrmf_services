package com.ndrmf.integration.service;

import javax.net.ssl.SSLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ndrmf.integration.dto.SAPResponseWrapper;
import com.ndrmf.integration.dto.UserItem;
import com.ndrmf.integration.dto.UserSyncStatsItem;
import com.ndrmf.user.model.User;
import com.ndrmf.user.repository.OrganisationRepository;
import com.ndrmf.user.repository.UserRepository;
import com.ndrmf.util.constants.SystemRoles;
import com.ndrmf.util.enums.CRUDType;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import reactor.netty.http.client.HttpClient;

@Service
public class SAPIntegrationService {
	private final String SAP_BASE_URL, SAP_USERNAME, SAP_PASSWORD;
	
	private final WebClient webClient;
	
	@Autowired private OrganisationRepository orgRepo;
	@Autowired private UserRepository userRepo;
	
	@Autowired
	public SAPIntegrationService(@Value("${bridge.sap.base-url}") String sapBaseUrl,
			@Value("${bridge.sap.username}") String sapUsername,
			@Value("${bridge.sap.password}") String sapPassword) {
		
		this.SAP_BASE_URL = sapBaseUrl;
		this.SAP_USERNAME = sapUsername;
		this.SAP_PASSWORD = sapPassword;
		
		try {
			SslContext context = SslContextBuilder.forClient()
					.trustManager(InsecureTrustManagerFactory.INSTANCE)
					.build();
			
			HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(context));
			
			this.webClient = WebClient
					.builder()
					.clientConnector(new ReactorClientHttpConnector(httpClient))
					.baseUrl(this.SAP_BASE_URL)
					.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
					.defaultHeaders(h -> h.setBasicAuth(this.SAP_USERNAME, this.SAP_PASSWORD))
					.build();
		} catch (SSLException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public UserSyncStatsItem downloadUsers() {
		final String url = "opu/odata/sap/Z_UM_INFO_PROJ_SRV/ET_UM_INFO_SET";
		
		SAPResponseWrapper<UserItem> wrappedResonse = this.webClient.get().uri(url)
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<SAPResponseWrapper<UserItem>>() {})
				.block();
		
		UserSyncStatsItem dto = new UserSyncStatsItem();
		
		if(wrappedResonse != null && wrappedResonse.getResults() != null) {
			for(UserItem sapUser: wrappedResonse.getResults()) {
				CRUDType crudType;
				
				User user = userRepo.findByUsernameOrEmail(sapUser.getUsername(), sapUser.getEmail());
				
				if(user != null) {
					user.setUsername(sapUser.getUsername());
					user.setFirstName(sapUser.getFirstName());
					user.setLastName(sapUser.getLastName());
					user.setEmail(sapUser.getEmail());
					user.setEnabled(true);
					
					crudType = CRUDType.UPDATE;
				}
				else {
					user = new User();
					user.setUsername(sapUser.getUsername());
					user.setFirstName(sapUser.getFirstName());
					user.setLastName(sapUser.getLastName());
					user.setEmail(sapUser.getEmail());
					user.setEnabled(true);
					
					if(sapUser.getCompany() != null) {
						if(sapUser.getCompany().equals("NDMRF")) {
							user.setOrg(orgRepo.findById(SystemRoles.ORG_NDRMF_ID).get());
						}
						//TODO: apply conditions for all ORG Types
					}
					
					crudType = CRUDType.INSERT;
				}
				
				try {
					userRepo.save(user);
					
					if(crudType == CRUDType.INSERT) {
						dto.incrementInserted();
					}
					else if(crudType == CRUDType.UPDATE) {
						dto.incrementUpdated();
					}
				}
				catch(Exception ex) {
					dto.addRejectedUser(sapUser);
				}
			}
		}
		
		return dto;
	}
}
