package com.xu.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Used by the FilterChainProxy to determine which Spring Security Filter instances should
     * be invoked for the current process. This filter chain will be configured for 2FA.
     * @param http Configures the web security for specific HTTP requests
     * @return A SecurityFilterChain configuration
     * @throws Exception If an error occurs
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.
                authorizeHttpRequests((requests) -> requests
                        .anyRequest().authenticated())
                .oauth2Login((oauth2) -> oauth2
                        .defaultSuccessUrl("http://localhost:5173/dashboard")
                );



        return http.build();
    }
}
