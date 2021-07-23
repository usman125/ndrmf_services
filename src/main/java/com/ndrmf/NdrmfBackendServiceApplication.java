package com.ndrmf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import it.ozimov.springboot.mail.configuration.EnableEmailTools;

@SpringBootApplication
@EnableScheduling
@EnableEmailTools
public class NdrmfBackendServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NdrmfBackendServiceApplication.class, args);
    }
    
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
