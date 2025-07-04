package com.ecourier.parcelservice.security;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getCredentials() != null) {
                String jwt = authentication.getCredentials().toString();

                if (!jwt.startsWith("Bearer ")) {
                    jwt = "Bearer " + jwt;
                }

                requestTemplate.header("Authorization", jwt);
            }
        };
    }
}
