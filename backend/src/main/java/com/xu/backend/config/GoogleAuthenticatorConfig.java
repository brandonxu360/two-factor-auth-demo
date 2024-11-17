package com.xu.backend.config;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.xu.backend.repository.TotpCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * GoogleAuthenticatorConfig class to create a GoogleAuthenticator bean
 * and set the credential repository.
 */
@Configuration
@RequiredArgsConstructor
public class GoogleAuthenticatorConfig {
    private final TotpCredentialRepository totpCredentialRepository;

    // Create a GoogleAuthenticator bean
    @Bean
    public GoogleAuthenticator googleAuthenticator() {
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        googleAuthenticator.setCredentialRepository(totpCredentialRepository);
        return googleAuthenticator;
    }
}
